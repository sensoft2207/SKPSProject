package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.DemoDynamicActivity;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ToDoEmotionClickActivity;
import com.mxicoders.skepci.activity.ToDoSleepClickActivity;
import com.mxicoders.skepci.activity.ToDoSleepClickTwo;
import com.mxicoders.skepci.model.ToDoData;

import java.util.List;

/**
 * Created by mxicoders on 24/7/17.
 */

public class ToDoAnyoneAdapter extends RecyclerView.Adapter<ToDoAnyoneAdapter.ViewHolder> {
    private List<ToDoData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    public ToDoAnyoneAdapter(List<ToDoData> countries, int rowLayout, Context context) {
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
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final ToDoData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.d_date.setText(myItem.getD_date());
        viewHolder.d_day.setText(myItem.getD_day());
       /* viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
        viewHolder.email.setText(myItem.getEmail());*/


        viewHolder.lnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (myItem.getName().contains("Sleep")){

                    Intent emotion = new Intent(mContext, ToDoSleepClickTwo.class);

                  *//*  emotion.putExtra("task_id",countries.get(i).getTask_id());
                    emotion.putExtra("time",countries.get(i).getTiming());
                    emotion.putExtra("date",countries.get(i).getFull_date());*//*
                    ((Activity)mContext).finish();
                    mContext.startActivity(emotion);

                }else if (myItem.getName().contains("RPD")){


                }*/

                Intent mood = new Intent(mContext, DemoDynamicActivity.class);
                mContext.startActivity(mood);


            }
        });


    }
    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        public TextView Name,email,d_date,d_day;
        public ImageView Image;
        public LinearLayout lnClick;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            d_date= (TextView) itemView.findViewById(R.id.tv_date_list);
            d_day= (TextView) itemView.findViewById(R.id.tv_day_list);
            lnClick = (LinearLayout) itemView.findViewById(R.id.ln_item_click);

            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View v) {



        }*/
    }}

