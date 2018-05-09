package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.PatientChatListAdapter;
import com.mxicoders.skepci.model.PatientListData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackChat;
import com.mxicoders.skepci.utils.ItemTouchHelperExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class ActivityPatientChatList extends AppCompatActivity {

    ImageView back_toolbar;
    RecyclerView recyclerView;
    CommanClass cc;
    ProgressDialog pDialog;
    ArrayList<PatientListData> patientChatList;
    PatientChatListAdapter patientChatListAdapter;

    public  ItemTouchHelperExtension mItemTouchHelper;
    public  ItemTouchHelperExtension.Callback mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat_list);
        back_toolbar=(ImageView)findViewById(R.id.back_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        back_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ButterKnife.bind(ActivityPatientChatList.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        cc = new CommanClass(this);

        getPatientChatList();

        if (cc.loadPrefBoolean("isInactiveDevice") || cc.loadPrefBoolean("isDialogOpen") ){


            Log.e("IsLoginDevice","Psychologist...If");

            cc.savePrefBoolean("isInactiveDevice",false);

        }else {

            if (cc.loadPrefBoolean("Inactive")) {

                cc.savePrefBoolean("Inactive",false);

                cc.savePrefBoolean("isDialogOpen",true);

                cc.showInactiveDialog();
            }


            Log.e("IsLoginDevice","Psychologist...Else");
        }
    }

    private void getPatientChatList() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PATIENT_CHAT_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);

                        patientChatList= new ArrayList<>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("chat_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    PatientListData listData=new PatientListData();
                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    listData.setId(jsonObject1.getString("id"));
                                    listData.setName(jsonObject1.getString("name"));
                                    listData.setLast_name_initial(jsonObject1.getString("last_name_initial"));
                                    listData.setMessage(jsonObject1.getString("message"));
                                    listData.setUnread_msg(jsonObject1.getString("unread_msg"));

                                patientChatList.add(listData);
                                }

                               /* LinearLayoutManager layoutManager= new LinearLayoutManager(ActivityPatientChatList.this);
                                patientChatListAdapter = new PatientChatListAdapter(ActivityPatientChatList.this, patientChatList);
                                recyclerView.setLayoutManager(layoutManager);

                                mCallback = new ItemTouchHelperCallbackChat();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setAdapter(patientChatListAdapter);*/

                                patientChatListAdapter = new PatientChatListAdapter(ActivityPatientChatList.this, patientChatList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ActivityPatientChatList.this));

                                mCallback = new ItemTouchHelperCallbackChat();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setAdapter(patientChatListAdapter);


                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request profile data", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


}
