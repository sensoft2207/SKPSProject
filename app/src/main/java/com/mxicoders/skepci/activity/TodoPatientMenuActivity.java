package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mxicoders.skepci.adapter.ToDoAnyoneAdapter;
import com.mxicoders.skepci.adapter.ToDoTodayAdapter;
import com.mxicoders.skepci.adapter.ToDoYesterdayAdapter;
import com.mxicoders.skepci.model.ToDoData;
import com.mxicoders.skepci.model.ToDoListData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModelTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxicoders on 24/7/17.
 */

public class TodoPatientMenuActivity extends AppCompatActivity {

    CommanClass cc;

    public static List<ToDoListData> yesterday_task;
    public static List<ToDoListData> today_task;

    RecyclerView mRecyclerViewYesterday,mRecyclerViewToday,mRecyclerViewAnyone;

    ToDoYesterdayAdapter mAdapterYesterday;
    ToDoTodayAdapter mAdapterToday;
    ToDoAnyoneAdapter mAdapterAnyone;

//    ArrayList<ToDoData> list,list2,list3;
    ArrayList<ToDoData> list3;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    Calendar c,c2;
    String currentDate,preDate;



    String[] days = new String[] {"SUN","MON", "TUE", "WED", "THU", "FRI", "SAT"};

    ProgressDialog pDialog;

    LinearLayout ln_no_to_do_1,ln_no_to_do_2;

    public TodoPatientMenuActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_patient_to_list);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);


        currentDate = new SimpleDateFormat("dd").format(new Date());
        String dayOfTheWeek =  new SimpleDateFormat("EEE").format(new Date());

        mRecyclerViewYesterday = (RecyclerView)findViewById(R.id.rc_yesterday_todo);
        mRecyclerViewYesterday.setLayoutManager(new LinearLayoutManager(TodoPatientMenuActivity.this));
        mRecyclerViewYesterday.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewToday = (RecyclerView)findViewById(R.id.rc_today_todo);
        mRecyclerViewToday.setLayoutManager(new LinearLayoutManager(TodoPatientMenuActivity.this));
        mRecyclerViewToday.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewAnyone = (RecyclerView)findViewById(R.id.rc_anytime_todo);
        mRecyclerViewAnyone.setLayoutManager(new LinearLayoutManager(TodoPatientMenuActivity.this));
        mRecyclerViewAnyone.setItemAnimator(new DefaultItemAnimator());

        ln_no_to_do_1 = (LinearLayout) findViewById(R.id.ln_no_to_do_1);
        ln_no_to_do_2 = (LinearLayout) findViewById(R.id.ln_no_to_do_2);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.todo_list));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(TodoPatientMenuActivity.this,getString(R.string.no_internet));
        }
        else {

            getToDoList();
        }




        list3 = new ArrayList<ToDoData>();
        list3.add(new ToDoData(getString(R.string.rpd3),currentDate,dayOfTheWeek));
      //  list3.add(new ToDoData("Sleep",currentDate,dayOfTheWeek));

        mAdapterAnyone = new ToDoAnyoneAdapter(list3,R.layout.todo_yesterday_rc_item,TodoPatientMenuActivity.this);
        mRecyclerViewAnyone.setAdapter(mAdapterAnyone);

    }

    public void getToDoList() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.TODO_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:todolist data", response);

                        List<ItemModelTwo> listcontent = new ArrayList<>();
                        yesterday_task = new ArrayList<>();
                        today_task = new ArrayList<>();

                        String doneT = null;

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("patient_to_do_list");


                                ln_no_to_do_1.setVisibility(View.GONE);
                                ln_no_to_do_2.setVisibility(View.GONE);

                                mRecyclerViewToday.setVisibility(View.VISIBLE);
                                mRecyclerViewYesterday.setVisibility(View.VISIBLE);

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    doneT = jsonObject1.getString("status");

                                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        date = inFormat.parse(jsonObject1.getString("assign_date"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                                    SimpleDateFormat outFormat_date = new SimpleDateFormat("dd");
                                    SimpleDateFormat outFormat_fulldate = new SimpleDateFormat("dd/MM");
                                    String d_day = outFormat.format(date);
                                    String d_date= outFormat_date.format(date);
                                    String full_date = outFormat_fulldate.format(date);



                                    String Todays_currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


                                    c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    c.add(Calendar.DATE, -1);

                                    preDate = df.format(c.getTime());

                                    Log.e("@@Currentdate",Todays_currentDate);
                                    Log.e("@@Predate",preDate);

                                    if (jsonObject1.getString("task_type").equals("Postais")){

                                    }else{

                                        if (jsonObject1.getString("assign_date").equals(Todays_currentDate) && jsonObject1.getString("status").equals("Desfeita")) {

                                            ToDoListData model = new ToDoListData();

                                            model.setName(jsonObject1.getString("task_type"));
                                            model.setTask_id(jsonObject1.getString("id"));
                                            model.setAssign_date(jsonObject1.getString("assign_date"));
                                            model.setTiming(jsonObject1.getString("timing"));

                                            if (jsonObject1.has("schedule_status")){

                                                model.setSchedule_status(jsonObject1.getString("schedule_status"));

                                                Log.e("S_status",jsonObject1.getString("schedule_status"));

                                            }else {

                                                model.setSchedule_status("No");
                                            }

                                            if (jsonObject1.has("situation")){

                                                model.setSituation(jsonObject1.getString("situation"));

                                                Log.e("situation",jsonObject1.getString("situation"));

                                            }else {

                                                model.setSituation("nill");
                                            }

                                            if (jsonObject1.has("thoughts")){

                                                model.setThoughts(jsonObject1.getString("thoughts"));

                                                Log.e("thoughts",jsonObject1.getString("thoughts"));

                                            }else {

                                                model.setThoughts("nill");
                                            }


                                            if (jsonObject1.has("emotion_level")){

                                                model.setEmotion_level(jsonObject1.getString("emotion_level"));

                                                Log.e("emotion_levelTT",jsonObject1.getString("emotion_level"));

                                            }else {

                                            }
                                            model.setD_date(d_date);
                                            model.setD_day(d_day);
                                            model.setFull_date(full_date);



                                            today_task.add(model);

                                        } else if (jsonObject1.getString("assign_date").equals(preDate) && jsonObject1.getString("status").equals("Desfeita")) {
                                            ToDoListData model = new ToDoListData();

                                            model.setName(jsonObject1.getString("task_type"));
                                            model.setTask_id(jsonObject1.getString("id"));
                                            model.setAssign_date(jsonObject1.getString("assign_date"));
                                            model.setTiming(jsonObject1.getString("timing"));

                                            if (jsonObject1.has("schedule_status")){

                                                model.setSchedule_status(jsonObject1.getString("schedule_status"));

                                                Log.e("S_status",jsonObject1.getString("schedule_status"));

                                            }else {

                                                model.setSchedule_status("No");
                                            }

                                            if (jsonObject1.has("situation")){

                                                model.setSituation(jsonObject1.getString("situation"));

                                                Log.e("situation",jsonObject1.getString("situation"));

                                            }else {

                                                model.setSituation("nill");
                                            }

                                            if (jsonObject1.has("thoughts")){

                                                model.setThoughts(jsonObject1.getString("thoughts"));

                                                Log.e("thoughts",jsonObject1.getString("thoughts"));

                                            }else {

                                                model.setThoughts("nill");
                                            }

                                            if (jsonObject1.has("emotion_level")){

                                                model.setEmotion_level(jsonObject1.getString("emotion_level"));

                                                Log.e("emotion_levelYY",jsonObject1.getString("emotion_level"));

                                            }else {

                                            }


                                            model.setD_date(d_date);
                                            model.setD_day(d_day);
                                            model.setFull_date(full_date);



                                            yesterday_task.add(model);


                                        }
                                    }

                                }

                                if(mAdapterToday != null){
                                    mAdapterToday = null;
                                }

                                if(mAdapterYesterday != null){
                                    mAdapterYesterday = null;
                                }


                                    LinearLayoutManager layoutManager= new LinearLayoutManager(TodoPatientMenuActivity.this);
                                    mAdapterToday = new ToDoTodayAdapter(today_task,R.layout.todo_yesterday_rc_item,TodoPatientMenuActivity.this);

                                    mRecyclerViewToday.setLayoutManager(layoutManager);

                                    mRecyclerViewToday.setAdapter(mAdapterToday);


                                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(TodoPatientMenuActivity.this);
                                    mAdapterYesterday = new ToDoYesterdayAdapter(yesterday_task,R.layout.todo_yesterday_rc_item,TodoPatientMenuActivity.this);

                                    mRecyclerViewYesterday.setLayoutManager(layoutManager2);

                                    mRecyclerViewYesterday.setAdapter(mAdapterYesterday);

                                Log.e("YesterdayTask", String.valueOf(yesterday_task.size()));
                                Log.e("TodayTask", String.valueOf(today_task.size()));


                                if (yesterday_task.size() == 0){

                                    ln_no_to_do_1.setVisibility(View.VISIBLE);

                                    mRecyclerViewYesterday.setVisibility(View.GONE);

                                }else {


                                }

                                if (today_task.size() == 0){

                                    ln_no_to_do_2.setVisibility(View.VISIBLE);

                                    mRecyclerViewToday.setVisibility(View.GONE);

                                }else {


                                }


                            }else if (jsonObject.getString("status").equals("402")){


                                ln_no_to_do_1.setVisibility(View.VISIBLE);
                                ln_no_to_do_2.setVisibility(View.VISIBLE);

                                mRecyclerViewToday.setVisibility(View.GONE);
                                mRecyclerViewYesterday.setVisibility(View.GONE);

                                pDialog.dismiss();

                            }else {
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
                AndyUtils.showToast(TodoPatientMenuActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request todolist data", params.toString());
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
