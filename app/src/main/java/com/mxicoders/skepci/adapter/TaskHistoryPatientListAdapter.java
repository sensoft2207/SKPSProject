package com.mxicoders.skepci.adapter;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.ToDoData;

import java.util.List;

/**
 * Created by mxicoders on 10/8/17.
 */

public class TaskHistoryPatientListAdapter extends RecyclerView.Adapter<TaskHistoryPatientListAdapter.ViewHolder> {


    private List<ToDoData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";


    public TaskHistoryPatientListAdapter(List<ToDoData> countries, int rowLayout, Context context) {


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
                case 0:

                    break;



                default:

                    break;
            }

        }


    }
}



