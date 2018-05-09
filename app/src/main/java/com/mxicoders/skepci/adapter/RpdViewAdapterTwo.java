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
import com.mxicoders.skepci.model.RpdData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.List;

/**
 * Created by mxicoders on 24/8/17.
 */

public class RpdViewAdapterTwo extends RecyclerView.Adapter<RpdViewAdapterTwo.ViewHolder> {


    private List<RpdData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";
    RpdData myItem;

    CommanClass cc;


    public RpdViewAdapterTwo(List<RpdData> countries, int rowLayout, Context context) {

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
        myItem = countries.get(i);
        viewHolder.name.setText(myItem.getRpd_name());
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.tv_rpd_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }


    }
}






