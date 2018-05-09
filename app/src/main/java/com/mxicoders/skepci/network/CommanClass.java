package com.mxicoders.skepci.network;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityMenuPatient;
import com.mxicoders.skepci.activity.ActivityMenuPsychologist;
import com.mxicoders.skepci.activity.TaskHisPatientMenuActivity;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by admin1 on 21/3/16.
 */
public class CommanClass {

    private Context _context;
    SharedPreferences pref,pref2,pref3,pref4;

    EditText et_history_pass;

    ProgressDialog  pDialog3;

    Dialog  dialog;

    public CommanClass(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences("Goalkeeper",
                _context.MODE_PRIVATE);

        pref2 = _context.getSharedPreferences("Skepsis",
                _context.MODE_PRIVATE);

        pref3 = _context.getSharedPreferences("Skepsis2",
                _context.MODE_PRIVATE);

        pref4 = _context.getSharedPreferences("Skepsis2",
                _context.MODE_PRIVATE);
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showToast(String text) {
        // TODO Auto-generated method stub

        final Toast t = Toast.makeText(_context, text, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);

        new CountDownTimer(Math.max(100,100),200) {
            @Override
            public void onFinish() {
                t.show();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                t.show();
            }
        }.start();

       /* Toast.makeText(_context, text, Toast.LENGTH_LONG).show();*/
    }

    public void showSnackbar(View coordinatorLayout, String text) {

        Snackbar
                .make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }

    public void savePrefString(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefString2(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref2.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefString3(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref3.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefString4(String key, String value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref4.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePrefBoolean(String key, Boolean value) {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadPrefString(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref.getString(key, "");
        return strSaved;
    }

    public String loadPrefString2(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref2.getString(key, "");
        return strSaved;
    }

    public String loadPrefString3(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref3.getString(key, "");
        return strSaved;
    }

    public String loadPrefString4(String key) {
        // TODO Auto-generated method stub
        String strSaved = pref4.getString(key, "");
        return strSaved;
    }

    public Boolean loadPrefBoolean(String key) {
        // TODO Auto-generated method stub
        boolean isbool = pref.getBoolean(key, false);
        return isbool;
    }

    public void logoutapp() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void logoutapp2() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref2.edit();
        editor.clear();
        editor.commit();
    }

    public void logoutapp3() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref3.edit();
        editor.clear();
        editor.commit();
    }

    public void logoutapp4() {
        // TODO Auto-generated method stub
        SharedPreferences.Editor editor = pref4.edit();
        editor.clear();
        editor.commit();
    }


    public void showInactiveDialog(){


       passProtectedDialog();
    }

    private void passProtectedDialog() {

        savePrefBoolean("Inactive",true);

        dialog = new Dialog(_context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pass_history_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = _context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);



        ImageView iv_submit_pass = (ImageView)dialog.findViewById(R.id.iv_submit_pass);

        et_history_pass = (EditText) dialog.findViewById(R.id.et_history_pass);

        iv_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passValidation()){

                    savePrefBoolean("Inactive",false);

                    savePrefBoolean("isDialogOpen",false);

                    dialog.dismiss();
                }else {

                    showToast(_context.getString(R.string.invalid_pass));
                }
            }
        });

        dialog.show();


    }

    private boolean passValidation() {

        boolean in = false;

        String pass = et_history_pass.getText().toString().trim();

        if (!isConnectingToInternet()) {

            AndyUtils.showToast(_context,_context.getString(R.string.no_internet));

        } else if (pass.equals("")) {

            AndyUtils.showToast(_context,_context.getString(R.string.enter_password));
        } else {

            if (pass.equals(loadPrefString("inactivePass"))){

                in = true;
            }

        }

        return in;
    }

    private void makeJsonPass(final String pass) {

      final ProgressDialog  pDialog3 = new ProgressDialog(_context);
        pDialog3.setMessage("Please wait...");
        pDialog3.setIndeterminate(false);
        pDialog3.setCancelable(false);
        pDialog3.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.INACTIVE_PROTECTION,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changepass", response);
                        jsonParsePass(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog3.dismiss();
                AndyUtils.showToast(_context,_context.getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid",loadPrefString("user_id"));
                params.put("password", pass);

                Log.i("request change password", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", loadPrefString("user_token"));
                headers.put("Authorization", loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParsePass(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog3.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                dialog.dismiss();

            } else {

                showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("changepass Error",e.toString());
        }
    }

    public String MyText(String text) {
        String s = "";
        try {
            s = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;

    }

    @SuppressLint("SimpleDateFormat")
    public String dateConvert(String timestmp)
            throws ParseException {
        String str_date_to;
        Date date = null;
        SimpleDateFormat formate = new SimpleDateFormat("MM-dd-yyyy");
        try {
            date = formate.parse(timestmp);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat formate_to = new SimpleDateFormat("dd MMM yyyy");
        str_date_to = formate_to.format(date);

        return str_date_to;
    }

}
