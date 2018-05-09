package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 4/8/17.
 */

public class ViewArticlePsychologistActivity extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    String articleCategoryID,articleID,articleTitle,articleBody,articlePhoto;

    TextView tvArticleViewTitle,tvArticleViewBody;

    NetworkImageView image;

    ProgressDialog pDialog;

    public static Fragment newFragment;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public ViewArticlePsychologistActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_article_click_patient);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(ViewArticlePsychologistActivity.this);


        articleCategoryID = cc.loadPrefString("article_category_id");
        articleID = cc.loadPrefString("article_id_main");
        articleTitle = cc.loadPrefString("article_title");
        articleBody = cc.loadPrefString("article_body");
        articlePhoto = cc.loadPrefString("article_image");

        Log.i("ArticleCategoryID",articleCategoryID.toString());
        Log.i("ArticleID",articleID.toString());
        Log.i("ArticleTitle",articleTitle.toString());
        Log.i("ArticleBody",articleBody.toString());

        tvArticleViewTitle = (TextView)findViewById(R.id.tv_article_view_title);
        tvArticleViewBody = (TextView)findViewById(R.id.tv_article_view_body);
        image = (NetworkImageView)findViewById(R.id.Image);

        tvArticleViewTitle.setText(articleTitle);
        tvArticleViewBody.setText(articleBody);
        image.setImageUrl(articlePhoto, imageLoader);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setBackgroundResource(R.drawable.remove_note);
        tooName.setText(getString(R.string.title_coupens));
        tooName.setPadding(75, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ViewArticlePsychologistActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.patient_list_delete_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics3 = getResources().getDisplayMetrics();
                int width3 = metrics3.widthPixels;
                int height3 = metrics3.heightPixels;
                dialog.getWindow().setLayout((6 * width3) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();

                ImageView close_dialogg = (ImageView) dialog.findViewById(R.id.close);
                TextView tvInfo = (TextView)dialog.findViewById(R.id.tv_info);
                TextView closeBtn = (TextView)dialog.findViewById(R.id.tv_no_dialog);
                TextView yesBtn = (TextView)dialog.findViewById(R.id.tv_yes_dialog);

                tvInfo.setText(getString(R.string.delete_article));

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteArticle(articleCategoryID,articleID);
                        dialog.dismiss();
                    }
                });

                close_dialogg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_article_delete, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_article_delete) {



            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteArticle(final String articleCategoryID, final String articleID) {

        pDialog = new ProgressDialog(ViewArticlePsychologistActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_ARTICLE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:deletearticle", response);

                        jsonParseDeleteArticles(response);
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
                params.put("article_id", articleID);
                params.put("article_category_id", articleCategoryID);

                Log.i("request delete", params.toString());

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

    private void jsonParseDeleteArticles(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                AndyUtils.showToast(ViewArticlePsychologistActivity.this,getString(R.string.article_delete));

                Intent article = new Intent(ViewArticlePsychologistActivity.this, ArticlesTwoClickActivity.class);
                startActivity(article);
                finish();


            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error",e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cc.loadPrefBoolean("isInactiveDevice") || cc.loadPrefBoolean("isDialogOpen") ){


            Log.e("IsLoginDevice","Psychologist...If");

            cc.savePrefBoolean("isInactiveDevice",false);

        }else {

            if (cc.loadPrefBoolean("Inactive")) {

                cc.savePrefBoolean("Inactive",false);

                cc.savePrefBoolean("isDialogOpen",true);

                cc.showInactiveDialog();
            }


            Log.e("IsLoginDevice","Psychologist...Else");
        }

    }
}

