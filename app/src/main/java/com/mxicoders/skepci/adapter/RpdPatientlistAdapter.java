package com.mxicoders.skepci.adapter;

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
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.RpdPatientListClickMenu;
import com.mxicoders.skepci.model.QuestionariesClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.utils.AndyUtils;

import java.util.List;

/**
 * Created by mxicoders on 23/8/17.
 */

public class RpdPatientlistAdapter extends RecyclerView.Adapter<RpdPatientlistAdapter.ViewHolder> {


    private List<QuestionariesClickData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";
    QuestionariesClickData myItem;

    CommanClass cc;


    public RpdPatientlistAdapter(List<QuestionariesClickData> countries, int rowLayout, Context context) {

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
        viewHolder.Name.setText(myItem.getName());
        viewHolder.emotion.setText(myItem.getSemotion());
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,emotion;
        public ImageView Image;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            emotion = (TextView) itemView.findViewById(R.id.text_emotion);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (!cc.isConnectingToInternet()) {

                AndyUtils.showToast(mContext,mContext.getString(R.string.no_internet));

            }else{
                Intent in = new Intent(mContext, RpdPatientListClickMenu.class);
                getPosList(getAdapterPosition());
                mContext.startActivity(in);
            }


        }


    }

    public void getPosList(int adapterPosition) {

        QuestionariesClickData modelTwo = countries.get(adapterPosition);

        cc.savePrefString("rpd_id",modelTwo.getQue_id());

    }
}




