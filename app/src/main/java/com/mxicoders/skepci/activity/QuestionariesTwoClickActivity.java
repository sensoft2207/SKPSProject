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
import com.mxicoders.skepci.adapter.QuestionariesClickAdapter;
import com.mxicoders.skepci.model.QuestionariesClickData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 14/7/17.
 */

public class QuestionariesTwoClickActivity extends AppCompatActivity /*implements ActivityMenuPsychologist.OnBackClickListener*/ {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    RecyclerView mRecyclerView;
    QuestionariesClickAdapter mAdapter;

    ArrayList<QuestionariesClickData> list;


    TextView tvDemo,tvCount;
    NetworkImageView image;

    ProgressDialog pDialog;

    String dem,countQues,photo;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public QuestionariesTwoClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_questionaries_two_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(QuestionariesTwoClickActivity.this);

        dem =  cc.loadPrefString("category_idd");
        countQues =  cc.loadPrefString("count_ques");
        photo =  cc.loadPrefString("photo");

        tvDemo = (TextView)findViewById(R.id.text_name);
        tvCount = (TextView)findViewById(R.id.text_count);
        image = (NetworkImageView)findViewById(R.id.Image);

        tvDemo.setText(dem);
        tvCount.setText(countQues);
        image.setImageUrl(photo, imageLoader);

        mRecyclerView = (RecyclerView)findViewById(R.id.que_re_two);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionariesTwoClickActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

        getQuestionnariesList();

    }


    public void getQuestionnariesList() {

        pDialog = new ProgressDialog(QuestionariesTwoClickActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.QUESTINNARIES_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:ques data", response);


                        list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("questionnaire_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    String cname = jsonObject1.getString("category_name");

                                    if (dem.contentEquals(cname)) {

                                        QuestionariesClickData model = new QuestionariesClickData();

                                        model.setName(jsonObject1.getString("title"));
                                        model.setEmail(jsonObject1.getString("category_name"));

                                        model.setName(model.getName());
                                        model.setEmail(model.getEmail());

                                        list.add(model);

                                        mAdapter = new QuestionariesClickAdapter(list,R.layout.questionaries_click_item,QuestionariesTwoClickActivity.this);
                                        mRecyclerView.setAdapter(mAdapter);

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
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request profile data", params.toString());
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

