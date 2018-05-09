package com.mxicoders.skepci.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mxicoders on 13/7/17.
 */

public class ActivitySignupPatient extends AppCompatActivity {

    private SessionManager session;
    CommanClass cc;
    private RequestQueue requestQueue;

    Calendar dobChoose = Calendar.getInstance();

    ProgressDialog pDialog;

    LinearLayout male,female;
    public  static TextView tv_male,tv_female;
    TextView signupNext,tvLastnameHead;
    TextView tv_date,tv_month,tv_year;

    String dateInString;

    EditText edName,edLatnameInitial,edEmail,edPassword,edConfirmPassword,edCity;

   // Spinner spDay,spMonth,spYear,spState;
    Spinner spState;

   SpinnerAdapter customAdapter;

   /* String[] day = { "01","02","03","04","05","06","07","08","09",
            "10","11","12","13","14","15","16","17","18","19","20","21","22","23","24",
            "25","26","27","28","29","30","31"};

    String[] month = {"01","02","03","04","05","06","07","08","09",
            "10","11","12"};

    String[] year = {"2000", "2001",
            "2002", "2003" };*/

    String[] statee = {"Gujarat","Punjab","Goa","Diu"};

    /*String convertDay;
    String convertMonth;
    String convertYear;*/

    String convertSate;

    String convertMale,convertFemale;

    String name,email,password,cpassword,city,lname_initial;

    String id;

    String gender = "";

    ArrayAdapter<String> adapter;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_patient);


        cc = new CommanClass(ActivitySignupPatient.this);

        requestQueue = Volley.newRequestQueue(this);

        signupNext = (TextView)findViewById(R.id.tv_signup_next);

        edName = (EditText)findViewById(R.id.ed_name);
        edLatnameInitial = (EditText)findViewById(R.id.ed_last_name_initial);
        edEmail = (EditText)findViewById(R.id.ed_email);
        edPassword = (EditText)findViewById(R.id.ed_password);
        edConfirmPassword = (EditText)findViewById(R.id.ed_confirm_password);
        edCity = (EditText)findViewById(R.id.ed_city);

       /* spDay = (Spinner)findViewById(R.id.sp_day);
        spMonth = (Spinner)findViewById(R.id.sp_month);
        spYear = (Spinner)findViewById(R.id.sp_uear);*/

        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {

                    Log.e("TAG", "e1 focused");

                } else {

                    if (!cc.isConnectingToInternet()) {

                        AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.no_internet));

                    }else if (!AndyUtils.eMailValidation(edEmail.getText().toString())) {
                        AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.email_phy));

                    }else {

                        emailExitOrNotWS(edEmail.getText().toString());

                        Log.e("TAG", "e1 not focused");
                    }

                }
            }
        });


        spState = (Spinner)findViewById(R.id.sp_statee);

        getState();


        /*spDay.setOnItemSelectedListener(this);
        spMonth.setOnItemSelectedListener(this);
        spYear.setOnItemSelectedListener(this);*/


        tv_male = (TextView)findViewById(R.id.tv_male);
        tv_female = (TextView)findViewById(R.id.tv_female);
        tvLastnameHead = (TextView)findViewById(R.id.tv_last_name_head);

        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_month = (TextView)findViewById(R.id.tv_month);
        tv_year = (TextView)findViewById(R.id.tv_year);

        male = (LinearLayout)findViewById(R.id.ln_male);
        female = (LinearLayout)findViewById(R.id.ln_female);

        if (tv_male.getText().equals("Masculino")){

            gender = "Male";


        }else {

            gender = "Female";
        }

        tv_female.setTextColor(Color.parseColor("#000000"));

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


       /* customAdapter=new SpinnerAdapter(ActivitySignupPatient.this,day);
        spDay.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(ActivitySignupPatient.this,month);
        spMonth.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(ActivitySignupPatient.this,year);
        spYear.setAdapter(customAdapter);

        *//*customAdapter=new SpinnerAdapter(ActivitySignupPatient.this,statee);
        spState.setAdapter(customAdapter);*//*


        spDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                convertDay = String.valueOf(customAdapter.getItem(position));

                convertDay = day[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                convertMonth = String.valueOf(customAdapter.getItem(position));

                convertMonth = month[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                convertYear = String.valueOf(customAdapter.getItem(position));

                convertYear = year[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        customAdapter=new SpinnerAdapter(ActivitySignupPatient.this,statee);
        spState.setAdapter(customAdapter);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                convertSate = String.valueOf(customAdapter.getItem(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // printDateInMyFormat(convertDay,convertMonth,convertYear);


        signupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpValidation();
            }
        });

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignupPatient.this,date, dobChoose
                        .get(Calendar.YEAR), dobChoose.get(Calendar.MONTH),
                        dobChoose.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignupPatient.this,date, dobChoose
                        .get(Calendar.YEAR), dobChoose.get(Calendar.MONTH),
                        dobChoose.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ActivitySignupPatient.this,date, dobChoose
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


   /* public void printDateInMyFormat (String date, String month, String year) {
        // This is the format you are requesting for.
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        dateInString = date + "-" + month + "-" + year;
        System.out.println("dateInString : " + dateInString);

        try {

            Date dt = formatter.parse(dateInString);
            System.out.println("My Date ::" + dt);
            System.out.println("My Required Date String :: " + formatter.format(dt));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    private void getState() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,Const.ServiceType.GET_STATE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                JSONArray jsonArray = jsonObject.optJSONArray("state_data");
                                final String[] items = new String[jsonArray.length()];
                                //Iterate the jsonArray and print the info of JSONObjects
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    String name = jsonObject1.optString("state_name").toString();
                                    id = jsonObject1.optString("id").toString();
                                    items[i]=jsonObject1.getString("state_name");


                                    Log.d("nameee", name);
                                    Log.d("id", id);
                                }
                                adapter= new ArrayAdapter<String>(ActivitySignupPatient.this,  android.R.layout.simple_spinner_item, items);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spState.setAdapter(adapter);
                            } else {

                                jsonObject.getString("message");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Login Error",e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("user_token", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());

                Log.i("token",cc.loadPrefString("user_token"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void SignUpValidation() {

        name = edName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        password = edPassword.getText().toString().trim();
        cpassword = edConfirmPassword.getText().toString().trim();
        city = edCity.getText().toString().trim();
        lname_initial = edLatnameInitial.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.no_internet));
        } else if (name.equals("")) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.naame));

            edName.requestFocus();
        } else if (!AndyUtils.eMailValidation(edEmail.getText().toString())) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.email_phy));

            edEmail.requestFocus();
        } else if (password.equals("")) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.pass_phy));

            edPassword.requestFocus();
        } else if (cpassword.equals("")) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.cpass_phy));

            edConfirmPassword.requestFocus();

        }else if (!cpassword.equals(password)) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.pass_not_match));

            edConfirmPassword.requestFocus();
        }else if (dateInString == null) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.date));
        }
        else if (city.equals("")) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.male_v));

            edCity.requestFocus();
        }else if (lname_initial.equals("")) {
            AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.lname));

            edLatnameInitial.requestFocus();
        }

        else {

            emailExitOrNotWSFinalSubmit(email);

        }
    }

    private void emailExitOrNotWS(final String email_id) {

        pDialog = new ProgressDialog(ActivitySignupPatient.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.CHECK_EXITS_EMAIL,
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
                AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.ws_error));
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

        pDialog = new ProgressDialog(ActivitySignupPatient.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.CHECK_EXITS_EMAIL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:checkemail", response);

                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {




                                gotoAgreePage(name,email,password,city,lname_initial,gender,id,dateInString);

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
                AndyUtils.showToast(ActivitySignupPatient.this,getString(R.string.ws_error));
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
            if (jsonObject.getString("status").equals("200")) {

                pDialog.dismiss();

            } else {

                cc.showToast(jsonObject.getString("message"));

                pDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("checkemail Error",e.toString());
        }
    }

    private void gotoAgreePage(String name, String email, String password,
                               String city, String lname_initial, String gender, String id, String dateInString) {

      Intent inn = new Intent(ActivitySignupPatient.this,ActivitySignupPatientTwo.class);
        inn.putExtra("name", name)
                .putExtra("email", email)
                .putExtra("password", password)
                .putExtra("city",city)
                .putExtra("lname",lname_initial)

               /* .putExtra("b_day",convertDay)
                .putExtra("b_month",convertMonth)
                .putExtra("b_year",convertYear)*/
                .putExtra("gender",gender)
                .putExtra("state_id",id)
                .putExtra("b_date",dateInString);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        inn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        inn.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0,0);
        startActivity(inn);

        /*startActivity(new Intent(ActivitySignupPatient.this, ActivitySignupPatientTwo.class)
                .putExtra("name", name)
                .putExtra("email", email)
                .putExtra("password", password)
                .putExtra("city",city)
                .putExtra("lname",lname_initial)
                .putExtra("b_day",convertDay)
                .putExtra("b_month",convertMonth)
                .putExtra("b_year",convertYear)
                .putExtra("gender",gender)
                .putExtra("state_id",id));*/

    }


}
