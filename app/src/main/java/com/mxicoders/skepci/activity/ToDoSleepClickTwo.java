package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.SleepTableData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
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
import java.util.Map;


/**
 * Created by mxicoders on 17/8/17.
 */

public class ToDoSleepClickTwo extends AppCompatActivity implements View.OnClickListener {


    String currentDate = "", nextDate = "";
    CommanClass cc;
    ProgressDialog pDialog;

    ArrayList<SleepTableData> list;
    ArrayList<SleepTableData> currentList;
    ArrayList<SleepTableData> nextList;

    TextView tv_next_date, tv_current_date;

    LinearLayout ll_main;

    LinearLayout lnCurrent1, lnCurrent2, lnCurrent3, lnCurrent4, lnCurrent5, lnCurrent6, lnCurrent7, lnCurrent8, lnCurrent9, lnCurrent10,
            lnCurrent11, lnCurrent12, lnCurrent13, lnCurrent14, lnCurrent15, lnCurrent16, lnCurrent17, lnCurrent18, lnCurrent19, lnCurrent20,
            lnCurrent21, lnCurrent22, lnCurrent23, lnCurrent24;

    LinearLayout lnNext1, lnNext2, lnNext3, lnNext4, lnNext5, lnNext6, lnNext7, lnNext8, lnNext9, lnNext10,
            lnNext11, lnNext12, lnNext13, lnNext14, lnNext15, lnNext16, lnNext17, lnNext18, lnNext19, lnNext20,
            lnNext21, lnNext22, lnNext23, lnNext24;

    TextView lnHour1, lnHour2, lnHour3, lnHour4, lnHour5, lnHour6, lnHour7, lnHour8, lnHour9, lnHour10,
            lnHour11, lnHour12, lnHour13, lnHour14, lnHour15, lnHour16, lnHour17, lnHour18, lnHour19, lnHour20,
            lnHour21, lnHour22, lnHour23, lnHour24;

    TextView tvSub;

    //    boolean iscolor = true;
    boolean iscolor1 = false, iscolor2 = false, iscolor3 = false, iscolor4 = false, iscolor5 = false, iscolor6 = false, iscolor7 = false, iscolor8 = false, iscolor9 = false, iscolor10 = false, iscolor11 = false, iscolor12 = false, iscolor13 = false, iscolor14 = false, iscolor15 = false, iscolor16 = false, iscolor17 = false, iscolor18 = false, iscolor19 = false, iscolor20 = false, iscolor21 = false, iscolor22 = false, iscolor23 = false, iscolor24 = false, iscolor25 = false, iscolor26 = false, iscolor27 = false, iscolor28 = false, iscolor29 = false, iscolor30 = false, iscolor31 = false, iscolor32 = false, iscolor33 = false, iscolor34 = false, iscolor35 = false, iscolor36 = false, iscolor37 = false, iscolor38 = false, iscolor39 = false, iscolor40 = false, iscolor41 = false, iscolor42 = false, iscolor43 = false, iscolor44 = false, iscolor45 = false, iscolor46 = false, iscolor47 = false, iscolor48 = false;
    boolean isselect = true;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne, imgToolTwo, imgToolBack;


    static final String[] MOBILE_OS = new String[48];

    String taskId,date_time,date_time_next,assign_date,assign_next_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.sleep_todo_list);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        taskId = getIntent().getStringExtra("task_id");
        assign_date = getIntent().getStringExtra("assign_date");
        date_time = getIntent().getStringExtra("date");

        Log.e("task_id",taskId);
        Log.e("assign_date",assign_date);
        Log.e("date_time",date_time);

        tvSub = (TextView) findViewById(R.id.tv_save_submit);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);


        tooName = (TextView) findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView) findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView) findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView) findViewById(R.id.img_tool_two);
        tv_current_date = (TextView) findViewById(R.id.tv_current_date);
        tv_next_date = (TextView) findViewById(R.id.tv_next_date);


        initCurrent();
        initNext();
        initHour();
        initDate();

        imgToolOne.setVisibility(View.GONE);
        tooName.setText(getString(R.string.sleep));
        tooName.setPadding(75, 0, 0, 0);


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backToDo = new Intent(ToDoSleepClickTwo.this,TodoPatientMenuActivity.class);
                startActivity(backToDo);
                finish();
            }
        });

        list = new ArrayList<>();

        tvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentList = new ArrayList<SleepTableData>();
                nextList = new ArrayList<SleepTableData>();

                int count = 0;
                for (SleepTableData s : list) {

                    if (s.isSelected()) {

                        if (s.getDate().equals(tv_current_date.getText().toString())) {
                            currentList.add(s);
                        } else if (s.getDate().equals(tv_next_date.getText().toString())) {
                            nextList.add(s);
                        }
                        count++;
                    }
                }

                if (count == 0) {
                    cc.showToast("Please Enter Data");
                } else {
                    makeJsonCallForSleepData();
                }

                Log.e("@@List Size", count + "");

            }
        });
    }

    private void initDate() {

        SimpleDateFormat formatNext = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatNextDisplay = new SimpleDateFormat("dd/MM");

        Date date = null;
        try {
            date = formatNext.parse(assign_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        assign_next_date = formatNext.format(calendar.getTime());
        date_time_next = formatNextDisplay.format(calendar.getTime());

        Log.e("assign_next_date", assign_next_date);


        Log.e("date_time_next", date_time_next);

        tv_current_date.setText(date_time);
        tv_next_date.setText(date_time_next);

    }

    private void makeJsonCallForSleepData() {
        pDialog = new ProgressDialog(ToDoSleepClickTwo.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_SLEEP,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();

                        jsonParseSleepResponse(response);
                        Log.e("response:login", response);

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ToDoSleepClickTwo.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                for (int i = 0; i < currentList.size(); i++) {
                    SleepTableData sdata = currentList.get(i);
                    params.put("sleep_data[0][" + i + "]", sdata.getHour());
                }

                for (int i = 0; i < nextList.size(); i++) {
                    SleepTableData sdata = nextList.get(i);
                    params.put("sleep_data[1][" + i + "]", sdata.getHour());
                }

                params.put("sleep_date[0]", assign_date);
                params.put("sleep_date[1]", assign_next_date);
                params.put("sleep_assign_id", taskId);

                Log.e("@@request SleepData", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.e("@@@request header", headers.toString());


                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseSleepResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                Intent backToDo = new Intent(ToDoSleepClickTwo.this,TodoPatientMenuActivity.class);

                startActivity(backToDo);
                finish();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.toString());
        }
    }


    private void initCurrent() {

        lnCurrent1 = (LinearLayout) findViewById(R.id.ln_current_one);
        lnCurrent2 = (LinearLayout) findViewById(R.id.ln_current_two);
        lnCurrent3 = (LinearLayout) findViewById(R.id.ln_current_three);
        lnCurrent4 = (LinearLayout) findViewById(R.id.ln_current_four);
        lnCurrent5 = (LinearLayout) findViewById(R.id.ln_current_five);
        lnCurrent6 = (LinearLayout) findViewById(R.id.ln_current_six);
        lnCurrent7 = (LinearLayout) findViewById(R.id.ln_current_seven);
        lnCurrent8 = (LinearLayout) findViewById(R.id.ln_current_8);
        lnCurrent9 = (LinearLayout) findViewById(R.id.ln_current_9);
        lnCurrent10 = (LinearLayout) findViewById(R.id.ln_current_10);
        lnCurrent11 = (LinearLayout) findViewById(R.id.ln_current_11);
        lnCurrent12 = (LinearLayout) findViewById(R.id.ln_current_12);
        lnCurrent13 = (LinearLayout) findViewById(R.id.ln_current_13);
        lnCurrent14 = (LinearLayout) findViewById(R.id.ln_current_14);
        lnCurrent15 = (LinearLayout) findViewById(R.id.ln_current_15);
        lnCurrent16 = (LinearLayout) findViewById(R.id.ln_current_16);
        lnCurrent17 = (LinearLayout) findViewById(R.id.ln_current_17);
        lnCurrent18 = (LinearLayout) findViewById(R.id.ln_current_18);
        lnCurrent19 = (LinearLayout) findViewById(R.id.ln_current_19);
        lnCurrent20 = (LinearLayout) findViewById(R.id.ln_current_20);
        lnCurrent21 = (LinearLayout) findViewById(R.id.ln_current_21);
        lnCurrent22 = (LinearLayout) findViewById(R.id.ln_current_22);
        lnCurrent23 = (LinearLayout) findViewById(R.id.ln_current_23);
        lnCurrent24 = (LinearLayout) findViewById(R.id.ln_current_24);


        lnCurrent1.setOnClickListener(this);
        lnCurrent2.setOnClickListener(this);
        lnCurrent3.setOnClickListener(this);
        lnCurrent4.setOnClickListener(this);
        lnCurrent5.setOnClickListener(this);
        lnCurrent6.setOnClickListener(this);
        lnCurrent7.setOnClickListener(this);
        lnCurrent8.setOnClickListener(this);
        lnCurrent9.setOnClickListener(this);
        lnCurrent10.setOnClickListener(this);
        lnCurrent11.setOnClickListener(this);
        lnCurrent12.setOnClickListener(this);
        lnCurrent13.setOnClickListener(this);
        lnCurrent14.setOnClickListener(this);
        lnCurrent15.setOnClickListener(this);
        lnCurrent16.setOnClickListener(this);
        lnCurrent17.setOnClickListener(this);
        lnCurrent18.setOnClickListener(this);
        lnCurrent19.setOnClickListener(this);
        lnCurrent20.setOnClickListener(this);
        lnCurrent21.setOnClickListener(this);
        lnCurrent22.setOnClickListener(this);
        lnCurrent23.setOnClickListener(this);
        lnCurrent24.setOnClickListener(this);

    }


    private void initNext() {

        lnNext1 = (LinearLayout) findViewById(R.id.ln_next_one);
        lnNext2 = (LinearLayout) findViewById(R.id.ln_next_two);
        lnNext3 = (LinearLayout) findViewById(R.id.ln_next_three);
        lnNext4 = (LinearLayout) findViewById(R.id.ln_next_four);
        lnNext5 = (LinearLayout) findViewById(R.id.ln_next_five);
        lnNext6 = (LinearLayout) findViewById(R.id.ln_next_six);
        lnNext7 = (LinearLayout) findViewById(R.id.ln_next_seven);
        lnNext8 = (LinearLayout) findViewById(R.id.ln_next_8);
        lnNext9 = (LinearLayout) findViewById(R.id.ln_next_9);
        lnNext10 = (LinearLayout) findViewById(R.id.ln_next_10);
        lnNext11 = (LinearLayout) findViewById(R.id.ln_next_11);
        lnNext12 = (LinearLayout) findViewById(R.id.ln_next_12);
        lnNext13 = (LinearLayout) findViewById(R.id.ln_next_13);
        lnNext14 = (LinearLayout) findViewById(R.id.ln_next_14);
        lnNext15 = (LinearLayout) findViewById(R.id.ln_next_15);
        lnNext16 = (LinearLayout) findViewById(R.id.ln_next_16);
        lnNext17 = (LinearLayout) findViewById(R.id.ln_next_17);
        lnNext18 = (LinearLayout) findViewById(R.id.ln_next_18);
        lnNext19 = (LinearLayout) findViewById(R.id.ln_next_19);
        lnNext20 = (LinearLayout) findViewById(R.id.ln_next_20);
        lnNext21 = (LinearLayout) findViewById(R.id.ln_next_21);
        lnNext22 = (LinearLayout) findViewById(R.id.ln_next_22);
        lnNext23 = (LinearLayout) findViewById(R.id.ln_next_23);
        lnNext24 = (LinearLayout) findViewById(R.id.ln_next_24);

        lnNext1.setOnClickListener(this);
        lnNext2.setOnClickListener(this);
        lnNext3.setOnClickListener(this);
        lnNext4.setOnClickListener(this);
        lnNext5.setOnClickListener(this);
        lnNext6.setOnClickListener(this);
        lnNext7.setOnClickListener(this);
        lnNext8.setOnClickListener(this);
        lnNext9.setOnClickListener(this);
        lnNext10.setOnClickListener(this);
        lnNext11.setOnClickListener(this);
        lnNext12.setOnClickListener(this);
        lnNext13.setOnClickListener(this);
        lnNext14.setOnClickListener(this);
        lnNext15.setOnClickListener(this);
        lnNext16.setOnClickListener(this);
        lnNext17.setOnClickListener(this);
        lnNext18.setOnClickListener(this);
        lnNext19.setOnClickListener(this);
        lnNext20.setOnClickListener(this);
        lnNext21.setOnClickListener(this);
        lnNext22.setOnClickListener(this);
        lnNext23.setOnClickListener(this);
        lnNext24.setOnClickListener(this);

    }

    private void initHour() {

        lnHour1 = (TextView) findViewById(R.id.ln_hour_0);
        lnHour2 = (TextView) findViewById(R.id.ln_hour_1);
        lnHour3 = (TextView) findViewById(R.id.ln_hour_2);
        lnHour4 = (TextView) findViewById(R.id.ln_hour_3);
        lnHour5 = (TextView) findViewById(R.id.ln_hour_4);
        lnHour6 = (TextView) findViewById(R.id.ln_hour_5);
        lnHour7 = (TextView) findViewById(R.id.ln_hour_6);
        lnHour8 = (TextView) findViewById(R.id.ln_hour_7);
        lnHour9 = (TextView) findViewById(R.id.ln_hour_8);
        lnHour10 = (TextView) findViewById(R.id.ln_hour_9);
        lnHour11 = (TextView) findViewById(R.id.ln_hour_10);
        lnHour12 = (TextView) findViewById(R.id.ln_hour_11);
        lnHour13 = (TextView) findViewById(R.id.ln_hour_12);
        lnHour14 = (TextView) findViewById(R.id.ln_hour_13);
        lnHour15 = (TextView) findViewById(R.id.ln_hour_14);
        lnHour16 = (TextView) findViewById(R.id.ln_hour_15);
        lnHour17 = (TextView) findViewById(R.id.ln_hour_16);
        lnHour18 = (TextView) findViewById(R.id.ln_hour_17);
        lnHour19 = (TextView) findViewById(R.id.ln_hour_18);
        lnHour20 = (TextView) findViewById(R.id.ln_hour_19);
        lnHour21 = (TextView) findViewById(R.id.ln_hour_20);
        lnHour22 = (TextView) findViewById(R.id.ln_hour_21);
        lnHour23 = (TextView) findViewById(R.id.ln_hour_22);
        lnHour24 = (TextView) findViewById(R.id.ln_hour_23);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ln_current_one:

                if (!iscolor1) {
                    iscolor1 = true;
                    lnCurrent1.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData1 = new SleepTableData();
                    sleepTableData1.setDate(tv_current_date.getText().toString());
                    sleepTableData1.setHour(0 + "");
                    sleepTableData1.setSelected(true);
                    list.add(sleepTableData1);
                    Log.e("@@InFirst", "isselect");
                } else {
                    lnCurrent1.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor1 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("0")) {
                            s.setSelected(false);
                        }
                    }
                    Log.e("@@InFirst", "isselectNot");
                }

                break;
            case R.id.ln_current_two:
                Log.e("@@InSec", "");

                if (!iscolor2) {
                    iscolor2 = true;
                    lnCurrent2.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData2 = new SleepTableData();
                    sleepTableData2.setDate(tv_current_date.getText().toString());
                    sleepTableData2.setHour(1 + "");
                    sleepTableData2.setSelected(true);
                    list.add(sleepTableData2);
                    Log.e("@@InSec", "isselect");
                } else {
                    lnCurrent2.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor2 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("1")) {
                            s.setSelected(false);
                        }
                    }
                    Log.e("@@InSec", "isselectNot");
                }

                break;
            case R.id.ln_current_three:

                if (!iscolor3) {
                    iscolor3 = true;
                    lnCurrent3.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData3 = new SleepTableData();
                    sleepTableData3.setDate(tv_current_date.getText().toString());
                    sleepTableData3.setHour(2 + "");
                    sleepTableData3.setSelected(true);
                    list.add(sleepTableData3);
                } else {
                    lnCurrent3.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor3 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("2")) {
                            s.setSelected(false);
                        }
                    }
                }


                break;
            case R.id.ln_current_four:


                if (!iscolor4) {
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(3 + "");
                    sleepTableData.setSelected(true);
                    iscolor4 = true;
                    lnCurrent4.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    list.add(sleepTableData);
                } else {
                    lnCurrent4.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor4 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("3")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_five:


                if (!iscolor5) {

                    SleepTableData sleepTableData5 = new SleepTableData();
                    sleepTableData5.setDate(tv_current_date.getText().toString());
                    sleepTableData5.setHour(4 + "");
                    sleepTableData5.setSelected(true);
                    iscolor5 = true;
                    lnCurrent5.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    list.add(sleepTableData5);
                } else {
                    lnCurrent5.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor5 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("4")) {
                            s.setSelected(false);
                        }
                    }
                }


                break;
            case R.id.ln_current_six:

                if (!iscolor6) {
                    iscolor6 = true;
                    lnCurrent6.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(5 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent6.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor6 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("5")) {
                            s.setSelected(false);
                        }
                    }
                }


                break;
            case R.id.ln_current_seven:

                if (!iscolor7) {
                    iscolor7 = true;
                    lnCurrent7.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(6 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent7.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor7 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("6")) {
                            s.setSelected(false);
                        }
                    }
                }


                break;
            case R.id.ln_current_8:

                if (!iscolor8) {
                    iscolor8 = true;
                    lnCurrent8.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(7 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent8.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor8 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("7")) {
                            s.setSelected(false);
                        }
                    }
                }


                break;
            case R.id.ln_current_9:
                if (!iscolor9) {
                    iscolor9 = true;
                    lnCurrent9.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(8 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent9.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor9 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("8")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_10:

                if (!iscolor10) {
                    iscolor10 = true;
                    lnCurrent10.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(9 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent10.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor10 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("9")) {
                            s.setSelected(false);
                        }
                    }
                }

                break;
            case R.id.ln_current_11:

                if (!iscolor11) {
                    iscolor11 = true;
                    lnCurrent11.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(10 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent11.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor11 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("10")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_12:

                if (!iscolor12) {
                    iscolor12 = true;
                    lnCurrent12.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(11 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent12.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor12 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("11")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_13:

                if (!iscolor13) {
                    iscolor13 = true;
                    lnCurrent13.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(12 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent13.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor13 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("12")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_14:

                if (!iscolor14) {
                    iscolor14 = true;
                    lnCurrent14.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(13 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent14.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor14 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("13")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_15:
                if (!iscolor15) {
                    iscolor15 = true;
                    lnCurrent15.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(14 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent15.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor15 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("14")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_16:
                if (!iscolor16) {
                    iscolor16 = true;
                    lnCurrent16.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(15 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent16.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor16 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("15")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_17:

                if (!iscolor17) {
                    iscolor17 = true;
                    lnCurrent17.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(16 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent17.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor17 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("16")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_18:

                if (!iscolor18) {
                    iscolor18 = true;
                    lnCurrent18.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(17 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent18.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor18 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("17")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_19:

                if (!iscolor19) {
                    iscolor19 = true;
                    lnCurrent19.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(18 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent19.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor19 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("18")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_20:

                if (!iscolor20) {
                    iscolor20 = true;
                    lnCurrent20.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(19 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent20.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor20 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("19")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_21:
                if (!iscolor21) {
                    iscolor21 = true;
                    lnCurrent21.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(20 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent21.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor21 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("20")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_22:

                if (!iscolor22) {
                    iscolor22 = true;
                    lnCurrent22.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(21 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent22.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor22 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("21")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_23:

                if (!iscolor23) {
                    iscolor23 = true;
                    lnCurrent23.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(22 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent23.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor23 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("22")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_current_24:

                if (!iscolor24) {
                    iscolor24 = true;
                    lnCurrent24.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_current_date.getText().toString());
                    sleepTableData.setHour(23 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnCurrent24.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor24 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_current_date.getText().toString()) && s.getHour().equals("23")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_one:

                if (!iscolor25) {
                    iscolor25 = true;
                    lnNext1.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(0 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext1.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor25 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("0")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_two:

                if (!iscolor26) {
                    iscolor26 = true;
                    lnNext2.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(1 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext2.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor26 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("1")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_three:

                if (!iscolor27) {
                    iscolor27 = true;
                    lnNext3.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(2 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext3.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor27 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("2")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_four:

                if (!iscolor28) {
                    iscolor28 = true;
                    lnNext4.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(3 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext4.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor28 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("3")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_five:

                if (!iscolor29) {
                    iscolor29 = true;
                    lnNext5.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(4 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext5.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor29 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("4")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_six:

                if (!iscolor30) {
                    iscolor30 = true;
                    lnNext6.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(5 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext6.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor30 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("5")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_seven:

                if (!iscolor31) {
                    iscolor31 = true;
                    lnNext7.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(6 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext7.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor31 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("6")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_8:

                if (!iscolor32) {
                    iscolor32 = true;
                    lnNext8.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(7 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext8.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor32 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("7")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_9:

                if (!iscolor33) {
                    iscolor33 = true;
                    lnNext9.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(8 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext9.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor33 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("8")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_10:

                if (!iscolor34) {
                    iscolor34 = true;
                    lnNext10.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(9 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext10.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor34 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("9")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_11:

                if (!iscolor35) {
                    iscolor35 = true;
                    lnNext11.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(10 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext11.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor35 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("10")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_12:

                if (!iscolor36) {
                    iscolor36 = true;
                    lnNext12.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(11 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext12.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor36 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("11")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_13:

                if (!iscolor37) {
                    iscolor37 = true;
                    lnNext13.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(12 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext13.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor37 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("12")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_14:

                if (!iscolor38) {
                    iscolor38 = true;
                    lnNext14.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(13 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext14.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor38 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("13")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_15:
                if (!iscolor39) {
                    iscolor39 = true;
                    lnNext15.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(14 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext15.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor39 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("14")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_16:
                if (!iscolor40) {
                    iscolor40 = true;
                    lnNext16.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(15 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext16.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor40 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("15")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_17:
                if (!iscolor41) {
                    iscolor41 = true;
                    lnNext17.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(16 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext17.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor41 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("16")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_18:
                if (!iscolor42) {
                    iscolor42 = true;
                    lnNext18.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(17 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext18.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor42 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("17")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_19:
                if (!iscolor43) {
                    iscolor43 = true;
                    lnNext19.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(18 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext19.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor43 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("18")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_20:
                if (!iscolor44) {
                    iscolor44 = true;
                    lnNext20.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(19 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext20.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor44 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("19")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_21:
                if (!iscolor45) {
                    iscolor45 = true;
                    lnNext21.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(20 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext21.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor45 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("20")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_22:
                if (!iscolor46) {
                    iscolor46 = true;
                    lnNext22.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(21 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext22.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor46 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("21")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_23:
                if (!iscolor47) {
                    iscolor47 = true;
                    lnNext23.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(22 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext23.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor47 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("22")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
            case R.id.ln_next_24:
                if (!iscolor48) {
                    iscolor48 = true;
                    lnNext24.setBackground(getResources().getDrawable(R.drawable.sleep_click_back));
                    SleepTableData sleepTableData = new SleepTableData();
                    sleepTableData.setDate(tv_next_date.getText().toString());
                    sleepTableData.setHour(23 + "");
                    sleepTableData.setSelected(true);
                    list.add(sleepTableData);

                } else {
                    lnNext24.setBackground(getResources().getDrawable(R.drawable.sleep_grid_border));
                    iscolor48 = false;
                    for (SleepTableData s : list) {
                        if (s.getDate().equals(tv_next_date.getText().toString()) && s.getHour().equals("23")) {
                            s.setSelected(false);
                        }
                    }
                }
                break;
        }

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

