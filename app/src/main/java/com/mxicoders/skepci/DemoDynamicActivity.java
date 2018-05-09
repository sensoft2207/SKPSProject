package com.mxicoders.skepci;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import java.util.List;
import java.util.Map;


public class DemoDynamicActivity extends Activity  {

    CommanClass cc;
    ProgressDialog pDialog;

    LinearLayout lnRpdOne,lnRpdOneRadio,lnRpdTwo,lnRpdThree,lnRpdFour,lnRpdRadio;
    ImageView ivRpdOne,ivRpdTwo,ivRpdThree,ivRpdFour;

    TextView tvSubmit;

    int numberOfid,numberOfid2,numberOfid3,numberOfid4,numberOfid5;

    List<EditText> evidenceList = new ArrayList<EditText>();
    List<EditText> counterEvidenceList = new ArrayList<EditText>();
    List<EditText> conclusionList = new ArrayList<EditText>();
    List<EditText> automaticList = new ArrayList<EditText>();
    List<RadioButton> radioList = new ArrayList<RadioButton>();

    ArrayList<RpdData> hot_list_yes;
    ArrayList<RpdData> hot_list_no;
    ArrayList<RpdData> hot_list_no1;
    ArrayList<RpdData> hot_list_no2;

    String [] evidence;
    String [] counter_evidence;
    String [] conclusion;
    String [] automaticthoughts;

    RadioGroup rgg;
    RadioButton radioButton;
    String radio_one;

    EditText editViewTwo;
    TextView tvChooseEmotion;

    GridView emotionGridDialog;
    ImageView closeEmotionDialog;
    TextView tvSubmitEmotionDialog;
    EmotionGridAdapterDialog gridAdapter;

    EditText edSituation;

    String Situation;
    String hott,hottt;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    LinearLayout lnEmotionChoose;

    Dialog dialog;

    String emotionName;
    String emo;

    ArrayList<Integer> items;

    static final String[] MOBILE_OS = new String[] {
            "Tranqullidade", "Tedlo","Saudades", "Vergonha", "Orgulho", "Tristeza", "Amor", "Solidao","Esperanca",
            "Decepcao", "Alegria", "Confusao", "Entusiansmo", "Nojo","Ansiedade",
            "Preacupacao", "Raiva", "Desconfianca", "Medo", "Cupla"};

    RpdData sdata,sdata2,sdata3;


    public DemoDynamicActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_dynamic);

        cc = new CommanClass(this);

        hottt = "No";

        hot_list_yes = new ArrayList<>();
        hot_list_no = new ArrayList<>();
        hot_list_no1 = new ArrayList<>();
        hot_list_no2 = new ArrayList<>();

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


        lnRpdOneRadio = (LinearLayout) findViewById(R.id.ln_rpd_one_radio);
        lnRpdOne = (LinearLayout) findViewById(R.id.ln_rpd_one);
        lnRpdTwo = (LinearLayout) findViewById(R.id.ln_rpd_two);
        lnRpdThree = (LinearLayout) findViewById(R.id.ln_rpd_three);
        lnRpdFour = (LinearLayout) findViewById(R.id.ln_rpd_four);


        tvSubmit = (TextView) findViewById(R.id.tv_rpd_pa_submit);


        ivRpdOne = (ImageView)findViewById(R.id.iv_rpd_one);
        ivRpdTwo = (ImageView)findViewById(R.id.iv_rpd_two);
        ivRpdThree = (ImageView)findViewById(R.id.iv_rpd_three);
        ivRpdFour = (ImageView)findViewById(R.id.iv_rpd_four);

        rgg = (RadioGroup)findViewById(R.id.optionRadioGroup);
        editViewTwo = new EditText(DemoDynamicActivity.this);

        lnEmotionChoose = (LinearLayout)findViewById(R.id.ln_emotion_choose);
        tvChooseEmotion = (TextView)findViewById(R.id.tv_choose_emotion);
        edSituation = (EditText)findViewById(R.id.ed_rpd_situation);

        sdata = new RpdData();

        radioBtn();
        edittextDynamic();
        edittextDynamic1();
        edittextDynamic2();
        edittextDynamic3();




        ivRpdOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 1; i++) {
                    LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
                    l.setOrientation(LinearLayout.HORIZONTAL);

                    for (int j = 0; j < 1 ; j++) {
                        EditText editView = new EditText(DemoDynamicActivity.this);

                        editViewTwo = editView;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                        editView.setId(numberOfid4 + 1);
                        editView.setHint(getString(R.string.a_thougts));
                        editView.setPadding(5,2,0,2);
                        editView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.brain, 0, 0, 0);
                        editView.setCompoundDrawablePadding(11);
                        editView.setHintTextColor(Color.parseColor("#000000"));
                        editView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        editView.setSingleLine(true);
                        editView.setTypeface(null, Typeface.ITALIC);
                        lp.setMargins(0, 7, 0, 0);
                        editView.setLayoutParams(lp);
                        numberOfid4++;
                        automaticList.add(editView);
                        l.addView(editView, lp);
                    }
                    lnRpdOne.addView(l);


                    Log.i("PlusCkikedOneOne", String.valueOf(numberOfid4));

                    Log.i("ListSizeOneOne", String.valueOf(automaticList.size()));

                }


                for (int p = 0; p < 1 ; p++) {

                    radioButton = new RadioButton(DemoDynamicActivity.this);
                    radioList.add(radioButton);
                    rgg.addView(radioButton);
                }
            }
        });


        ivRpdTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 1; i++) {
                    LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
                    l.setOrientation(LinearLayout.HORIZONTAL);

                    for (int j = 0; j < 1 ; j++) {
                        EditText editView = new EditText(DemoDynamicActivity.this);

                        editViewTwo = editView;

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                        editView.setId(numberOfid + 1);
                        editView.setHint(getString(R.string.evidence));
                        editView.setPadding(5,2,0,2);
                        editView.setCompoundDrawablesWithIntrinsicBounds( R.drawable.serch, 0, 0, 0);
                        editView.setCompoundDrawablePadding(11);
                        editView.setHintTextColor(Color.parseColor("#000000"));
                        editView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        editView.setSingleLine(true);
                        editView.setTypeface(null, Typeface.ITALIC);
                        lp.setMargins(0, 7, 0, 0);
                        editView.setLayoutParams(lp);
                        numberOfid++;



                        evidenceList.add(editView);
                        l.addView(editView, lp);
                    }
                    lnRpdTwo.addView(l);
                    sdata.setHot_no(hottt);
                    hot_list_no.add(sdata);


                    Log.i("PlusCkikedOne", String.valueOf(numberOfid));

                    Log.i("ListSizeOne", String.valueOf(evidenceList.size()));

                }
            }
        });

        ivRpdThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 1; i++) {
                    LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
                    l.setOrientation(LinearLayout.HORIZONTAL);

                    for (int j = 0; j < 1; j++) {
                        EditText editView = new EditText(DemoDynamicActivity.this);
                        editViewTwo = editView;
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                        editView.setId(numberOfid2 + 1);
                        editView.setHint(getString(R.string.c_evidence));
                        editView.setPadding(5, 2, 0, 2);
                        editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);
                        editView.setCompoundDrawablePadding(11);
                        editView.setHintTextColor(Color.parseColor("#000000"));
                        editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        editView.setSingleLine(true);
                        editView.setTypeface(null, Typeface.ITALIC);
                        lp.setMargins(0, 7, 0, 0);
                        editView.setLayoutParams(lp);
                        numberOfid2++;

                        counterEvidenceList.add(editView);
                        l.addView(editView, lp);
                    }
                    lnRpdThree.addView(l);
                    sdata2 = new RpdData();
                    sdata2.setHot_no(hottt);
                    hot_list_no1.add(sdata2);


                    Log.i("PlusCkikedTwo", String.valueOf(numberOfid2));
                    Log.i("ListSizeTwo", String.valueOf(counterEvidenceList.size()));

                }

            }
        });

        ivRpdFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 1; i++) {
                    LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
                    l.setOrientation(LinearLayout.HORIZONTAL);

                    for (int j = 0; j < 1; j++) {
                        EditText editView = new EditText(DemoDynamicActivity.this);
                        editViewTwo = editView;
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                        editView.setId(numberOfid3 + 1);
                        editView.setHint(getString(R.string.conclusion));
                        editView.setPadding(5, 2, 0, 2);
                        editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_rpd, 0, 0, 0);
                        editView.setCompoundDrawablePadding(11);
                        editView.setHintTextColor(Color.parseColor("#000000"));
                        editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        editView.setSingleLine(true);
                        editView.setTypeface(null, Typeface.ITALIC);
                        lp.setMargins(0, 7, 0, 0);
                        editView.setLayoutParams(lp);
                        numberOfid3++;

                        conclusionList.add(editView);
                        l.addView(editView, lp);
                    }

                    lnRpdFour.addView(l);
                    sdata3 = new RpdData();
                    sdata3.setHot_no(hottt);
                    hot_list_no2.add(sdata3);


                    Log.i("PlusCkikedThree", String.valueOf(numberOfid3));
                    Log.i("ListSizeThree", String.valueOf(conclusionList.size()));

                }
            }
        });


        lnEmotionChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(DemoDynamicActivity.this);
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

                gridAdapter = new EmotionGridAdapterDialog(DemoDynamicActivity.this, MOBILE_OS);
                emotionGridDialog.setAdapter(gridAdapter);


                /*emotionGridDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        emotionName = String.valueOf(gridAdapter.getItem(position));

                        emotionName = MOBILE_OS[position];


                        Log.i("EmotionNameGrid",emotionName.toString());
                    }
                });*/
                closeEmotionDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                tvSubmitEmotionDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*if (tvChooseEmotion == null){

                            emotionName = "Tranqullidade";

                            tvChooseEmotion.setText(emotionName);

                        }else {

                            tvChooseEmotion.setText(emotionName);
                        }*/

                        if (emotionName == null){

                            emotionName = "Tranqullidade";

                            tvChooseEmotion.setText(emotionName);

                            Log.i("EmotionNameGridIf",emotionName.toString());

                        }else {

                            emotionName = cc.loadPrefString("emotion_name");

                            tvChooseEmotion.setText(emotionName);

                            Log.i("EmotionNameGridElse",emotionName.toString());
                        }


                        dialog.dismiss();

                    }
                });

            }
        });


        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createRpdValidation();

            }
        });

    }

    private void createRpdValidation() {

        hot_list_yes.clear();

        Situation = edSituation.getText().toString().trim();
        evidence = new String[evidenceList.size()];
        counter_evidence = new String[counterEvidenceList.size()];
        conclusion = new String[conclusionList.size()];
        automaticthoughts = new String[automaticList.size()];
        emo = tvChooseEmotion.getText().toString().trim();


        hott = "No";


        for(int i=0; i < evidenceList.size(); i++){
            evidence[i] = evidenceList.get(i).getText().toString();
            RpdData rd2 = new RpdData();
            rd2.setRpd_name(evidence[i]);
            rd2.setHot_no(hott);
            System.out.println(evidence[i]);
        }

        for(int j = 0; j < counterEvidenceList.size(); j++){
            counter_evidence[j] = counterEvidenceList.get(j).getText().toString();
            RpdData rd1 = new RpdData();
            rd1.setRpd_name(counter_evidence[j]);
            rd1.setHot_no(hott);
            System.out.println(counter_evidence[j]);
        }

        for(int k = 0; k < conclusionList.size(); k++){
            conclusion[k] = conclusionList.get(k).getText().toString();
            RpdData rd3 = new RpdData();
            rd3.setRpd_name(conclusion[k]);
            rd3.setHot_no(hott);
            System.out.println(conclusion[k]);
        }

        int selectedRadioButtonID = rgg.getCheckedRadioButtonId();


        for(int i=0; i < radioList.size(); i++) {

            radioButton = radioList.get(i);

            if(radioButton.getId() == selectedRadioButtonID) {


                radio_one = "Yes";

                RpdData hotYes = new RpdData();

                hotYes.setHot_yes(radio_one);

                for(int pl=0; pl < automaticList.size(); pl++){
                    automaticthoughts[i] = automaticList.get(i).getText().toString();
                    hotYes.setRpd_name(automaticthoughts[i]);
                    System.out.println(automaticthoughts[i]);
                }

                hot_list_yes.add(hotYes);
            }
            else {

                radio_one = "No";

                RpdData hotYes = new RpdData();

                hotYes.setHot_yes(radio_one);

                for(int pl=0; pl < automaticList.size(); pl++){

                    automaticthoughts[i] = automaticList.get(i).getText().toString();

                    hotYes.setRpd_name(automaticthoughts[i]);

                    System.out.println(automaticthoughts[i]);
                }

                hot_list_yes.add(hotYes);
            }
        }

        int kt = 0;
        for (int i = 0; i < hot_list_yes.size(); i++) {

            RpdData sdata = hot_list_yes.get(i);

            Log.i("hot_one[0][" + kt++ + "]",sdata.getHot_yes());
            int k = kt - 1;
            Log.i("rpd_text[0][" + k++ + "]",sdata.getRpd_name());

        }

      /*  int km = 0;
        for(int i=0; i < evidenceList.size(); i++){
            evidence[i] = evidenceList.get(i).getText().toString();

            Log.i("rpd_text[1][" + km++ + "]",evidence[i]);

            RpdData sdata = hot_list_no.get(i);

            Log.i("hot_one[1][" + i + "]",sdata.getHot_no());
        }*/

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(DemoDynamicActivity.this,getString(R.string.no_internet));
        }else if (Situation.equals("")) {
            AndyUtils.showToast(DemoDynamicActivity.this,getString(R.string.situation));
        }
        else{

            makeJsonCallForCreateRpd(Situation,emo);
        }


    }

    private void edittextDynamic() {

        for (int i = 0; i < 2; i++) {
            LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 1; j++) {
                EditText editView = new EditText(DemoDynamicActivity.this);
                editViewTwo = editView;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                editView.setId(numberOfid4 + 1);
                editView.setHint(getString(R.string.a_thougts));
                editView.setPadding(5, 2, 0, 2);
                editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.brain, 0, 0, 0);
                editView.setCompoundDrawablePadding(11);
                editView.setHintTextColor(Color.parseColor("#000000"));
                editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                editView.setSingleLine(true);
                editView.setTypeface(null, Typeface.ITALIC);
                lp.setMargins(0, 7, 0, 0);
                editView.setLayoutParams(lp);
                numberOfid4++;



                automaticList.add(editView);
                l.addView(editView, lp);
            }
            lnRpdOne.addView(l);

            Log.i("PlusCkikedOneOne", String.valueOf(numberOfid4));
            Log.i("ListSizeOneOne", String.valueOf(automaticList.size()));
        }
    }

    private void edittextDynamic1() {

        for (int i = 0; i < 2; i++) {
            LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 1; j++) {
                EditText editView = new EditText(DemoDynamicActivity.this);
                editViewTwo = editView;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                editView.setId(numberOfid + 1);
                editView.setHint(getString(R.string.evidence));
                editView.setPadding(5, 2, 0, 2);
                editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.serch, 0, 0, 0);
                editView.setCompoundDrawablePadding(11);
                editView.setHintTextColor(Color.parseColor("#000000"));
                editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                editView.setSingleLine(true);
                editView.setTypeface(null, Typeface.ITALIC);
                lp.setMargins(0, 7, 0, 0);
                editView.setLayoutParams(lp);
                numberOfid++;


                evidenceList.add(editView);
                l.addView(editView, lp);
            }
            lnRpdTwo.addView(l);
            sdata.setHot_no(hottt);
            hot_list_no.add(sdata);

            Log.i("PlusCkikedOne", String.valueOf(numberOfid));
            Log.i("ListSizeOne", String.valueOf(evidenceList.size()));
        }
    }

    private void edittextDynamic2() {

        for (int i = 0; i < 2; i++) {
            LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 1; j++) {
                EditText editView = new EditText(DemoDynamicActivity.this);
                editViewTwo = editView;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                editView.setId(numberOfid2 + 1);
                editView.setHint(getString(R.string.c_evidence));
                editView.setPadding(5, 2, 0, 2);
                editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);
                editView.setCompoundDrawablePadding(11);
                editView.setHintTextColor(Color.parseColor("#000000"));
                editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                editView.setSingleLine(true);
                editView.setTypeface(null, Typeface.ITALIC);
                lp.setMargins(0, 7, 0, 0);
                editView.setLayoutParams(lp);
                numberOfid2++;



                counterEvidenceList.add(editView);
                l.addView(editView, lp);
            }
            lnRpdThree.addView(l);
            sdata2 = new RpdData();
            sdata2.setHot_no(hottt);
            hot_list_no1.add(sdata2);

            Log.i("PlusCkikedTwo", String.valueOf(numberOfid2));
            Log.i("ListSizeTwo", String.valueOf(counterEvidenceList.size()));
        }
    }

    private void edittextDynamic3() {

        for (int i = 0; i < 2; i++) {
            LinearLayout l = new LinearLayout(DemoDynamicActivity.this);
            l.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 1; j++) {
                EditText editView = new EditText(DemoDynamicActivity.this);
                editViewTwo = editView;
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                editView.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_back));

                editView.setId(numberOfid3 + 1);
                editView.setHint(getString(R.string.conclusion));
                editView.setPadding(5, 2, 0, 2);
                editView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_rpd, 0, 0, 0);
                editView.setCompoundDrawablePadding(11);
                editView.setHintTextColor(Color.parseColor("#000000"));
                editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                editView.setSingleLine(true);
                editView.setTypeface(null, Typeface.ITALIC);
                lp.setMargins(0, 7, 0, 0);
                editView.setLayoutParams(lp);
                numberOfid3++;



                conclusionList.add(editView);
                l.addView(editView, lp);
            }
            lnRpdFour.addView(l);
            sdata3 = new RpdData();
            sdata3.setHot_no(hottt);
            hot_list_no2.add(sdata3);

            Log.i("PlusCkikedThree", String.valueOf(numberOfid3));
            Log.i("ListSizeThree", String.valueOf(conclusionList.size()));
        }
    }

    private void radioBtn() {


        for (int p = 0; p < 2 ; p++) {

            radioButton = new RadioButton(DemoDynamicActivity.this);
            radioList.add(radioButton);
            rgg.addView(radioButton);
        }
    }


    private void makeJsonCallForCreateRpd(final String situation,final String emo) {
        pDialog = new ProgressDialog(DemoDynamicActivity.this);
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
                AndyUtils.showToast(DemoDynamicActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("situation", situation);
                params.put("emotion", emo);

                int kt = 0;
                for (int i = 0; i < hot_list_yes.size(); i++) {

                    RpdData sdata = hot_list_yes.get(i);

                    params.put("hot_one[0][" + kt++ + "]",sdata.getHot_yes());
                    int k = kt - 1;
                    params.put("rpd_text[0][" + k++ + "]",sdata.getRpd_name());

                }

                int km = 0;
                for(int ia=0; ia < evidenceList.size(); ia++){
                    evidence[ia] = evidenceList.get(ia).getText().toString();

                    params.put("rpd_text[1][" + km++ + "]",evidence[ia]);

                    RpdData sdata = hot_list_no.get(ia);

                    params.put("hot_one[1][" + ia + "]",sdata.getHot_no());
                }

                int kn = 0;
                for(int in=0; in < counterEvidenceList.size(); in++){
                    counter_evidence[in] = counterEvidenceList.get(in).getText().toString();

                    params.put("rpd_text[2][" + kn++ + "]",counter_evidence[in]);

                    RpdData sdata = hot_list_no1.get(in);

                    params.put("hot_one[2][" + in + "]",sdata.getHot_no());
                }

                int kp = 0;
                for(int inn=0; inn < conclusionList.size(); inn++){
                    conclusion[inn] = conclusionList.get(inn).getText().toString();

                    params.put("rpd_text[3][" + kp++ + "]",conclusion[inn]);

                    RpdData sdata = hot_list_no2.get(inn);

                    params.put("hot_one[3][" + inn + "]",sdata.getHot_no());
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

    }

}