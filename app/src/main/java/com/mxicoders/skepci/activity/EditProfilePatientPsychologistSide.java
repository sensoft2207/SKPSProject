package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
 * Created by mxi on 7/10/17.
 */

public class EditProfilePatientPsychologistSide extends AppCompatActivity {

    CommanClass cc;

    EditText ed_patient_name,ed_patient_last_name,ed_patient_email,ed_patient_city;
    String patient_name,patient_last_name,patient_email,patient_gender,patient_city,patient_birth_date;

    String p_name,p_last_name,p_email,p_gender,p_city;

    Dialog dialog;

    SpinnerAdapter customAdapter;

    LinearLayout male,female;
    TextView tv_male,tv_female;
    TextView tv_submit_profile;

    Spinner spDay,spMonth,spYear;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String[] day = { "01","02","03","04","05","06","07","08","09",
            "10","11","12","13","14","15","16","17","18","19","20","21","22","23","24",
            "25","26","27","28","29","30","31"};

    String[] month = {"01","02","03","04","05","06","07","08","09",
            "10","11","12"};

    String[] year;

    ProgressDialog pDialog,pDialog2;

    String convertDay;
    String convertMonth;
    String convertYear;

    String convertMale,convertFemale;

    String dateInString;

    String date;
    String[] dateParts;

    ArrayAdapter<String> adapter;

    public  static String gender = "";


    public EditProfilePatientPsychologistSide() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.edit_profile_patient_psychologist_side);

        cc = new CommanClass(this);

        init();

        getProfileData();

    }

    private void init() {

       /* patient_name = cc.loadPrefString("p_namee2");
        patient_last_name = cc.loadPrefString("p_namee_last");
        patient_email = cc.loadPrefString("p_email");
        patient_gender = cc.loadPrefString("p_gender");
        patient_city = cc.loadPrefString("p_city");
        dateInString = cc.loadPrefString("p_dob");

        Log.e("profile_data",dateInString);*/


        ed_patient_name = (EditText)findViewById(R.id.ed_patient_name);
        ed_patient_last_name = (EditText)findViewById(R.id.ed_patient_last_name_initial);
        ed_patient_email = (EditText)findViewById(R.id.ed_patient_email);
        ed_patient_city = (EditText)findViewById(R.id.ed_patient_city);

       /* ed_patient_name.setText(patient_name);
        ed_patient_last_name.setText(patient_last_name);
        ed_patient_email.setText(patient_email);
        ed_patient_city.setText(patient_city);*/

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        ArrayList<String> stringArrayList = new ArrayList<String>();
        for (int i = 1900; i <= thisYear; i++) {
            stringArrayList.add(i+""); //add to arraylist
        }

        year = stringArrayList.toArray(new String[stringArrayList.size()]);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        imgToolTwo.setBackground(getResources().getDrawable(R.drawable.change_pass));
        tooName.setText(getString(R.string.edit_profile));
        tooName.setPadding(75, 0, 0, 0);

        spDay = (Spinner)findViewById(R.id.sp_patient_day);
        spMonth = (Spinner)findViewById(R.id.sp_patient_month);
        spYear = (Spinner)findViewById(R.id.sp_patient_uear);


        male = (LinearLayout)findViewById(R.id.ln_patient_male);
        female = (LinearLayout)findViewById(R.id.ln_patient_female);

        tv_male = (TextView)findViewById(R.id.tv_patient_male);
        tv_female = (TextView)findViewById(R.id.tv_patient_female);


       /* if (patient_gender.contains("Male")){

            male.setBackground(getResources().getDrawable(R.drawable.male_female_back));
            female.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));

            tv_male.setTextColor(Color.parseColor("#FFFFFF"));
            tv_female.setTextColor(Color.parseColor("#000000"));

        }else {

            male.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
            female.setBackground(getResources().getDrawable(R.drawable.male_female_back));

            tv_male.setTextColor(Color.parseColor("#000000"));
            tv_female.setTextColor(Color.parseColor("#FFFFFF"));
        }*/


        if (tv_male.getText().equals("Masculino")){

            gender = "Male";

        }else {

            gender = "Female";
        }

        tv_submit_profile = (TextView)findViewById(R.id.tv_submit_patient_profile);


        tv_female.setTextColor(Color.parseColor("#000000"));

        customAdapter=new SpinnerAdapter(this,day);
        spDay.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,month);
        spMonth.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,year);
        spYear.setAdapter(customAdapter);


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

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
        });




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


        tv_submit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditProfile();

                printDateInMyFormat (convertDay, convertMonth, convertYear);
            }
        });


    }

    private void EditProfile() {

        p_name = ed_patient_name.getText().toString().trim();
        p_last_name = ed_patient_last_name.getText().toString().trim();
        p_email = ed_patient_email.getText().toString().trim();
        p_city = ed_patient_city.getText().toString().trim();
        convertMale = tv_male.getText().toString();
        convertFemale = tv_female.getText().toString();

        dateInString = convertYear + "-" + convertMonth + "-" + convertDay;

        Log.i("MainDateOFBirth",dateInString);


        PostEditProfileData(p_name,p_last_name,p_email,p_city,convertMale,convertFemale,dateInString);


    }

    private void getProfileData() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PROFILE_PATIENT_PSYCHOLOGIST_SIDE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("patient_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jobj = dataArray.getJSONObject(i);


                                    ed_patient_name.setText(jobj.getString("name"));
                                    ed_patient_last_name.setText(jobj.getString("last_name_initial"));
                                    ed_patient_email.setText(jobj.getString("email"));
                                    ed_patient_city.setText(jobj.getString("city"));

                                    dateInString = jobj.getString("birth_date");

//                                    date = dateInString;

                                    setupDate(dateInString);

//                                    dateParts = date.split("-");
//
//                                    convertYear = dateParts[0];
//                                    convertMonth = dateParts[1];
//                                    convertDay = dateParts[2];

                                   /* DateModel dm = new DateModel();

                                    dm.setYear(convertYear);
                                    dm.setYear(convertMonth);
                                    dm.setYear(convertDay);*/

                                    if (jobj.getString("gender").contains("Male")){

                                        male.setBackground(getResources().getDrawable(R.drawable.male_female_back));
                                        female.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));

                                        tv_male.setTextColor(Color.parseColor("#FFFFFF"));
                                        tv_female.setTextColor(Color.parseColor("#000000"));

                                    }else {

                                        male.setBackground(getResources().getDrawable(R.drawable.male_female_back_two));
                                        female.setBackground(getResources().getDrawable(R.drawable.male_female_back));

                                        tv_male.setTextColor(Color.parseColor("#000000"));
                                        tv_female.setTextColor(Color.parseColor("#FFFFFF"));
                                    }


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
                AndyUtils.showToast(EditProfilePatientPsychologistSide.this,getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("patient_id", cc.loadPrefString("patient_id_main"));
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

    private void PostEditProfileData(final String p_name, final String p_last_name, final String p_email, final String p_city,
                                     String convertMale, String convertFemale, final String dateInString) {

        pDialog2 = new ProgressDialog(EditProfilePatientPsychologistSide.this);
        pDialog2.setMessage(getString(R.string.please_wait));
        pDialog2.setIndeterminate(false);
        pDialog2.setCancelable(false);
        pDialog2.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.EDIT_PROFILE_PATIENT_PSYCHOLOGIST_SIDE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profileupdated", response);

                        jsonParseGetprofile(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog2.dismiss();
                AndyUtils.showToast(EditProfilePatientPsychologistSide.this,getString(R.string.ws_error));
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_psycologist_id", cc.loadPrefString("user_id"));
                params.put("name", p_name);
                params.put("last_name_initial",p_last_name);
                params.put("email", p_email);
                params.put("gender", gender);
                params.put("birth_date", dateInString);
                params.put("city", p_city);

                params.put("user_patient_id", cc.loadPrefString("patient_id_main"));


                Log.i("dateeee", dateInString.toString());
                Log.i("patientID", cc.loadPrefString("patient_id_main"));

                Log.i("request edit", params.toString());

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

            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                getProfileData();

                pDialog2.dismiss();
            } else {
                pDialog2.dismiss();
                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();

            pDialog2.dismiss();
            Log.e("Login Error",e.toString());
        }
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

    private void setupDate(String s) {


        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(s);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String newFormat = formatter.format(testDate);
        System.out.println(".....Date..."+newFormat);

        String[] dateTempParts;
        dateTempParts= newFormat.split("-");

        convertYear = dateTempParts[0];
        convertMonth = dateTempParts[1];
        convertDay = dateTempParts[2];

        int posDay=getDayId(convertDay);
        int posMonth=getMonthId(convertMonth);
        int posYear=getYearId(convertYear);

        spDay.setSelection(posDay);
        spMonth.setSelection(posMonth);
        spYear.setSelection(posYear);

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
