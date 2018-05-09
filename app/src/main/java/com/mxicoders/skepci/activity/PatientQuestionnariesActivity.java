package com.mxicoders.skepci.activity;

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

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.PatientQuestionariesAdapter;
import com.mxicoders.skepci.model.PatientQuestionnariesData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by mxicoders on 17/7/17.
 */

public class PatientQuestionnariesActivity extends AppCompatActivity {

    CommanClass cc;

    RecyclerView mRecyclerView;
    PatientQuestionariesAdapter mAdapter;
    ArrayList<PatientQuestionnariesData> list;

    public PatientQuestionnariesActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_patient_questionnaries);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.patient_ques_re_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PatientQuestionnariesActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        list = new ArrayList<PatientQuestionnariesData>();
        list.add(new PatientQuestionnariesData("Mood","148",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Schema","120",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Sport physchologist","130",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Mood","160",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Mood","170",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Schema","180",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Schema","190",R.drawable.bg));
        list.add(new PatientQuestionnariesData("Sport","20",R.drawable.bg));

        mAdapter = new PatientQuestionariesAdapter(list,R.layout.patient_questionnaries_adapter_item,PatientQuestionnariesActivity.this);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_questionarries, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        String title = "";

        if(id == R.id.action_result) {

            Intent narticle = new Intent(PatientQuestionnariesActivity.this, NewQuestiResultActivity.class);
            startActivity(narticle);

            return true;

        }
        return super.onOptionsItemSelected(item);
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

