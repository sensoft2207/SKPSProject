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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.QuestionariesAdapter;
import com.mxicoders.skepci.model.QuestionariesData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.ArrayList;

/**
 * Created by mxicoders on 17/7/17.
 */

public class QuestionnariesPatientActivity extends AppCompatActivity {

    CommanClass cc;

    RecyclerView mRecyclerView;
    QuestionariesAdapter mAdapter;
    ArrayList<QuestionariesData> list;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public QuestionnariesPatientActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_questionarues);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.ques_re_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionnariesPatientActivity.this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        list = new ArrayList<QuestionariesData>();
        list.add(new QuestionariesData("Mood","148",R.drawable.bg));
        list.add(new QuestionariesData("Schema","120",R.drawable.bg));
        list.add(new QuestionariesData("Sport physchologist","130",R.drawable.bg));
        list.add(new QuestionariesData("Mood","160",R.drawable.bg));
        list.add(new QuestionariesData("Mood","170",R.drawable.bg));
        list.add(new QuestionariesData("Schema","180",R.drawable.bg));
        list.add(new QuestionariesData("Schema","190",R.drawable.bg));
        list.add(new QuestionariesData("Sport","20",R.drawable.bg));

        mAdapter = new QuestionariesAdapter(list,R.layout.questionaries_adapter_item,QuestionnariesPatientActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        tooName.setText("Questionnaries Category");

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
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
        if(id == R.id.action_result) {

            Intent narticle = new Intent(QuestionnariesPatientActivity.this, NewQuestiResultActivity.class);
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
