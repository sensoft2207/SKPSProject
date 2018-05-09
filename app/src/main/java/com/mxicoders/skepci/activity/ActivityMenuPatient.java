package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.mxicoders.skepci.fragment.FragmentDrawerPatientMenu;
import com.mxicoders.skepci.fragment.FragmentHome;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mxicoders.skepci.fragment.FragmentDrawerPatientMenu.mDrawerLayout;

/**
 * Created by mxicoders on 20/7/17.
 */

public class ActivityMenuPatient extends AppCompatActivity {

    private static String TAG = ActivityMenuPsychologist.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawerPatientMenu drawerFragment;

    EditText  et_history_pass;
    EditText edOldPass,edNewPass,edConfirmPass;

    TextView tvPatientName,tvChat;

    LinearLayout tvProfile,tvHistory,tvArticle,tvNotes,tvTodo,tvHelp,tvLogout,ln_patient_changepass;
    LinearLayout chat_icon;

    ImageView backDrawer;

    ProgressDialog pDialog,pDialog3;

    CommanClass cc;

    String fname,lname;
    String oldPass,newPass,confirmNewPass;
    String title = "";

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_patient);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        cc = new CommanClass(ActivityMenuPatient.this);

        fname = cc.loadPrefString("fname");
        lname = cc.loadPrefString("lname");

        drawerFragment = (FragmentDrawerPatientMenu)
                getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_patientlist_menu);

        chat_icon=(LinearLayout)drawerFragment.getActivity().findViewById(R.id.chat_icon);

        backDrawer = (ImageView) drawerFragment.getActivity().findViewById(R.id.back_close_drawer);

        tvProfile = (LinearLayout) drawerFragment.getActivity().findViewById(R.id.ln_patient_profile);
        tvHistory = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_history);
        tvArticle = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_article);
        tvNotes = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_notes);
        tvTodo = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_todo);
        tvHelp = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_help);
        tvLogout = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_logout);
        ln_patient_changepass = (LinearLayout)drawerFragment.getActivity().findViewById(R.id.ln_patient_changepass);

        tvTodo.setBackgroundColor(Color.parseColor("#6DB0E4"));

        tvPatientName = (TextView)drawerFragment.getActivity().findViewById(R.id.patient_name);
        tvChat = (TextView)drawerFragment.getActivity().findViewById(R.id.tv_chat);

        tvPatientName.setText(fname +" "+ lname);

        chat_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chat = new Intent(ActivityMenuPatient.this, ActivityChat.class);
                chat.putExtra("target_user_id",cc.loadPrefString("psychologist_id"));
                chat.putExtra("fromPatient",true);
                startActivity(chat);
                mDrawerLayout.closeDrawers();

            }
        });

        backDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawers();

            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent chat = new Intent(ActivityMenuPatient.this, EditProfilePatientActivity.class);
                startActivity(chat);
                mDrawerLayout.closeDrawers();

            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                passProtectedDialog();

            }
        });

        tvArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent t_history = new Intent(ActivityMenuPatient.this, PatientArticleMenuFragment.class);
                startActivity(t_history);
                mDrawerLayout.closeDrawers();

            }
        });

        tvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent note = new Intent(ActivityMenuPatient.this,NotesPatientMenuActivity.class);
                startActivity(note);
                mDrawerLayout.closeDrawers();

            }
        });

        tvTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent todo = new Intent(ActivityMenuPatient.this,TodoPatientMenuActivity.class);
                startActivity(todo);
                mDrawerLayout.closeDrawers();

            }
        });

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#6DB0E4"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                Intent help = new Intent(ActivityMenuPatient.this,HelpPatientMenuActivity.class);
                startActivity(help);
                mDrawerLayout.closeDrawers();

            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#6DB0E4"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#00FFFFFF"));

                final Dialog dialog = new Dialog(ActivityMenuPatient.this);
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.signout_dialog);
                //dialog.setTitle("Hello");

                dialog.show();

                ImageView close_dialogg = (ImageView)dialog.findViewById(R.id.close_dialog);

               TextView tvNodialog = (TextView) dialog.findViewById(R.id.tv_no_dialog);
               TextView  tvYesdialog = (TextView) dialog.findViewById(R.id.tv_yes_dialog);

                tvYesdialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        logout();

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

            }
        });

        ln_patient_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvProfile.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHistory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvArticle.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvNotes.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvTodo.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvHelp.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                tvLogout.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ln_patient_changepass.setBackgroundColor(Color.parseColor("#6DB0E4"));

                changePassDialog();

                mDrawerLayout.closeDrawers();

            }
        });


        drawerFragment.setUp(R.id.fragment_nav_drawer_patientlist_menu, (DrawerLayout)findViewById(R.id.drawer_layout), mToolbar);


        android.app.FragmentTransaction   tra = getFragmentManager().beginTransaction();
        Fragment newFragment = new FragmentHome();
        tra.replace(R.id.container_body, newFragment);
        title = getResources().getString(R.string.nav_item_home);
        getSupportActionBar().setTitle(title);
        tra.addToBackStack(null);
        tra.commit();


    }

    private void changePassDialog() {

        dialog = new Dialog(ActivityMenuPatient.this);
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

    private void passProtectedDialog() {

        dialog = new Dialog(ActivityMenuPatient.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pass_history_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);



        ImageView iv_submit_pass = (ImageView)dialog.findViewById(R.id.iv_submit_pass);

        et_history_pass = (EditText) dialog.findViewById(R.id.et_history_pass);

        iv_submit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passValidation();

            }
        });

        dialog.show();


}

    private void passValidation() {

       String pass = et_history_pass.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(ActivityMenuPatient.this,getString(R.string.no_internet));

        } else if (pass.equals("")) {

            AndyUtils.showToast(ActivityMenuPatient.this,getString(R.string.enter_password));
        } else {

            makeJsonPass(pass);

        }
    }

    private void makeJsonPass(final String pass) {

        pDialog3 = new ProgressDialog(ActivityMenuPatient.this);
        pDialog3.setMessage("Please wait...");
        pDialog3.setIndeterminate(false);
        pDialog3.setCancelable(false);
        pDialog3.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,Const.ServiceType.TASK_HISTORY_PROTECTION,
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
                AndyUtils.showToast(ActivityMenuPatient.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("patient_id",cc.loadPrefString("user_id"));
                params.put("password", pass);

                Log.i("request change password", params.toString());

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

    private void jsonParsePass(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog3.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                Intent t_history = new Intent(ActivityMenuPatient.this, TaskHisPatientMenuActivity.class);
                startActivity(t_history);
                mDrawerLayout.closeDrawers();

                dialog.dismiss();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("changepass Error",e.toString());
        }
    }


    private void logout() {

        pDialog = new ProgressDialog(ActivityMenuPatient.this);
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
                                startActivity(new Intent(ActivityMenuPatient.this,ActivityLoginScreen.class));
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
                cc.showToast(getString(R.string.ws_error));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

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
    @Override
    public void onBackPressed() {

        finish();
    }

}
