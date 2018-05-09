package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.RpdViewAdapter;
import com.mxicoders.skepci.adapter.RpdViewAdapterOne;
import com.mxicoders.skepci.adapter.RpdViewAdapterThree;
import com.mxicoders.skepci.adapter.RpdViewAdapterTwo;
import com.mxicoders.skepci.model.RpdData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mxicoders on 24/8/17.
 */

public class RpdPatientListClickMenu extends AppCompatActivity {

    RpdViewAdapter mAdapterOne;
    RpdViewAdapterTwo mAdapterTwo;
    RpdViewAdapterThree mAdapterThree;
    RpdViewAdapterOne mAdapterfour;

    ArrayList<RpdData> rpd_list_thought = null;
    ArrayList<RpdData> rpd_list_evidence = null;
    ArrayList<RpdData> rpd_list_count_evi = null;
    ArrayList<RpdData> rpd_list_conclusion = null;
    ArrayList<RpdData> rpd_list_situation = null;


    ArrayList<RpdData> hot_list_yes;

    /*TextView tvSituation,tvHotOne,tvHotTwo,tvHotThree,tvEvidenceOne,tvEvidenceTwo,tvCounterEvidenceOne,
            tvCounterEvidenceTwo,tvConclusionOne,tvConclusionTwo,tvClose;

    String Situation,HotOne,HotTwo,HotThree,EvidenceOne,EvidenceTwo,
            CounterEvidenceOne,CounterEvidenceTwo,ConclusionOne,ConclusionTwo;*/

    TextView tvSituation,tvDateOne,tvDateTwo,tvClose;

    RecyclerView rcEvidence,rcCounterEvidence,rcConclusion,rcThoght;

    String rpdID;

    RelativeLayout toolbar;
    TextView tooName,tv_rpd_emotionname;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    ProgressDialog pDialog;

    ImageView iv_emotion;

    CommanClass cc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.rpd_patientlist_click_menu);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);


        /*SimpleDateFormat fromFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.US);
        SimpleDateFormat toFormat = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.US);

        try {
            Date fromDate = fromFormat.parse(pubDate[i]) //pubDate[i] is your date (node value)
            pubDate[i] = toFormat.format(fromDate);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        hot_list_yes = new ArrayList<>();
        rpdID = cc.loadPrefString("rpd_id");

        Log.i("RPD_ID",rpdID);


        rcEvidence = (RecyclerView)findViewById(R.id.rc_evidence);
        rcCounterEvidence = (RecyclerView)findViewById(R.id.rc_counter_evidence);
        rcConclusion = (RecyclerView)findViewById(R.id.rc_conclusion);
        rcThoght = (RecyclerView)findViewById(R.id.rcThoghts);

        init();



    }

    private void init() {

        tvSituation = (TextView)findViewById(R.id.tv_rpd_situation);
        tv_rpd_emotionname = (TextView)findViewById(R.id.tv_rpd_emotionname);

        /*tvSituation = (TextView)findViewById(R.id.tv_rpd_situation);
        tvHotOne = (TextView)findViewById(R.id.tv_rpd_hot_1);
        tvHotTwo = (TextView)findViewById(R.id.tv_rpd_hot_2);
        tvHotThree = (TextView)findViewById(R.id.tv_rpd_hot_3);
        tvEvidenceOne = (TextView)findViewById(R.id.tv_rpd_evidence_one);
        tvEvidenceTwo = (TextView)findViewById(R.id.tv_rpd_evidence_two);
        tvConclusionOne = (TextView)findViewById(R.id.tv_rpd_conclusion_one);
        tvConclusionTwo = (TextView)findViewById(R.id.tv_rpd_conclusion_two);
        tvCounterEvidenceOne = (TextView)findViewById(R.id.tv_rpd_counter_evidence_one);
        tvCounterEvidenceTwo = (TextView)findViewById(R.id.tv_rpd_counter_evidence_two);
*/
        tvClose = (TextView)findViewById(R.id.tv_rpd_close_view);
        tvDateOne = (TextView)findViewById(R.id.tv_rpd_date_one);
        tvDateTwo = (TextView)findViewById(R.id.tv_rpd_date_two);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);
        iv_emotion = (ImageView)findViewById(R.id.iv_emotion);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);


        tooName.setText(getString(R.string.rpd2));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    public void getRpd(final String rpdId) {

        pDialog = new ProgressDialog(RpdPatientListClickMenu.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_RPD,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:rpd data", response);


                        rpd_list_thought = new ArrayList<>();
                        rpd_list_evidence = new ArrayList<>();
                        rpd_list_count_evi = new ArrayList<>();
                        rpd_list_conclusion = new ArrayList<>();
                        rpd_list_situation = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                                JSONArray dataArray = jsonObject.getJSONArray("rpd_list");



                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    String situation = jsonObject1.getString("situation");
                                    String date = jsonObject1.getString("modified_date");

                                    String emotion = jsonObject1.getString("emotion");

                                    tv_rpd_emotionname.setText(emotion);

                                    if (emotion.equals("Tranqullidade")) {
                                        iv_emotion.setImageResource(R.drawable.tranqullidade);
                                    } else if (emotion.equals("Tedlo")) {
                                        iv_emotion.setImageResource(R.drawable.tedlo);
                                    } else if (emotion.equals("Saudades")) {
                                        iv_emotion.setImageResource(R.drawable.saudades);
                                    } else if (emotion.equals("Vergonha")) {
                                        iv_emotion.setImageResource(R.drawable.vergonha);
                                    }else if (emotion.equals("Orgulho")) {
                                        iv_emotion.setImageResource(R.drawable.orgulho);
                                    }else if (emotion.equals("Tristeza")) {
                                        iv_emotion.setImageResource(R.drawable.tristeza);
                                    }
                                    else if (emotion.equals("Amor")) {
                                        iv_emotion.setImageResource(R.drawable.amor);
                                    }
                                    else if (emotion.equals("Solidao")) {
                                        iv_emotion.setImageResource(R.drawable.solidao);
                                    }else if (emotion.equals("Esperanca")) {
                                        iv_emotion.setImageResource(R.drawable.esperanca);
                                    }else if (emotion.equals("Decepcao")) {
                                        iv_emotion.setImageResource(R.drawable.decepcao);
                                    }else if (emotion.equals("Alegria")) {
                                        iv_emotion.setImageResource(R.drawable.alegria);
                                    }else if (emotion.equals("Confusao")) {
                                        iv_emotion.setImageResource(R.drawable.confusao);
                                    }else if (emotion.equals("Entusiansmo")) {
                                        iv_emotion.setImageResource(R.drawable.entusiansmo);
                                    }else if (emotion.equals("Nojo")) {
                                        iv_emotion.setImageResource(R.drawable.nojo);
                                    }else if (emotion.equals("Ansiedade")) {
                                        iv_emotion.setImageResource(R.drawable.ansiedade);
                                    }else if (emotion.equals("Preacupacao")) {
                                        iv_emotion.setImageResource(R.drawable.preacupacao);
                                    }else if (emotion.equals("Raiva")) {
                                        iv_emotion.setImageResource(R.drawable.raiva);
                                    }else if (emotion.equals("Desconfianca")) {
                                        iv_emotion.setImageResource(R.drawable.desconfianca);
                                    }else if (emotion.equals("Medo")) {
                                        iv_emotion.setImageResource(R.drawable.medo);
                                    }else if (emotion.equals("Cupla")) {
                                        iv_emotion.setImageResource(R.drawable.cupla);
                                    }
                                    else {
                                        iv_emotion.setImageResource(R.drawable.angry);
                                    }

                                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                    SimpleDateFormat output = new SimpleDateFormat("EEEE, dd-MMM-yyyy",Locale.ENGLISH);

                                    try {
                                        Date oneWayTripDate = input.parse(date);
                                        date = output.format(oneWayTripDate);

                                        Log.i("DateRpd",date);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    String[] firstdateSplit = date.split(",");

                                    String day = firstdateSplit[0];
                                    String datee = firstdateSplit[1];

                                    String[] seconddateSplit = datee.split("-");

                                    String finalDate = seconddateSplit[0];
                                    String finalMonth = seconddateSplit[1];
                                    String finalYear = seconddateSplit[2];

                                    tvDateOne.setText(day);
                                    tvDateTwo.setText(finalDate +" "+"of"+" "+ finalMonth);

                                    Log.i("Day",day);
                                    Log.i("Datee",datee);
                                    Log.i("FinalDate",finalDate);
                                    Log.i("month",finalMonth);
                                    Log.i("year",finalYear);

                                    tvSituation.setText(situation);

                                    if (jsonObject1.getString("rpd_text_type").contains("thoughts")){

                                        RpdData model1 = new RpdData();

                                        /*model1.setRpd_name(jsonObject1.getString("rpd_text"));*/
                                        model1.setRpd_name(jsonObject1.getString("rpd_text"));
                                        if(jsonObject1.getString("hot_one").equalsIgnoreCase("Yes")){

                                            model1.setHot_selected(true);
                                        }else{
                                            model1.setHot_selected(false);
                                        }
                                        rpd_list_thought.add(model1);


                                    }else if (jsonObject1.getString("rpd_text_type").contentEquals("evidance")){

                                        RpdData model2 = new RpdData();

                                        model2.setRpd_name(jsonObject1.getString("rpd_text"));

                                        rpd_list_evidence.add(model2);


                                    }else if (jsonObject1.getString("rpd_text_type").contentEquals("counterevidance")){

                                        RpdData model3 = new RpdData();

                                        model3.setRpd_name(jsonObject1.getString("rpd_text"));

                                        rpd_list_count_evi.add(model3);


                                    }else if (jsonObject1.getString("rpd_text_type").contains("conclusion")){

                                        RpdData model4 = new RpdData();

                                        model4.setRpd_name(jsonObject1.getString("rpd_text"));

                                        rpd_list_conclusion.add(model4);

                                    }

                                }

                                LinearLayoutManager layoutManage1r= new LinearLayoutManager(RpdPatientListClickMenu.this);
                                mAdapterfour = new RpdViewAdapterOne(rpd_list_thought, R.layout.rpd_view_item,RpdPatientListClickMenu.this);
                                rcThoght.setLayoutManager(layoutManage1r);
                                rcThoght.setAdapter(mAdapterfour);

                                LinearLayoutManager layoutManager= new LinearLayoutManager(RpdPatientListClickMenu.this);
                                mAdapterOne = new RpdViewAdapter(rpd_list_evidence, R.layout.rpd_view_item_2,RpdPatientListClickMenu.this);
                                rcEvidence.setLayoutManager(layoutManager);
                                rcEvidence.setAdapter(mAdapterOne);

                                LinearLayoutManager layoutManager2= new LinearLayoutManager(RpdPatientListClickMenu.this);
                                mAdapterTwo = new RpdViewAdapterTwo(rpd_list_count_evi, R.layout.rpd_view_item_3,RpdPatientListClickMenu.this);
                                rcCounterEvidence.setLayoutManager(layoutManager2);
                                rcCounterEvidence.setAdapter(mAdapterTwo);


                                LinearLayoutManager layoutManager3= new LinearLayoutManager(RpdPatientListClickMenu.this);
                                mAdapterThree = new RpdViewAdapterThree(rpd_list_conclusion, R.layout.rpd_view_4,RpdPatientListClickMenu.this);
                                rcConclusion.setLayoutManager(layoutManager3);
                                rcConclusion.setAdapter(mAdapterThree);


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
                AndyUtils.showToast(RpdPatientListClickMenu.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", cc.loadPrefString("patient_id_main"));
                params.put("rpd_id",rpdId);
                Log.e("request rpd data", params.toString());
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


        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(RpdPatientListClickMenu.this,getString(R.string.no_internet));

        }else{

            getRpd(rpdID);

        }


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
