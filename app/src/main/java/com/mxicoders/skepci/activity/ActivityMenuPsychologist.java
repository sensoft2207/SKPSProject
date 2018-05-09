package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.fragment.FragmentDrawer;
import com.mxicoders.skepci.fragment.FragmentHome;
import com.mxicoders.skepci.fragment.FragmentHomePsychologist;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mxicoders.skepci.fragment.FragmentDrawer.mDrawerLayout;

/**
 * Created by mxicoders on 11/7/17.
 */

public class ActivityMenuPsychologist extends AppCompatActivity {

    CommanClass cc;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_PLIST = "plist";
    private static final String TAG_ARTICLES = "articles";
    private static final String TAG_QUEST = "quest";
    public static String CURRENT_TAG = TAG_PROFILE;

    private static String TAG = ActivityMenuPsychologist.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    LinearLayout lnProfile,lnPatientList,lnArticles,lnQuestionary,lnSignout,tvHelp;
    TextView tvNodialog,tvYesdialog,tvPatientName;


    ProgressDialog pDialog;
    Dialog dialog;

    LinearLayout chat_icon,ln_patient_changepass;
    EditText edOldPass,edNewPass,edConfirmPass;

    String fname,lname;
    String oldPass,newPass,confirmNewPass;
    String title = "";

    ImageView backDrawer;

//    public static final long DISCONNECT_TIMEOUT = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_phychologist);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        cc = new CommanClass(ActivityMenuPsychologist.this);


        fname = cc.loadPrefString("fname");
        lname = cc.loadPrefString("lname");

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        chat_icon=(LinearLayout)drawerFragment.getActivity().findViewById(R.id.chat_icon);
        ln_patient_changepass=(LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_changepass);


        lnProfile = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_profile);
        lnPatientList = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_list);
        lnArticles = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_articles);
        lnQuestionary = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_questionnaries);
        lnSignout = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.lnSignout);
        tvHelp = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.tv_help_psycho);


        lnQuestionary.setBackgroundColor(Color.parseColor("#6DB0E4"));

        tvPatientName = (TextView)drawerFragment.getActivity().findViewById(R.id.patient_name);

        tvPatientName.setText(fname +" "+ lname);

        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chat = new Intent(ActivityMenuPsychologist.this, ActivityPatientChatList.class);
                startActivity(chat);
                mDrawerLayout.closeDrawers();

            }
        });

        lnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#6DB0E4"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));


                Intent prof = new Intent(ActivityMenuPsychologist.this, EditProfileActivity.class);
                startActivity(prof);
                mDrawerLayout.closeDrawers();



            }
        });

        lnPatientList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#6DB0E4"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent plist = new Intent(ActivityMenuPsychologist.this, ActivityPatientList.class);
                startActivity(plist);
                mDrawerLayout.closeDrawers();

            }
        });

        lnArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#6DB0E4"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent article = new Intent(ActivityMenuPsychologist.this, ArticlesActivity.class);
                startActivity(article);
                mDrawerLayout.closeDrawers();


            }
        });

        lnQuestionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#6DB0E4"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent Ques = new Intent(ActivityMenuPsychologist.this, QuestionariesActivity.class);
                startActivity(Ques);
                mDrawerLayout.closeDrawers();


            }
        });

        lnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                dialog = new Dialog(ActivityMenuPsychologist.this);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.signout_dialog);



                ImageView close_dialogg = (ImageView)dialog.findViewById(R.id.close_dialog);

                tvNodialog = (TextView) dialog.findViewById(R.id.tv_no_dialog);
                tvYesdialog = (TextView) dialog.findViewById(R.id.tv_yes_dialog);

                tvYesdialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!cc.isConnectingToInternet()) {
                            AndyUtils.showToast(ActivityMenuPsychologist.this,getString(R.string.no_internet));
                        } else{

                            logout();

                        }


                    }
                });

                tvNodialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                close_dialogg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#6DB0E4"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent help = new Intent(ActivityMenuPsychologist.this, HelpPsychologistMenu.class);
                startActivity(help);
                mDrawerLayout.closeDrawers();

            }
        });

        ln_patient_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lnProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnPatientList.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnArticles.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnQuestionary.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                lnSignout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#6DB0E4"));


                changePassDialog();

                mDrawerLayout.closeDrawers();



            }
        });


        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);


        android.app.FragmentTransaction   tra = getFragmentManager().beginTransaction();
        Fragment newFragment = new FragmentHomePsychologist();
        tra.replace(R.id.container_body, newFragment);
        title = getResources().getString(R.string.nav_item_home);
        getSupportActionBar().setTitle(title);
        tra.addToBackStack(null);
        tra.commit();
    }


    private void changePassDialog() {

        dialog = new Dialog(ActivityMenuPsychologist.this);
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.change_password_dialog);
        dialog.setCancelable(false);

        dialog.show();

        ImageView close_dialogg = (ImageView)dialog.findViewById(R.id.close_dialog);

        edOldPass = (EditText)dialog.findViewById(R.id.ed_old_pass);
        edNewPass = (EditText)dialog.findViewById(R.id.ed_new_pass);
        edConfirmPass = (EditText)dialog.findViewById(R.id.ed_new_confirm_pass);

        TextView tvOkay = (TextView) dialog.findViewById(R.id.tv_okay_dialog);
        TextView  tvNo = (TextView) dialog.findViewById(R.id.tv_cancle_dialog);
        TextView changeHead = (TextView) dialog.findViewById(R.id.tv_change_pass_head);


        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword();

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        close_dialogg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }

    private void ChangePassword() {

        oldPass = edOldPass.getText().toString().trim();
        newPass = edNewPass.getText().toString().trim();
        confirmNewPass = edConfirmPass.getText().toString().trim();
        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(this,getString(R.string.no_internet));
        } else if (oldPass.equals("")) {

            AndyUtils.showToast(this,getString(R.string.enter_old_pass));
        } else if (newPass.equals("")) {
            AndyUtils.showToast(this,getString(R.string.enter_new_pass));
        } else if (confirmNewPass.equals("")) {
            AndyUtils.showToast(this,getString(R.string.enter_confirm_new_pass));
        } else if (!confirmNewPass.matches(newPass)) {
            AndyUtils.showToast(this,getString(R.string.pass_not_match2));
        } else {
            ChangePasswordJson(oldPass,newPass);
        }
    }

    private void ChangePasswordJson(final String oldPass, final String newPass) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHANGE_PASSWORD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:changepassword", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                            } else {
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
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("old_password", oldPass);
                params.put("new_password", newPass);
                Log.e("request:change_password", params.toString());
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

    private void logout() {

        pDialog = new ProgressDialog(ActivityMenuPsychologist.this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.LOGOUT,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:logout", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                cc.logoutapp();
                                dialog.dismiss();
                                startActivity(new Intent(ActivityMenuPsychologist.this,ActivityLoginScreen.class));
                                finish();
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
                AndyUtils.showToast(ActivityMenuPsychologist.this,getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request logout", params.toString());
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

   /* private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {

        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect

            Log.e("Inactive called",".............");
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }*/

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }
}
