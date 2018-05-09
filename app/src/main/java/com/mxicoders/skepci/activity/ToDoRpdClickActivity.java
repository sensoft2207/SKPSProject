package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.EmotionGridAdapterDialog;
import com.mxicoders.skepci.model.RpdData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 26/7/17.
 */

public class ToDoRpdClickActivity extends AppCompatActivity {

    CommanClass cc;
    ProgressDialog pDialog;

    ArrayList<RpdData> rpd_list_thought = null;
    ArrayList<RpdData> rpd_list_evidence = null;
    ArrayList<RpdData> rpd_list_count_evi = null;
    ArrayList<RpdData> rpd_list_conclusion = null;

    ArrayList<RpdData> hot_list_yes;
    ArrayList<RpdData> hot_list_no;

    LinearLayout lnRpdOne,lnRpdTwo,lnRpdThree,lnRpdFour,lnRpdRadio;
    ImageView ivRpdOne,ivRpdTwo,ivRpdThree,ivRpdFour;
    EditText dOne,dTwo,dThree,dFour;

    TextView tvRpdSubmit;
    TextView tvChooseEmotion;

    GridView emotionGridDialog;
    ImageView closeEmotionDialog;
    TextView tvSubmitEmotionDialog;
    EmotionGridAdapterDialog gridAdapter;

    EditText edSituation,edAutoThoghtOne,edAutoThoghtTwo,edEvidenceOne,edEvidenceTwo,
            edCounEvidenceOne,edCounEvidenceTwo,edConclusionOne,edConclusionTwo;

    String Situation,AutoThoghtOne,AutoThoghtTwo,EvidenceOne,EvidenceTwo,CounEvidenceOne,
            CounEvidenceTwo,ConclusionOne,ConclusionTwo;

    RadioGroup rgHot,rgHotDyna;

    RadioButton hotone,hottwo;

    String hot,hot2,hott;


    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    LinearLayout lnEmotionChoose;

    Dialog dialog;

    String emotionName;
    String emo;

    ArrayList<Integer> items;

    String radio_one,radio_two;


    static final String[] MOBILE_OS = new String[] {
            "Tranqullidade", "Tedlo","Saudades", "Vergonha", "Orgulho", "Tristeza", "Amor", "Solidao","Esperanca",
            "Decepcao", "Alegria", "Confusao", "Entusiansmo", "Nojo","Ansiedade",
            "Preacupacao", "Raiva", "Desconfianca", "Medo", "Cupla"};

    public ToDoRpdClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.frag_patient_todo_rpd_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        rpd_list_thought = new ArrayList<>();
        rpd_list_evidence = new ArrayList<>();
        rpd_list_count_evi = new ArrayList<>();
        rpd_list_conclusion = new ArrayList<>();



        hot_list_yes = new ArrayList<>();
        hot_list_no = new ArrayList<>();



        init();

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.rpd2));
        tooName.setPadding(75, 0, 0, 0);


        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void init() {

        lnRpdOne = (LinearLayout) findViewById(R.id.ln_rpd_one);
        lnRpdTwo = (LinearLayout) findViewById(R.id.ln_rpd_two);
        lnRpdThree = (LinearLayout) findViewById(R.id.ln_rpd_three);
        lnRpdFour = (LinearLayout) findViewById(R.id.ln_rpd_four);


        ivRpdOne = (ImageView)findViewById(R.id.iv_rpd_one);
        ivRpdTwo = (ImageView)findViewById(R.id.iv_rpd_two);
        ivRpdThree = (ImageView)findViewById(R.id.iv_rpd_three);
        ivRpdFour = (ImageView)findViewById(R.id.iv_rpd_four);


        lnEmotionChoose = (LinearLayout)findViewById(R.id.ln_emotion_choose);


        rgHot = (RadioGroup) findViewById(R.id.rg_rpd_hot);

        hotone = (RadioButton)findViewById(R.id.rd_hot_one);
        hottwo = (RadioButton)findViewById(R.id.rd_hot_two);

        tvRpdSubmit = (TextView)findViewById(R.id.tv_rpd_pa_submit);
        tvChooseEmotion = (TextView)findViewById(R.id.tv_choose_emotion);

        edSituation = (EditText)findViewById(R.id.ed_rpd_situation);
        edAutoThoghtOne = (EditText)findViewById(R.id.ed_rpd_auto_thoght_one);
        edAutoThoghtTwo = (EditText)findViewById(R.id.ed_rpd_auto_thoght_two);
        edCounEvidenceOne = (EditText)findViewById(R.id.ed_rpd_counter_evi_one);
        edCounEvidenceTwo = (EditText)findViewById(R.id.ed_rpd_counter_evi_two);
        edConclusionOne = (EditText)findViewById(R.id.ed_rpd_conclusion_one);
        edConclusionTwo = (EditText)findViewById(R.id.ed_rpd_conclusion_two);
        edEvidenceOne = (EditText)findViewById(R.id.ed_rpd_evidence_one);
        edEvidenceTwo = (EditText)findViewById(R.id.ed_rpd_evicence_two);

        hot = "Yes";
        hot2 = "No";



        /*ivRpdOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.rpd_edittext_item_two, null);
                dOne = (EditText)addView.findViewById(R.id.ed_rpd_auto_thoght_one);
                lnRpdRadio = (LinearLayout) addView.findViewById(R.id.ln_rd_rpd);

                rgHotDyna = (RadioGroup)addView.findViewById(R.id.rg_rpd_hot_dyna);

                String[] websitesArray = new String[1];

                //create radio buttons
                for (int i = 0; i < websitesArray.length; i++) {
                    RadioButton radioButton = new RadioButton(ToDoRpdClickActivity.this);
                    radioButton.setText(websitesArray[i]);
                    radioButton.setId(i);
                    lnRpdRadio.addView(radioButton);
                }

                lnRpdOne.addView(addView);
            }
        });

        ivRpdTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.rpd_edittext_item, null);
                dTwo = (EditText)addView.findViewById(R.id.ed_rpd_evidence_dyna);

                lnRpdTwo.addView(addView);
            }
        });

        ivRpdThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.rpd_edittext_item, null);
                dThree = (EditText)addView.findViewById(R.id.ed_rpd_evidence_dyna);
                dThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong,0, 0,0);
                dThree.setHint("Counter evidence");

                lnRpdThree.addView(addView);
            }
        });

        ivRpdFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.rpd_edittext_item, null);
                dFour = (EditText)addView.findViewById(R.id.ed_rpd_evidence_dyna);
                dFour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_rpd,0, 0, 0);
                dFour.setHint("Conclusion");

                lnRpdFour.addView(addView);
            }
        });
*/

        lnEmotionChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(ToDoRpdClickActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.rpd_emotion_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics3 = getResources().getDisplayMetrics();
                int width3 = metrics3.widthPixels;
                int height3 = metrics3.heightPixels;
                dialog.getWindow().setLayout((6 * width3) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

                dialog.show();

                emotionGridDialog = (GridView)dialog.findViewById(R.id.emotion_grid);
                tvSubmitEmotionDialog = (TextView) dialog.findViewById(R.id.tv_emotion_submit);
                closeEmotionDialog = (ImageView)dialog.findViewById(R.id.close_dialog);

                gridAdapter = new EmotionGridAdapterDialog(ToDoRpdClickActivity.this, MOBILE_OS);
                emotionGridDialog.setAdapter(gridAdapter);


                emotionGridDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        emotionName = String.valueOf(gridAdapter.getItem(position));

                        emotionName = MOBILE_OS[position];


                        Log.i("EmotionNameGrid",emotionName.toString());
                    }
                });


                closeEmotionDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                tvSubmitEmotionDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (tvChooseEmotion == null){

                            emotionName = "Tranqullidade";

                            tvChooseEmotion.setText(emotionName);

                        }else {

                            tvChooseEmotion.setText(emotionName);
                        }
                        dialog.dismiss();

                    }
                });

            }
        });

        tvRpdSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createRpdValidation();
            }
        });
    }

    private void createRpdValidation() {

        rpd_list_thought.clear();
        rpd_list_evidence.clear();
        rpd_list_count_evi.clear();
        rpd_list_conclusion.clear();
        hot_list_yes.clear();


        Situation = edSituation.getText().toString().trim();
        AutoThoghtOne = edAutoThoghtOne.getText().toString().trim();
        AutoThoghtTwo = edAutoThoghtTwo.getText().toString().trim();
        CounEvidenceOne = edCounEvidenceOne.getText().toString().trim();
        CounEvidenceTwo = edCounEvidenceTwo.getText().toString().trim();
        ConclusionOne = edConclusionOne.getText().toString().trim();
        ConclusionTwo = edConclusionTwo.getText().toString().trim();
        EvidenceOne = edEvidenceOne.getText().toString().trim();
        EvidenceTwo = edEvidenceTwo.getText().toString().trim();
        emo = tvChooseEmotion.getText().toString().trim();

        hott = "No";

        RpdData rd = new RpdData();

        rd.setRpd_name(AutoThoghtOne);
        rd.setRpd_name2(AutoThoghtTwo);

        rpd_list_thought.add(rd);

        RpdData rd1 = new RpdData();
        rd1.setRpd_name(CounEvidenceOne);
        rd1.setRpd_name2(CounEvidenceTwo);
        rd1.setHot_no(hott);

        rpd_list_count_evi.add(rd1);

        RpdData rd2 = new RpdData();
        rd2.setRpd_name(ConclusionOne);
        rd2.setRpd_name2(ConclusionTwo);
        rd2.setHot_no(hott);

        rpd_list_conclusion.add(rd2);

        RpdData rd3 = new RpdData();
        rd3.setRpd_name(EvidenceOne);
        rd3.setRpd_name2(EvidenceTwo);
        rd3.setHot_no(hott);

        rpd_list_evidence.add(rd3);

        Log.i("RPD list",rpd_list_thought.size()+ "");
        Log.i("RPD list",rpd_list_count_evi.size()+ "");
        Log.i("RPD list",rpd_list_conclusion.size()+ "");
        Log.i("RPD list",rpd_list_evidence.size()+ "");

        int selectedRadioButtonID = rgHot.getCheckedRadioButtonId();

        if(selectedRadioButtonID == hotone.getId())
        {

            radio_one = "Yes";

            RpdData hotYes = new RpdData();

            hotYes.setHot_yes(radio_one);
            hotYes.setRpd_name(AutoThoghtOne);

            hot_list_yes.add(hotYes);

        }
        else
        {
            radio_one ="No";

            RpdData hotYes = new RpdData();

            hotYes.setHot_yes(radio_one);
            hotYes.setRpd_name(AutoThoghtTwo);

            hot_list_yes.add(hotYes);
        }



        if (selectedRadioButtonID == hottwo.getId()){

            radio_one = "Yes";

            RpdData hotYes = new RpdData();

            hotYes.setHot_yes(radio_one);
            hotYes.setRpd_name(AutoThoghtOne);

            hot_list_yes.add(hotYes);

        }else {

            radio_one ="No";

            RpdData hotYes = new RpdData();

            hotYes.setHot_yes(radio_one);
            hotYes.setRpd_name(AutoThoghtTwo);

            hot_list_yes.add(hotYes);
        }



        int kt = 0;
        for (int i = 0; i < hot_list_yes.size(); i++) {

            RpdData sdata = hot_list_yes.get(i);

            Log.i("hot_one[0][" + kt++ + "]",sdata.getHot_yes());

            int k = kt - 1;
            Log.i("rpd_text[0][" + k++ + "]",sdata.getRpd_name());


        }


        /*int k = 0;

        for (int i = 0; i < rpd_list_thought.size(); i++) {
            RpdData sdata = rpd_list_thought.get(i);

            Log.i("rpd_text[0][" + k++ + "]",sdata.getRpd_name());
            Log.i("rpd_text[0][" + k++ + "]",sdata.getRpd_name2());
        }

        int km = 0;
        for (int i = 0; i < rpd_list_evidence.size(); i++) {
            RpdData sdata = rpd_list_evidence.get(i);

            Log.i("rpd_text[1][" + km++ + "]",sdata.getRpd_name());
            Log.i("rpd_text[1][" + km++ + "]",sdata.getRpd_name2());

            int j = 1;
            Log.i("hot_one[1][" + j + "]",sdata.getHot_no());
        }

        int kn = 0;

        for (int i = 0; i < rpd_list_count_evi.size(); i++) {
            RpdData sdata = rpd_list_count_evi.get(i);

            Log.i("rpd_text[2][" + kn++ + "]",sdata.getRpd_name());
            Log.i("rpd_text[2][" + kn++ + "]",sdata.getRpd_name2());
            int l = 1;
            Log.i("hot_one[2][" + l + "]",sdata.getHot_no());
        }

        int kl = 0;
        for (int i = 0; i < rpd_list_conclusion.size(); i++) {
            RpdData sdata = rpd_list_conclusion.get(i);

            Log.i("rpd_text[3][" + kl++ + "]",sdata.getRpd_name());
            Log.i("rpd_text[3][" + kl++ + "]",sdata.getRpd_name2());
            int p = 1;
            Log.i("hot_one[3][" + p + "]",sdata.getHot_no());
        }

        int kt = 0;
        for (int i = 0; i < hot_list_yes.size(); i++) {

            RpdData sdata = hot_list_yes.get(i);

            Log.i("hot_one[0][" + kt++ + "]",sdata.getHot_yes());

            Log.i("hot_one[0][" + kt++ + "]",sdata.getHot_no());
        }*/

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.no_internet));
        }else if (Situation.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.situation));
        }else if (AutoThoghtOne.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.rpd_thoght));
        }else if (CounEvidenceOne.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.rpd_c_evidence));
        }else if (ConclusionOne.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.rpd_conclusion));
        }else if (EvidenceOne.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.rpd_evidence));
        }else if (emotionName.equals("")) {
            AndyUtils.showToast(ToDoRpdClickActivity.this,getString(R.string.rpd_emotion));
        }
        else{

            makeJsonCallForCreateRpd(Situation,emo);
        }

    }

    private void makeJsonCallForCreateRpd(final String situation,final String emo) {
        pDialog = new ProgressDialog(ToDoRpdClickActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CREATE_RPD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.e("response:createrpd", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {

                                cc.showToast(jsonObject.getString("message"));

                                clearText();

                            } else {

                                cc.showToast(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error",e.toString());

                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ToDoRpdClickActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("situation", situation);
                params.put("emotion", emo);


               /* int k = 0;
                for (int i = 0; i < rpd_list_thought.size(); i++) {
                    RpdData sdata = rpd_list_thought.get(i);

                    params.put("rpd_text[0][" + k++ + "]",sdata.getRpd_name());
                    params.put("rpd_text[0][" + k++ + "]",sdata.getRpd_name2());
                }*/

                int km = 0;
                for (int i = 0; i < rpd_list_evidence.size(); i++) {
                    RpdData sdata = rpd_list_evidence.get(i);

                    params.put("rpd_text[1][" + km++ + "]",sdata.getRpd_name());
                    params.put("rpd_text[1][" + km++ + "]",sdata.getRpd_name2());

                    int j = 1;
                    params.put("hot_one[1][" + j + "]",sdata.getHot_no());
                }

                int kn = 0;
                for (int i = 0; i < rpd_list_count_evi.size(); i++) {
                    RpdData sdata = rpd_list_count_evi.get(i);

                    params.put("rpd_text[2][" + kn++ + "]",sdata.getRpd_name());
                    params.put("rpd_text[2][" + kn++ + "]",sdata.getRpd_name2());
                    int l = 1;
                    params.put("hot_one[2][" + l + "]",sdata.getHot_no());
                }

                int kl = 0;
                for (int i = 0; i < rpd_list_conclusion.size(); i++) {
                    RpdData sdata = rpd_list_conclusion.get(i);

                    params.put("rpd_text[3][" + kl++ + "]",sdata.getRpd_name());
                    params.put("rpd_text[3][" + kl++ + "]",sdata.getRpd_name2());
                    int p = 1;
                    params.put("hot_one[3][" + p + "]",sdata.getHot_no());
                }

                /*int kt = 0;
                for (int i = 0; i < hot_list_yes.size(); i++) {

                    RpdData sdata = hot_list_yes.get(i);

                    params.put("hot_one[0][" + kt++ + "]",sdata.getHot_yes());

                }*/

                int kt = 0;
                for (int i = 0; i < hot_list_yes.size(); i++) {

                    RpdData sdata = hot_list_yes.get(i);

                    params.put("hot_one[0][" + kt++ + "]",sdata.getHot_yes());

                    int kg = kt - 1;
                    params.put("rpd_text[0][" + kg++ + "]",sdata.getRpd_name());

                }



                Log.e("@@request createrpd", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.e("@@@request header", headers.toString());

                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    public void clearText(){

        edSituation.setText("");
        edAutoThoghtOne.setText("");
        edAutoThoghtTwo.setText("");
        edCounEvidenceOne.setText("");
        edCounEvidenceTwo.setText("");
        edConclusionOne.setText("");
        edConclusionTwo.setText("");
        edEvidenceOne.setText("");
        edEvidenceTwo.setText("");
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
