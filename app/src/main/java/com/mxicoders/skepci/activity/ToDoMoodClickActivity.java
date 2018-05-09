package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.VerticalSeekBar;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mxicoders.skepci.R.id.tvDateTime;

/**
 * Created by mxicoders on 24/7/17.
 */

public class ToDoMoodClickActivity extends AppCompatActivity {

    CommanClass cc;
    static Dialog dialog;
    static ProgressDialog pDialog;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    TextView tvMoodSubmit;
    TextView tvDatetime;

    VerticalSeekBar seekBar;

    String zeroProgress;

    String taskId,time,date_time;

    int progresTwo;

    public ToDoMoodClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.frag_patient_todo_mood_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(ToDoMoodClickActivity.this);

        taskId = getIntent().getStringExtra("task_id");
        time = getIntent().getStringExtra("time");
        date_time = getIntent().getStringExtra("date");

        Log.i("task_id",taskId);
        Log.i("time",time);

        seekBar = (VerticalSeekBar)findViewById(R.id.seekBar1);
        seekBar.setProgress(0);
        seekBar.setMax(20);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        tvMoodSubmit = (TextView)findViewById(R.id.tv_mood_submit);
        tvDatetime = (TextView)findViewById(tvDateTime);

        tvDatetime.setText(date_time + " " + time);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.mood));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToDo = new Intent(ToDoMoodClickActivity.this,TodoPatientMenuActivity.class);

                startActivity(backToDo);
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress = progress / 1;
                progress = progress * 1;

                progress -= 10;

                progresTwo = progress;

                zeroProgress = String.valueOf(progresTwo);

                Log.i("MoodRating",String.valueOf(progresTwo));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                seekBar.incrementProgressBy(1);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        tvMoodSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                postMoodResponseValidation();

            }
        });
    }

    private void postMoodResponseValidation() {


        if (zeroProgress == null){

            int i = -10;

            zeroProgress = String.valueOf(i);
        }else {

            zeroProgress = String.valueOf(progresTwo);
        }


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ToDoMoodClickActivity.this,getString(R.string.no_internet));
        }
        else{
            postMoodResponse(taskId,time,zeroProgress);
        }
    }

    private void postMoodResponse(final String taskId, final String time, final String zeroProgress) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_MOOD_RESPONSE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:moodresponse", response);

                        jsonParseMoodResponse(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("task_id", taskId);
                params.put("rating",zeroProgress);
                params.put("time", time);

                Log.i("request mood", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header mood", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void jsonParseMoodResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                Intent backToDo = new Intent(ToDoMoodClickActivity.this,TodoPatientMenuActivity.class);

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
