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
 * Created by mxicoders on 20/7/17.
 */

public class PatientMenuArticleAdapter extends RecyclerView.Adapter<PatientMenuArticleAdapter.ViewHolder> {

    static CommanClass cc;

    private static List<PatientArticleData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    public PatientMenuArticleAdapter(List<PatientArticleData> countries, int rowLayout, Context context) {

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
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        PatientArticleData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.author.setText(myItem.getAuthor());

        if (myItem.getIsLike().equals("Yes")){

            viewHolder.Image.setImageResource(R.drawable.heart_rate);

        }else {

            viewHolder.Image.setImageResource(R.drawable.heart_rate_two);

        }

        /*viewHolder.Image.setOnClickListener(new View.OnClickListener() {

            private boolean toggle = false;

            @Override
            public void onClick(View v) {

                if(toggle)
                {
                    viewHolder.Image.setImageResource(R.drawable.heart_rate_two);
                    toggle=false;
                }
                else
                {
                    viewHolder.Image.setImageResource(R.drawable.heart_rate);
                    toggle=true;
                }

            }
        });*/


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,author;
        public ImageView Image;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            author = (TextView) itemView.findViewById(R.id.text_email);
            Image = (ImageView)itemView.findViewById(R.id.Image);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            Intent t_history = new Intent(mContext, PatientArticleClickMenuActivity.class);
            mContext.startActivity(t_history);

            getArticlePosList(getAdapterPosition());

        }


    }
    public static void getArticlePosList(int adapterPosition) {

        PatientArticleData modelTwo = countries.get(adapterPosition);

        cc.savePrefString("article_patient_id",modelTwo.getaId());
        cc.savePrefString("article_patient_title",modelTwo.getName());
        cc.savePrefString("article_patient_body",modelTwo.getBody());
        cc.savePrefString("article_patient_image",modelTwo.getPhoto());

    }
}
