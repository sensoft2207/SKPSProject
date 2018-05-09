package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.NotesPatientMenuAdapter;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ConstantNotes;
import com.mxicoders.skepci.utils.ItemModel;
import com.mxicoders.skepci.utils.ItemTouchHelperCallbackNotesPatient;
import com.mxicoders.skepci.utils.ItemTouchHelperExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by mxicoders on 21/7/17.
 */

public class NotesPatientMenuActivity extends AppCompatActivity {

    CommanClass cc;


    public static List<ItemModel> note_list;

    int imageUrl[] = ConstantNotes.image;
    String names[] = ConstantNotes.name;
    NotesPatientMenuAdapter adapter;
    TextView tvInviteClick;

    ProgressDialog pDialog;

    EditText edEmail;

    String emailUser;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public static RecyclerView recyclerView;

    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

    String title = "";

    public NotesPatientMenuActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_patientlist);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        ButterKnife.bind(this);

        /*List<ItemModel> list = getList();
        adapter = new NotesPatientMenuAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCallback = new ItemTouchHelperCallbackNotesPatient();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);*/

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setBackgroundResource(R.drawable.archived_icon);
        imgToolTwo.setBackgroundResource(R.drawable.archived_icon_two);
        tooName.setText(getString(R.string.notes));

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imgToolOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent alist = new Intent(NotesPatientMenuActivity.this, NotesArchivedActivity.class);
                cc.savePrefString("from_patient_note","fpn");
                startActivity(alist);
                finish();
            }
        });

        imgToolTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent plist = new Intent(NotesPatientMenuActivity.this, CreateNoteActivity.class);
                startActivity(plist);
                finish();
            }
        });

    }

    public void getNoteList() {

        pDialog = new ProgressDialog(NotesPatientMenuActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.NOTE_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);

                        note_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("note_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    Log.e("noteID", String.valueOf(jsonObject1.getInt("id")));

                                    ItemModel model = new ItemModel();

                                    model.setName(jsonObject1.getString("title"));
                                    model.setBody(jsonObject1.getString("body"));
                                    model.setNoteid(jsonObject1.getString("id"));

                                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        date = inFormat.parse(jsonObject1.getString("created_date"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
                                    SimpleDateFormat outFormat_date = new SimpleDateFormat("dd");
                                    String d_day = outFormat.format(date);
                                    String d_date= outFormat_date.format(date);

                                    Log.i("notedate",d_day);
                                    Log.i("notedate",d_date);

                                    model.setD_date(d_date);
                                    model.setD_day(d_day);

                                    model.setName(model.getName());

                                    note_list.add(model);

                                }

                                if(adapter != null){
                                    adapter = null;
                                }


                                adapter = new NotesPatientMenuAdapter(NotesPatientMenuActivity.this, note_list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(NotesPatientMenuActivity.this));

                                mCallback = new ItemTouchHelperCallbackNotesPatient();
                                mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                                mItemTouchHelper.attachToRecyclerView(recyclerView);
                                recyclerView.setAdapter(adapter);

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
                AndyUtils.showToast(NotesPatientMenuActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
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

    /*private List<ItemModel> getList() {
        List<ItemModel> list = new ArrayList<>();
        for (int i = 0; i < imageUrl.length; i++) {
            ItemModel model = new ItemModel();
            model.setName(names[i]);
            model.setImagePath(imageUrl[i]);
            list.add(model);
        }
        return list;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_trash) {

            Intent pa = new Intent(NotesPatientMenuActivity.this,PatientArchievedActivity.class);
            startActivity(pa);

            return true;

        }else if (id == R.id.action_invite){



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(NotesPatientMenuActivity.this,getString(R.string.no_internet));
        }
        else {

            getNoteList();
        }



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
