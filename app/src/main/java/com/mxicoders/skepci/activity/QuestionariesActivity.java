package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.QuestionnariPsychologistAdapter;
import com.mxicoders.skepci.model.QuestionariesData;
import com.mxicoders.skepci.model.QuestionnariPyschologistData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxicoders on 14/7/17.
 */

public class QuestionariesActivity extends AppCompatActivity {

    CommanClass cc;

    RecyclerView mRecyclerView;
    QuestionnariPsychologistAdapter mAdapter;
    ArrayList<QuestionariesData> list;

    String id;
    ProgressDialog pDialog;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public QuestionariesActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_questionarues);
        super.onCreate(savedInstanceState);


        cc = new CommanClass(QuestionariesActivity.this);

        mRecyclerView = (RecyclerView)findViewById(R.id.ques_re_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionariesActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);
        tooName.setText(getString(R.string.ques_category));
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

                Intent narticle = new Intent(QuestionariesActivity.this, NewQuestiResultActivity.class);
                finish();
                startActivity(narticle);
            }
        });

        getCategory();

    }

    private void getCategory() {

        pDialog = new ProgressDialog(QuestionariesActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CATEGORY_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        List<QuestionnariPyschologistData> data = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray = jsonObject.optJSONArray("category_data");

                                for(int i=0; i < jsonArray.length(); i++){

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    QuestionnariPyschologistData items = new QuestionnariPyschologistData();

                                    items.setName(jsonObject1.getString("category_name"));
                                    items.setCount_question(jsonObject1.getString("que_count"));
                                    items.setPhoto(jsonObject1.getString("category_image"));

                                    data.add(items);

                                }

                                mAdapter = new QuestionnariPsychologistAdapter(QuestionariesActivity.this,data);
                                mRecyclerView.setAdapter(mAdapter);

                                pDialog.dismiss();

                            } else {

                                jsonObject.getString("message");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Login Error",e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(QuestionariesActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());

                Log.i("token",cc.loadPrefString("user_token"));
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_questionarries, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_result) {



            return true;

        }
        return super.onOptionsItemSelected(item);
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
