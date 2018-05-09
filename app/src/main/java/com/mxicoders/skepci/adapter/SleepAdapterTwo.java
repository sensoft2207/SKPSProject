package com.mxicoders.skepci.adapter;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.mxicoders.skepci.model.QuestionariesClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxicoders.skepci.adapter.SleepAdapterOne.patientID;
import static com.mxicoders.skepci.activity.SleepPatientlistActivity.date_month_year;

/**
 * Created by mxicoders on 31/7/17.
 */

public class SleepAdapterTwo extends RecyclerView.Adapter<SleepAdapterTwo.ViewHolder> {

    static CommanClass cc;

    private static List<QuestionariesClickData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    static Dialog dialog;

    static String questionnaryID;

    static String titleID;

    public static String dateSelected;

    static ProgressDialog pDialog;

    static String radio_schedule_ques;

    static RadioGroup r_schedule_ques;

    static TextView submit;

    public SleepAdapterTwo(List<QuestionariesClickData> countries, int rowLayout, Context context) {

        cc = new CommanClass(context);

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
        QuestionariesClickData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());



    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email;


        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.mood_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            DisplayMetrics metrics4 = mContext.getResources().getDisplayMetrics();
            int width4 = metrics4.widthPixels;
            int height4 = metrics4.heightPixels;
            dialog.getWindow().setLayout((6 * width4) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                    dialog.show();

                    TextView set_que = (TextView)dialog.findViewById(R.id.set_quest);
                    submit = (TextView)dialog.findViewById(R.id.tv_submit_dialog_m);
                    r_schedule_ques = (RadioGroup)dialog.findViewById(R.id.rb_schedule_m);



                    set_que.setText(mContext.getString(R.string.que_one));

                    ImageView close = (ImageView)dialog.findViewById(R.id.close_dialog);

                    getPosList(getAdapterPosition());

                    questionnaryID = cc.loadPrefString("questionary_id_main");

                    Log.e("QuestionnaryID",questionnaryID.toString());

            r_schedule_ques.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.rb_once_m:

                            radio_schedule_ques = "Once";

                            break;
                        case R.id.rb_daily_m:

                            radio_schedule_ques = "Daily";

                            break;
                        case R.id.rb_threex_m:

//                            radio_schedule_ques = ((RadioButton)dialog.findViewById(r_schedule_ques.getCheckedRadioButtonId())).getText().toString();

                            radio_schedule_ques = "3xdays";

                            break;
                    }
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (dateSelected == null){

                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        dateSelected = date;

                    }else {

                        dateSelected = date_month_year;
                    }

                    getPos(getAdapterPosition());

                }
            });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });

            }


        public void getPos(int adapterPosition) {

            QuestionariesClickData modelTwo = countries.get(adapterPosition);

            Log.e("@@titleID", modelTwo.getQue_id() + "");

            postQuestionaryTask(patientID,radio_schedule_ques,dateSelected,modelTwo.getQue_id());

        }

        private void postQuestionaryTask(final String patientID, final String radio_schedule_ques,
                                         final String dateSelected,final String titleID) {

            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.please_wait));
            pDialog.show();

            StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.QUESTIONNARY_TASK,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.e("response:questask", response);

                            jsonParseQuestionaryTask(response);
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
                    params.put("task_type", "Question");
                    params.put("schedule", radio_schedule_ques);
                    params.put("title_id", titleID);

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

        private void jsonParseQuestionaryTask(String response) {


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
                Log.e("Error", e.toString());
            }
        }

    }

        public static void getPosList(int adapterPosition) {

            QuestionariesClickData modelTwo = countries.get(adapterPosition);

            cc.savePrefString("questionary_id_main",modelTwo.getQue_id());

        }
    }




