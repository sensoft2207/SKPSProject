package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.ArticleClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxicoders.skepci.activity.SleepPatientlistActivity.date_month_year;

/**
 * Created by mxicoders on 10/8/17.
 */

public class SendArticleSleepAdapterClick extends RecyclerView.Adapter<SendArticleSleepAdapterClick.ViewHolder> {

    CommanClass cc;

    private List<ArticleClickData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    String patientId,articleId,assign_date = null;

    static ProgressDialog pDialog;

    Dialog dialog;

    public SendArticleSleepAdapterClick(List<ArticleClickData> countries, int rowLayout, Context context) {

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

            viewHolder.heart.setImageResource(R.drawable.heart_rate);


        }else {

            viewHolder.heart.setImageResource(R.drawable.heart_rate_two);

        }

    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,email,tv_article_liked_count;
        ImageView heart;

        public ViewHolder(View itemView) {
            super(itemView);


            Name = (TextView) itemView.findViewById(R.id.text_name);
            email = (TextView) itemView.findViewById(R.id.text_email);
            tv_article_liked_count = (TextView) itemView.findViewById(R.id.tv_article_liked_count);
            heart = (ImageView) itemView.findViewById(R.id.Image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            getArticlePosList(getAdapterPosition());






        }
    }

    private void confirmSendDialog() {

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.patient_list_delete_dialog);
        dialog.show();

        TextView tvYes = (TextView)dialog.findViewById(R.id.tv_yes_dialog);
        TextView tvNo = (TextView)dialog.findViewById(R.id.tv_no_dialog);
        TextView tvInfo = (TextView)dialog.findViewById(R.id.tv_info);
        ImageView closee = (ImageView)dialog.findViewById(R.id.close);

        tvInfo.setText(mContext.getString(R.string.send_article));

        closee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialog.dismiss();
                sendArticle(patientId,articleId,assign_date);

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }

    public void getArticlePosList(int adapterPosition) {

        ArticleClickData modelTwo = countries.get(adapterPosition);

        cc.savePrefString("article_id_main",modelTwo.getQue_id());
        cc.savePrefString("article_title",modelTwo.getName());
        cc.savePrefString("article_body",modelTwo.getBody());
        cc.savePrefString("article_image",modelTwo.getPhoto());


        if (modelTwo.getIs_send_before().equals("Yes")){

            cc.showToast("Artigo já enviado");

        }else {

            patientId = cc.loadPrefString("patient_id_main");
            articleId = cc.loadPrefString("article_id_main");

            if (assign_date == null){

                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                assign_date = date;

            }else {

                assign_date = date_month_year;
            }

            confirmSendDialog();
        }



    }

    private void sendArticle(final String patientId, final String articleId, final String assign_date) {

        Log.i("@@SendData",patientId.toString());
        Log.i("@@SendData",articleId.toString());

        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.SEND_ARTICLE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:sendarticle", response);

                        jsonParseSendArticleTask(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        }) {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("patient_id", patientId);
                params.put("article_id", articleId);
                params.put("task_type", "Article");
                params.put("assign_date", assign_date);

                Log.i("request sendarticle", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header profile", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void jsonParseSendArticleTask(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                dialog.dismiss();

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.patient_invite_sent_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics3 = mContext.getResources().getDisplayMetrics();
                int width3 = metrics3.widthPixels;
                int height3 = metrics3.heightPixels;
                dialog.getWindow().setLayout((6 * width3) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();

                TextView pone = (TextView)dialog.findViewById(R.id.tv_pone);
                TextView ptwo = (TextView)dialog.findViewById(R.id.tv_ptwo);
                TextView pthree = (TextView)dialog.findViewById(R.id.tv_pthree);

                pone.setText("");
                ptwo.setText("");
                pthree.setText(mContext.getString(R.string.sent_article));

                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            ((Activity)mContext).onBackPressed();
                        }
                    }
                };

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable, 2000);


                cc.showToast(jsonObject.getString("message"));

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.toString());
        }
    }
}

