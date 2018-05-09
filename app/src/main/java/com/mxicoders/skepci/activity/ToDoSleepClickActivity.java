package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopeer.android.librarys.scrolltable.CustomTableView;
import com.loopeer.android.librarys.scrolltable.Position;
import com.loopeer.android.librarys.scrolltable.ScrollTableView;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.loopeer.android.librarys.scrolltable.ScrollTableView.contentView;
import static com.loopeer.android.librarys.scrolltable.ScrollTableView.selectPositions;

/**
 * Created by mxicoders on 25/7/17.
 */

public class ToDoSleepClickActivity extends AppCompatActivity implements  CustomTableView.OnPositionClickListener  {

    CommanClass cc;

    private static String[] topTitles;
    private static final String[] leftTitles = new String[] {"1", "2","3","4","5","6","7","8","9","10","11","12","13","14","15","16",
                                                             "17","18","19","20","21","22","23","24"};

    private ScrollTableView scrollTableView;

    TextView tvSave;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String currentDate,nextDate;
    String convertCurrentDay,convertCurrentMonth,convertNextDay,convertNextMonth;
    String currentModDate,nextModDate;
    Calendar c;

    public ToDoSleepClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.frag_patient_todo_sleep_click);
        super.onCreate(savedInstanceState);


        cc = new CommanClass(this);

        c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        c.add(Calendar.DATE, 1);
        nextDate = df.format(c.getTime());
        setupNextDate(nextDate);

        currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        setupCurrentDate(currentDate);

        Log.i("CurrentDate",currentDate.toString());
        Log.i("CurrentModifiedDate",currentModDate.toString());
        Log.i("NextDate",nextDate.toString());
        Log.i("NextModifiedDate",nextModDate.toString());

        topTitles = new String[] {currentModDate, nextModDate};

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        tooName.setText(getString(R.string.sleep));
        tooName.setPadding(75, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        tvSave = (TextView)findViewById(R.id.tv_save_submit);

        scrollTableView = (ScrollTableView)findViewById(R.id.scroll_table_view);
        ArrayList<String> leftTitle = createLeftTitle();
        ArrayList<String> topTitles = createTopTitles();
        scrollTableView.setDatas(createTopTitles(), createLeftTitle(), createContent(leftTitle.size(), topTitles.size()));

        scrollTableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ToDoSleepClickActivity.this);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.task_completed_dialog);
                dialog.show();

                final Handler handler  = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                };

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                handler.postDelayed(runnable,2000);
            }
        });

    }

    private ArrayList<ArrayList<String>> createContent(int row, int column) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        return results;
    }

    private ArrayList<String> createTopTitles() {
        ArrayList<String> results = new ArrayList<>();

        for (String string : topTitles) {
            results.add(string);
        }
        return results;
    }

    private ArrayList<String> createLeftTitle() {
        ArrayList<String> results = new ArrayList<>();
       /* for (int i = 0; i < 23; i++) {
            results.add(i + "h");
        }*/
        for (String string : leftTitles) {
            results.add(string + "h");
        }
        return results;
    }

    private void setupCurrentDate(String s) {
        String[] dateTempParts;
        dateTempParts= s.split("-");

        convertCurrentDay = dateTempParts[0];
        convertCurrentMonth = dateTempParts[1];

        currentModDate = convertCurrentDay + "/" + convertCurrentMonth;

    }

    private void setupNextDate(String s) {
        String[] dateTempParts;
        dateTempParts= s.split("-");

        convertNextDay = dateTempParts[0];
        convertNextMonth = dateTempParts[1];

        nextModDate = convertNextDay + "/" + convertNextMonth;

    }

    @Override
    public void onPositionClick(Position position) {

        if (selectPositions.contains(position)) {
            selectPositions.remove(position);
        } else {
            selectPositions.add(position);
        }
        contentView.setSelectPositions(selectPositions);
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

