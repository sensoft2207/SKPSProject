package com.mxicoders.skepci.adapter;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ArticleSleepPatientList;
import com.mxicoders.skepci.activity.WebviewSleep;
import com.mxicoders.skepci.model.ToDoData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxicoders.skepci.activity.SleepPatientlistActivity.date_month_year;

/**
 * Created by mxicoders on 31/7/17.
 */

public class SleepAdapterOne extends RecyclerView.Adapter<SleepAdapterOne.ViewHolder> {

    static CommanClass cc;

    private List<ToDoData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    static Dialog dialog;

    static TextView submit,submit2,submit3;

    static ImageView close;

    static RadioGroup r_age,r_part,r_e_schedule,r_e_schedule_m,r_e_level;
    static CheckBox chOne,chTwo,chThree,chOneS,chTwoS,chThreeS;

    static String radio_age,radio_e_schedule,radio_schedule_m;
    static String radio_part,radio_e_level;
    static String chValueOne = "No",
            chValueTwo = "No",
            chValueThree = "No",
            chValueOneS = "No",
            chValueTwoS = "No",
            chValueThreeS = "No";

    static String[] days ;

    static String patientID;

    public static String dateSelected;

    static ProgressDialog pDialog;

    static ArrayList<String> numbers;

    static String url_compare;


    public SleepAdapterOne(List<ToDoData> countries, int rowLayout, Context context) {

        cc = new CommanClass(context);

        patientID = cc.loadPrefString("patient_id_main");

        http://localhost/skepsi/ws/sleep_data/sleep_data/patient_id


        url_compare = "http://mbdbtechnology.com/projects/skepsi/ws/sleep_data/sleep_data/" + patientID;

        this.countries = countries;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }
    @Override
    public int getItemCount() {
        return countries == null ? 0 : countries.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ToDoData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
//        viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email;
        public ImageView Image;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            Image = (ImageView)itemView.findViewById(R.id.Image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            switch (getPosition()) {
                /*case 0:
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.schedule_task_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    submit = (TextView)dialog.findViewById(R.id.tv_submit_dialog);

                    close = (ImageView)dialog.findViewById(R.id.close_dialog);

                    r_age = (RadioGroup)dialog.findViewById(R.id.rb_age);
                    r_part = (RadioGroup)dialog.findViewById(R.id.rb_part);

                    final RadioButton rb = (RadioButton)dialog.findViewById(R.id.rb_adults);
                    final RadioButton rb2 = (RadioButton)dialog.findViewById(R.id.rb_primary_emotion);

                    chOne = (CheckBox)dialog.findViewById(R.id.chk_one);
                    chTwo = (CheckBox)dialog.findViewById(R.id.chk_two);
                    chThree = (CheckBox)dialog.findViewById(R.id.chk_three);


                    r_age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){
                                case R.id.rb_children:

                                    radio_age = ((RadioButton)dialog.findViewById(r_age.getCheckedRadioButtonId())).getText().toString();

                                    break;
                                case R.id.rb_adults:

                                    radio_age = ((RadioButton)dialog.findViewById(r_age.getCheckedRadioButtonId())).getText().toString();

                                    break;
                            }
                        }
                    });

                    r_part.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){

                                case R.id.rb_primary_emotion:

                                    radio_part = "Primary";

                                    break;
                                case R.id.rb_primary_seco_emotion:

                                    radio_part = "Primary and Secondary";

                                    break;
                                case R.id.rb_terory:

                                    radio_part = "Terclary";

                                    break;
                            }
                        }
                    });

                    chOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueOne = "Yes";

                            } else {

                                chValueOne = "No";
                            }
                        }
                    });

                    chTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueTwo = "Yes";

                            } else {

                                chValueTwo = "No";
                            }
                        }
                    });

                    chThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueThree = "Yes";


                            } else {

                                chValueThree = "No";

                            }
                        }
                    });



                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (dateSelected == null){

//                                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                dateSelected = date;

                            }else {

                                dateSelected = date_month_year;

//                                dateConvert(dateSelected);


                            }

                            postInterviewTask(patientID,radio_age,radio_part,chValueOne,chValueTwo,chValueThree,dateSelected);
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });
                    break;
*/
                case 0:

                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.emotion_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    DisplayMetrics metrics2 = mContext.getResources().getDisplayMetrics();
                    int width2 = metrics2.widthPixels;
                    int height2 = metrics2.heightPixels;
                    dialog.getWindow().setLayout((6 * width2) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    ImageView close2 = (ImageView)dialog.findViewById(R.id.close_dialog);

                    submit2 = (TextView)dialog.findViewById(R.id.tv_submit_dialog_s);


                    r_e_schedule = (RadioGroup)dialog.findViewById(R.id.rb_schedule);
                    r_e_level = (RadioGroup)dialog.findViewById(R.id.rb_level);


                    chOneS = (CheckBox)dialog.findViewById(R.id.chk_one_s);
                    chTwoS = (CheckBox)dialog.findViewById(R.id.chk_two_s);
                    chThreeS = (CheckBox)dialog.findViewById(R.id.chk_three_s);

                    r_e_schedule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){
                                case R.id.rb_once:

                                    radio_e_schedule = "Once";

                                    Log.i("@@1RadioClick",radio_e_schedule);
                                    break;
                                case R.id.rb_daily:

                                    radio_e_schedule = "Daily";
                                    Log.i("@@2RadioClick",radio_e_schedule);
                                    break;

                                case R.id.rb_threex:

                                    radio_e_schedule = "3xdays";

                                    break;
                            }

                        }
                    });

                    r_e_level.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){

                                case R.id.rb_primary_emotion:

                                    radio_e_level = "Primary";

                                    break;
                                case R.id.rb_primary_seco_emotion:

                                    radio_e_level = "Primary and Secondary";

                                    break;
                               /* case R.id.rb_terory:

                                    radio_e_level = "Terclary";

                                    break;*/
                            }
                        }
                    });

                    chOneS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueOneS = "Yes";


                            } else {

                                chValueOneS = "No";
                            }
                        }
                    });

                    chTwoS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueTwoS = "Yes";

                            } else {

                                chValueTwoS = "No";
                            }
                        }
                    });

                    chThreeS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {

                                chValueThreeS = "Yes";


                            } else {

                                chValueThreeS = "No";

                            }
                        }
                    });

                    submit2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (dateSelected == null || date_month_year == null){

                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                dateSelected = date;

                            }else {

                                dateSelected = date_month_year;

                            }

                            postEmotionTask(patientID,radio_e_schedule,radio_e_level,chValueOneS,chValueTwoS,chValueThreeS,dateSelected);
                        }
                    });

                    close2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    break;

                case 1:

                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.mood_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    DisplayMetrics metrics3 = mContext.getResources().getDisplayMetrics();
                    int width3 = metrics3.widthPixels;
                    int height3 = metrics3.heightPixels;
                    dialog.getWindow().setLayout((6 * width3) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    ImageView close3 = (ImageView)dialog.findViewById(R.id.close_dialog);

                    submit3 = (TextView)dialog.findViewById(R.id.tv_submit_dialog_m);

                    r_e_schedule_m = (RadioGroup)dialog.findViewById(R.id.rb_schedule_m);

                    r_e_schedule_m.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                    {
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch(checkedId){
                                case R.id.rb_once_m:

                                    radio_schedule_m = "Once";

                                    break;
                                case R.id.rb_daily_m:

                                    radio_schedule_m = "Daily";

                                    break;

                                case R.id.rb_threex_m:

//                                    radio_schedule_m = ((RadioButton)dialog.findViewById(r_e_schedule_m.getCheckedRadioButtonId())).getText().toString();

                                    radio_schedule_m = "3xdays";
                                    break;
                            }
                        }
                    });

                    submit3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (dateSelected == null || date_month_year == null){

                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                dateSelected = date;

                            }else {

                                dateSelected = date_month_year;

                            }

                            postMoodTask(patientID,radio_schedule_m,dateSelected);
                        }
                    });

                    close3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    break;

                case 2:

                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.card_task_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    DisplayMetrics metrics4 = mContext.getResources().getDisplayMetrics();
                    int width4 = metrics4.widthPixels;
                    int height4 = metrics4.heightPixels;
                    dialog.getWindow().setLayout((6 * width4) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    ImageView close4 = (ImageView)dialog.findViewById(R.id.close_dialog);

                    TextView schedule = (TextView)dialog.findViewById(R.id.tv_schedule_task);

                    schedule.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (dateSelected == null || date_month_year == null){

                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                dateSelected = date;

                            }else {

                                dateSelected = date_month_year;
                            }

                            scheduleCardTask(patientID,dateSelected);

                        }
                    });

                    close4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    break;

                case 3:

                   /* Intent intent = new Intent(mContext, WebviewSleep.class);
                    intent.putExtra("Url",url_compare);
                    intent.putExtra("pid",patientID);
                    mContext.startActivity(intent);*/

                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.card_task_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    DisplayMetrics metrics5 = mContext.getResources().getDisplayMetrics();
                    int width5 = metrics5.widthPixels;
                    int height5 = metrics5.heightPixels;
                    dialog.getWindow().setLayout((6 * width5) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    ImageView close5 = (ImageView)dialog.findViewById(R.id.close_dialog);

                    TextView schedule5 = (TextView)dialog.findViewById(R.id.tv_schedule_task);
                    TextView set_quest = (TextView)dialog.findViewById(R.id.set_quest);

                    set_quest.setText("Sono");

                    schedule5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (dateSelected == null || date_month_year == null){

                                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                                dateSelected = date;

                            }else {

                                dateSelected = date_month_year;
                            }

                            scheduleSleepTask(patientID,dateSelected);

                        }
                    });

                    close5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

                    break;


                case 4:

                    Intent send = new Intent(mContext, ArticleSleepPatientList.class);
                    mContext.startActivity(send);

                    break;

                default:

                    break;
            }

        }

        private void scheduleSleepTask(final String patientID, final String dateSelected) {

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.please_wait));
            pDialog.show();

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.SLEEP_TASK,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:sleeptask", response);

                            jsonParseCardTask(response);
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

                    params.put("psychologist_id", cc.loadPrefString("user_id"));
                    params.put("assign_date", dateSelected);
                    params.put("patient_id", patientID);
                    params.put("task_type", "Sleep");

                    Log.i("request sleep", params.toString());

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


        private void postInterviewTask(final String patientID, final String radio_age, final String radio_part, final String chValueOne, final String chValueTwo, final String chValueThree, final String dateSelected) {

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.please_wait));
            pDialog.show();

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.INTERVIEW_TASK,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:interviewtask", response);

                            jsonParseInterviewTask(response);
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
                    params.put("assign_date", dateSelected);
                    params.put("patient_id", patientID);
                    params.put("task_type", "Interview");
                    params.put("age", radio_age);
                    params.put("parts", radio_part);
                    params.put("basic_information", chValueOne);
                    params.put("diagnostics", chValueTwo);
                    params.put("social_skills",chValueThree);

                    Log.i("request interview", params.toString());

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
    }

    private static void postEmotionTask(final String patientID, final String radio_e_schedule, final String radio_e_level,
                                        final String chValueOneS, final String chValueTwoS, final String chValueThreeS, final String dateSelected) {

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.EMOTION_TASK,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:emotiontask", response);

                        jsonParseInterviewTask(response);
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
                params.put("assign_date", dateSelected);
                params.put("patient_id", patientID);
                params.put("task_type", "Emotions");
                params.put("schedule", radio_e_schedule);
                params.put("emotion_level", radio_e_level);
                params.put("use_intensity", chValueOneS);
                params.put("use_situation", chValueTwoS);
                params.put("use_thoughts",chValueThreeS);


                /*int i=0;
                for(String object: numbers){
                    params.put("timing["+(i++)+"]", object);
                }*/



                Log.i("request emotions", params.toString());

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

    private static void postMoodTask(final String patientID, final String radio_schedule_m, final String dateSelected) {

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.MOOD_TASK,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:moodtask", response);

                        jsonParseInterviewTask(response);
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
                params.put("assign_date", dateSelected);
                params.put("patient_id", patientID);
                params.put("task_type", "Mood");
                params.put("schedule", radio_schedule_m);

                Log.i("request mood", params.toString());

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

    private static void scheduleCardTask(final String patientID, final String dateSelected) {

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CARD_TASK,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:cardtask", response);

                        jsonParseCardTask(response);
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
                params.put("assign_date", dateSelected);
                params.put("patient_id", patientID);
                params.put("task_type", "Cards");
                params.put("card_assign","Yes");

                Log.i("request card", params.toString());

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


    private static void jsonParseInterviewTask(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                chValueOneS = "No";
                chValueTwoS = "No";
                chValueThreeS = "No";

                dialog.dismiss();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.toString());
        }
    }

    private static void jsonParseCardTask(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                dialog.dismiss();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.toString());
        }
    }
}


