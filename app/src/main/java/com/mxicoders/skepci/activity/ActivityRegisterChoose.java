package com.mxicoders.skepci.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;

/**
 * Created by mxicoders on 11/7/17.
 */

public class ActivityRegisterChoose extends AppCompatActivity {

    TextView tvSignupPhy,tvSignupPatient,tvNext;
    TextView tvChoosePatientHead,tvChoosePsychoHead;

    LinearLayout lnPatient,lnPsychologist;

    String patient,psychologist;

    boolean selected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_choose);

        tvSignupPhy = (TextView)findViewById(R.id.tv_signup_phsychologist);
        tvSignupPatient = (TextView)findViewById(R.id.tv_signup_patient);
        tvNext = (TextView)findViewById(R.id.tv_next_choose);


        tvChoosePatientHead = (TextView)findViewById(R.id.tv_r_choose_patient);
        tvChoosePsychoHead = (TextView)findViewById(R.id.tv_r_choose_psychologist);

        lnPatient = (LinearLayout)findViewById(R.id.ln_signup_patient);
        lnPsychologist = (LinearLayout)findViewById(R.id.ln_signup_pschologist);

       /* tvSignupPhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sp = new Intent(ActivityRegisterChoose.this,ActivitySignUpPsychologist.class);
                startActivity(sp);

            }
        });

        tvSignupPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sp = new Intent(ActivityRegisterChoose.this,ActivitySignupPatient.class);
                startActivity(sp);

            }
        });*/

        lnPatient.setBackground(getResources().getDrawable(R.drawable.login_facebook_button_back));
        lnPsychologist.setBackground(getResources().getDrawable(R.drawable.signup_back));

        lnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnPatient.setBackground(getResources().getDrawable(R.drawable.login_facebook_button_back));
                lnPsychologist.setBackground(getResources().getDrawable(R.drawable.signup_back));

                selected = true;

                patient = tvSignupPatient.getText().toString().trim();

            }
        });

        lnPsychologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnPsychologist.setBackground(getResources().getDrawable(R.drawable.login_facebook_button_back));
                lnPatient.setBackground(getResources().getDrawable(R.drawable.signup_back));

                selected = false;

                psychologist = tvSignupPhy.getText().toString().trim();
            }
        });


        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selected == true){

                    Intent sp = new Intent(ActivityRegisterChoose.this,ActivitySignupPatient.class);
                    startActivity(sp);

                }else {

                    Intent sp = new Intent(ActivityRegisterChoose.this,ActivitySignUpPsychologist.class);
                    startActivity(sp);
                }
            }
        });


    }
}
