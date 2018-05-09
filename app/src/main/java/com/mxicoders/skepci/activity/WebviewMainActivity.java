package com.mxicoders.skepci.activity;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

/**
 * Created by mxicoders on 8/9/17.
 */

public class WebviewMainActivity extends AppCompatActivity {


    WebView wv_description;
    CommanClass cc;
    String strUrl;

    ProgressDialog dialog;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private String currentUrl;
    private boolean isPageLoadedComplete = false;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    String p_name,p_last_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main);


        strUrl = getIntent().getStringExtra("Url");

        wv_description = (WebView) findViewById(R.id.wv_description);

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
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(p_name +" "+ p_last_name);
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void startWebView(String url) {


        /*wv_description.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "PenialDownload");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                        Toast.LENGTH_LONG).show();

            }
        });*/

        wv_description.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                currentUrl = url;

                view.loadUrl(url);

                Log.e("when you click :-", url);

                /*if(!strUrl.equals("http://mbdbtechnology.com/projects/peniel/?category=ministerios")){

                    if (url.contains("http://mbdbtechnology.com/projects/peniel/?category=home") || url.contains("http://mbdbtechnology.com/projects/peniel/?post-category")) {

                        Intent launchNextActivity = new Intent(DesActivity.this, MainActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(launchNextActivity);
                        finish();
                    }
                }else{
                    if (url.contains("http://mbdbtechnology.com/projects/peniel/?category=home")) {
                        Intent launchNextActivity = new Intent(DesActivity.this, MainActivity.class);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(launchNextActivity);
                        finish();
                    }
                }*/

                return true;
            }



            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {

                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
//                ll_error.setVisibility(View.VISIBLE);
                wv_description.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);

                if(dialog != null){
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                }


//                ll_error.setVisibility(View.VISIBLE);
                wv_description.setVisibility(View.GONE);
            }

            public void onLoadResource(WebView view, String url) {


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog=new ProgressDialog(WebviewMainActivity.this);
                dialog.setMessage(getString(R.string.please_wait));
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

        final Dialog dialog = new Dialog(WebviewMainActivity.this);
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
