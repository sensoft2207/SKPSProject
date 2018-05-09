package com.mxicoders.skepci.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 20/7/17.
 */

public class ActivitySignupPatientTwo extends AppCompatActivity {

    CommanClass cc;

    TextView tvAgreeClick,tvAgreeDetail;

    String dateInString;

    String name,email,password,cpassword,city,lname_initial;

    String id;


    String gender = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_physchologist_three);


        cc = new CommanClass(ActivitySignupPatientTwo.this);

        try {
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            city = getIntent().getStringExtra("city");
            lname_initial = getIntent().getStringExtra("lname");
            dateInString = getIntent().getStringExtra("b_date");
            gender = getIntent().getStringExtra("gender");
            id = getIntent().getStringExtra("state_id");

            agreePage();

            Log.i("@@data", name.toString());
            Log.i("@@data", email.toString());
            Log.i("@@data", password.toString());
            Log.i("@@data", city.toString());
            Log.i("@@data", lname_initial.toString());
            Log.i("@@data", gender.toString());
            Log.i("@@data", id.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvAgreeClick = (TextView)findViewById(R.id.tv_agree);
        tvAgreeDetail = (TextView)findViewById(R.id.tv_agree_details);

        tvAgreeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoFinalstep(name,email,password,city,lname_initial,dateInString,gender,id);
            }
        });

    }

    private void gotoFinalstep(String name, String email, String password,
                               String city, String lname_initial, String dateInString, String gender, String id) {

        Intent inn = new Intent(ActivitySignupPatientTwo.this,ActivitySignupPatientThree.class);
        inn.putExtra("name", name)
                .putExtra("email", email)
                .putExtra("password", password)
                .putExtra("city",city)
                .putExtra("lname",lname_initial)
                .putExtra("dob",dateInString)
                .putExtra("gender",gender)
                .putExtra("state_id",id);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0,0);
        startActivity(inn);

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

                AndyUtils.showToast(ActivitySignupPatientTwo.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page_id", "1");

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
