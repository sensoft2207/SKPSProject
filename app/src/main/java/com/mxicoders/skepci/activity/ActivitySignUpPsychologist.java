package com.mxicoders.skepci.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.SpinnerAdapter;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.sessionmanager.SessionManager;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mxicoders on 10/7/17.
 */

public class ActivitySignUpPsychologist extends AppCompatActivity {

    private SessionManager session;
    CommanClass cc;
    private RequestQueue requestQueue;

    Calendar dobChoose = Calendar.getInstance();

    ProgressDialog pDialog;

    private int lastInteractionTime;
    private Boolean isScreenOff = false;


    LinearLayout male,female;
    TextView tv_male,tv_female;
    TextView tvSignupNext,tvDobHead;
    TextView tv_date,tv_month,tv_year;

    EditText edName,edEmail,edPass,edCpass;

    String dateInString;


    String[] year;

    String convertMale,convertFemale;

    String name,email,pass,cpass;

    String gender = "";

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_VALIDATION_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    public ActivitySignUpPsychologist(){

        pattern = Pattern.compile(DATE_VALIDATION_PATTERN);

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dobChoose.set(Calendar.YEAR, year);
            dobChoose.set(Calendar.MONTH, monthOfYear);
            dobChoose.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_psychologist);

        cc = new CommanClass(ActivitySignUpPsychologist.this);

        requestQueue = Volley.newRequestQueue(this);



        edName = (EditText)findViewById(R.id.ed_username);
        edEmail = (EditText)findViewById(R.id.ed_email);
        edPass = (EditText)findViewById(R.id.ed_pass);
        edCpass = (EditText)findViewById(R.id.ed_cpass);


        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {

                    Log.e("TAG", "e1 focused");

                } else {

                    if (!cc.isConnectingToInternet()) {

                        AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.no_internet));

                    }else if (!AndyUtils.eMailValidation(edEmail.getText().toString())) {
                        AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.email_phy));

                    }else {

                        emailExitOrNotWS(edEmail.getText().toString());

                        Log.e("TAG", "e1 not focused");
                    }

                }
            }
        });

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 1900; i <= thisYear; i++) {
            stringArrayList.add(i+""); //add to arraylist
        }

        year = stringArrayList.toArray(new String[stringArrayList.size()]);

        male = (LinearLayout)findViewById(R.id.ln_male);
        female = (LinearLayout)findViewById(R.id.ln_female);

        tv_male = (TextView)findViewById(R.id.tv_male);
        tv_female = (TextView)findViewById(R.id.tv_female);

        tvSignupNext = (TextView)findViewById(R.id.tv_signup_next);
        tvDobHead = (TextView)findViewById(R.id.tv_psy_dob_head);

        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_month = (TextView)findViewById(R.id.tv_month);
        tv_year = (TextView)findViewById(R.id.tv_year);

        tv_female.setTextColor(Color.parseColor("#000000"));



        if (tv_male.getText().equals("Masculino")){

            gender = "Male";

        }else {

            gender = "Female";
        }
        male.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                gender = "Male";

                male.setBackground(getResources().getDrawable(R.drawable.male_female_back));
                female.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
                tv_male.setTextColor(Color.parseColor("#FFFFFF"));
                tv_female.setTextColor(Color.parseColor("#000000"));

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                gender = "Female";

                male.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
                female.setBackground(getResources().getDrawable(R.drawable.male_female_back));

                tv_male.setTextColor(Color.parseColor("#000000"));
                tv_female.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });

        tvSignupNext.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                SignUpValidation();


            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignUpPsychologist.this,date, dobChoose
                        .get(Calendar.YEAR), dobChoose.get(Calendar.MONTH),
                        dobChoose.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignUpPsychologist.this,date, dobChoose
                        .get(Calendar.YEAR), dobChoose.get(Calendar.MONTH),
                        dobChoose.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignUpPsychologist.this,date, dobChoose
                        .get(Calendar.YEAR), dobChoose.get(Calendar.MONTH),
                        dobChoose.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }




    private void updateLabel() {

        String myDate = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myDate, Locale.US);

        dateInString = sdf.format(dobChoose.getTime());

        String[] items1 = dateInString.split("-");
        String date1 = items1[0];
        String month = items1[1];
        String year = items1[2];

        tv_date.setText(date1);
        tv_month.setText(month);
        tv_year.setText(year);

        Log.e("@@DOBCHOOSE",sdf.format(dobChoose.getTime()));


    }

    private void SignUpValidation() {

        name = edName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        pass = edPass.getText().toString().trim();
        cpass = edCpass.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.no_internet));
        } else if (name.equals("")) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.naame));

            edName.requestFocus();
        } else if (!AndyUtils.eMailValidation(edEmail.getText().toString())) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.email_phy));

            edEmail.requestFocus();
        } else if (pass.equals("")) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.pass_phy));

            edPass.requestFocus();
        } else if (cpass.equals("")) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.cpass_phy));

            edCpass.requestFocus();

        }else if (!cpass.equals(pass)) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.pass_not_match));

            edCpass.requestFocus();
        }else if (dateInString == null) {
            AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.date));
        }
        else {

            emailExitOrNotWSFinalSubmit(email);

        }
    }

    private void emailExitOrNotWS(final String email_id) {

        pDialog = new ProgressDialog(ActivitySignUpPsychologist.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHECK_EXITS_EMAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:checkemail", response);

                        jsonParseCheckEmail(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_id", email_id);

                Log.i("request check email", params.toString());

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

    private void emailExitOrNotWSFinalSubmit(final String email_id) {

        pDialog = new ProgressDialog(ActivitySignUpPsychologist.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHECK_EXITS_EMAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:checkemail", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                gotoNextActivity(name,email,pass,dateInString);

                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("checkemail Error",e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ActivitySignUpPsychologist.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email_id", email_id);

                Log.i("request check email", params.toString());

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

    private void jsonParseCheckEmail(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("checkemail Error",e.toString());
        }
    }

    private void gotoNextActivity(String name, String email, String pass, String dateInString) {

        Intent inn = new Intent(ActivitySignUpPsychologist.this,ActivitySignupPhyschologistTwo.class);
        inn.putExtra("name", name)
                .putExtra("email", email)
                .putExtra("password", pass)
                .putExtra("b_date",dateInString)
                .putExtra("gender",gender);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0,0);
        startActivity(inn);

    }

    public boolean validate(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){



            matcher.reset();

            if(matcher.find()){

                String dd = matcher.group(1);

                String mm = matcher.group(2);

                int yy = Integer.parseInt(matcher.group(3));

                if (dd.equals("31") &&  (mm.equals("4") || mm .equals("6") || mm.equals("9") ||

                mm.equals("11") || mm.equals("04") || mm .equals("06") ||

                mm.equals("09"))) {

                    return false;

                } else if (mm.equals("2") || mm.equals("02")) {



                    if(yy % 4==0){

                        if(dd.equals("30") || dd.equals("31")){

                            return false;

                        }else{

                            return true;

                        }

                    }else{

                        if(dd.equals("29")||dd.equals("30")||dd.equals("31")){

                            return false;

                        }else{

                            return true;

                        }

                    }

                }else{

                    return true;

                }

            }else{

                return false;

            }

        }else{

            return false;

        }

    }

}
