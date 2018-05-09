package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.mxicoders.skepci.adapter.PatientArchievedAdapter;
import com.mxicoders.skepci.adapter.PatientStatusAdapter;
import com.mxicoders.skepci.adapter.RecyclerAdapter;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.Constant;
import com.mxicoders.skepci.utils.ItemModel;
import com.mxicoders.skepci.utils.ItemModelTwo;
import com.mxicoders.skepci.utils.ItemTouchHelperCallback;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackPatientArchieved;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackPatientStatus;
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
 * Created by mxicoders on 15/7/17.
 */

public class PatientArchievedActivity extends AppCompatActivity {


    /*int imageUrl[] = Constant.image;
    String names[] = Constant.name;
*/
    public static List<ItemModelTwo> archive_list;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    PatientArchievedAdapter adapter;

    TextView tvInviteClick;
    ProgressDialog pDialog;
    EditText edEmail;
    String emailUser;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    Dialog dialog1;

    public PatientArchievedActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_patient_archieved);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        ButterKnife.bind(this);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        tooName.setText(getString(R.string.patient_archieve));

        imgToolOne.setBackgroundResource(R.drawable.ic_archieved);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pa = new Intent(PatientArchievedActivity.this,ActivityPatientList.class);
                startActivity(pa);
                finish();
            }
        });

        imgToolOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pa = new Intent(PatientArchievedActivity.this,ActivityPatientList.class);
                startActivity(pa);
                finish();
            }
        });


        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog1 = new Dialog(PatientArchievedActivity.this);
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


        /*List<ItemModel> list = getList();
        adapter = new PatientArchievedAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCallback = new ItemTouchHelperCallbackPatientArchieved();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);*/


    }


    /*private List<ItemModel> getList() {
        List<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < imageUrl.length; i++) {
            ItemModel model = new ItemModel();
            model.setName(names[i]);
            model.setImagePath(imageUrl[i]);
            list.add(model);
        }
        return list;
    }*/


    public void getArchiveList() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.patient_archieve));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_ARCHIEVE_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:archive data", response);

                        List<ItemModelTwo> listcontent = new ArrayList<>();
                        archive_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("patient_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    if (jsonObject1.getString("status").contains("Approved")) {

                                        ItemModelTwo model = new ItemModelTwo();

                                        model.setName(jsonObject1.getString("name"));
                                        model.setName2(jsonObject1.getString("name"));
                                        model.setLname(jsonObject1.getString("last_name_initial"));
                                        model.setId(jsonObject1.getString("patient_id"));
                                        model.setP_birthdate(jsonObject1.getString("birth_date"));

                                        model.setName(model.getName() + " " + " " + model.getLname() + ".");

                                        archive_list.add(model);

                                    } else {

                                        Log.e("Not set","else...response");

                                    }

                                }

                                if(adapter != null){
                                    adapter = null;
                                }


                                LinearLayoutManager layoutManager= new LinearLayoutManager(PatientArchievedActivity.this);
                                adapter = new PatientArchievedAdapter(PatientArchievedActivity.this,archive_list);
                                recyclerView.setLayoutManager(layoutManager);

                                /*mCallback = new ItemTouchHelperCallback();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setAdapter(adapter);*/

                                mCallback = new ItemTouchHelperCallbackPatientArchieved();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
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
                AndyUtils.showToast(PatientArchievedActivity.this,getString(R.string.ws_error));
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


    private void InvitePatient() {

        emailUser = edEmail.getText().toString().trim();


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(PatientArchievedActivity.this,getString(R.string.no_internet));
        }else if (emailUser.equals("")) {
            AndyUtils.showToast(PatientArchievedActivity.this,getString(R.string.invite_email));
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
                AndyUtils.showToast(PatientArchievedActivity.this, getString(R.string.ws_error));
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
    protected void onResume() {
        super.onResume();


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(PatientArchievedActivity.this,getString(R.string.no_internet));
        }
        else {

            getArchiveList();
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
}
