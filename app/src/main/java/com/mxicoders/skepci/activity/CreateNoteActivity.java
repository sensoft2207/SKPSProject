package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxicoders on 5/8/17.
 */

public class CreateNoteActivity extends AppCompatActivity {

    CommanClass cc;

    EditText edNoteTitle,edNoteBody;

    TextView tvNoteSubmit;

    String noteTitle,noteBody;

    ProgressDialog pDialog;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public CreateNoteActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.create_note_psychologist);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(this);

        edNoteTitle = (EditText)findViewById(R.id.ed_note_title);
        edNoteBody = (EditText)findViewById(R.id.ed_note_body);

        tvNoteSubmit = (TextView)findViewById(R.id.tv_notes_submit);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText(getString(R.string.create_note));
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        tvNoteSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noteValidation();
            }
        });
    }



    private void noteValidation() {

        noteTitle = edNoteTitle.getText().toString().trim();
        noteBody = edNoteBody.getText().toString().trim();


        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(CreateNoteActivity.this,getString(R.string.no_internet));
        } else if (noteTitle.equals("")) {
            AndyUtils.showToast(CreateNoteActivity.this,getString(R.string.article_title));
        } else if (noteBody.equals("")) {
            AndyUtils.showToast(CreateNoteActivity.this,getString(R.string.note_body));
        }
        else {

            createNote(noteTitle,noteBody);
        }


    }

    private void createNote(final String noteTitle, final String noteBody) {

        pDialog = new ProgressDialog(CreateNoteActivity.this);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CREATE_NOTE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:createnote", response);

                        jsonParseNoteCreate(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(CreateNoteActivity.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id",cc.loadPrefString("user_id"));
                params.put("title", noteTitle);
                params.put("body", noteBody);

                Log.i("request create note", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put(Const.Params.Authorization, Const.Params.Authorization_value);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseNoteCreate(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                clearField();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("article Error",e.toString());
        }
    }

    private void clearField() {

        edNoteTitle.setText("");
        edNoteBody.setText("");
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
