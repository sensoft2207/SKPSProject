package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
 * Created by mxicoders on 30/8/17.
 */

public class EditConceptualization extends AppCompatActivity  {

    CommanClass cc;
    ProgressDialog pDialog;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgSave,imgToolBack;

    LinearLayout lnBold,lnBold2,lnBold3,lnItalic,lnItalic2,lnItalic3;
    EditText edOne,edTwo,edThree;

    String eOne,eTwo,eThree;
    String enOne,enTwo,enThree;

    CharacterStyle styleBold,styleItalc;
    boolean bold = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_conceptualization);

        cc = new CommanClass(this);

        /*enOne = getIntent().getStringExtra("nucleares");
        enTwo = getIntent().getStringExtra("regras");
        enThree = getIntent().getStringExtra("infancia");*/

        enOne = cc.loadPrefString("nucleares");
        enTwo = cc.loadPrefString("regras");
        enThree = cc.loadPrefString("infancia");

        Log.i("Datatata",enOne);
        Log.i("Datatata",enTwo);
        Log.i("Datatata",enThree);

        init();

    }

    private void init() {


        styleBold = new StyleSpan(Typeface.BOLD);
        styleItalc = new StyleSpan(Typeface.ITALIC);

        lnBold = (LinearLayout)findViewById(R.id.ln_bold);
        lnBold2 = (LinearLayout)findViewById(R.id.ln_bold2);
        lnBold3 = (LinearLayout)findViewById(R.id.ln_bold3);
        lnItalic = (LinearLayout)findViewById(R.id.ln_italic);
        lnItalic2 = (LinearLayout)findViewById(R.id.ln_italic2);
        lnItalic3 = (LinearLayout)findViewById(R.id.ln_italic3);


        edOne = (EditText) findViewById(R.id.ed_concep_one);
        edTwo = (EditText)findViewById(R.id.ed_concep_two);
        edThree = (EditText)findViewById(R.id.ed_concep_three);

        edOne.setText(enOne);
        edTwo.setText(enTwo);
        edThree.setText(enThree);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgSave = (ImageView)findViewById(R.id.tool_concep_save);


        tooName.setText(getString(R.string.edit_condeptualization));
        //tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        lnBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  String bold = edOne.getText().toString();

                edOne.setTypeface(null, Typeface.BOLD);
               /* int start = edOne.getSelectionStart();
                int end = edOne.getSelectionEnd();

                SpannableStringBuilder sb = new SpannableStringBuilder(bold);

                sb.setSpan(styleItalc, start, end, 0);*/
//                edOne.setText(sb);
            }
        });

        lnBold2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edTwo.setTypeface(null, Typeface.BOLD);
            }
        });

        lnBold3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edThree.setTypeface(null, Typeface.BOLD);
            }
        });

        lnItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* String italic = edOne.getText().toString();
                int start = edOne.getSelectionStart();
                int end = edOne.getSelectionEnd();

                SpannableStringBuilder sb = new SpannableStringBuilder(italic);

                sb.setSpan(styleBold, start, end, 0);
                edOne.setText(sb);*/

                edOne.setTypeface(null, Typeface.ITALIC);


            }
        });

        lnItalic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edTwo.setTypeface(null, Typeface.ITALIC);
            }
        });

        lnItalic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edThree.setTypeface(null, Typeface.ITALIC);
            }
        });



        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editConceptualizationValidation();

            }
        });


    }

    private void editConceptualizationValidation() {


        eOne = edOne.getText().toString().trim();
        eTwo = edTwo.getText().toString().trim();
        eThree = edThree.getText().toString().trim();


        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(EditConceptualization.this,getString(R.string.no_internet));

        } else {

            editConceptualization(eOne,eTwo,eThree);
        }

    }

    private void editConceptualization(final String eOne, final String eTwo, final String eThree) {

        pDialog = new ProgressDialog(EditConceptualization.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.EDIT_CONCEPTUALIZATION,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                pDialog.dismiss();

                                AndyUtils.showToast(EditConceptualization.this,jsonObject.getString("message"));

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
                AndyUtils.showToast(EditConceptualization.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", cc.loadPrefString("patient_id_main"));
                params.put("nucleares", eOne);
                params.put("regras", eTwo);
                params.put("infancia", eThree);

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

