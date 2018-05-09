package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.Category;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 17/7/17.
 */

public class NewQuestiResultActivity extends AppCompatActivity {


    EditText edTitle,edRangeFrom,edRangeTo,edMedianFrom,edMedianTo;

    TextView tvQuesSubmit;

    Spinner spCatagory;

    String cid;

    ProgressDialog pDialog;

    ArrayAdapter<String> adapter;

    CommanClass cc;

    String title,rangeFrom,rangeTo,medianFrom,medianTo;

    int c_position;

    String[] itemstwo = {};

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public NewQuestiResultActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.new_questionnaries_result);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(NewQuestiResultActivity.this);

        edTitle = (EditText)findViewById(R.id.ed_que_title);
        edRangeFrom = (EditText)findViewById(R.id.ed_range_from);
        edRangeTo = (EditText)findViewById(R.id.ed_range_to);
        edMedianFrom = (EditText)findViewById(R.id.ed_median_from);
        edMedianTo = (EditText)findViewById(R.id.ed_median_to);

        tvQuesSubmit = (TextView)findViewById(R.id.tv_que_submit);

        spCatagory = (Spinner)findViewById(R.id.sp_category);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.new_que_result));
        tooName.setPadding(80, 0, 0, 0);

        getCategory();


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backCategory = new Intent(NewQuestiResultActivity.this,QuestionariesActivity.class);
                startActivity(backCategory);
                finish();
            }
        });

        spCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                c_position = position;

                cid = String.valueOf(adapter.getItemId(position));

                cid = itemstwo[c_position];

                Log.i("Selected ID",cid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvQuesSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QuestionnariesValidation();
            }
        });



    }

    private void getCategory() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CATEGORY_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray = jsonObject.optJSONArray("category_data");
                                String[] items = new String[jsonArray.length()];
                                itemstwo = new String[jsonArray.length()];
                                //Iterate the jsonArray and print the info of JSONObjects
                                for(int i=0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                                    Category cmodel = new Category();

                                        cmodel.setId(jsonObject1.optString("id").toString());

                                        cmodel.setName(jsonObject1.optString("category_name").toString());

                                        items[i] = cmodel.getName();

                                        itemstwo[i] = cmodel.getId();

                                }
                                adapter= new ArrayAdapter<String>(NewQuestiResultActivity.this,android.R.layout.simple_spinner_item, items);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spCatagory.setAdapter(adapter);


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
                AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.ws_error));
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

    private void QuestionnariesValidation(){

        title = edTitle.getText().toString().trim();
        rangeFrom = edRangeFrom.getText().toString().trim();
        rangeTo = edRangeTo.getText().toString().trim();
        medianFrom = edMedianFrom.getText().toString().trim();
        medianTo = edMedianTo.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.no_internet));
        } else if (title.equals("")) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.v_title));
        } else if (rangeFrom.equals("")) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.v_range_from));
        } else if (rangeTo.equals("")) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.v_range_to));
        } else if (medianFrom.equals("")) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.v_median_from));

        }else if (medianTo.equals("")) {
            AndyUtils.showToast(NewQuestiResultActivity.this,getString(R.string.v_median_to));
        }
        else {

            createQuestionnaries(title,rangeFrom,rangeTo,medianFrom,medianTo,cid);
        }
    }

    private void createQuestionnaries(final String title, final String rangeFrom, final String rangeTo,
                                      final String medianFrom, final String medianTo, final String cid) {

        pDialog = new ProgressDialog(NewQuestiResultActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.QUESTINNARIES_CREATE_BY_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:questionnaries", response);

                        clearfield();
                        jsonParseQuestionnariesCreate(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(NewQuestiResultActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("title", title);
                params.put("range_from", rangeFrom);
                params.put("range_to", rangeTo);
                params.put("median_from", medianFrom);
                params.put("median_to",medianTo);

                params.put("category_id", cid);

                params.put("user_id",cc.loadPrefString("user_id"));

                Log.i("request create ques", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put(Const.Params.Authorization, Const.Params.Authorization_value);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseQuestionnariesCreate(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                Intent backCategory = new Intent(NewQuestiResultActivity.this,QuestionariesActivity.class);
                startActivity(backCategory);
                finish();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Login Error",e.toString());
        }
    }

    private void clearfield() {

        edTitle.setText("");
        edRangeFrom.setText("");
        edRangeTo.setText("");
        edMedianFrom.setText("");
        edMedianTo.setText("");
    }

    @Override
    public void onBackPressed() {

        Intent backCategory = new Intent(NewQuestiResultActivity.this,QuestionariesActivity.class);
        startActivity(backCategory);
        finish();
        super.onBackPressed();
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
