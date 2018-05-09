package com.mxicoders.skepci.adapter;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.ToDoData;

import java.util.List;

/**
 * Created by mxicoders on 25/8/17.
 */

public class TaskHistoryAdapter extends RecyclerView.Adapter<TaskHistoryAdapter.ViewHolder> {
    private List<ToDoData> history;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    public TaskHistoryAdapter(List<ToDoData> history, int rowLayout, Context context) {
        this.history = history;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }
    @Override
    public int getItemCount() {
        return history == null ? 0 : history.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ToDoData myItem = history.get(i);
        viewHolder.Name.setText(myItem.getName() +"-"+myItem.gettiming());
        viewHolder.tv_date.setText(myItem.getD_date());
        viewHolder.tv_day.setText(myItem.getD_day());
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,tv_date,tv_day;

        public ViewHolder(View itemView) {
            super(itemView);



            Name = (TextView) itemView.findViewById(R.id.text_name);
            tv_date= (TextView) itemView.findViewById(R.id.tv_date_list);
            tv_day= (TextView) itemView.findViewById(R.id.tv_day_list);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


    }
}

