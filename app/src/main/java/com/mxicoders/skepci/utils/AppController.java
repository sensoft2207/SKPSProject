package com.mxicoders.skepci.utils;

import android.app.Application;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityMenuPsychologist;
import com.mxicoders.skepci.network.CommanClass;

import java.util.Locale;

/**
 * Created by Sachin on 3/2/2017.
 */

public class AppController extends Application {


    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    private ImageLoader mImageLoader;

    private int lastInteractionTime;
    private Boolean isScreenOff = false;

    CommanClass cc;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        cc = new CommanClass(getApplicationContext());

        startUserInactivityDetectThread(); // start the thread to detect inactivity
        new ScreenReceiver();  // creating receive SCREEN_OFF and SCREEN_ON broadcast msgs from the device.


        setLocale("pt");
    }

//    5 min = 5 * 60 * 1000

    public void startUserInactivityDetectThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(25000); // checks every 15sec for inactivity
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isScreenOff || getLastInteractionTime()> 120000/* ||  !isInForeGrnd*/)
                    {

                        // and you do your stuff like log the user out

                        cc.savePrefBoolean("Inactive",true);

                        Log.e("User Inactive detected",".............");
                    }
                }
            }
        }).start();
    }

    public long getLastInteractionTime() {
        return lastInteractionTime;
    }

    public void setLastInteractionTime(int lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }

    private class ScreenReceiver extends BroadcastReceiver {

        protected ScreenReceiver() {
            // register receiver that handles screen on and screen off logic
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                isScreenOff = true;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                isScreenOff = false;
            }
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}