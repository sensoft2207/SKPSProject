package com.mxicoders.skepci.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ArticlesTwoClickActivity;
import com.mxicoders.skepci.model.ArticlesData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.utils.AppController;

import java.util.Collections;
import java.util.List;

/**
 * Created by mxicoders on 13/7/17.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    String title;

    int currentPos=0;

    Bitmap bitmap;

    public static String id = "", name = "";

    private List<ArticlesData> countries = Collections.emptyList();;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;
    public ArticlesAdapter(List<ArticlesData> countries, int rowLayout, Context context) {

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
        ArticlesData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.article_count.setText(myItem.getCount_question());
        viewHolder.tv_category_like_count.setText(myItem.getCategory_like_count());
        viewHolder.c_image.setImageUrl(myItem.photo, imageLoader);

        viewHolder.lnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doClick(viewHolder.getAdapterPosition());

            }
        });
    }

    private void doClick(int adapterPosition) {

        ArticlesData model = countries.get(adapterPosition);

        cc.savePrefString("article_name",model.getName());
        cc.savePrefString("article_count",model.getCount_question());
        cc.savePrefString("article_photo",model.getPhoto());
        cc.savePrefString("article_category_id",model.getId());


        cc.savePrefBoolean("isInPatentList",true);


        Intent article = new Intent(mContext, ArticlesTwoClickActivity.class);
        mContext.startActivity(article);


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,article_count,tv_category_like_count;
        LinearLayout lnClick;
        NetworkImageView c_image;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            article_count = (TextView) itemView.findViewById(R.id.text_email);
            tv_category_like_count = (TextView) itemView.findViewById(R.id.tv_category_like_count);
            c_image = (NetworkImageView)itemView.findViewById(R.id.Image);
            lnClick = (LinearLayout) itemView.findViewById(R.id.category_click);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }}