package com.mxicoders.skepci.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.fragment.FragmentDrawerPatientList;
import com.mxicoders.skepci.fragment.FragmentPatientlistHome;
import com.mxicoders.skepci.network.CommanClass;

import static com.mxicoders.skepci.fragment.FragmentDrawerPatientList.mDrawerLayout;

/**
 * Created by mxicoders on 17/7/17.
 */

public class ActivityMenuPatientList extends AppCompatActivity {



//    Fragment newFragment;
//    android.app.FragmentTransaction tra;

    CommanClass cc;

    private static String TAG = ActivityMenuPsychologist.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawerPatientList drawerFragment;

    TextView lnProfile,lnTaskHistory,lnMood,lnEmotion,lnQuestionnaries,
            lnSleep,lnCopingCard,lnNotes,lnRpd,lnConceptu,lnReport;

    ImageView backDrawer;

    String title = "";

    TextView pname;

    String p_name,l_name;

    String p_name2,p_last_name,pid;

    LinearLayout chat_icon;

    String url_compare;
    String url_compare2;
    String url_compare3;
    String url_compare4;

    TextView tvToolTitle;
    LinearLayout lnNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_menu_patientlist_click);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        tvToolTitle = (TextView)findViewById(R.id.tv_tool_name);
        lnNewTask = (LinearLayout)findViewById(R.id.ln_new_task);

        cc = new CommanClass(ActivityMenuPatientList.this);

        p_name = cc.loadPrefString("p_namee");
        pid = cc.loadPrefString("patient_id_main");

        Log.i("patient Id",pid);


        url_compare = "http://mbdbtechnology.com/projects/skepsi/ws/mood_view/mood/" + pid;
        url_compare2 = "http://mbdbtechnology.com/projects/skepsi/ws/emotion_view/emotion_view/" + pid;
        url_compare3 = "http://mbdbtechnology.com/projects/skepsi/ws/sleep_data/sleep_data/" + pid;
        url_compare4 = "http://mbdbtechnology.com/projects/skepsi/ws/mood_view/mood/" + pid;

        drawerFragment = (FragmentDrawerPatientList)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_patientlist_menu);

        chat_icon=(LinearLayout)drawerFragment.getActivity().findViewById(R.id.chat_icon);

        pname = (TextView)drawerFragment.getActivity().findViewById(R.id.tv_patient_name);

        tvToolTitle.setText(p_name);
        pname.setText(p_name);

        backDrawer = (ImageView)drawerFragment.getActivity().findViewById(R.id.back_drawer);

        lnProfile = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_patient_profile);
        lnTaskHistory = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_task_history);
        lnMood = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_mood);
        lnEmotion = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_emotion);
        lnQuestionnaries = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_questionnaries_plist);
        lnSleep = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_sleep);
        lnCopingCard = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_coppingcard);
        lnNotes = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_notes);
        lnRpd = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_rpd);
        lnConceptu = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_conceptialization);
        lnReport = (TextView)drawerFragment.getActivity().findViewById(R.id.ln_report);


        lnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sleep = new Intent(ActivityMenuPatientList.this, SleepPatientlistActivity.class);
                startActivity(sleep);

            }
        });

        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chat = new Intent(ActivityMenuPatientList.this, ActivityChat.class);
                chat.putExtra("target_user_id",cc.loadPrefString("patient_id_main"));
                //chat.putExtra("fromPatient",true);
                startActivity(chat);

            }
        });

        backDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editProfile = new Intent(ActivityMenuPatientList.this, EditProfilePatientPsychologistSide.class);
                startActivity(editProfile);
                mDrawerLayout.closeDrawers();

            }
        });

        lnTaskHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent thistory = new Intent(ActivityMenuPatientList.this, TaskHistoryPatientList.class);
                startActivity(thistory);
                mDrawerLayout.closeDrawers();
            }
        });

        lnMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMenuPatientList.this, WebViewMood.class);
                intent.putExtra("Url",url_compare);
                intent.putExtra("pid",pid);
                startActivity(intent);

            }
        });

        lnEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMenuPatientList.this, WebviewMainActivity.class);
                intent.putExtra("Url",url_compare2);
                startActivity(intent);

            }
        });

        lnQuestionnaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              /*  Intent narticle = new Intent(ActivityMenuPatientList.this, QuestionariesActivity.class);
                startActivity(narticle);
                mDrawerLayout.closeDrawers();*/

                Intent narticle = new Intent(ActivityMenuPatientList.this, QuestionnariesPatientListActivity.class);
                startActivity(narticle);
                mDrawerLayout.closeDrawers();

            }
        });

        lnSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActivityMenuPatientList.this, WebviewSleep.class);
                intent.putExtra("Url",url_compare3);
                intent.putExtra("pid",pid);
                startActivity(intent);

            }
        });

        lnCopingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sleep = new Intent(ActivityMenuPatientList.this, CopindCardPatientList.class);
                startActivity(sleep);
                mDrawerLayout.closeDrawers();
            }
        });

        lnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sleep = new Intent(ActivityMenuPatientList.this, NotesPsychologistActivity.class);
                startActivity(sleep);
                mDrawerLayout.closeDrawers();

            }
        });

        lnRpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent thistory = new Intent(ActivityMenuPatientList.this, RpdPatientListMenu.class);
                startActivity(thistory);
                mDrawerLayout.closeDrawers();
            }
        });

        lnConceptu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent conceIn = new Intent(ActivityMenuPatientList.this, ConceptualizationViewActivity.class);
                startActivity(conceIn);
                mDrawerLayout.closeDrawers();
            }
        });

        lnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent report = new Intent(ActivityMenuPatientList.this, ReportPatientList.class);
                report.putExtra("Url",url_compare4);
                report.putExtra("pid",pid);
                report.putExtra("pname",p_name);
                startActivity(report);
                mDrawerLayout.closeDrawers();

            }
        });

        drawerFragment.setUp(R.id.fragment_nav_drawer_patientlist_menu, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);


        p_name2 = cc.loadPrefString("p_namee2");
        p_last_name = cc.loadPrefString("p_namee_last");

        android.app.FragmentTransaction   tra = getFragmentManager().beginTransaction();
        Fragment newFragment = new FragmentPatientlistHome();
        tra.replace(R.id.container_body, newFragment);
        title = p_name2 + " " + p_last_name;
        getSupportActionBar().setTitle(title);
        tra.addToBackStack(null);
        tra.commit();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

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

    @Override
    public void onBackPressed() {

        Intent conceIn = new Intent(ActivityMenuPatientList.this, ActivityPatientList.class);
        startActivity(conceIn);
        finish();
        super.onBackPressed();
    }
}
