package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.ChatAdapter;
import com.mxicoders.skepci.model.ChatMessageData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mxicoders on 29/7/17.
 */

public class ActivityChat extends AppCompatActivity {

    RecyclerView rv_chat;
    boolean SEMAFOR = false;
    boolean SEMAFOR_Send_Message = false;
    ImageView iv_send_chat;
    EditText et_message_chat;
    CommanClass cc;
    ProgressDialog pDialog;
    String FinalUrl = "";
    LinearLayout ll_main,ll_sendMessage;
    String target_user_id = "";
    boolean patient = false;
    int total_messages = 0;
    Integer responseString = null;
    ArrayList<ChatMessageData> chatMessageList;
    ArrayList<ChatMessageData> tempChatMessageList;
    ChatAdapter chatAdapter;
    LinearLayoutManager llm;
    int total_chat_count = 0;
    int total_pages = 0;
    boolean flag =true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatMessageList = new ArrayList<>();
        tempChatMessageList = new ArrayList<>();
        llm = new LinearLayoutManager(this);
        cc = new CommanClass(this);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        iv_send_chat = (ImageView) findViewById(R.id.iv_send_chat);
        et_message_chat = (EditText) findViewById(R.id.et_message_chat);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_sendMessage= (LinearLayout) findViewById(R.id.ll_sendMessage);
        ll_sendMessage.setEnabled(false);
        target_user_id = getIntent().getStringExtra("target_user_id");
        patient = getIntent().getBooleanExtra("fromPatient", false);

        if (patient) {
            FinalUrl = Const.ServiceType.GET_PATIENT_CHAT;

            Log.e("@@patientchat","....final");

        } else {
            FinalUrl = Const.ServiceType.GET_PSYCHOLOGIST_CHAT;

            Log.e("@@psychologistchat","....final");
        }

        iv_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_message_chat.getText().toString();
                if (!message.isEmpty()) {
                    SEMAFOR = true;
                    et_message_chat.setText("");
                    makeJsonCallSendMessage(message);
                }
            }
        });
        makeJsonCallGetChat();

    }

    private void makeJsonCallSendMessage(final String message) {
        SEMAFOR_Send_Message=true;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.SEND_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@All Chat", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                et_message_chat.setText("");
                                ChatMessageData data = new ChatMessageData();

                                JSONObject object = jsonObject.getJSONObject("message_data");

                                String time = object.getString("created_date");

                                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date date = null;
                                try {
                                    date = dFormat.parse(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                String timeString = dateFormat.format(date);

                                data.setFromId(object.getString("from_id"));
                                data.setFromName(object.getString("from_name"));
                                data.setFromTime(timeString);
                                data.setToId(object.getString("to_id"));
                                data.setToName(object.getString("to_name"));
                                data.setToTime(timeString);

                                data.setChatMessage(object.getString("message"));

                                chatMessageList.add(data);
                                if (chatAdapter == null){

                                    chatAdapter = new ChatAdapter(ActivityChat.this, chatMessageList);
                                }

                                //chatAdapter.notifyItemRangeInserted(0, chatMessageList.size());
                                chatAdapter.notifyItemInserted(chatMessageList.size()-1);
                                rv_chat.scrollToPosition(chatMessageList.size()-1);
                                SEMAFOR_Send_Message=false;
                                SEMAFOR = false;

                            } else {
                                SEMAFOR_Send_Message=false;
                                SEMAFOR = false;

                                cc.showToast(jsonObject.getString("message"));

                            }

                        } catch (JSONException e) {
                            SEMAFOR = false;
                            e.printStackTrace();
                            Log.e("forgot_pass Error", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cc.showSnackbar(ll_main, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", target_user_id);
                params.put("message", message);


                Log.i("@@@Send Message", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                Log.i("request header", headers.toString());
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }

    private void readMessage() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.READ_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:read", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){


                            }else{

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                cc.showToast("No Internet connection");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("psychologist_id", cc.loadPrefString("user_id"));
                params.put("patient_id", target_user_id);
                Log.e("request read", params.toString());
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

    private void makeJsonCallGetChat() {
        pDialog = new ProgressDialog(ActivityChat.this);
        pDialog.setMessage("Please wait...");
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, FinalUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@All Chat", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("200")) {
                                total_chat_count = jsonObject.getInt("total_chat_count");


                                total_pages = total_chat_count / 20;
                                float temp = total_chat_count % 20;
                                if (temp != 0.00) {
                                    total_pages = total_pages + 1;
                                }
                                if (!chatMessageList.isEmpty()) {
                                    chatMessageList.clear();
                                    total_messages = 0;
                                }

                                Log.e("@TotalPages",total_pages+"");

                                JSONArray jsonChatArray = jsonObject.getJSONArray("chat_detail");

//
//                                for (int i = 0; i < jsonChatArray.length(); i++) {
//
//                                    ChatMessageData data = new ChatMessageData();
//
//                                    JSONObject object = (JSONObject) jsonChatArray.get(i);
//
//                                    String time = object.getString("created_date");
//
//                                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                                    Date date = null;
//                                    try {
//                                        date = dFormat.parse(time);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                                    String timeString = dateFormat.format(date);
//
//                                    data.setFromId(object.getString("from_id"));
//                                    data.setFromName(object.getString("from_name"));
//                                    data.setFromTime(timeString);
//                                    data.setToId(object.getString("to_id"));
//                                    data.setToName(object.getString("to_name"));
//                                    data.setToTime(timeString);
//                                    data.setMessageId(object.getString("id"));
//                                    data.setChatMessage(object.getString("message"));
//
//
//                                    chatMessageList.add(data);
//                                }

                                total_messages = jsonChatArray.length();


//                                chatAdapter = new ChatAdapter(ActivityChat.this, chatMessageList);
//                                llm.setStackFromEnd(true);
//                                rv_chat.setLayoutManager(llm);
//                                rv_chat.setAdapter(chatAdapter);

                                readMessage();

                                callAsynchronousTask();

                            } else {

                                if (chatMessageList.isEmpty()) {
                                    cc.showToast(jsonObject.getString("message"));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("forgot_pass Error", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showSnackbar(ll_main, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("page", 1 + "");

                if (!patient) {
                    params.put("target_user_id", target_user_id);
                }

                Log.i("@@@ GetChat", params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                Log.i("request header", headers.toString());
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (!SEMAFOR) {
                                Log.e("@@SEMAFOR", "Is Free");

                                new PerformBackgroundTask().execute();
                            } else {
                                Log.e("@@SEMAFOR", "Is In Use");
                            }

                            // PerformBackgroundTask this class is the class that extends AsynchTask

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000); //execute in every 50000 ms

        //new PerformBackgroundTask().execute();
    }


    private class PerformBackgroundTask extends AsyncTask<Void, Integer, Integer> {


        public PerformBackgroundTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            SEMAFOR = true;
            Log.e("@@@In Async Task", "In Background");
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private Integer uploadFile() {

            if (!tempChatMessageList.isEmpty()) {
                tempChatMessageList.clear();
                responseString = 0;
            }


            for (int i = 1; i <= total_pages; i++) {

                final int finalI = i;
                final int finalI1 = i;
                StringRequest jsonObjReq = new StringRequest(Request.Method.POST, FinalUrl,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e("@@@All Chat Async", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if (jsonObject.getString("status").equals("200")) {


                                        total_chat_count = jsonObject.getInt("total_chat_count");

                                        total_pages = total_chat_count / 20;
                                        float temp = total_chat_count % 20;
                                        if (temp != 0.00) {
                                            total_pages = total_pages + 1;
                                        }

                                        /*if (!chatMessageList.isEmpty()) {
                                            chatMessageList.clear();
                                            chatAdapter.notifyDataSetChanged();

                                            total_messages = 0;
                                        }*/

                                        Log.e("@TotalPages_routine",total_pages+"");


                                        JSONArray jsonChatArray = jsonObject.getJSONArray("chat_detail");

                                        for (int i = 0; i < jsonChatArray.length(); i++) {

                                            ChatMessageData data = new ChatMessageData();

                                            JSONObject object = (JSONObject) jsonChatArray.get(i);

                                            String time = object.getString("created_date");

                                            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                            Date date = null;
                                            try {
                                                date = dFormat.parse(time);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                            String timeString = dateFormat.format(date);

                                            data.setFromId(object.getString("from_id"));
                                            data.setFromName(object.getString("from_name"));
                                            data.setFromTime(timeString);
                                            data.setToId(object.getString("to_id"));
                                            data.setToName(object.getString("to_name"));
                                            data.setToTime(timeString);
                                            data.setMessageId(object.getString("id"));
                                            data.setChatMessage(object.getString("message"));

                                            tempChatMessageList.add(data);

                                        }


                                        if(finalI == total_pages){
                                            if(!SEMAFOR_Send_Message){
                                                /*if (tempChatMessageList.size() == total_messages) {

                                                    if (!chatMessageList.isEmpty()) {
                                                        chatMessageList.clear();
                                                    }

                                                    chatMessageList.addAll(tempChatMessageList);

                                                    total_messages=chatMessageList.size();

                                                    tempChatMessageList.clear();

                                                    chatAdapter = new ChatAdapter(ActivityChat.this, chatMessageList);


                                                    if(flag){
                                                        flag=false;
                                                        ll_sendMessage.setEnabled(true);
                                                    }

                                                    llm.setStackFromEnd(true);
                                                    rv_chat.setLayoutManager(llm);
                                                    rv_chat.setAdapter(chatAdapter);
                                                }else{
                                                    Log.e("@@Rv","Remains Same");
                                                }*/

                                                if (!chatMessageList.isEmpty()) {
                                                    chatMessageList.clear();
                                                }

                                                chatMessageList.addAll(tempChatMessageList);

                                                total_messages=chatMessageList.size();

                                                tempChatMessageList.clear();

                                                chatAdapter = new ChatAdapter(ActivityChat.this, chatMessageList);


                                                if(flag){
                                                    flag=false;
                                                    ll_sendMessage.setEnabled(true);
                                                }

                                                llm.setStackFromEnd(true);
                                                rv_chat.setLayoutManager(llm);
                                                rv_chat.setAdapter(chatAdapter);
                                            }

                                        }

//                                        if(current_page!=total_pages){
//                                            current_page ++;
//                                        }

//                                    chatAdapter=new ChatAdapter(ActivityChat.this,chatMessageList);
//                                    llm.setStackFromEnd(true);
//                                    rv_chat.setLayoutManager(llm);
//                                    rv_chat.setAdapter(chatAdapter);

                                    } else {
//
//                                    if(chatMessageList.isEmpty()){
//                                        cc.showToast(jsonObject.getString("message"));
//                                    }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("forgot_pass Error", e.toString());
                                }
                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cc.showSnackbar(ll_main, getString(R.string.ws_error));
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("user_id", cc.loadPrefString("user_id"));
                        params.put("page", finalI1 + "");

                        if (!patient) {
                            params.put("target_user_id", target_user_id);
                        }

                        Log.i("@@@ GetChat", params.toString());

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("UserAuth", cc.loadPrefString("user_token"));
                        headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                        Log.i("request header", headers.toString());
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e("Register: result", "Response from server: " + result);
            SEMAFOR = false;
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
