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
import com.mxicoders.skepci.adapter.QuestionaryPatientListAdapter;
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
 * Created by vishal on 13/2/18.
 */

public class QuestionnariesPatientListActivity extends AppCompatActivity {

    CommanClass cc;

    RecyclerView mRecyclerView;
    QuestionaryPatientListAdapter mAdapter;
    ArrayList<QuestionariesData> list;

    String id;
    ProgressDialog pDialog;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String admin_count;
    int admin_count_pos = 0;

    public QuestionnariesPatientListActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_questionarues);
        super.onCreate(savedInstanceState);


        cc = new CommanClass(QuestionnariesPatientListActivity.this);

        mRecyclerView = (RecyclerView)findViewById(R.id.ques_re_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionnariesPatientListActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);
        imgToolTwo.setVisibility(View.INVISIBLE);
        tooName.setText(getString(R.string.ques_category_re));
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

                Intent narticle = new Intent(QuestionnariesPatientListActivity.this, NewQuestiResultActivity.class);
                finish();
                startActivity(narticle);
            }
        });

        getCategory();

    }

    private void getCategory() {

        pDialog = new ProgressDialog(QuestionnariesPatientListActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CATEGORY_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("Response",response);

                        List<QuestionnariPyschologistData> data = new ArrayList<>();
                        List<QuestionnariPyschologistData> data2 = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray2 = jsonObject.optJSONArray("category_data_admin");

                                for(int j=0; j < jsonArray2.length(); j++){

                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);

                                    QuestionnariPyschologistData items2 = new QuestionnariPyschologistData();

                                    items2.setQue_count_admin_demo(jsonObject2.getString("que_count_admin"));

                                    data2.add(items2);

                                }

                                JSONArray jsonArray = jsonObject.optJSONArray("category_data");

                                for(int i=0; i < jsonArray.length(); i++){

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    QuestionnariPyschologistData items = new QuestionnariPyschologistData();

                                    items.setId(jsonObject1.getString("id"));
                                    items.setName(jsonObject1.getString("category_name"));
                                    items.setCount_question(jsonObject1.getString("que_count"));
                                    items.setPhoto(jsonObject1.getString("category_image"));

                                    for(int k=0; k < data2.size(); k++){

                                        if (admin_count_pos == k){

                                            QuestionnariPyschologistData admin_count_data = data2.get(k);

                                            admin_count = admin_count_data.getQue_count_admin_demo();

                                            Log.e("admin_count",admin_count);
                                        }
                                    }

                                    if (admin_count_pos == i){

                                        items.setQue_count_admin(admin_count);
                                    }
                                    admin_count_pos++;

                                    data.add(items);

                                }


                                mAdapter = new QuestionaryPatientListAdapter(QuestionnariesPatientListActivity.this,data);
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
                AndyUtils.showToast(QuestionnariesPatientListActivity.this,getString(R.string.ws_error));
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

