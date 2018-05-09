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
import com.mxicoders.skepci.adapter.PatientMenuArticleAdapter;
import com.mxicoders.skepci.model.PatientArticleData;
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
 * Created by mxicoders on 20/7/17.
 */

public class PatientArticleMenuFragment extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    RecyclerView mRecyclerView;
    PatientMenuArticleAdapter mAdapter;
    ArrayList<PatientArticleData> list;
    TextView tvDemo,tvCount;
    NetworkImageView image;

    ProgressDialog pDialog;

    String dem,countArticle,articlePhoto;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String a_count;

    public PatientArticleMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_questionaries_two_click);
        super.onCreate(savedInstanceState);



        cc = new CommanClass(PatientArticleMenuFragment.this);

        /*a_count = cc.loadPrefString("a_count");*/

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);
        tvCount = (TextView)findViewById(R.id.text_count);



        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.article_list));
        tooName.setPadding(80, 0, 0, 0);


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.que_re_two);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PatientArticleMenuFragment.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(PatientArticleMenuFragment.this,getString(R.string.no_internet));
        }
        else {

            getArticleList();
        }


    }
    public void getArticleList() {

        pDialog = new ProgressDialog(PatientArticleMenuFragment.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ARTICLE_PATIENT,
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

                                JSONArray dataArray = jsonObject.getJSONArray("article_patient_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                   /* String cname = jsonObject1.getString("category_name");*/


                                    PatientArticleData model = new PatientArticleData();

                                    model.setName(jsonObject1.getString("title"));
                                    model.setaId(jsonObject1.getString("article_id"));
                                    model.setPhoto(jsonObject1.getString("category_image"));
                                    model.setaCount(jsonObject1.getString("article_count"));
                                    model.setBody(jsonObject1.getString("body"));
                                    model.setAuthor(jsonObject1.getString("author"));
                                    model.setIsLike(jsonObject1.getString("is_liked"));

                                    tvCount.setText(model.getaCount());

                                    model.setName(model.getName());

                                    list.add(model);


                                    mAdapter = new PatientMenuArticleAdapter(list,R.layout.questionaries_click_item,PatientArticleMenuFragment.this);
                                    mRecyclerView.setAdapter(mAdapter);
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
                AndyUtils.showToast(PatientArticleMenuFragment.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
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

