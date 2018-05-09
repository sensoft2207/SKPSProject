package com.mxicoders.skepci.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

/**
 * Created by mxi on 5/10/17.
 */

public class HelpPsychologistMenu extends AppCompatActivity {

    CommanClass cc;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    LinearLayout lnHelpPsycho,lnHelpPatient;


    public HelpPsychologistMenu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.psychologist_menu_help);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.faq));
        tooName.setPadding(80, 0, 0, 0);

        lnHelpPsycho = (LinearLayout)findViewById(R.id.ln_help_psycho);
        lnHelpPatient = (LinearLayout)findViewById(R.id.ln_help_patient);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        lnHelpPsycho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent helpPsy = new Intent(HelpPsychologistMenu.this,HelpPatientMenuActivity.class);
                cc.savePrefString2("Psychologist","psychologist");
                startActivity(helpPsy);
                finish();
            }
        });

        lnHelpPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent helpPatient = new Intent(HelpPsychologistMenu.this,HelpPatientMenuActivity.class);
                cc.savePrefString2("Patient","patient");
                startActivity(helpPatient);
                finish();
            }
        });

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
