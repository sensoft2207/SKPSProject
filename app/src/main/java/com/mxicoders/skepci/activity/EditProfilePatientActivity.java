package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.SpinnerAdapter;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 9/8/17.
 */

public class EditProfilePatientActivity extends AppCompatActivity {

    String id;

    CommanClass cc;

    Dialog dialog;

    SpinnerAdapter customAdapter;

    LinearLayout male,female;
    TextView tv_patient_male,tv_patient_female;
    TextView tv_patient_submit_profile,tvDobHead,tvLastnameHead;

    EditText edPatientName,edPatientLname,edPatientCity;
    EditText edOldPass,edNewPass,edConfirmPass;

    Spinner spPatientState;
    Spinner spPatientDay,spPatientMonth,spPatientYear;

    String pName,lName,city;

    String convertDay;
    String convertMonth;
    String convertYear;

    String convertMale,convertFemale;

    String dateInString;

    String oldPass,newPass,confirmNewPass;

    ArrayAdapter<String> adapter;

    public  static String gender = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String[] day = { "01","02","03","04","05","06","07","08","09",
            "10","11","12","13","14","15","16","17","18","19","20","21","22","23","24",
            "25","26","27","28","29","30","31"};

    String[] month = {"01","02","03","04","05","06","07","08","09",
            "10","11","12"};

    String[] year;

    String[] state = {"Gujarat", "Punjab",
            "Haryana", "Chennai" };

    ProgressDialog pDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.edit_profile_patient);


        cc = new CommanClass(EditProfilePatientActivity.this);

        initView();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(EditProfilePatientActivity.this,getString(R.string.no_internet));
        }
        else {
            getProfileData();
        }


    }

    private void initView() {

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 1900; i <= thisYear; i++) {
            stringArrayList.add(i+""); //add to arraylist
        }

        year = stringArrayList.toArray(new String[stringArrayList.size()]);

        edPatientName = (EditText)findViewById(R.id.ed_patient_name);
        edPatientLname = (EditText)findViewById(R.id.ed_patient_last_name_initial);
        edPatientCity = (EditText)findViewById(R.id.ed_patient_city);

        spPatientDay = (Spinner)findViewById(R.id.sp_patient_day);
        spPatientMonth = (Spinner)findViewById(R.id.sp_patient_month);
        spPatientYear = (Spinner)findViewById(R.id.sp_patient_uear);
        spPatientState = (Spinner)findViewById(R.id.sp_patient_state);

        male = (LinearLayout)findViewById(R.id.ln_patient_male);
        female = (LinearLayout)findViewById(R.id.ln_patient_female);

        tv_patient_male = (TextView)findViewById(R.id.tv_patient_male);
        tv_patient_female = (TextView)findViewById(R.id.tv_patient_female);


        tvDobHead = (TextView)findViewById(R.id.tv_dob_profile);
        tvLastnameHead = (TextView)findViewById(R.id.tv_lastname_head_profile);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        if (tv_patient_male.getText().equals("Masculino")){

            gender = "Male";

        }else {

            gender = "Female";
        }

        tv_patient_submit_profile = (TextView)findViewById(R.id.tv_submit_patient_profile);


        tv_patient_female.setTextColor(Color.parseColor("#000000"));

        customAdapter=new SpinnerAdapter(this,day);
        spPatientDay.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,month);
        spPatientMonth.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,year);
        spPatientYear.setAdapter(customAdapter);


        imgToolOne.setVisibility(View.GONE);
        tooName.setText(getString(R.string.edit_profile));
        imgToolTwo.setBackground(getResources().getDrawable(R.drawable.change_pass));
        imgToolTwo.setVisibility(View.INVISIBLE);
        tooName.setPadding(75, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(EditProfilePatientActivity.this);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.change_password_dialog);

                dialog.show();

                ImageView close_dialogg = (ImageView)dialog.findViewById(R.id.close_dialog);

                edOldPass = (EditText)dialog.findViewById(R.id.ed_old_pass);
                edNewPass = (EditText)dialog.findViewById(R.id.ed_new_pass);
                edConfirmPass = (EditText)dialog.findViewById(R.id.ed_new_confirm_pass);

                TextView tvOkay = (TextView) dialog.findViewById(R.id.tv_okay_dialog);
                TextView  tvNo = (TextView) dialog.findViewById(R.id.tv_cancle_dialog);
                TextView changeHead = (TextView) dialog.findViewById(R.id.tv_change_pass_head);


                tvOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ChangePassword();

                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                close_dialogg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
            }
        });

        spPatientDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                convertDay = String.valueOf(customAdapter.getItem(position));

                convertDay = day[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPatientMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                convertMonth = String.valueOf(customAdapter.getItem(position));

                convertMonth = month[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPatientYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                convertYear = String.valueOf(customAdapter.getItem(position));

                convertYear = year[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                gender = "Male";

                male.setBackground(getResources().getDrawable(R.drawable.male_female_back));
                female.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));

                tv_patient_male.setTextColor(Color.parseColor("#FFFFFF"));
                tv_patient_female.setTextColor(Color.parseColor("#000000"));

            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                gender = "Female";

                male.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
                female.setBackground(getResources().getDrawable(R.drawable.male_female_back));

                tv_patient_male.setTextColor(Color.parseColor("#000000"));
                tv_patient_female.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });

        tv_patient_submit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditProfile();

                printDateInMyFormat (convertDay, convertMonth, convertYear);
            }
        });


    }

    public void printDateInMyFormat (String date, String month, String year) {
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
    }

    private void getProfileData() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PROFILE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("user_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jobj = dataArray.getJSONObject(i);


                                    edPatientName.setText(jobj.getString("name"));
                                    edPatientLname.setText(jobj.getString("last_name_initial"));
                                    edPatientCity.setText(jobj.getString("city"));

                                    dateInString = jobj.getString("birth_date");

//                                    date = dateInString;

                                    setupDate(dateInString);

                                    if (jobj.getString("gender").contains("Male")){

                                        male.setBackground(getResources().getDrawable(R.drawable.male_female_back));
                                        female.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));

                                        tv_patient_male.setTextColor(Color.parseColor("#FFFFFF"));
                                        tv_patient_female.setTextColor(Color.parseColor("#000000"));

                                    }else {

                                        male.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
                                        female.setBackground(getResources().getDrawable(R.drawable.male_female_back));

                                        tv_patient_male.setTextColor(Color.parseColor("#000000"));
                                        tv_patient_female.setTextColor(Color.parseColor("#FFFFFF"));
                                    }

                                    getState();

                                    Log.i("dob", jobj.getString("birth_date"));

                                    Log.i("dob",convertYear.toString());
                                    Log.i("dob",convertMonth.toString());
                                    Log.i("dob",convertDay.toString());


                                }



                            }else{
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
                AndyUtils.showToast(EditProfilePatientActivity.this,getString(R.string.ws_error));
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
                                adapter= new ArrayAdapter<String>(EditProfilePatientActivity.this,android.R.layout.simple_spinner_item, items);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPatientState.setAdapter(adapter);


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
                AndyUtils.showToast(EditProfilePatientActivity.this,getString(R.string.ws_error));
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

    private void setupDate(String s) {
        String[] dateTempParts;
        dateTempParts= s.split("-");

        convertYear = dateTempParts[0];
        convertMonth = dateTempParts[1];
        convertDay = dateTempParts[2];

        int posDay=getDayId(convertDay);
        int posMonth=getMonthId(convertMonth);
        int posYear=getYearId(convertYear);

        spPatientDay.setSelection(posDay);
        spPatientMonth.setSelection(posMonth);
        spPatientYear.setSelection(posYear);

    }

    private int getYearId(String convertYear) {

        int value=0;

        for (int i = 0; i < year.length; i++) {
            if(convertYear.equals(year[i]+"")){
                value=i;
            }
        }

        return value;
    }

    private int getMonthId(String convertMonth) {

        int value=0;

        for (int i = 0; i < month.length; i++) {
            if(convertMonth.equals(month[i]+"")){
                value=i;
            }
        }

        return value;
    }

    private int getDayId(String convertDay) {


        int value=0;

        for (int i = 0; i < day.length; i++) {
            if(convertDay.equals(day[i]+"")){
                value=i;
            }
        }

        return value;
    }

    private void EditProfile() {

        pName = edPatientName.getText().toString().trim();
        lName = edPatientLname.getText().toString().trim();
        city = edPatientCity.getText().toString().trim();
        convertMale = tv_patient_male.getText().toString();
        convertFemale = tv_patient_female.getText().toString();

        dateInString = convertDay + "-" + convertMonth + "-" + convertYear;


        pDialog = new ProgressDialog(EditProfilePatientActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        PostEditProfileData(pName,lName,city,convertMale,convertFemale,dateInString);
    }

    private void PostEditProfileData(final String pName, final String lName,final String city,
                                     String convertMale, String convertFemale, final String dateInString) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.EDIT_PROFILE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profileupdated", response);

                        jsonParseGetprofile(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(EditProfilePatientActivity.this,getString(R.string.ws_error));
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", pName);
                params.put("last_name_initial", lName);
                params.put("city", city);

                params.put("gender", gender);

                params.put("birth_date", dateInString);
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("state_id", id);
                params.put("device_id", Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
                params.put("user_type", "Patient");


                Log.i("dateeee", dateInString.toString());

                Log.i("request signup", params.toString());

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

    private void jsonParseGetprofile(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error",e.toString());
        }
    }

    private void ChangePassword() {

        oldPass = edOldPass.getText().toString().trim();
        newPass = edNewPass.getText().toString().trim();
        confirmNewPass = edConfirmPass.getText().toString().trim();
        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(this,getString(R.string.no_internet));
        } else if (oldPass.equals("")) {

            AndyUtils.showToast(this,getString(R.string.enter_old_pass));
        } else if (newPass.equals("")) {
            AndyUtils.showToast(this,getString(R.string.enter_new_pass));
        } else if (confirmNewPass.equals("")) {
            AndyUtils.showToast(this,getString(R.string.enter_confirm_new_pass));
        } else if (!confirmNewPass.matches(newPass)) {
            AndyUtils.showToast(this,getString(R.string.pass_not_match2));
        } else {
            ChangePasswordJson(oldPass,newPass);
        }
    }

    private void ChangePasswordJson(final String oldPass, final String newPass) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHANGE_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changepassword", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

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
                params.put("old_password", oldPass);
                params.put("new_password", newPass);
                Log.e("request:change_password", params.toString());
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
