package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.sessionmanager.SessionManager;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.Config;
import com.mxicoders.skepci.utils.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 10/7/17.
 */

public class ActivityLoginScreen extends AppCompatActivity {

    private SessionManager session;
    CommanClass cc;
    private RequestQueue requestQueue;

    ProgressDialog pDialog;

    TextView registerTv,loginTv,forgotPass;
    LinearLayout patient,phys;
    TextView tv_patient,tv_phys;
    TextView tvOkay,tvNo;
    String psychologist_id="0";

    EditText edtUser,edtPass;

    EditText edForgotEmail;

    String forgotMail;

    String convertPatient,convertPhys;

    String android_id;

    String user_type = "";
    String password;

    Dialog dialog;


    private BroadcastReceiver mRegistrationBroadcastReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //CommanClass
        cc = new CommanClass(ActivityLoginScreen.this);

        //Fetching Android ID
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        cc.savePrefString("device_id",android_id);


        fcmNotification();

        //Request Volley
        requestQueue = Volley.newRequestQueue(this);

        //TextView
        registerTv = (TextView)findViewById(R.id.tv_register);
        forgotPass = (TextView)findViewById(R.id.tv_forgot_pass);
        loginTv = (TextView)findViewById(R.id.tv_login);
        tv_patient = (TextView)findViewById(R.id.tv_patient);
        tv_phys = (TextView)findViewById(R.id.tv_phys);

        if (tv_patient.getText().toString().equals("Paciente")){

            user_type = "Patient";

        }else {

            user_type = "Psychologist";
        }

        //EditText
        edtUser = (EditText)findViewById(R.id.edt_username);
        edtPass = (EditText)findViewById(R.id.edt_password);

        patient = (LinearLayout)findViewById(R.id.ln_patient);
        phys = (LinearLayout)findViewById(R.id.ln_phys);

        //Setting color first time
        tv_phys.setTextColor(Color.parseColor("#000000"));

        //OnclickListner
        patient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                user_type = "Patient";

                patient.setBackground(getResources().getDrawable(R.drawable.login_facebook_button_back));
                phys.setBackground(getResources().getDrawable(R.drawable.phy_back));

                tv_patient.setTextColor(Color.parseColor("#FFFFFF"));
                tv_phys.setTextColor(Color.parseColor("#000000"));

            }
        });

        phys.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                user_type = "Psychologist";


                patient.setBackground(getResources().getDrawable(R.drawable.phy_back));
                phys.setBackground(getResources().getDrawable(R.drawable.login_facebook_button_back));

                tv_patient.setTextColor(Color.parseColor("#000000"));
                tv_phys.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent tv_reg = new Intent(ActivityLoginScreen.this,ActivityRegisterChoose.class);
                startActivity(tv_reg);

            }
        });

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignIn();



            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(ActivityLoginScreen.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.forgote_pass_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                dialog.show();

               ImageView close = (ImageView)dialog.findViewById(R.id.close);

                edForgotEmail = (EditText)dialog.findViewById(R.id.ed_forgote_email);

                tvOkay = (TextView) dialog.findViewById(R.id.tv_okay_dialog);
                tvNo = (TextView) dialog.findViewById(R.id.tv_cancle_dialog);

                tvOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        forgot();

                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }

        });


    }

    private void fcmNotification() {

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    //displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };
    }


    private void forgot() {

        forgotMail = edForgotEmail.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.no_internet));

        } else if (!AndyUtils.eMailValidation(edForgotEmail.getText().toString())) {

            AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.enter_email));

            edForgotEmail.requestFocus();
        } else {
            pDialog = new ProgressDialog(ActivityLoginScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            makeJsonForgot(forgotMail);

        }

    }

    private void makeJsonForgot(final String forgotMail) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.FORGET_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:forgot", response);
                        jsonParseForgot(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", forgotMail);

                Log.i("request forgot password", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put(Const.Params.Authorization, Const.Params.Authorization_value);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseForgot(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                dialog.dismiss();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Forgot Error",e.toString());
        }

    }

    private void SignIn() {
        String email = edtUser.getText().toString().trim();
        password = edtPass.getText().toString().trim();



        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.no_internet));
        } else if (!AndyUtils.eMailValidation(edtUser.getText().toString().trim())) {

            AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.enter_email));

            edtUser.requestFocus();
        } else if (password.equals("")) {

            AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.enter_password));

            edtPass.requestFocus();
        } else {
            pDialog = new ProgressDialog(ActivityLoginScreen.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.e("fcmId", refreshedToken);


            makeJsonlogin(email, password,refreshedToken);


        }
    }

    private void makeJsonlogin(final String email, final String password, final String refreshedToken) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.LOGIN,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:login", response);
                        jsonParseLogin(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivityLoginScreen.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put(Const.Params.USER_TYPE,user_type);
                params.put("device_type", "Android");
                params.put("device_id", cc.loadPrefString("device_id"));
                params.put("fcm_id", refreshedToken);
                params.put("ios_id", "");

                Log.i("request login", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
//                headers.put("user_token", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());


                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseLogin(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.savePrefString("inactivePass",password);

                JSONArray dataArray = jsonObject.getJSONArray("user_data");

                for (int i = 0; i < dataArray.length(); i++) {

                    JSONObject jobj = dataArray.getJSONObject(i);

                    cc.savePrefString("user_id", jobj.getString("id"));
                    cc.savePrefString("user_token", jobj.getString("user_token"));
                    cc.savePrefString("fname", jobj.getString("name"));
                    cc.savePrefString("lname", jobj.getString("last_name_initial"));

                    if (user_type.contains("Patient")) {
                        psychologist_id = jobj.getString("psychologist_id");
                    }


                }

                cc.showToast(jsonObject.getString("message"));

                if (user_type.contains("Patient")){

                    Intent tv_log = new Intent(ActivityLoginScreen.this,ActivityMenuPatient.class);
                    cc.savePrefString("psychologist_id", psychologist_id);
                    cc.savePrefBoolean("isLoginPatient", true);
                    cc.savePrefBoolean("isLogin", false);
                    cc.savePrefBoolean("isInactiveDevice", true);
                    startActivity(tv_log);
                    finish();

                }else {
                    Intent tv_log = new Intent(ActivityLoginScreen.this,ActivityMenuPsychologist.class);
                    cc.savePrefBoolean("isLogin", true);
                    cc.savePrefBoolean("isLoginPatient", false);
                    cc.savePrefBoolean("isInactiveDevice", true);
                    startActivity(tv_log);
                    finish();
                }


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error",e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
