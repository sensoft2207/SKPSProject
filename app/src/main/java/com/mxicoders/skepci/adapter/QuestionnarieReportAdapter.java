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
import com.mxicoders.skepci.model.PatientArticleData;
import com.mxicoders.skepci.model.QuestionDetailsData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.List;

/**
 * Created by mxi on 7/12/17.
 */

public class QuestionnarieReportAdapter extends RecyclerView.Adapter<QuestionnarieReportAdapter.ViewHolder> {

    static CommanClass cc;

    private static List<QuestionDetailsData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    public QuestionnarieReportAdapter(List<QuestionDetailsData> countries, int rowLayout, Context context) {

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
    public QuestionnarieReportAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new QuestionnarieReportAdapter.ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final QuestionnarieReportAdapter.ViewHolder viewHolder, int i) {
        QuestionDetailsData myItem = countries.get(i);
        viewHolder.question.setText(myItem.getQuestion());
        viewHolder.answer.setText(myItem.getAnswer());


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView question,answer;
        public ViewHolder(View itemView) {
            super(itemView);


            question = (TextView) itemView.findViewById(R.id.tv_question_title);
            answer = (TextView) itemView.findViewById(R.id.tv_answer);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }


    }
}

