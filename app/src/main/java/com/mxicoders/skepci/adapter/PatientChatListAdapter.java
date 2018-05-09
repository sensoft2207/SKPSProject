package com.mxicoders.skepci.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.Extension;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityChat;
import com.mxicoders.skepci.model.PatientListData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModelTwo;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aksahy on 10/8/17.
 */

public class PatientChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PatientListData> patientListDatas;
    private Context context;
    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;

    String patientID;

    CommanClass cc;

    Dialog dialog;

    public PatientChatListAdapter(Context context, ArrayList<PatientListData> patientListDatas) {

        cc = new CommanClass(context);

        this.patientListDatas = patientListDatas;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return patientListDatas.size();
    }


    /*pending......................................*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_chat_recyclerview_item_main, viewGroup, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH)
            return new ItemSwipeWithActionWidthViewHolder(view);

        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        PatientListData patientListData = patientListDatas.get(position);
        initializeViews(patientListData, holder, position);



       /* if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });
        } else */if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewRefresh.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog = new Dialog(context);
                            dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.patient_list_delete_dialog);


                            TextView tv_info = (TextView)dialog.findViewById(R.id.tv_info);
                            TextView tv_yes = (TextView)dialog.findViewById(R.id.tv_yes_dialog);
                            TextView tv_no = (TextView)dialog.findViewById(R.id.tv_no_dialog);

                            tv_info.setText(context.getString(R.string.delete_chat));

                            tv_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getPatientId(holder.getAdapterPosition());
                                }
                            });


                            tv_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });


                            dialog.show();
                        }
                    }
            );
            viewHolder.mActionViewMove.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.e("Move","Clicked");
                        }
                    }

            );
        }

    }

    public void getPatientId(int adapterPosition) {

        PatientListData modelTwo = patientListDatas.get(adapterPosition);

        deleteChat(modelTwo.getId(),adapterPosition);

    }

    private void deleteChat(final String id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_CHAT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:deletechat", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                patientListDatas.remove(adapterPosition);
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
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id", id + "");

                Log.e("request delete chat", params.toString());
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


    private void initializeViews(PatientListData model, final RecyclerView.ViewHolder holder, int position) {
/*

        int imageUrl = model.getImagePath();

        Glide.with(context)
                .load(imageUrl)
                .into(((ItemViewHolder)holder).imageView);
*/


        ((ItemViewHolder)holder).name.setText(model.getName()+" "+model.getLast_name_initial());

        ((ItemViewHolder)holder).description.setText(model.getMessage());

        ((ItemViewHolder)holder).notification_counter.setText(model.getUnread_msg());
    }

    /*@Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }

        return ITEM_TYPE_ACTION_WIDTH;
    }*/


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.notification_counter)
        TextView notification_counter;
//        @BindView(R.id.imageView)
//        ImageView imageView;

        @BindView(R.id.view_list_repo_action_container)
        public
        View mActionContainer;

        @BindView(R.id.view_list_main_content)
        public
        View mViewContent;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ActivityChat.class);
            PatientListData modelData= patientListDatas.get(getAdapterPosition());
            intent.putExtra("target_user_id",modelData.getId());
            context.startActivity(intent);
        }

    }


    class ItemViewHolderWithRecyclerWidth extends ItemViewHolder {

        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewMove;

        @BindView(R.id.view_list_repo_action_update)
        View mActionViewRefresh;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ItemViewHolder implements Extension {

        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewMove;
        @BindView(R.id.view_list_repo_action_update)
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