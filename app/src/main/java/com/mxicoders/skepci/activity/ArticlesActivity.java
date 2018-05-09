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
import com.mxicoders.skepci.adapter.ArticlesAdapter;
import com.mxicoders.skepci.model.ArticlesData;
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
 * Created by mxicoders on 29/07/17.
 */
public class ArticlesActivity extends AppCompatActivity {

    CommanClass cc;

    public static   RecyclerView mRecyclerVieww;
    ArticlesAdapter mAdapter;
    ArrayList<ArticlesData> list;

    String id;
    ProgressDialog pDialog;

    String title = "";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public ArticlesActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_articles);

        cc = new CommanClass(this);

        mRecyclerVieww = (RecyclerView)findViewById(R.id.list);
        mRecyclerVieww.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerVieww.setItemAnimator(new DefaultItemAnimator());

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);
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

                Intent narticle = new Intent(ArticlesActivity.this, NewArticlesActivity.class);
                startActivity(narticle);
            }
        });



    }

    private void getArticleCategory() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ARTICLE_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("response",response);

                        List<ArticlesData> data = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray = jsonObject.optJSONArray("category data");

                                for(int i=0; i < jsonArray.length(); i++){

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    ArticlesData items = new ArticlesData();

                                    items.setName(jsonObject1.getString("category_name"));
                                    items.setCount_question(jsonObject1.getString("article_count"));
                                    items.setCategory_like_count(jsonObject1.getString("category_like_count"));
                                    items.setPhoto(jsonObject1.getString("category_image"));
                                    items.setId(jsonObject1.getString("id"));

                                    Log.e("articlecountcategory",jsonObject1.getString("article_count"));

                                    data.add(items);

                                }

                                mAdapter = new ArticlesAdapter(data,R.layout.articles_adapter_item,ArticlesActivity.this);
                                mRecyclerVieww.setAdapter(mAdapter);

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
                AndyUtils.showToast(ArticlesActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("patient_id","");

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
        inflater.inflate(R.menu.menu_articles, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_new_articles) {

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        Log.e("OOOOOOOOPy",cc.loadPrefString("user_id"));
        Log.e("OOOOOOOOP",cc.loadPrefString("patient_id_main"));

        getArticleCategory();
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
