package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

public class EditProfileActivity extends AppCompatActivity {

    String id;

    CommanClass cc;

    Dialog dialog;

    SpinnerAdapter customAdapter;

    LinearLayout male,female;
    TextView tv_male,tv_female;
    TextView tv_submit_profile;

    EditText edName,edCrpno,edCfpno,edAddress,edCity,edApproach;

    EditText edOldPass,edNewPass,edConfirmPass;

    Spinner spDay,spMonth,spYear,spState;

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

    String convertDay;
    String convertMonth;
    String convertYear;

    String name,crpno,cpfno,address,city,approach;

    String convertMale,convertFemale;

    String dateInString;

    String oldPass,newPass,confirmNewPass;

    String date;
    String[] dateParts;

    ArrayAdapter<String> adapter;

    public  static String gender = "";


    public EditProfileActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.edit_profile_fragment);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(EditProfileActivity.this);

        initView();
        getProfileData();

    }

    private void initView() {

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
        imgToolTwo.setBackground(getResources().getDrawable(R.drawable.change_pass));
        imgToolTwo.setVisibility(View.INVISIBLE);
        tooName.setText(getString(R.string.edit_profile));
        tooName.setPadding(75, 0, 0, 0);

        edName = (EditText)findViewById(R.id.ed_name);
        edCrpno = (EditText)findViewById(R.id.ed_crp_no);
        edCfpno = (EditText)findViewById(R.id.ed_cpf_no);
        edAddress = (EditText)findViewById(R.id.ed_address);
        edCity = (EditText)findViewById(R.id.ed_city);
        edApproach = (EditText)findViewById(R.id.ed_approach);

        spDay = (Spinner)findViewById(R.id.sp_day);
        spMonth = (Spinner)findViewById(R.id.sp_month);
        spYear = (Spinner)findViewById(R.id.sp_uear);
        spState = (Spinner)findViewById(R.id.sp_state);



        male = (LinearLayout)findViewById(R.id.ln_male);
        female = (LinearLayout)findViewById(R.id.ln_female);

        tv_male = (TextView)findViewById(R.id.tv_male);
        tv_female = (TextView)findViewById(R.id.tv_female);

        if (tv_male.getText().equals("Masculino")){

            gender = "Male";

        }else {

            gender = "Female";
        }

        tv_submit_profile = (TextView)findViewById(R.id.tv_submit_profile);


        tv_female.setTextColor(Color.parseColor("#000000"));

        customAdapter=new SpinnerAdapter(this,day);
        spDay.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,month);
        spMonth.setAdapter(customAdapter);

        customAdapter=new SpinnerAdapter(this,year);
        spYear.setAdapter(customAdapter);

        /*customAdapter=new SpinnerAdapter(getActivity(),state);
        spState.setAdapter(customAdapter);*/


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.change_password_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                dialog.show();

                ImageView close_dialogg = (ImageView)dialog.findViewById(R.id.close_dialog);

                edOldPass = (EditText)dialog.findViewById(R.id.ed_old_pass);
                edNewPass = (EditText)dialog.findViewById(R.id.ed_new_pass);
                edConfirmPass = (EditText)dialog.findViewById(R.id.ed_new_confirm_pass);

                TextView tvOkay = (TextView) dialog.findViewById(R.id.tv_okay_dialog);
                TextView  tvNo = (TextView) dialog.findViewById(R.id.tv_cancle_dialog);

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


                                    edName.setText(jobj.getString("name"));
                                    edCrpno.setText(jobj.getString("crp_number"));
                                    edCfpno.setText(jobj.getString("cpf_number"));
                                    edAddress.setText(jobj.getString("address"));
                                    edCity.setText(jobj.getString("city"));
                                    edApproach.setText(jobj.getString("approach"));

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
                AndyUtils.showToast(EditProfileActivity.this,getString(R.string.no_internet));
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
                                adapter= new ArrayAdapter<String>(EditProfileActivity.this,android.R.layout.simple_spinner_item, items);
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
                AndyUtils.showToast(EditProfileActivity.this,getString(R.string.ws_error));
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

    private void EditProfile() {

        name = edName.getText().toString().trim();
        crpno = edCrpno.getText().toString().trim();
        cpfno = edCfpno.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        city = edCity.getText().toString().trim();
        approach = edApproach.getText().toString();
        convertMale = tv_male.getText().toString();
        convertFemale = tv_female.getText().toString();

        dateInString = convertDay + "-" + convertMonth + "-" + convertYear;


        pDialog = new ProgressDialog(EditProfileActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        PostEditProfileData(name,crpno,cpfno,address,city,approach,convertMale,convertFemale,dateInString);
    }

    private void PostEditProfileData(final String name, final String crpno, final String cpfno,
                                     final String address, final String city, final String approach,
                                     final String convertMale, final String convertFemale,final String dateInString ) {


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
                AndyUtils.showToast(EditProfileActivity.this,getString(R.string.ws_error));
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name);
                params.put("crp_number", crpno);
                params.put("cpf_number", cpfno);
                params.put("address", address);
                params.put("city", city);
                params.put("approach", approach);

                params.put("gender", gender);

                params.put("birth_date", dateInString);
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("state_id", "1");
                params.put("device_id", Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID));
                params.put("user_type", "Psychologist");


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_change_password, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_pass){


            return true;
        }
        return super.onOptionsItemSelected(item);
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