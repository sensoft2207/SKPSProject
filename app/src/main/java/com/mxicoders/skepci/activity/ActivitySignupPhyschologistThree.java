package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 11/7/17.
 */

public class ActivitySignupPhyschologistThree  extends AppCompatActivity {

    CommanClass cc;

    TextView tvAgree,tvAgreeHead;

    String name, email, pass, cpass;

    String crp_no, cpf_no, address, city, approach,id;

    String gender = "";

    String dateInString;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_physchologist_three);


        cc = new CommanClass(ActivitySignupPhyschologistThree.this);


        try {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            pass = getIntent().getStringExtra("password");
            dateInString = getIntent().getStringExtra("birth_date");
            crp_no = getIntent().getStringExtra("crp_no");
            cpf_no = getIntent().getStringExtra("cpf_no");
            address = getIntent().getStringExtra("address");
            city = getIntent().getStringExtra("city");
            id = getIntent().getStringExtra("state_id");
            approach = getIntent().getStringExtra("approach");
            gender = getIntent().getStringExtra("gender");


            Log.i("@@data", name.toString());
            Log.i("@@data", email.toString());
            Log.i("@@data", pass.toString());
            Log.i("@@data", dateInString.toString());
            Log.i("@@data", crp_no.toString());
            Log.i("@@data", cpf_no.toString());
            Log.i("@@data", address.toString());
            Log.i("@@data", city.toString());
            Log.i("@@data", id.toString());
            Log.i("@@data", approach.toString());
            Log.i("@@data", gender.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        tvAgree = (TextView) findViewById(R.id.tv_agree);
        tvAgreeHead = (TextView) findViewById(R.id.tv_agree_details);

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Signup();

            }
        });

        agreePage();



    }

    private void Signup() {

        pDialog = new ProgressDialog(ActivitySignupPhyschologistThree.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        goToNext(name, email, pass, dateInString,crp_no,
                cpf_no, address, city,id,approach,gender);
    }

    private void goToNext(final String name, final String email, final String pass,
                          final String dateInString, final String crp_no, final String cpf_no,
                          final String address, final String city, final String id, final String approach,
                          final String gender) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.REGISTER2,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:signup", response);

                        jsonParseSignup(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivitySignupPhyschologistThree.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", pass);
                params.put("birth_date", dateInString);
                params.put("gender",gender);
                params.put("city", city);
                params.put("state_id", id);
                params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("device_type", "Android");
                params.put("crp_number", crp_no);
                params.put("cpf_number", cpf_no);
                params.put("address", address);
                params.put("approach", approach);

                Log.i("request signup", params.toString());

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

    private void jsonParseSignup(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));
                Intent log = new Intent(ActivitySignupPhyschologistThree.this,ActivityLoginScreen.class);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                log.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                log.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0,0);
                startActivity(log);

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error", e.toString());
        }
    }

    private void agreePage() {

            makeJsonAgree();

    }

    private void makeJsonAgree() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PAGES,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:page", response);
                        jsonParsePages(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                AndyUtils.showToast(ActivitySignupPhyschologistThree.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page_id", "2");

                Log.i("request pages", params.toString());

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

    private void jsonParsePages(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("200")) {

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Pages Error",e.toString());
        }
    }


}
