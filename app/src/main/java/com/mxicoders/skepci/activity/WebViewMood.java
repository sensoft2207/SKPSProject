package com.mxicoders.skepci.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vishal on 11/4/18.
 */

public class WebViewMood extends AppCompatActivity {


    WebView wv_description;
    CommanClass cc;
    String strUrl,strUrlDateChange;

    ProgressDialog dialog;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private String currentUrl;
    private boolean isPageLoadedComplete = false;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolBack;

    LinearLayout ln_date_picker_sleep;
    TextView tv_start_date2,tv_end_date2;

    private Calendar startDate;
    private Calendar endDate;

    static final int DATE_DIALOG_ID = 0;

    private Calendar activeDate;

    boolean dateSelection = false;

    String currentDate = "", nextDate = "";

    String formatedStartDate,formatedEndDate;
    String patientID;

    Dialog dialog2;

    String p_name,p_last_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_sleep);


        strUrl = getIntent().getStringExtra("Url");
        patientID = getIntent().getStringExtra("pid");

       /* startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();*/

        wv_description = (WebView) findViewById(R.id.wv_description);

        ln_date_picker_sleep = (LinearLayout) findViewById(R.id.ln_date_picker_sleep);
        tv_start_date2 = (TextView) findViewById(R.id.tv_start_date);
        tv_end_date2 = (TextView) findViewById(R.id.tv_end_date);

        wv_description.getSettings().setBuiltInZoomControls(true);

        cc = new CommanClass(this);

        p_name = cc.loadPrefString("p_namee2");
        p_last_name = cc.loadPrefString("p_namee_last");


        if (cc.isConnectingToInternet()) {
            startWebView(strUrl);
        } else {
            noInternetAlert();
        }


        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);

        tooName.setText(p_name +" "+ p_last_name);
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ln_date_picker_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (dateSelection == false){

                    showDateDialog(tv_start_date, startDate);

                    dateSelection = true;
                }else {

                    showDateDialog(tv_end_date, endDate);
                    dateSelection = false;
                }*/

                popUpDialog();

            }
        });

        /*updateDisplay(tv_start_date, startDate);
        updateDisplay(tv_end_date, endDate);*/

        initDate();

    }

    private void initDate() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        c1.add(java.util.Calendar.DATE, 1);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM");

        currentDate = df.format(c.getTime());
        String formattedDate1 = df1.format(c.getTime());
        nextDate = df.format(c1.getTime());
        String formattedDate2 = df1.format(c1.getTime());


        tv_start_date2.setText(formattedDate1);
        tv_end_date2.setText(formattedDate2);

    }

    private void popUpDialog() {

        dialog2 = new Dialog(WebViewMood.this);

        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.sleep_date_choose_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog2.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);



        ImageView close_dialog = (ImageView)dialog2.findViewById(R.id.close_dialog);
        TextView tv_submit_int_dialog = (TextView)dialog2.findViewById(R.id.tv_submit_int_dialog);
        TextView tv_report = (TextView)dialog2.findViewById(R.id.tv_report);
        TextView tv_date = (TextView)dialog2.findViewById(R.id.tv_date);
        LinearLayout ln_start_date = (LinearLayout)dialog2.findViewById(R.id.ln_start_date);
        LinearLayout ln_end_date = (LinearLayout)dialog2.findViewById(R.id.ln_end_date);

        final TextView tv_start_date = (TextView)dialog2.findViewById(R.id.tv_start_date);
        final TextView tv_end_date = (TextView)dialog2.findViewById(R.id.tv_end_date);

        tv_report.setText(getString(R.string.mood));
        tv_date.setText(getString(R.string.choose_date));

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();



        ln_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog(tv_start_date, startDate);



                dateSelection = true;
            }
        });

        ln_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog(tv_end_date, endDate);


                dateSelection = true;
            }
        });


        tv_submit_int_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start = tv_start_date.getText().toString().trim();
                String end = tv_end_date.getText().toString().trim();


                if(start.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})") && end.matches("([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})")){

                    Log.e("StartDate",start);
                    Log.e("EndDate",end);

                    if (startDate.after(endDate))
                    {
                        Log.e("WrongDate","Not perfect date");

                        cc.showToast(getString(R.string.correct_date_validation));

                    }else {



                        //Start date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date myDate = null;
                        try {
                            myDate = dateFormat.parse(start);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
                        start = timeFormat.format(myDate);

                        System.out.println(start);


                        //End date

                        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                        Date myDate2 = null;
                        try {
                            myDate2 = dateFormat2.parse(end);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat timeFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                        end = timeFormat.format(myDate2);

                        System.out.println(end);

                        webViewStart(start,end,patientID);

                        dialog2.dismiss();

                    }



                }else {

                    cc.showToast(getString(R.string.correct_date_validation));

                    Log.e("DateWrong","Not perfect");
                }



            }
        });

        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog2.dismiss();

            }
        });

        dialog2.show();

    }

    private void webViewStart(String start, String end, String patientID) {


        strUrlDateChange = "http://mbdbtechnology.com/projects/skepsi/ws/mood_view/mood/"+patientID.trim()+"/"+start.trim()+"/"+end.trim();

        Log.e("PassedUrl",strUrlDateChange);

        startWebView(strUrlDateChange);

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate= null;
        try {
            newDate = spf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat("dd/MM");
        start = spf.format(newDate);

        SimpleDateFormat spf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate2= null;
        try {
            newDate2 = spf2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf2= new SimpleDateFormat("dd/MM");
        end = spf2.format(newDate2);


        tv_start_date2.setText(start);
        tv_end_date2.setText(end);

        dialog2.dismiss();
    }

    private void webviewPostDate(Calendar date) {


        StringBuilder dateWeb = new StringBuilder()
                .append(date.get(Calendar.YEAR)).append("-")
                .append(date.get(Calendar.MONTH) + 1).append("-")
                .append(date.get(Calendar.DAY_OF_MONTH)).append(" ");

/*
        if (dateSelection == false){

            formatedEndDate = String.valueOf(dateWeb);

            startWebviewDateChange();

        }else {

            startWebviewDateChange();
            formatedStartDate = String.valueOf(dateWeb);

        }*/


    }

    /*private void startWebviewDateChange() {

        if (formatedStartDate == null && formatedEndDate == null){

        }else {

            Log.e("DateChangeStartDate",formatedStartDate);
            Log.e("DateChangeEndDate",formatedEndDate);



            strUrlDateChange = "http://mbdbtechnology.com/projects/skepsi/ws/sleep_data/sleep_data/"+patientID.trim()+"/"+formatedStartDate.trim()+"/"+formatedEndDate.trim();

            Log.e("PassedUrl",strUrlDateChange);

            startWebView(strUrlDateChange);

        }


    }*/

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH) + 1).append("/")
                        .append(date.get(Calendar.YEAR)));


    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        tooName = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateDisplay(tooName, activeDate);

            webviewPostDate(activeDate);

            unregisterDateDisplay();
        }
    };



    private void unregisterDateDisplay() {
        tooName = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private void startWebView(String url) {

        wv_description.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                currentUrl = url;

                view.loadUrl(url);

                Log.e("when you click :-", url);

                return true;
            }



            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {

                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
                wv_description.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }


                wv_description.setVisibility(View.GONE);
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog=new ProgressDialog(WebViewMood.this);
                dialog.setMessage("Please wait");
                dialog.show();
            }

            public void onPageFinished(WebView view, String url) {

                if(dialog.isShowing()){
                    dialog.dismiss();
                }

                isPageLoadedComplete=true;

            }

        });

        wv_description.getSettings().setJavaScriptEnabled(true);

        wv_description.loadUrl(url);

    }

    private void noInternetAlert() {

        final Dialog dialog = new Dialog(WebViewMood.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_webview);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView iv_close = (TextView) dialog.findViewById(R.id.tv_ok);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cc.isConnectingToInternet()) {
                    startWebView(strUrl);
                } else {
                    finish();
                }
            }
        });
        if(!dialog.isShowing()){
            dialog.show();
        }

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