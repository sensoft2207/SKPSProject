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
import com.mxicoders.skepci.activity.QuestionariesTwoClickActivity;
import com.mxicoders.skepci.model.QuestionariesData;

import java.util.List;

/**
 * Created by mxicoders on 14/7/17.
 */

public class QuestionariesAdapter extends RecyclerView.Adapter<QuestionariesAdapter.ViewHolder> {
    private List<QuestionariesData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;
    public QuestionariesAdapter(List<QuestionariesData> countries, int rowLayout, Context context) {
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
        QuestionariesData myItem = countries.get(i);
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

            Intent Ques = new Intent(mContext, QuestionariesTwoClickActivity.class);
            mContext.startActivity(Ques);
        }
    }}
