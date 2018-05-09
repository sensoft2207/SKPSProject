package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ArticlesTwoClickActivity;
import com.mxicoders.skepci.activity.ViewArticlePsychologistActivity;
import com.mxicoders.skepci.model.ArticleClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxicoders.skepci.activity.ArticlesTwoClickActivity.mAdapter;

/**
 * Created by mxicoders on 13/7/17.
 */

public class ArticlesClickAdapter extends RecyclerView.Adapter<ArticlesClickAdapter.ViewHolder> {

    CommanClass cc;

    private List<ArticleClickData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    private boolean toggle = false;

    public ArticlesClickAdapter(List<ArticleClickData> countries, int rowLayout, Context context) {

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
        ArticleClickData myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.email.setText(myItem.getAuthor());
        viewHolder.tv_article_liked_count.setText(myItem.getArticle_liked_count());

        if (myItem.getUser_like_count().equals("Yes")){

            viewHolder.image_favourite.setImageResource(R.drawable.heart_rate);

            toggle = true;

        }else {

            viewHolder.image_favourite.setImageResource(R.drawable.heart_rate_two);

            toggle = false;
        }

        viewHolder.image_favourite.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

               /* if(toggle)
                {
                    viewHolder.image_favourite.setImageResource(R.drawable.heart_rate_two);
                    toggle=false;

                    getArticleIdForDisLike(viewHolder.getAdapterPosition());
                }
                else
                {
                    viewHolder.image_favourite.setImageResource(R.drawable.heart_rate);
                    toggle=true;

                    getArticleIdForLike(viewHolder.getAdapterPosition());

                }*/

                if (toggle == true){

                    viewHolder.image_favourite.setImageResource(R.drawable.heart_rate_two);
                    getArticleIdForDisLike(viewHolder.getAdapterPosition());

                    viewHolder.tv_article_liked_count.setText("0");

                    toggle = false;

                }else {
                    viewHolder.image_favourite.setImageResource(R.drawable.heart_rate);

                    getArticleIdForLike(viewHolder.getAdapterPosition());

                    viewHolder.tv_article_liked_count.setText("1");

                    toggle = true;
                }
            }
        });


    }

    public void getArticleIdForLike(int adapterPosition) {

        ArticleClickData modelTwo = countries.get(adapterPosition);


        articleLike(modelTwo.getQue_id(),modelTwo.getArticle_category_id(),adapterPosition);

    }

    public void getArticleIdForDisLike(int adapterPosition) {

        ArticleClickData modelTwo = countries.get(adapterPosition);


        articleDisLike(modelTwo.getQue_id(),adapterPosition);

    }



    private void articleLike(final String que_id, final String article_category_id, int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.FAVOURITE_ARTICLE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:like article", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                cc.showToast(mContext.getString(R.string.like_success_msg));

                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("article_id", que_id);
                params.put("article_category_id",cc.loadPrefString("article_category_id"));

                Log.e("request like article", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }


    private void articleDisLike(final String que_id, int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.FAVOURITE_REMOVE_ARTICLE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:like article", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                cc.showToast(mContext.getString(R.string.dislike_success_msg));

                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("article_id", que_id);
                params.put("article_category_id",cc.loadPrefString("article_category_id"));

                Log.e("request like article", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email,tv_article_liked_count;
        public ImageView image_favourite;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            tv_article_liked_count = (TextView) itemView.findViewById(R.id.tv_article_liked_count);
            email = (TextView) itemView.findViewById(R.id.text_email);
            image_favourite = (ImageView) itemView.findViewById(R.id.Image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent article = new Intent(mContext, ViewArticlePsychologistActivity.class);
            ((Activity)mContext).finish();
            mContext.startActivity(article);


            getArticlePosList(getAdapterPosition());

        }
    }
    public void getArticlePosList(int adapterPosition) {

        ArticleClickData modelTwo = countries.get(adapterPosition);

        cc.savePrefString("article_id_main",modelTwo.getQue_id());
        cc.savePrefString("article_title",modelTwo.getName());
        cc.savePrefString("article_body",modelTwo.getBody());
        cc.savePrefString("article_image",modelTwo.getPhoto());

    }
}
