package com.mxicoders.skepci.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.CopingCardReportAdapter;
import com.mxicoders.skepci.adapter.NoteReportAdapter;
import com.mxicoders.skepci.adapter.QuestionariesAdapter;
import com.mxicoders.skepci.adapter.QuestionnarieReportAdapter;
import com.mxicoders.skepci.model.CopingData;
import com.mxicoders.skepci.model.PatientArticleData;
import com.mxicoders.skepci.model.QuestionDetailsData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxi on 21/11/17.
 */

public class ReportPatientList extends AppCompatActivity {

    WebView wv_description;
    CommanClass cc;
    String strUrl,strUrlDateChange;

    ProgressDialog dialog1;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private String currentUrl;
    private boolean isPageLoadedComplete = false;

    RelativeLayout toolbar;
    TextView tooName,no_coping,no_note,no_ques;
    ImageView imgToolBack,iv_popup_add_report,iv_pdf_report;

    LinearLayout ln_date_picker_sleep;
    FrameLayout ln_pdf_webview;
    TextView tv_start_date,tv_end_date;

    private Calendar startDate;
    private Calendar endDate;

    static final int DATE_DIALOG_ID = 0;

    private Calendar activeDate;

    boolean dateSelection = false;

    String currentDate = "", nextDate = "";

    String formatedStartDate,formatedEndDate;
    String patientID,patientName;

    Dialog dialog;

    String selectedType;

    int counter = 0;

    ProgressDialog pDialog;

    GridView gridMessage;
    RecyclerView rc_note_report,rc_ques_report;
    CopingCardReportAdapter cp;
    NoteReportAdapter nAdapter;
    QuestionnarieReportAdapter qAdapter;

    public static List<CopingData> card_names;
    public static List<PatientArticleData> note_list;
    public static List<QuestionDetailsData> question_list;

    String note = "Notas";
    String coping_card = "Cartões de enfrentamento";
    String questionnaries = "Questionário";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_patientlist);

        cc = new CommanClass(this);

        strUrl = getIntent().getStringExtra("Url");
        patientID = getIntent().getStringExtra("pid");
        patientName = getIntent().getStringExtra("pname");

        wv_description = (WebView) findViewById(R.id.wv_description);

        gridMessage = (GridView) findViewById(R.id.gridMessage);

        rc_note_report = (RecyclerView) findViewById(R.id.rc_note_report);
        rc_note_report.setLayoutManager(new LinearLayoutManager(ReportPatientList.this));
        rc_note_report.setItemAnimator(new DefaultItemAnimator());

        rc_ques_report = (RecyclerView) findViewById(R.id.rc_ques_report);
        rc_ques_report.setLayoutManager(new LinearLayoutManager(ReportPatientList.this));
        rc_ques_report.setItemAnimator(new DefaultItemAnimator());

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        no_coping = (TextView)findViewById(R.id.no_coping);
        no_note = (TextView)findViewById(R.id.no_note);
        no_ques = (TextView)findViewById(R.id.no_ques);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        iv_popup_add_report = (ImageView)findViewById(R.id.iv_popup_add_report);
        iv_pdf_report = (ImageView)findViewById(R.id.iv_pdf_report);
        ln_pdf_webview = (FrameLayout)findViewById(R.id.ln_pdf_webview);




        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        iv_popup_add_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpDialog();
            }
        });

        iv_pdf_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //createPdf();

                if (note.equals(selectedType)){

                    createPdf(ln_pdf_webview);

                }else if (coping_card.equals(selectedType)){

                    createPdf(ln_pdf_webview);

                }else if (questionnaries.equals(selectedType)){

                    createPdf(ln_pdf_webview);

                }else {

                    createWebPagePrint(wv_description);
                }

            }
        });

        if (cc.isConnectingToInternet()) {

            java.util.Calendar c = java.util.Calendar.getInstance();
            java.util.Calendar c1 = java.util.Calendar.getInstance();
            c1.add(java.util.Calendar.DATE, 1);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");

            currentDate = df.format(c.getTime());
            String formattedDate1 = df1.format(c.getTime());
            nextDate = df.format(c1.getTime());
            String formattedDate2 = df1.format(c1.getTime());

            firstTimeStartWebview(currentDate,nextDate,patientID);

          //  startWebView(strUrl);
        } else {
            noInternetAlert();
        }
    }


    private void createPdf(View v){



        // create a new document
        PdfDocument document = new PdfDocument();


        View content =  (View) v;
        Log.e("content", content + "");
        Log.e("content_height", content.getHeight() + "");
        Log.e("content_width", content.getWidth() + "");

       /* int totalHeight = wv_description.getChildAt(0).getHeight();
        int totalWidth = wv_description.getChildAt(0).getWidth();*/

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),content.getHeight(),1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        content.draw(page.getCanvas());
        document.finishPage(page);



        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String pdfName = "skepsis_report"+patientName+"_"+ counter +"_"+ sdf.format(Calendar.getInstance().getTime()) + ".pdf";
        counter++;

        File outputFile = new File("/sdcard/skepsisreport/", pdfName);


        try {
            outputFile.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
            Toast.makeText(this,getString(R.string.report_pdf_download), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,getString(R.string.ws_error)+ e.toString(),
                    Toast.LENGTH_LONG).show();
        }


    }

    private void firstTimeStartWebview(String currentDate, String nextDate, String patientID) {

        wv_description.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                currentUrl = url;

                view.loadUrl(url);

                Log.e("when you click :-", url);

                return true;
            }



            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {

                if(dialog1 != null){
                    if(dialog1.isShowing()){
                        dialog1.dismiss();
                    }
                }
                wv_description.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                if(dialog1 != null){
                    if(dialog1.isShowing()){
                        dialog1.dismiss();
                    }
                }


                wv_description.setVisibility(View.GONE);
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog1=new ProgressDialog(ReportPatientList.this);
                dialog1.setMessage(getString(R.string.please_wait));
                dialog1.show();
            }

            public void onPageFinished(WebView view, String url) {

                if(dialog1.isShowing()){
                    dialog1.dismiss();
                }

                isPageLoadedComplete=true;

            }

        });

        wv_description.getSettings().setJavaScriptEnabled(true);

        strUrlDateChange = strUrl;


        wv_description.loadUrl(strUrlDateChange);
    }

    private void popUpDialog() {

        dialog = new Dialog(ReportPatientList.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.report_choose_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);

        String[] type = new String[]{"Humor","Sono","Emoção","Cartões de enfrentamento","Notas","Questionário"};


        ImageView close_dialog = (ImageView)dialog.findViewById(R.id.close_dialog);
        TextView tv_submit_int_dialog = (TextView)dialog.findViewById(R.id.tv_submit_int_dialog);
        Spinner sp_type_report = (Spinner)dialog.findViewById(R.id.sp_type_report);
        LinearLayout ln_start_date = (LinearLayout)dialog.findViewById(R.id.ln_start_date);
        LinearLayout ln_end_date = (LinearLayout)dialog.findViewById(R.id.ln_end_date);

        tv_start_date = (TextView)dialog.findViewById(R.id.tv_start_date);
        tv_end_date = (TextView)dialog.findViewById(R.id.tv_end_date);


        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        ArrayAdapter<String> gameKindArray= new ArrayAdapter<String>(ReportPatientList.this,android.R.layout.simple_spinner_item,type);
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type_report.setAdapter(gameKindArray);

        sp_type_report.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                selectedType = (String) parent.getItemAtPosition(pos);

                Log.e("SpinnerValueSelected",selectedType);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



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

                        webViewStart(start,end,selectedType,patientID);

                        dialog.dismiss();

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

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public  void createWebPagePrint(WebView webView) {
		/*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;*/
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + " Document";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        PrintJob printJob = printManager.print(jobName, printAdapter, builder.build());

        if(printJob.isCompleted()){
            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
        }
        else if(printJob.isFailed()){
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
        // Save the job object for later status checking
    }


    private void webViewStart(String start, String end, String selectedType, String patientID) {


        if (selectedType.equals("Humor")){

            strUrlDateChange = "http://mbdbtechnology.com/projects/skepsi/ws/Report/Report_view/"+patientID.trim()+"/"+"Mood"+"/"+start.trim()+"/"+end.trim();

            Log.e("PassedUrl",strUrlDateChange);

            startWebView(strUrlDateChange);

            gridMessage.setVisibility(View.GONE);
            rc_note_report.setVisibility(View.GONE);
            rc_ques_report.setVisibility(View.GONE);
            wv_description.setVisibility(View.VISIBLE);
            no_coping.setVisibility(View.GONE);
            no_note.setVisibility(View.GONE);
            no_ques.setVisibility(View.GONE);

            dialog.dismiss();

        }else if (selectedType.equals("Sono")){

            strUrlDateChange = "http://mbdbtechnology.com/projects/skepsi/ws/Report/Report_view/"+patientID.trim()+"/"+"Sleep"+"/"+start.trim()+"/"+end.trim();

            Log.e("PassedUrl",strUrlDateChange);

            startWebView(strUrlDateChange);

            gridMessage.setVisibility(View.GONE);
            rc_note_report.setVisibility(View.GONE);
            rc_ques_report.setVisibility(View.GONE);
            wv_description.setVisibility(View.VISIBLE);
            no_coping.setVisibility(View.GONE);
            no_note.setVisibility(View.GONE);
            no_ques.setVisibility(View.GONE);

            dialog.dismiss();

        }else if (selectedType.equals("Emoção")){

            strUrlDateChange = "http://mbdbtechnology.com/projects/skepsi/ws/Report/report_view/"+patientID.trim()+"/"+"Emotion"+"/"+start.trim()+"/"+end.trim();

            Log.e("PassedUrl",strUrlDateChange);

            startWebView(strUrlDateChange);

            gridMessage.setVisibility(View.GONE);
            rc_note_report.setVisibility(View.GONE);
            rc_ques_report.setVisibility(View.GONE);
            wv_description.setVisibility(View.VISIBLE);
            no_coping.setVisibility(View.GONE);
            no_note.setVisibility(View.GONE);
            no_ques.setVisibility(View.GONE);

            dialog.dismiss();

        }else if (selectedType.equals("Cartões de enfrentamento")){

            Log.e("Coping card",".......");

            getCopingCard(start,end);

        }else if (selectedType.equals("Notas")){

            Log.e("Notes",".........");

            getNote(start,end);
        }else if (selectedType.equals("Questionário")){

            Log.e("Questionaries",".........");

            getQuestionnaries(start,end);
        }else {

            Log.e("Nothing","Not start webview");
        }


    }

    private void getQuestionnaries(final String start, final String end) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.QUESTION_REPORT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:question data", response);

                        question_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();

                                rc_ques_report.setVisibility(View.VISIBLE);
                                rc_note_report.setVisibility(View.GONE);
                                gridMessage.setVisibility(View.GONE);
                                wv_description.setVisibility(View.GONE);
                                no_coping.setVisibility(View.GONE);
                                no_note.setVisibility(View.GONE);
                                no_ques.setVisibility(View.GONE);

                                if (jsonObject.getString("message").equals("Registro não encontrado")){

                                    no_ques.setVisibility(View.VISIBLE);
                                    no_note.setVisibility(View.GONE);
                                    no_coping.setVisibility(View.GONE);
                                    gridMessage.setVisibility(View.GONE);
                                    rc_note_report.setVisibility(View.GONE);
                                    rc_ques_report.setVisibility(View.GONE);
                                    wv_description.setVisibility(View.GONE);

                                }

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    QuestionDetailsData model = new QuestionDetailsData();

                                    model.setQuestion(jsonObject1.getString("question"));
                                    model.setAnswer(jsonObject1.getString("answer"));

                                    question_list.add(model);


                                }

                                qAdapter = new QuestionnarieReportAdapter(question_list,R.layout.questionnaries_report_item,ReportPatientList.this);
                                rc_ques_report.setAdapter(qAdapter);


                            }else{
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
                AndyUtils.showToast(ReportPatientList.this,getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("psychologist_id", cc.loadPrefString("user_id"));
                params.put("patient_id",patientID);
                params.put("startdate",start);
                params.put("enddate", end);
                Log.e("request question data", params.toString());
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

    private void getCopingCard(final String start, final String end) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COPING_CARD_REPORT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:coping data", response);

                        card_names = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();

                                gridMessage.setVisibility(View.VISIBLE);
                                rc_note_report.setVisibility(View.GONE);
                                rc_ques_report.setVisibility(View.GONE);
                                wv_description.setVisibility(View.GONE);
                                no_coping.setVisibility(View.GONE);
                                no_note.setVisibility(View.GONE);
                                no_ques.setVisibility(View.GONE);

                                if (jsonObject.getString("message").equals("Registro não encontrado")){

                                    no_coping.setVisibility(View.VISIBLE);
                                    gridMessage.setVisibility(View.GONE);
                                    wv_description.setVisibility(View.GONE);

                                }

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    CopingData model = new CopingData();

                                    model.setMessage(jsonObject1.getString("message"));
                                    model.setId(jsonObject1.getString("id"));

                                    card_names.add(model);


                                }


                                cp = new CopingCardReportAdapter(getApplicationContext(),card_names,ReportPatientList.this);
                                gridMessage.setAdapter(cp);


                            }else{
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
                AndyUtils.showToast(ReportPatientList.this,getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", cc.loadPrefString("user_id"));
                params.put("startdate",start);
                params.put("enddate", end);
                Log.e("request coping data", params.toString());
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

    private void getNote(final String start, final String end) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.NOTE_REPORT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:note data", response);

                        note_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();

                                rc_note_report.setVisibility(View.VISIBLE);
                                rc_ques_report.setVisibility(View.GONE);
                                gridMessage.setVisibility(View.GONE);
                                wv_description.setVisibility(View.GONE);
                                no_coping.setVisibility(View.GONE);
                                no_note.setVisibility(View.GONE);
                                no_ques.setVisibility(View.GONE);

                                if (jsonObject.getString("message").equals("Registro não encontrado")){

                                    no_note.setVisibility(View.VISIBLE);
                                    no_coping.setVisibility(View.GONE);
                                    no_ques.setVisibility(View.GONE);
                                    gridMessage.setVisibility(View.GONE);
                                    rc_note_report.setVisibility(View.GONE);
                                    rc_ques_report.setVisibility(View.GONE);
                                    wv_description.setVisibility(View.GONE);

                                }

                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    PatientArticleData model = new PatientArticleData();

                                    model.setName(jsonObject1.getString("title"));
                                    model.setaId(jsonObject1.getString("id"));

                                    note_list.add(model);


                                }

                                nAdapter = new NoteReportAdapter(note_list,R.layout.note_report_item,ReportPatientList.this);
                                rc_note_report.setAdapter(nAdapter);


                            }else{
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
                AndyUtils.showToast(ReportPatientList.this,getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", cc.loadPrefString("user_id"));
                params.put("startdate",start);
                params.put("enddate", end);
                Log.e("request note data", params.toString());
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

                if(dialog1 != null){
                    if(dialog1.isShowing()){
                        dialog1.dismiss();
                    }
                }
                wv_description.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                if(dialog1 != null){
                    if(dialog1.isShowing()){
                        dialog1.dismiss();
                    }
                }


                wv_description.setVisibility(View.GONE);
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog1=new ProgressDialog(ReportPatientList.this);
                dialog1.setMessage(getString(R.string.please_wait));
                dialog1.show();
            }

            public void onPageFinished(WebView view, String url) {

                if(dialog1.isShowing()){
                    dialog1.dismiss();
                }

                isPageLoadedComplete=true;

            }

        });

        wv_description.getSettings().setJavaScriptEnabled(true);

        wv_description.loadUrl(url);

    }

    private void noInternetAlert() {

        final Dialog dialog = new Dialog(ReportPatientList.this);
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
