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
import com.mxicoders.skepci.model.PatientQuestionnariesData;

import java.util.List;

/**
 * Created by mxicoders on 17/7/17.
 */

public class PatientQuestionariesAdapter extends RecyclerView.Adapter<PatientQuestionariesAdapter.ViewHolder> {

    private List<PatientQuestionnariesData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    public PatientQuestionariesAdapter(List<PatientQuestionnariesData> countries, int rowLayout, Context context) {
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
        PatientQuestionnariesData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
        viewHolder.email.setText(myItem.getEmail());
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email;
        public ImageView Image;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            Image = (ImageView)itemView.findViewById(R.id.Image);
            email = (TextView) itemView.findViewById(R.id.text_email);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
/*
            android.app.FragmentTransaction   tra =((Activity) mContext).getFragmentManager().beginTransaction();
            newFragment = new QuestionariesTwoClickActivity();
            tra.replace(R.id.container_body, newFragment);
            tra.addToBackStack(null);
            tra.commit();*/
        }
    }}
