package com.mxicoders.skepci.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    CommanClass cc;



    private static int SPLASH_TIME_OUT = 4000;


    int PERMISSION_ALL = 1;

    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        cc = new CommanClass(SplashScreen.this);

        cc.savePrefString("Authorization","delta141forceSEAL8PARA9MARCOSKATJRT");


        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{

            CountDown();
        }

        setLocale("pt");

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    CountDown();

                } else {
                    showErrorDialog();
                }
                break;
        }
    }
    private void CountDown() {

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {


                if (!cc.isConnectingToInternet()) {

                    AndyUtils.showToast(SplashScreen.this,getString(R.string.no_internet));

                }else {
                    if(cc.loadPrefBoolean("isLoginPatient")){
                        Intent i = new Intent(SplashScreen.this, ActivityMenuPatient.class);
                        cc.savePrefBoolean("isInactiveDevice", true);
                        startActivity(i);



                    }else if(cc.loadPrefBoolean("isLogin")){
                        Intent i = new Intent(SplashScreen.this, ActivityMenuPsychologist.class);
                        cc.savePrefBoolean("isInactiveDevice", true);
                        startActivity(i);



                    }else{
                        Intent i = new Intent(SplashScreen.this, ActivityLoginScreen.class);
                        startActivity(i);
                    }
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);

    }


    public void showErrorDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(SplashScreen.this);
        alert.setTitle(getString(R.string.app_name));
        alert.setMessage(getString(R.string.allow_permission));
        alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(!hasPermissions(SplashScreen.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(SplashScreen.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });
        alert.show();
    }

}
