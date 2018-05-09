package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import  java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.ConceptualizationData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxicoders on 29/8/17.
 */

public class ConceptualizationViewActivity extends AppCompatActivity {

    CommanClass cc;
    ProgressDialog pDialog;

    LinearLayout lnFullConcep,ln_no_concep;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    ImageView ivCreateConceptualization,ivCreatePdf;

    TextView tvConcepOne,tvConcepTwo,tvConcepThree,tvPname;

    String sOne,sTwo,sThree;

    int counter = 0;

    String p_name,p_last_name,p_dob;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conceptualization_view);

        cc = new CommanClass(this);

        p_name = cc.loadPrefString("p_namee2");
        p_last_name = cc.loadPrefString("p_namee_last");
        p_dob = cc.loadPrefString("p_dob");

        Log.i("PatientData",p_name);
        Log.i("PatientData",p_last_name);
        Log.i("PatientData",p_dob);

        init();

    }

    private void init() {

        ivCreateConceptualization = (ImageView)findViewById(R.id.iv_create_conceptualization);
        ivCreatePdf = (ImageView)findViewById(R.id.iv_create_pdf);

        tvConcepOne = (TextView) findViewById(R.id.tv_concep_one);
        tvConcepTwo = (TextView) findViewById(R.id.tv_concep_two);
        tvConcepThree = (TextView) findViewById(R.id.tv_concep_three);

        tvPname = (TextView) findViewById(R.id.tv_pname);


        lnFullConcep = (LinearLayout) findViewById(R.id.ln_full_conceptualization);
        ln_no_concep = (LinearLayout) findViewById(R.id.ln_no_concep);


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.view_conceptualization));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        sOne = tvConcepOne.getText().toString().trim();
        sTwo = tvConcepTwo.getText().toString().trim();
        sThree = tvConcepThree.getText().toString().trim();

        ivCreateConceptualization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent editConIn = new Intent(ConceptualizationViewActivity.this,EditConceptualization.class);

               /* editConIn.putExtra("nucleares",sOne);
                editConIn.putExtra("regras",sTwo);
                editConIn.putExtra("infancia",sThree);*/

                startActivity(editConIn);

            }
        });

        ivCreatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPdf();
            }
        });
    }
    private void getConceptialization() {

        pDialog = new ProgressDialog(ConceptualizationViewActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CONCEPTUALIZATION,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        List<ConceptualizationData> data = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray jsonArray = jsonObject.optJSONArray("conceptualization_detail");

                                for(int i=0; i < jsonArray.length(); i++){

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    ConceptualizationData items = new ConceptualizationData();

                                    lnFullConcep.setVisibility(View.VISIBLE);
                                    ln_no_concep.setVisibility(View.GONE);

                                    items.setInfancia(jsonObject1.getString("infancia"));
                                    items.setNucleares(jsonObject1.getString("nucleares"));
                                    items.setRegras(jsonObject1.getString("regras"));
                                    items.setConcepId(jsonObject1.getString("id"));
                                    items.setPatient_name(jsonObject1.getString("name"));
                                    items.setPatient_last_name(jsonObject1.getString("last_name_initial"));
                                    items.setUser_type(jsonObject1.getString("user_type"));

                                    String birthday = jsonObject1.getString("birth_date");
                                    Calendar calendarBirthday = Calendar.getInstance();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        calendarBirthday.setTime(sdf.parse(birthday));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar calendarNow = Calendar.getInstance();
                                    int yearNow = calendarNow.get(Calendar.YEAR);
                                    int yearBirthday = calendarBirthday.get(Calendar.YEAR);
                                    int years = yearNow - yearBirthday;

                                    Log.i("TotalYears", String.valueOf(years));


                                    tvConcepOne.setText(items.getNucleares());
                                    tvConcepTwo.setText(items.getRegras());
                                    tvConcepThree.setText(items.getInfancia());
                                    tvPname.setText(items.getPatient_name() + " " + items.getPatient_last_name() +", "+ years +" " +"years old" + ", " + items.getUser_type());

                                    cc.savePrefString("nucleares",items.getNucleares());
                                    cc.savePrefString("regras",items.getRegras());
                                    cc.savePrefString("infancia",items.getInfancia());

                                    data.add(items);

                                }

                                pDialog.dismiss();

                            } else if (jsonObject.getString("status").equals("404")){

                                String noData = jsonObject.getString("message");

                                lnFullConcep.setVisibility(View.GONE);
                                ln_no_concep.setVisibility(View.VISIBLE);

                                tvConcepOne.setText(noData);
                                tvConcepTwo.setText(noData);
                                tvConcepThree.setText(noData);

                                Calendar calendarBirthday = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    calendarBirthday.setTime(sdf.parse(p_dob));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Calendar calendarNow = Calendar.getInstance();
                                int yearNow = calendarNow.get(Calendar.YEAR);
                                int yearBirthday = calendarBirthday.get(Calendar.YEAR);
                                int years = yearNow - yearBirthday;

                                tvPname.setText(p_name + " " + p_last_name +", "+ years +" " +getResources().getString(R.string.year_old)+ ", " + getResources().getString(R.string.patient2));

                                pDialog.dismiss();


                            }else {

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
                AndyUtils.showToast(ConceptualizationViewActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", cc.loadPrefString("patient_id_main"));

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

    private void createPdf(){
        // create a new document
        PdfDocument document = new PdfDocument();


        View content =  (View) lnFullConcep;
        Log.e("content", content + "");
        Log.e("content_height", content.getHeight() + "");
        Log.e("content_width", content.getWidth() + "");

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                content.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        content.draw(page.getCanvas());
        document.finishPage(page);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String pdfName = "skepsis_"+p_name+"_"+ counter +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
        counter++;

        File outputFile = new File("/sdcard/skepsis/", pdfName);


        try {
                outputFile.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
                Toast.makeText(this,getString(R.string.pdf_download), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,getString(R.string.ws_error)+ e.toString(),
                    Toast.LENGTH_LONG).show();
        }

      /*  // write the document content
        String targetPdf = "/sdcard/skepsis.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Pdf created successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(ConceptualizationViewActivity.this,getString(R.string.no_internet));

        } else {

            getConceptialization();
        }

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
