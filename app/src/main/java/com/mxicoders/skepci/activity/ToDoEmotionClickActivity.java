package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.ToDoEmotionGridAdapter;
import com.mxicoders.skepci.model.EmotionResponseData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 25/7/17.
 */

public class ToDoEmotionClickActivity extends AppCompatActivity {

    CommanClass cc;
    static Dialog dialog;
    static ProgressDialog pDialog;




    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    TextView tvSave;

    EditText edSituation,edThoughtOne,edThoughtTwo,edThoughtThree;
    LinearLayout ln_dialogpart_one,ln_dialogpart_two;

    GridView gridView;

    String emotionName,intensity,situation;

    int progressZero;

    SeekBar seekIntensity;

    ToDoEmotionGridAdapter gridAdapter;

    String taskId,time,date_time,emotion_level,situation_main,thoughts_main;

    String thoughtOne,thoughtTwo,thoughtThree;

    ArrayList<EmotionResponseData> emotion_list;

    Dialog dialog1,dialog3;

   /* static final String[] MOBILE_OS = new String[] {
            "angry", "anxious","ashamed", "bored", "calm", "confused", "despair", "disappointed","disgusted",
            "excited", "fear", "guilty", "happy", "hopefull","lonely",
            "love", "mistrust", "proud", "sad", "saudades"};*/

    static final String[] MOBILE_OS = new String[] {
            "Tranqullidade", "Tedlo","Saudades", "Vergonha", "Orgulho", "Tristeza", "Amor", "Solidao","Esperanca",
            "Decepcao", "Alegria", "Confusao", "Entusiansmo", "Nojo","Ansiedade",
            "Preacupacao", "Raiva", "Desconfianca", "Medo", "Cupla"};


    static final String[] MOBILE_OS_PRIMARY = new String[] {
            "Alegria", "Amor","Tristeza", "Medo", "Nojo"};

    static final String[] MOBILE_OS_PRIMARY_SECONDARY = new String[] {
            "Tranquilidade", "Saudades","Orgulho","Esperança","Entusiasmado","Entediado","Vergonha","Solidão","Decepção","Confusão","Preocupação","Desconfiança","Culpa","Ansiedade","Raiva","Desespero"};



    TextView tvSub,max,min;
    TextView tvDateTime;

    public ToDoEmotionClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.frag_patient_todo_emotion_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(ToDoEmotionClickActivity.this);



        emotion_list = new ArrayList<>();

        taskId = getIntent().getStringExtra("task_id");
        time = getIntent().getStringExtra("time");
        date_time = getIntent().getStringExtra("date");
        emotion_level = getIntent().getStringExtra("emotion_level");
        situation_main = getIntent().getStringExtra("situation_main");
        thoughts_main = getIntent().getStringExtra("thoughts_main");


        Log.i("task_id",taskId);
        Log.i("time",time);


        gridView = (GridView)findViewById(R.id.gridview1);




        if (emotion_level.equals("Primário")){

            gridAdapter = new ToDoEmotionGridAdapter(ToDoEmotionClickActivity.this, MOBILE_OS_PRIMARY);
            gridView.setAdapter(gridAdapter);

        }else if (emotion_level.equals("Primário e secundário")){

            gridAdapter = new ToDoEmotionGridAdapter(ToDoEmotionClickActivity.this, MOBILE_OS_PRIMARY_SECONDARY);
            gridView.setAdapter(gridAdapter);

        }


        /*gridView.setAdapter(new ToDoEmotionGridAdapter(ToDoEmotionClickActivity.this, MOBILE_OS));*/

        tvSave = (TextView)findViewById(R.id.tv_save_submit);
        tvDateTime = (TextView)findViewById(R.id.tvDateTime);

        tvDateTime.setText(date_time + " " + time);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.emotion));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backToDo = new Intent(ToDoEmotionClickActivity.this,TodoPatientMenuActivity.class);

                startActivity(backToDo);
                finish();
            }
        });

        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                emotionName = String.valueOf(gridAdapter.getItem(position));

                emotionName = MOBILE_OS[position];

                Log.i("EmotionName",emotionName.toString());
            }
        });*/

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog3 = new Dialog(ToDoEmotionClickActivity.this);
                dialog3.setCancelable(false);
                dialog3.setContentView(R.layout.intensity_dialog);
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                dialog3.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                tvSub = (TextView)dialog3.findViewById(R.id.tv_submit_int_dialog);
                seekIntensity = (SeekBar)dialog3.findViewById(R.id.seekBar1);
                max = (TextView)dialog3.findViewById(R.id.max_seek);
                //min = (TextView)dialog.findViewById(R.id.min_seek);
                ImageView close = (ImageView)dialog3.findViewById(R.id.close_dialog);

                dialog3.show();

                seekIntensity.setProgress(0);
                seekIntensity.setMax(10);

                seekIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        progress = progress / 1;
                        progress = progress * 1;

                        progressZero = progress;

                        intensity = String.valueOf(progressZero);

//                        intensity = max.getText().toString();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        seekIntensity.incrementProgressBy(1);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                tvSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (situation_main.equals("No") && thoughts_main.equals("No")){

                            situation = "";
                            thoughtOne = "";
                            thoughtTwo = "";
                            thoughtThree = "";

                            EmotionResponseData ed = new EmotionResponseData();
                            ed.setT_one(thoughtOne);
                            ed.setT_two(thoughtTwo);
                            ed.setT_three(thoughtThree);
                            emotion_list.add(ed);

                            emotionName = cc.loadPrefString("emotion_name2");

                            postEmotionResponse(emotionName,intensity,taskId,time,situation);

                            Log.e("@SSSS",situation_main);
                            Log.e("@TTTT",thoughts_main);

                        }else {

                            Log.e("@SSSS",situation_main);
                            Log.e("@TTTT",thoughts_main);

                            dialog1 = new Dialog(ToDoEmotionClickActivity.this);
                            dialog1.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog1.setContentView(R.layout.situation_dialog);

                            TextView tvSub = (TextView)dialog1.findViewById(R.id.tv_submit_emotion_dialog);
                            ImageView close2 = (ImageView)dialog1.findViewById(R.id.close_dialog);

                            edSituation  = (EditText)dialog1.findViewById(R.id.ed_situation_dialog);
                            edThoughtOne  = (EditText)dialog1.findViewById(R.id.ed_situation_thougt_one);
                            edThoughtTwo  = (EditText)dialog1.findViewById(R.id.ed_situation_thougt_two);
                            edThoughtThree  = (EditText)dialog1.findViewById(R.id.ed_situationthougt_three);

                            ln_dialogpart_one  = (LinearLayout) dialog1.findViewById(R.id.ln_dialogpart_one);
                            ln_dialogpart_two  = (LinearLayout) dialog1.findViewById(R.id.ln_dialogpart_two);

                            if (situation_main.equals("Yes") && thoughts_main.equals("Yes")){

                                ln_dialogpart_one.setVisibility(View.VISIBLE);
                                ln_dialogpart_two.setVisibility(View.VISIBLE);

                            }else if (situation_main.equals("Yes")){

                                ln_dialogpart_one.setVisibility(View.VISIBLE);
                                ln_dialogpart_two.setVisibility(View.GONE);

                            }else if (thoughts_main.equals("Yes")){

                                ln_dialogpart_two.setVisibility(View.VISIBLE);
                                ln_dialogpart_one.setVisibility(View.GONE);

                            }else {

                            }


                            dialog1.show();
                            dialog3.dismiss();

                            tvSub.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    postEmotionValidation();

                                }
                            });

                            close2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog1.dismiss();
                                }
                            });

                        }


                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog3.dismiss();
                    }
                });
            }
        });


    }

    private void postEmotionValidation() {


        situation = edSituation.getText().toString().trim();
        thoughtOne = edThoughtOne.getText().toString().trim();
        thoughtTwo = edThoughtTwo.getText().toString().trim();
        thoughtThree = edThoughtThree.getText().toString().trim();


        if (intensity == null){

            int i = 0;

            intensity = String.valueOf(i);
        }else {

            intensity = String.valueOf(progressZero);
        }

        if (situation_main.equals("Yes") && thoughts_main.equals("Yes")){

            if (thoughtOne.isEmpty() && thoughtTwo.isEmpty() && thoughtThree.isEmpty()){

                Log.e("All","....Empty");

            }else {

                EmotionResponseData ed = new EmotionResponseData();
                ed.setT_one(thoughtOne);
                ed.setT_two(thoughtTwo);
                ed.setT_three(thoughtThree);
                emotion_list.add(ed);
                Log.e("All","....NotEmpty");
            }

            if (!cc.isConnectingToInternet()) {
                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.no_internet));

            }else if (situation.equals("")) {

                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.situation));

            }else if (thoughtOne.isEmpty() && thoughtTwo.isEmpty() && thoughtThree.isEmpty()) {

                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.thoughts));
            }
            else{

                emotionName = cc.loadPrefString("emotion_name2");

                postEmotionResponse(emotionName,intensity,taskId,time,situation);

            }

        }else if (situation_main.equals("Yes")){

            EmotionResponseData ed = new EmotionResponseData();
            ed.setT_one(thoughtOne);
            ed.setT_two(thoughtTwo);
            ed.setT_three(thoughtThree);
            emotion_list.add(ed);

            if (!cc.isConnectingToInternet()) {
                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.no_internet));

            }else if (situation.equals("")) {

                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.situation));

            }
            else{

                emotionName = cc.loadPrefString("emotion_name2");

                postEmotionResponse(emotionName,intensity,taskId,time,situation);

            }


        }else if (thoughts_main.equals("Yes")){

            EmotionResponseData ed = new EmotionResponseData();
            ed.setT_one(thoughtOne);
            ed.setT_two(thoughtTwo);
            ed.setT_three(thoughtThree);
            emotion_list.add(ed);

            if (!cc.isConnectingToInternet()) {
                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.no_internet));

            }else if (thoughtOne.isEmpty() && thoughtTwo.isEmpty() && thoughtThree.isEmpty()) {

                AndyUtils.showToast(ToDoEmotionClickActivity.this,getString(R.string.thoughts));
            }
            else{

                emotionName = cc.loadPrefString("emotion_name2");

                postEmotionResponse(emotionName,intensity,taskId,time,situation);

            }

        }else {

        }


    }

    private void postEmotionResponse(final String emotionName, final String intensity,final String taskId,final String time,final String situation) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_EMOTION_RESPONSE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:emotioresponse", response);

                        jsonParseEmotionResponse(response);
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
                params.put("emotions_name",emotionName);
                params.put("intensity",intensity);
                params.put("time",time);
                params.put("situation",situation);

                if (emotion_level.equals("Primário")){

                    params.put("emotion_type","1");

                }else if (emotion_level.equals("Primário e secundário")){

                    params.put("emotion_type","0");

                }


               /* if (emotionName.equals("Tranqullidade")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Tedlo")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Saudades")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Vergonha")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Orgulho")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Tristeza")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Amor")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Solidao")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Esperanca")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Decepcao")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Alegria")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Confusao")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Entusiansmo")){

                    params.put("emotion_type","1");

                }else if (emotionName.equals("Nojo")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Ansiedade")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Preacupacao")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Raiva")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Desconfianca")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Medo")){

                    params.put("emotion_type","0");

                }else if (emotionName.equals("Cupla")){

                    params.put("emotion_type","0");

                }else {

                    Log.e("EmotionType","Not found");
                }*/




                int k = 0;

                for (int i = 0; i < emotion_list.size(); i++) {

                    EmotionResponseData sdata = emotion_list.get(i);

                    params.put("thought[" + k++ + "]",sdata.getT_one());

                    params.put("thought[" + k++ + "]",sdata.getT_two());

                    params.put("thought[" + k++ + "]",sdata.getT_three());
                }

                Log.i("request emotion", params.toString());

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

    private void jsonParseEmotionResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {


                cc.showToast(jsonObject.getString("message"));

                if (situation_main.equals("No") && thoughts_main.equals("No")){

                    dialog3.dismiss();

                }else {

                    dialog1.dismiss();
                }

                Intent backToDo = new Intent(ToDoEmotionClickActivity.this,TodoPatientMenuActivity.class);

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


