package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.HelpExpandableAdapter;
import com.mxicoders.skepci.model.HelpData;
import com.mxicoders.skepci.model.HelpDataChild;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxicoders on 26/7/17.
 */

public class HelpPatientMenuActivity extends AppCompatActivity {

    private static ExpandableListView expandableListView;
    private static HelpExpandableAdapter adapter;

    public static ArrayList<HelpData> question_list;
    public static ArrayList<HelpDataChild> answer_list;
    private ArrayList<ArrayList<HelpDataChild>> ListChild;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;


    ProgressDialog pDialog;

    CommanClass cc;

    String patient,psychologist;

    EditText ed_search_help;
    LinearLayout ln_search_help;

    public HelpPatientMenuActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_patient_menu_help);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        patient = cc.loadPrefString2("Patient");
        psychologist = cc.loadPrefString2("Psychologist");


        expandableListView = (ExpandableListView)findViewById(R.id.simple_expandable_listview);

        // Setting group indicator null for custom indicator
        expandableListView.setGroupIndicator(null);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);


        ed_search_help = (EditText) findViewById(R.id.ed_search_help);
        ln_search_help = (LinearLayout)findViewById(R.id.ln_search_help);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.faq));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (psychologist.equals("psychologist")){

                    Intent helpPyscho = new Intent(HelpPatientMenuActivity.this,HelpPsychologistMenu.class);
                    startActivity(helpPyscho);
                    finish();


                }else {

                    finish();
                }
            }
        });


        setItems();
        setListener();

        ln_search_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchKey = ed_search_help.getText().toString();
                adapter.getFilter().filter(searchKey);
                closeKeyboard();

            }
        });
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }


    // Setting headers and childs to expandable listview
    void setItems() {

        if (psychologist.equals("psychologist")){


            if (!cc.isConnectingToInternet()) {
                AndyUtils.showToast(HelpPatientMenuActivity.this,getString(R.string.no_internet));
            } else{

                getHelpDataPsychologist();

            }



            Log.i("psychologist",psychologist);



        }else {

            if (!cc.isConnectingToInternet()) {
                AndyUtils.showToast(HelpPatientMenuActivity.this,getString(R.string.no_internet));
            } else{

                getHelpDataPatient();

            }



            Log.i("patient",patient);
        }


    }

    // Setting different listeners to expandablelistview
    void setListener() {

        // This listener will show toast on group click
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView listview, View view,
                                        int group_pos, long id) {

                closeKeyboard();

                return false;
            }
        });

        // This listener will expand one group at one time
        // You can remove this listener for expanding all groups
        expandableListView
                .setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    // Default position
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)

                            // Collapse the expanded group
                            expandableListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }

                });

        // This listener will show toast on child click
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {

                return false;
            }
        });
    }


    public void getHelpDataPsychologist() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.FAQ_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:help data", response);


                        question_list = new ArrayList<>();
                        answer_list = new ArrayList<>();
                        ListChild = new ArrayList<ArrayList<HelpDataChild>>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("faq_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    HelpData model = new HelpData();

                                    model.setQuestion(jsonObject1.getString("question"));

                                    question_list.add(model);


                                    HelpDataChild model2 = new HelpDataChild();

                                    model2.setAnswer(jsonObject1.getString("answer"));

                                    answer_list.add(model2);

                                    System.out.println(answer_list);

                                    ListChild.add(answer_list);


                                }

                                if(adapter != null){
                                    adapter = null;
                                }


                                adapter = new HelpExpandableAdapter(HelpPatientMenuActivity.this, question_list,ListChild);

                                expandableListView.setAdapter(adapter);


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
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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


    public void getHelpDataPatient() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.FAQ_PATIENT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:help data", response);


                        question_list = new ArrayList<>();
                        answer_list = new ArrayList<>();
                        ListChild = new ArrayList<ArrayList<HelpDataChild>>();


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("faq_data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    HelpData model = new HelpData();

                                    model.setQuestion(jsonObject1.getString("question"));

                                    question_list.add(model);


                                    HelpDataChild model2 = new HelpDataChild();

                                    model2.setAnswer(jsonObject1.getString("answer"));

                                    answer_list.add(model2);

                                    System.out.println(answer_list);

                                    ListChild.add(answer_list);


                                }

                                if(adapter != null){
                                    adapter = null;
                                }


                                adapter = new HelpExpandableAdapter(HelpPatientMenuActivity.this, question_list,ListChild);

                                expandableListView.setAdapter(adapter);


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
    public void onBackPressed() {


        if (psychologist.equals("psychologist")){

            Intent helpPyscho = new Intent(HelpPatientMenuActivity.this,HelpPsychologistMenu.class);
            startActivity(helpPyscho);
            finish();


        }else {

            finish();
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        cc.logoutapp2();
        super.onDestroy();
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
