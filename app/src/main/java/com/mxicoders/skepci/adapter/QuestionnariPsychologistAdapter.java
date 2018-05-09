package com.mxicoders.skepci.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.QuestionariesTwoClickActivity;
import com.mxicoders.skepci.model.QuestionnariPyschologistData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.utils.AppController;

import java.util.Collections;
import java.util.List;

/**
 * Created by mxicoders on 1/8/17.
 */

public class QuestionnariPsychologistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private Context context;
    private LayoutInflater inflater;
    List<QuestionnariPyschologistData> data = Collections.emptyList();
    QuestionnariPyschologistData current;
    public static Fragment newFragment;

    CommanClass cc;

    String title;

    int currentPos=0;

    Bitmap bitmap;

    public static String id = "", name = "";


    public QuestionnariPsychologistAdapter(Context context, List<QuestionnariPyschologistData> data){

        cc = new CommanClass(context);
        this.context = context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.questionaries_adapter_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {



        final MyHolder myHolder= (MyHolder) holder;

        final QuestionnariPyschologistData current = data.get(position);

        myHolder.categoryName.setText(current.name);
        myHolder.categoryEmail.setText(current.count_question);

        myHolder.c_image.setImageUrl(current.photo, imageLoader);

        myHolder.lnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doClick(myHolder.getAdapterPosition());

            }
        });

    }

    private void doClick(int adapterPosition) {

        QuestionnariPyschologistData model = data.get(adapterPosition);

        cc.savePrefString("category_idd",model.getName());
        cc.savePrefString("count_ques",model.getCount_question());
        cc.savePrefString("photo",model.getPhoto());

        cc.savePrefBoolean("isInPatentList",true);


        Intent Ques = new Intent(context, QuestionariesTwoClickActivity.class);
        context.startActivity(Ques);


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryName,categoryEmail;
        LinearLayout lnClick;
        NetworkImageView c_image;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            categoryName= (TextView) itemView.findViewById(R.id.text_name);
            categoryEmail= (TextView) itemView.findViewById(R.id.text_email);
            lnClick = (LinearLayout) itemView.findViewById(R.id.category_click);
            c_image = (NetworkImageView) itemView.findViewById(R.id.Image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}