package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.LinearLayout;
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
import com.mxicoders.skepci.activity.ToDoQuestionnarClickActivity;
import com.mxicoders.skepci.model.QuestionDetailsData;
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
 * Created by mxi on 20/12/17.
 */

public class QuestionResponseTodoAdapter extends RecyclerView.Adapter<QuestionResponseTodoAdapter.ViewHolder> {

    static CommanClass cc;

    private List<QuestionDetailsData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    public QuestionResponseTodoAdapter(List<QuestionDetailsData> countries, int rowLayout, Context context) {

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
    public QuestionResponseTodoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new QuestionResponseTodoAdapter.ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(QuestionResponseTodoAdapter.ViewHolder viewHolder, int i) {
        QuestionDetailsData myItem = countries.get(i);



        viewHolder.Name.setText(myItem.getNumber_count());

        /*if (myItem.getIs_answered().equals("Yes")){

            viewHolder.Name.setTextColor(Color.parseColor("#8C8C8C"));

        }else {

            viewHolder.Name.setTextColor(Color.parseColor("#ffffffff"));
        }*/


        if (i == countries.size()-1){

            viewHolder.ln_line_verticle.setVisibility(View.INVISIBLE);

        }else {

            viewHolder.ln_line_verticle.setVisibility(View.VISIBLE);
        }



    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name;
        public LinearLayout ln_line_verticle;


        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            ln_line_verticle = (LinearLayout) itemView.findViewById(R.id.ln_line_verticle);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

                Log.e("QuestionResposnsePos", String.valueOf(getAdapterPosition()));

                Intent questinon = new Intent(mContext, ToDoQuestionnarClickActivity.class);
                questinon.putExtra("task_id",cc.loadPrefString("task_id"));
                cc.savePrefString3("question_position", String.valueOf(getAdapterPosition()));
                cc.savePrefString("q_position", String.valueOf(getAdapterPosition()));
                mContext.startActivity(questinon);
                ((Activity)mContext).finish();

           /* Intent intent = new Intent("custom-event-name");
            // You can also include some extra data.
            intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);*/

            }
        }

}



