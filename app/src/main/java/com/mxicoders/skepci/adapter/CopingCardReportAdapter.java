package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.model.CopingData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxi on 6/12/17.
 */

public class CopingCardReportAdapter extends BaseAdapter {

    CommanClass cc;

    private Context context ;
    private List<CopingData> names;
    private static LayoutInflater inflater=null;

    Activity ac;

    ProgressDialog pDialog;

    Dialog dialog;

    public CopingCardReportAdapter (Context context,List<CopingData> names,Activity activity){

        cc = new CommanClass(context);
        this.names = names;
        this.context = context;
        this.ac = activity;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tvMessage;
        ImageView delete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.coping_card_item, null);


        final CopingData cd = names.get(position);

        holder.delete = (ImageView)rowView.findViewById(R.id.delete_card);

        holder.delete.setVisibility(View.GONE);

        holder.tvMessage = (TextView)rowView.findViewById(R.id.text_message);
        holder.tvMessage.setText(cd.getMessage());

       /* holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                DeleteCopingCard(cd.getId(),position);

                dialog = new Dialog(ac);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.patient_list_delete_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();

                TextView tvYes = (TextView)dialog.findViewById(R.id.tv_yes_dialog);
                TextView tvNo = (TextView)dialog.findViewById(R.id.tv_no_dialog);
                TextView tvInfo = (TextView)dialog.findViewById(R.id.tv_info);
                ImageView closee = (ImageView)dialog.findViewById(R.id.close);


                tvInfo.setText("Are you sure you want to Delete this card?");

                closee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DeleteCopingCard(cd.getId(),position);

                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });
            }
        });
*/
        return rowView;
    }

    private void DeleteCopingCard(final String id, final int adapterPosition) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_COPING_CARD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:delete card", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                names.remove(adapterPosition);

                                notifyDataSetChanged();
//                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                                dialog.dismiss();

                            }
                        } catch (JSONException e) {
//                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("card_id", id);

                Log.e("request delete card", params.toString());
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

}