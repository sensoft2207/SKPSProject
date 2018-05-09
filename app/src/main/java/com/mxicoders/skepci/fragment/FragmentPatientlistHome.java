package com.mxicoders.skepci.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;

/**
 * Created by mxicoders on 8/9/17.
 */

public class FragmentPatientlistHome extends Fragment {

    WebView wv_description;
    CommanClass cc;
    String strUrl;

    ProgressDialog dialog;

    private long lastPressedTime;
    private static final int PERIOD = 2000;

    private String currentUrl;
    private boolean isPageLoadedComplete = false;

    String p_id;

    public FragmentPatientlistHome() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patientlist_home_fragment, container, false);

        cc = new CommanClass(getActivity());

        p_id = cc.loadPrefString("patient_id_main");


        strUrl = "http://mbdbtechnology.com/projects/skepsi/ws/patient_chart/mood/" + p_id;

        wv_description = (WebView)rootView.findViewById(R.id.wv_description);

        wv_description.getSettings().setBuiltInZoomControls(true);

        cc = new CommanClass(getActivity());

       /* p_name = cc.loadPrefString("p_namee2");
        p_last_name = cc.loadPrefString("p_namee_last");*/

        if (cc.isConnectingToInternet()) {
            startWebView(strUrl);
        } else {
            noInternetAlert();
        }


        return rootView;

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
                dialog=new ProgressDialog(getActivity());
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

        final Dialog dialog = new Dialog(getActivity());
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
                    getActivity().finish();
                }
            }
        });
        if(!dialog.isShowing()){
            dialog.show();
        }

    }
}
