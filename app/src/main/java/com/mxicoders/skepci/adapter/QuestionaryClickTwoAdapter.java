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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.QuesPatientListThreeClick;
import com.mxicoders.skepci.activity.QuesPatientListTwoClick;
import com.mxicoders.skepci.model.QuestionariesClickData;
import com.mxicoders.skepci.model.QuestionnariPyschologistData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.List;

/**
 * Created by vishal on 14/2/18.
 */

public class QuestionaryClickTwoAdapter extends RecyclerView.Adapter<QuestionaryClickTwoAdapter.ViewHolder> {
    private List<QuestionariesClickData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    CommanClass cc;

    public QuestionaryClickTwoAdapter(List<QuestionariesClickData> countries, int rowLayout, Context context) {

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
    public QuestionaryClickTwoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new QuestionaryClickTwoAdapter.ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final QuestionaryClickTwoAdapter.ViewHolder viewHolder, int i) {
        QuestionariesClickData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
         viewHolder.email.setVisibility(View.INVISIBLE);
         viewHolder.ln_ques_item.setVisibility(View.GONE);

        viewHolder.ln_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doClick(viewHolder.getAdapterPosition());
            }
        });

       /* viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
        viewHolder.email.setText(myItem.getEmail());*/
    }

    private void doClick(int adapterPosition) {

        QuestionariesClickData model = countries.get(adapterPosition);

        cc.savePrefString("assign_que_name",model.getName());
        cc.savePrefString("assign_que_id",model.getQue_id());


        Intent Ques = new Intent(mContext, QuesPatientListThreeClick.class);
        mContext.startActivity(Ques);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email;
        public LinearLayout ln_ques_item,ln_click;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            email = (TextView) itemView.findViewById(R.id.text_email);
            ln_ques_item = (LinearLayout) itemView.findViewById(R.id.ln_ques_item);
            ln_click = (LinearLayout) itemView.findViewById(R.id.ln_click);
           /* Image = (ImageView)itemView.findViewById(R.id.Image);
            email = (TextView) itemView.findViewById(R.id.text_email);*/

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }

}

