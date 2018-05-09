package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class NewArticlesActivity extends AppCompatActivity {

    CommanClass cc;

    EditText edArticleTitle,edArticleAuthor,edArticleBody;
    Spinner spArticleCategory;
    CheckBox chArticleVisibility;
    TextView tvArticleSubmit;
    LinearLayout lnBold,lnItalic;

    String articleCategoryId;

    ProgressDialog pDialog;

    ArrayAdapter<String> adapter;

    String articleTitle,articleAuthor,articleBody;

    int c_position;

    String[] itemstwo = {};
    String chArticleValue = "No";

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;


    public NewArticlesActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.new_articles_fragment);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        edArticleTitle = (EditText)findViewById(R.id.ed_Article_title);
        edArticleAuthor = (EditText)findViewById(R.id.ed_Article_Author);
        edArticleBody = (EditText)findViewById(R.id.ed_article_body);

        spArticleCategory = (Spinner)findViewById(R.id.sp_category_article);
        getArticleCategory();

        chArticleVisibility = (CheckBox)findViewById(R.id.ch_article_visibility);

        tvArticleSubmit = (TextView)findViewById(R.id.tv_article_signup);

        lnBold = (LinearLayout)findViewById(R.id.ln_bold);
        lnItalic = (LinearLayout)findViewById(R.id.ln_italic);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.new_article));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        lnBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edArticleBody.setTypeface(Typeface.DEFAULT_BOLD);
            }
        });

        lnItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edArticleBody.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            }
        });

        spArticleCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                c_position = position;

                articleCategoryId = String.valueOf(adapter.getItemId(position));

                articleCategoryId = itemstwo[c_position];

                Log.i("Selected Article ID",articleCategoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvArticleSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createArticleValidation();
            }
        });

        chArticleVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    chArticleValue = "Yes";


                } else {

                    chArticleValue = "No";
                }
            }
        });


    }


    private void createArticleValidation() {

        articleTitle = edArticleTitle.getText().toString().trim();
        articleAuthor = edArticleAuthor.getText().toString().trim();
        articleBody = edArticleBody.getText().toString().trim();


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(NewArticlesActivity.this,getString(R.string.no_internet));
        } else if (articleTitle.equals("")) {
            AndyUtils.showToast(NewArticlesActivity.this,getString(R.string.article_title));
        } else if (articleAuthor.equals("")) {
            AndyUtils.showToast(NewArticlesActivity.this,getString(R.string.article_author));
        } else if (articleBody.equals("")) {
            AndyUtils.showToast(NewArticlesActivity.this,getString(R.string.article_body));
        }
        else {

            createArticle(articleTitle,articleAuthor,articleBody,articleCategoryId,chArticleValue);

        }

    }

    private void getArticleCategory() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_ARTICLE_LIST,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray = jsonObject.optJSONArray("category data");
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
                                adapter= new ArrayAdapter<String>(NewArticlesActivity.this,android.R.layout.simple_spinner_item, items);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spArticleCategory.setAdapter(adapter);


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
                AndyUtils.showToast(NewArticlesActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("patient_id", cc.loadPrefString("patient_id_main"));
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

    private void createArticle(final String articleTitle, final String articleAuthor, final String articleBody,
                               final String articleCategoryId, final String chArticleValue) {

        pDialog = new ProgressDialog(NewArticlesActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CREATE_ARTICLE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:createarticle", response);

                        clearfield();
                        jsonParseArticleCreate(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(NewArticlesActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id",cc.loadPrefString("user_id"));
                params.put("title", articleTitle);
                params.put("author", articleAuthor);
                params.put("body", articleBody);
                params.put("article_category_id", articleCategoryId);
                params.put("public_visible", chArticleValue);

                Log.i("request create article", params.toString());

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



    private void jsonParseArticleCreate(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("article Error",e.toString());
        }
    }

    private void clearfield() {

        edArticleTitle.setText("");
        edArticleAuthor.setText("");
        edArticleBody.setText("");
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
