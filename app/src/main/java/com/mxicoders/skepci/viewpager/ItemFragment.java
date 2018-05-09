package com.mxicoders.skepci.viewpager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityMenuPatient;
import com.mxicoders.skepci.activity.NotesPatientMenuActivity;
import com.mxicoders.skepci.activity.PatientArticleMenuFragment;
import com.mxicoders.skepci.activity.TaskHisPatientMenuActivity;
import com.mxicoders.skepci.activity.TodoPatientMenuActivity;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mxicoders.skepci.fragment.FragmentDrawerPatientMenu.mDrawerLayout;

public class ItemFragment extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";

    private int screenWidth;
    private int screenHeight;

    Dialog dialog;
    EditText  et_history_pass;
    ProgressDialog pDialog3;

    CommanClass cc;

    private int[] imageArray = new int[]{R.drawable.article_caro, R.drawable.notes_coro,
            R.drawable.todolist_coro,R.drawable.history_coro};

    public static Fragment newInstance(Context context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        cc = new CommanClass(getActivity());

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);

        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);

      //  textView.setText("Carousel item: " + postion);
        imageView.setLayoutParams(layoutParams);


        Glide.with(this)
                .load(imageArray[postion])
                .into(imageView);

        //imageView.setImageResource(imageArray[postion]);

        //handling click event
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postion == 0){

                    Intent article = new Intent(getActivity(), PatientArticleMenuFragment.class);
                    startActivity(article);

                }else if (postion == 1){

                    Intent note = new Intent(getActivity(),NotesPatientMenuActivity.class);
                    startActivity(note);

                }else if (postion == 2){

                    Intent todo = new Intent(getActivity(),TodoPatientMenuActivity.class);
                    startActivity(todo);

                }else if (postion == 3){

                    passProtectedDialog();

                }else {

                    Log.e("Nothing","...");
                }


                /*Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra(DRAWABLE_RESOURE, imageArray[postion]);
                startActivity(intent);*/
            }
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

    private void passProtectedDialog() {

        dialog = new Dialog(getActivity());

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

            AndyUtils.showToast(getActivity(),getString(R.string.no_internet));

        } else if (pass.equals("")) {

            AndyUtils.showToast(getActivity(),getString(R.string.enter_password));
        } else {

            makeJsonPass(pass);

        }
    }

    private void makeJsonPass(final String pass) {

        pDialog3 = new ProgressDialog(getActivity());
        pDialog3.setMessage("Please wait...");
        pDialog3.setIndeterminate(false);
        pDialog3.setCancelable(false);
        pDialog3.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.TASK_HISTORY_PROTECTION,
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
                AndyUtils.showToast(getActivity(),getString(R.string.ws_error));
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

                Intent t_history = new Intent(getActivity(), TaskHisPatientMenuActivity.class);
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
}
