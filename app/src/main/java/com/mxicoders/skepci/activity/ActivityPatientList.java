package com.mxicoders.skepci.activity;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.mxicoders.skepci.adapter.PatientStatusAdapter;
import com.mxicoders.skepci.adapter.RecyclerAdapter;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.Constant;
import com.mxicoders.skepci.utils.ItemModel;
import com.mxicoders.skepci.utils.ItemModelTwo;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackPatientList;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackPatientStatus;
import com.mxicoders.skepci.utils.ItemTouchHelperExtension;
import com.mxicoders.skepci.utils.OnStartDragListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


public class ActivityPatientList extends AppCompatActivity implements PatientStatusAdapter.PlayPauseClick,OnStartDragListener{

    static CommanClass cc;

    public static List<ItemModelTwo> pending_listcontent;
    public static List<ItemModelTwo> approved_listcontent;

    ArrayList<String> stringArray;

    private static Context context = null;

    int imageUrl[] = Constant.image;
    String names[] = Constant.name;

    int imageUrl2[] = Constant.image2;
    String names2[] = Constant.name2;

    public static RecyclerAdapter adapter;
    public static  PatientStatusAdapter adapter2;

    TextView tvInviteClick;

    ProgressDialog pDialog;

    EditText edEmail;

    String emailUser;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public ItemModel content;


    public static  RecyclerView recyclerView;

    public static  RecyclerView recyclerView2;

    public static ItemTouchHelperExtension mItemTouchHelper;
    public static ItemTouchHelperExtension.Callback mCallback;

    String title = "";

    Dialog dialog1;

    public ActivityPatientList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_patientlist);
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView2 = (RecyclerView)findViewById(R.id.recyclerView2);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        tooName.setText(getString(R.string.patient_list));

        imgToolOne.setBackgroundResource(R.drawable.ic_archieved);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgToolOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pa = new Intent(ActivityPatientList.this,PatientArchievedActivity.class);
                startActivity(pa);
                finish();

            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1 = new Dialog(ActivityPatientList.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.patient_list_invite_dialog);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                dialog1.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                ImageView close_dialogg = (ImageView) dialog1.findViewById(R.id.close_dialog);

                edEmail = (EditText) dialog1.findViewById(R.id.ed_email);
                tvInviteClick = (TextView) dialog1.findViewById(R.id.tv_invite_click);

                tvInviteClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        InvitePatient();


                    }
                });

                close_dialogg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog1.dismiss();
                    }
                });
                dialog1.show();

            }
        });

        ButterKnife.bind(this);


    }


    @Override
    public void imageButtonOnClick(View v, int position) {
        // TODO: Implement this
        cc.showToast("Item Clicked");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_trash) {



            return true;

        } else if (id == R.id.action_invite) {



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getProfileData() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PATIENT_LIST_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);

                        List<ItemModelTwo> listcontent = new ArrayList<>();
                        pending_listcontent = new ArrayList<>();
                        approved_listcontent = new ArrayList<>();

                        stringArray = new ArrayList<String>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("patient_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);



                                    Log.e("patientID", String.valueOf(jsonObject1.getInt("patient_id")));
                                    Log.e("patientStatus", jsonObject1.getString("status"));

                                    if (jsonObject1.getString("status").contains("Approved")) {

                                        ItemModelTwo model = new ItemModelTwo();

                                        model.setName(jsonObject1.getString("name"));
                                        model.setName2(jsonObject1.getString("name"));
                                        model.setLname(jsonObject1.getString("last_name_initial"));
                                        model.setId(jsonObject1.getString("patient_id"));
                                        model.setP_birthdate(jsonObject1.getString("birth_date"));
                                        model.setEmail(jsonObject1.getString("email"));
                                        model.setCity(jsonObject1.getString("city"));
                                        model.setGender(jsonObject1.getString("gender"));
                                        model.setOrder(jsonObject1.getString("patient_order"));
                                        model.setCount_emotion(jsonObject1.getString("count_emotions"));

                                        String list_count = jsonObject1.getString("list_emotion");

                                        try {

                                            if (list_count.isEmpty()){

                                                model.setList_emotion_array("No");

                                            }else {

                                                Object obj = new JSONTokener(list_count).nextValue();

                                                if (obj instanceof JSONObject){

                                                    Log.e("JSONOBJECT",((JSONObject) obj).getString("list_emotion"));

                                                    model.setList_emotion_array("No");

                                                }else if (obj instanceof JSONArray){

                                                    JSONArray dataArray2 = jsonObject1.getJSONArray("list_emotion");

                                                    String list_emotion_final = "";

                                                    StringBuffer result_emotion = new StringBuffer();

                                                    for (int in = 0; in < dataArray2.length(); in++) {


                                                        if(in==0){
                                                            result_emotion.append(dataArray2.get(in).toString());
                                                        }else {

                                                            result_emotion.append(" , "+dataArray2.get(in).toString());
                                                        }

                                                    }

                                                    result_emotion.deleteCharAt(result_emotion.length()-1);

                                                    String result=result_emotion.toString();
                                                    model.setList_emotion_array(result);

                                                    Log.e("@@Array",result);

                                                }
                                            }



                                        } catch (JSONException e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                        }


                                        model.setName(model.getName() + " " + " " + model.getLname() + ".");

                                        approved_listcontent.add(model);

                                    } else if (jsonObject1.getString("status").contains("Pending")) {
                                        ItemModelTwo model = new ItemModelTwo();

                                        model.setName(jsonObject1.getString("name"));
                                        model.setLname(jsonObject1.getString("last_name_initial"));
                                        model.setId(jsonObject1.getString("patient_id"));

                                        String list_count = jsonObject1.getString("list_emotion");

                                        if (list_count.isEmpty()){

                                            model.setList_emotion_array("No");
                                            Log.e("NULLLISTEMOTION",list_count);
                                        }else {
                                            Log.e("NULLLISTEMOTION","......................");
                                        }



                                        model.setName(model.getName() + " " + " " + model.getLname() + ".");
                                        pending_listcontent.add(model);

                                    }

                                }

                                if(adapter != null){
                                    adapter = null;
                                }

                                if(adapter2 != null){
                                    adapter2 = null;
                                }

                                LinearLayoutManager layoutManager= new LinearLayoutManager(ActivityPatientList.this);
                                adapter = new RecyclerAdapter(ActivityPatientList.this, approved_listcontent,ActivityPatientList.this);
                                recyclerView.setLayoutManager(layoutManager);

                                mCallback = new ItemTouchHelperCallbackPatientList(adapter);
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setAdapter(adapter);


                                LinearLayoutManager layoutManager2 = new LinearLayoutManager(ActivityPatientList.this);

                                adapter2 = new PatientStatusAdapter(ActivityPatientList.this, pending_listcontent);
                                recyclerView2.setLayoutManager(layoutManager2);

                                mCallback = new ItemTouchHelperCallbackPatientStatus();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView2);
                                recyclerView2.setAdapter(adapter2);

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
                AndyUtils.showToast(ActivityPatientList.this,getString(R.string.ws_error));
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

    private void InvitePatient() {

        emailUser = edEmail.getText().toString().trim();


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ActivityPatientList.this,getString(R.string.no_internet));
        }else if (emailUser.equals("")) {
            AndyUtils.showToast(ActivityPatientList.this,getString(R.string.invite_email));
        }else {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.please_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            PostInviteEmail(emailUser);
        }
    }

    private void PostInviteEmail(final String emailUser) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.INVITE_PATIENT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:invite send", response);

                        jsonParseInvite(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivityPatientList.this, getString(R.string.ws_error));
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("invite_email", emailUser);
                params.put("user_id", cc.loadPrefString("user_id"));

                Log.i("request invite", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header profile", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void jsonParseInvite(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                dialog1.dismiss();

                final Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.patient_invite_sent_dialog);
                dialog.show();

                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                };

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 2000);

                cc.showToast(jsonObject.getString("message"));

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error", e.toString());
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        cc = new CommanClass(this);
        cc.savePrefBoolean("isInPatentList",true);

        context = this;
        Log.e("@@@InResume","Yes");

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ActivityPatientList.this,getString(R.string.no_internet));
        }
        else {

            getProfileData();
        }



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

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

        mItemTouchHelper.startDrag(viewHolder);

    }
}
