package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mxicoders.skepci.adapter.SendArticleSleepAdapterClick;
import com.mxicoders.skepci.model.ArticleClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 10/8/17.
 */

public class SendArticleListSleep extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    RecyclerView mRecyclerView;
    SendArticleSleepAdapterClick mAdapter;
    ArrayList<ArticleClickData> list;

    TextView tvDemo,tvCount;
    NetworkImageView image;

    ProgressDialog pDialog;

    String dem,countArticle,articlePhoto;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public SendArticleListSleep() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_articles_two_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(SendArticleListSleep.this);

        dem =  cc.loadPrefString("article_name");
        countArticle =  cc.loadPrefString("article_count");
        articlePhoto =  cc.loadPrefString("article_photo");

        tvDemo = (TextView)findViewById(R.id.text_name);
        tvCount = (TextView)findViewById(R.id.text_count);
        image = (NetworkImageView)findViewById(R.id.Image);

        tvCount.setText(countArticle);
        image.setImageUrl(articlePhoto, imageLoader);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(dem);
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SendArticleListSleep.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getArticleList();


    }


    public void getArticleList() {

        pDialog = new ProgressDialog(SendArticleListSleep.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ARTICLE_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:article data", response);


                        list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("category data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    String cname = jsonObject1.getString("category_name");

                                    if (dem.contentEquals(cname)) {

                                        ArticleClickData model = new ArticleClickData();

                                        model.setEmail(jsonObject1.getString("category_name"));
                                        /*model.setQue_id(jsonObject1.getString("id"));*/
                                        model.setPhoto(jsonObject1.getString("category_image"));

                                        model.setName(model.getName());
                                        model.setEmail(model.getEmail());

                                    }

                                    JSONArray dataArrayTwo = jsonObject1.getJSONArray("article_detail");

                                    for (int m = 0; m < dataArrayTwo.length(); m++) {

                                        JSONObject jsonObject2 = dataArrayTwo.getJSONObject(m);

                                        if (dem.contentEquals(cname)) {

                                            ArticleClickData model2 = new ArticleClickData();

                                            model2.setName(jsonObject2.getString("title"));
                                            model2.setQue_id(jsonObject2.getString("id"));
                                            model2.setBody(jsonObject2.getString("body"));
                                            model2.setAuthor(jsonObject2.getString("author"));
                                            model2.setArticle_liked_count(jsonObject2.getString("article_like_count"));
                                            model2.setUser_like_count(jsonObject2.getString("is_user_liked"));

                                            if (jsonObject2.has("is_send_before")){

                                                model2.setIs_send_before(jsonObject2.getString("is_send_before"));
                                            }else {
                                                model2.setIs_send_before("No");
                                            }

                                            model2.setName(model2.getName());
                                            model2.setEmail(model2.getEmail());

                                            list.add(model2);

                                            mAdapter = new SendArticleSleepAdapterClick(list, R.layout.articles_click_item,SendArticleListSleep.this);
                                            mRecyclerView.setAdapter(mAdapter);

                                        }
                                    }

                                }

                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(SendArticleListSleep.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("patient_id", cc.loadPrefString("patient_id_main"));
                Log.e("request article data", params.toString());
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
