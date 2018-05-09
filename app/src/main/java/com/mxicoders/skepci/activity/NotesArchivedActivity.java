package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.NotesArchivedAdapter;
import com.mxicoders.skepci.adapter.PatientArchievedAdapter;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModelTwo;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackPatientArchieved;
import com.mxicoders.skepci.utils.ItemTouchHelperExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mxicoders.skepci.activity.ActivityPatientList.cc;

/**
 * Created by mxi on 5/10/17.
 */

public class NotesArchivedActivity extends AppCompatActivity {

    CommanClass cc;

    public static List<ItemModelTwo> archive_note_list;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    NotesArchivedAdapter adapter;

    ProgressDialog pDialog;


    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;


    public NotesArchivedActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patient_archieved);

        cc = new CommanClass(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        ButterKnife.bind(this);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        tooName.setText(getString(R.string.note_archieved));

        imgToolOne.setBackgroundResource(R.drawable.ic_archieved);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pa = new Intent(NotesArchivedActivity.this,NotesPatientMenuActivity.class);
                startActivity(pa);
                finish();
            }
        });

        imgToolOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pa = new Intent(NotesArchivedActivity.this,NotesPatientMenuActivity.class);
                startActivity(pa);
                finish();
            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent plist = new Intent(NotesArchivedActivity.this, CreateNoteActivity.class);
                startActivity(plist);
                finish();
            }
        });

    }

    public void getNoteArchiveList() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.NOTE_ARCHIEVE_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:archive data", response);


                        archive_note_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("note_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    ItemModelTwo model = new ItemModelTwo();

                                    model.setName(jsonObject1.getString("title"));
                                    model.setName2(jsonObject1.getString("body"));
                                    model.setId(jsonObject1.getString("id"));

                                    archive_note_list.add(model);
                                }

                                if(adapter != null){
                                    adapter = null;
                                }


                                LinearLayoutManager layoutManager= new LinearLayoutManager(NotesArchivedActivity.this);
                                adapter = new NotesArchivedAdapter(NotesArchivedActivity.this,archive_note_list);
                                recyclerView.setLayoutManager(layoutManager);


                                /*mCallback = new ItemTouchHelperCallbackPatientArchieved();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);*/
                                recyclerView.setAdapter(adapter);


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
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request archive list", params.toString());
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


    @Override
    protected void onResume() {
        super.onResume();

        getNoteArchiveList();

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
}
