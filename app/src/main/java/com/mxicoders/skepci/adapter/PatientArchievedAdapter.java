package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxicoders.skepci.Extension;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityMenuPatientList;
import com.mxicoders.skepci.activity.EditProfilePatientPsychologistSide;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModel;
import com.mxicoders.skepci.utils.ItemModelTwo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mxicoders on 15/7/17.
 */

public class PatientArchievedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    CommanClass cc;

    private List<ItemModelTwo> itemModels;
    private Context context;
    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;

    Dialog dialog;

    public PatientArchievedAdapter(Context context, List<ItemModelTwo> wallTalls) {

        cc = new CommanClass(context);

        this.itemModels = wallTalls;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_archieved_item_main, viewGroup, false);
       /* if (viewType == ITEM_TYPE_ACTION_WIDTH)
            return new ItemSwipeWithActionWidthViewHolder(view);*/
       /* if (viewType == ITEM_TYPE_RECYCLER_WIDTH) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_archieved_single_delete_item, viewGroup, false);
            return new ItemViewHolderWithRecyclerWidth(view);
        }*/
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemModelTwo model = itemModels.get(position);
        initializeViews(model, holder, position);

        if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    doDelete(holder.getAdapterPosition());
                }
            });
        } else if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;

            viewHolder.mActionViewRefresh.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.patient_list_delete_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            DisplayMetrics metrics3 = context.getResources().getDisplayMetrics();
                            int width3 = metrics3.widthPixels;
                            int height3 = metrics3.heightPixels;
                            dialog.getWindow().setLayout((6 * width3) / 7, ActionBar.LayoutParams.WRAP_CONTENT);
                            dialog.show();

                            ImageView close_dialogg = (ImageView) dialog.findViewById(R.id.close);
                            TextView tvInfo = (TextView)dialog.findViewById(R.id.tv_info);
                            TextView closeBtn = (TextView)dialog.findViewById(R.id.tv_no_dialog);
                            TextView yesBtn = (TextView)dialog.findViewById(R.id.tv_yes_dialog);

                            tvInfo.setText(context.getString(R.string.unarchieve_patient));

                            close_dialogg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });

                            yesBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getPatientId(holder.getAdapterPosition());
                                }
                            });

                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });

                        }
                    }

            );
        }

    }


    public void getPatientId(int adapterPosition) {

        ItemModelTwo modelTwo = itemModels.get(adapterPosition);

        unarchivePatient(modelTwo.getId(),adapterPosition);

    }

    private void unarchivePatient(final String id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_UNARCHIEVE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:unarchivepatie", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                itemModels.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(context.getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", id + "");
                params.put("archive", "No");

                Log.e("request unarchive patie", params.toString());
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

    /*@Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }
       *//* if (position == 2) {
            return ITEM_TYPE_RECYCLER_WIDTH;
        }*//*
        return ITEM_TYPE_ACTION_WIDTH;
    }
*/


    private void initializeViews(ItemModelTwo model, final RecyclerView.ViewHolder holder, int position) {

        int imageUrl = model.getImagePath();

            Glide.with(context)
                    .load(imageUrl)
                    .into(((ItemViewHolder)holder).imageView);


        ((ItemViewHolder)holder).name.setText(model.getName());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.view_list_repo_action_container)
        public
        View mActionContainer;

        @BindView(R.id.view_list_main_content)
        public
        View mViewContent;

        @BindView(R.id.ln_open)
        LinearLayout ln_open;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ln_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cc.showToast("Deslize para a esquerda para abrir");

                    Log.e("LnOPEN","..................");
                }
            });


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (cc.loadPrefString("from_patient_note").equals("fpn")){

            }else {

                Intent intent = new Intent(context, ActivityMenuPatientList.class);
                getPosList(getAdapterPosition());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
    }

    public void getPosList(int adapterPosition) {

        ItemModelTwo modelTwo = itemModels.get(adapterPosition);

        cc.savePrefString("patient_id_main",modelTwo.getId());
        cc.savePrefString("p_namee",modelTwo.getName());
        cc.savePrefString("p_namee2",modelTwo.getName2());
        cc.savePrefString("p_namee_last",modelTwo.getLname());
        cc.savePrefString("p_dob",modelTwo.getP_birthdate());
        cc.savePrefString("p_email",modelTwo.getEmail());
        cc.savePrefString("p_city",modelTwo.getCity());
        cc.savePrefString("p_gender",modelTwo.getGender());

    }



    class ItemViewHolderWithRecyclerWidth extends ItemViewHolder {

        @BindView(R.id.view_list_repo_action_delete)
        View mActionViewDelete;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ItemViewHolder implements Extension {

        @BindView(R.id.view_list_repo_action_delete)
        View mActionViewDelete;
        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    public class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }
}