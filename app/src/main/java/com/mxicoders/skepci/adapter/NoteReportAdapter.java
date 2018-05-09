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
import com.mxicoders.skepci.activity.PatientArticleClickMenuActivity;
import com.mxicoders.skepci.model.PatientArticleData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.List;

/**
 * Created by mxi on 7/12/17.
 */

public class NoteReportAdapter extends RecyclerView.Adapter<NoteReportAdapter.ViewHolder> {

    static CommanClass cc;

    private static List<PatientArticleData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    public NoteReportAdapter(List<PatientArticleData> countries, int rowLayout, Context context) {

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
    public NoteReportAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new NoteReportAdapter.ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final NoteReportAdapter.ViewHolder viewHolder, int i) {
        PatientArticleData myItem = countries.get(i);
        viewHolder.noteTitle.setText(myItem.getName());


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView noteTitle;
        public ViewHolder(View itemView) {
            super(itemView);


            noteTitle = (TextView) itemView.findViewById(R.id.tv_note_title);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }


    }
}

