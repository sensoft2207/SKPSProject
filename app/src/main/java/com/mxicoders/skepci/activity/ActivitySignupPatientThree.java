package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
 * Created by mxicoders on 20/7/17.
 */

public class ActivitySignupPatientThree extends AppCompatActivity {

    CommanClass cc;

    CheckBox chCheck;

    EditText edEmail,edConfirmEmail;

    TextView tvSignup,tvHead;

    String name,email,password,city,lname_initial,id,dateInString;

    String phyEmail,phyConfirmEmail;

    String gender = "";

    ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_patient_three);

        Typeface fontRegular = Typeface.createFromAsset(getAssets(), "font/OPENSANS-REGULAR.TTF");
        Typeface fontItalic = Typeface.createFromAsset(getAssets(), "font/OPENSANS-ITALIC.TTF");
        Typeface fontBold = Typeface.createFromAsset(getAssets(), "font/OPENSANS-BOLD.TTF");

        cc = new CommanClass(ActivitySignupPatientThree.this);

        try {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            city = getIntent().getStringExtra("city");
            lname_initial = getIntent().getStringExtra("lname");
            dateInString = getIntent().getStringExtra("dob");
            gender = getIntent().getStringExtra("gender");
            id = getIntent().getStringExtra("state_id");



            Log.i("@@data", name.toString());
            Log.i("@@data", email.toString());
            Log.i("@@data", password.toString());
            Log.i("@@data", city.toString());
            Log.i("@@data", lname_initial.toString());
            Log.i("@@data", dateInString.toString());
            Log.i("@@data", gender.toString());
            Log.i("@@data", id.toString());



        } catch (Exception e) {
            e.printStackTrace();
        }

        chCheck = (CheckBox)findViewById(R.id.ch_check);

        edEmail = (EditText)findViewById(R.id.ed_p_email);
        edConfirmEmail = (EditText)findViewById(R.id.ed_p_confirm_email);

        tvSignup = (TextView)findViewById(R.id.tv_signup_patient);

        edEmail.setFocusable(false);
        edConfirmEmail.setFocusable(false);

        chCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true){
                    edEmail.setFocusableInTouchMode(true);
                    edConfirmEmail.setFocusableInTouchMode(true);


                }else if (isChecked == false){
                    edEmail.setFocusableInTouchMode(false);
                    edConfirmEmail.setFocusableInTouchMode(false);

                    edEmail.setFocusable(false);
                    edConfirmEmail.setFocusable(false);

                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupPatient();

            }
        });

        chCheck.setTypeface(fontItalic);

    }

    private void signupPatient() {



        phyEmail = edEmail.getText().toString().trim();
        phyConfirmEmail = edConfirmEmail.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ActivitySignupPatientThree.this,getString(R.string.no_internet));
        } else if (phyEmail.equals("")) {
            AndyUtils.showToast(ActivitySignupPatientThree.this,getString(R.string.s_phy_email));
        } else if (phyConfirmEmail.equals("")) {
            AndyUtils.showToast(ActivitySignupPatientThree.this,getString(R.string.s_phy_cemail));
        }else if (!phyConfirmEmail.equals(phyEmail)) {
            AndyUtils.showToast(ActivitySignupPatientThree.this,getString(R.string.s_phy_cemail));
        }
        else {

            signupWS(name,email,password,city,lname_initial,dateInString,gender,id,phyEmail);
        }
    }

    private void signupWS(final String name, final String email, final String password,
                          final String city, final String lname_initial, final String dateInString,
                          final String gender, final String id, final String phyEmail) {

        pDialog = new ProgressDialog(ActivitySignupPatientThree.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.REGISTER,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:signuppatient", response);

                        jsonParseSignup(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivitySignupPatientThree.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("city", city);

                params.put("last_name_initial",lname_initial);

                params.put("birth_date", dateInString);
                params.put("gender", gender);
                params.put("state_id", id);
                params.put("psychologist_email", phyEmail);
                params.put("device_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("device_type", "Android");


                Log.i("request signup patient", params.toString());

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

                Intent log = new Intent(ActivitySignupPatientThree.this,ActivityLoginScreen.class);
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

}
