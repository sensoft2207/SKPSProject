package com.mxicoders.skepci.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxicoders.skepci.Extension;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityPatientList;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModelTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mxicoders.skepci.activity.ActivityPatientList.adapter;

/**
 * Created by mxicoders on 28/7/17.
 */

public class PatientStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<ItemModelTwo> itemModels;
    private Context context;
    CommanClass cc;

    ActivityPatientList pss;

    Dialog dialog;

    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;

    public PatientStatusAdapter(Context context, List<ItemModelTwo> wallTalls) {

        cc = new CommanClass(context);
        this.itemModels = wallTalls;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }


    public interface PlayPauseClick {
        void imageButtonOnClick(View v, int position);
    }

    private PlayPauseClick callback;

    public void setPlayPauseClickListener(PlayPauseClick listener) {
        this.callback = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_status_item_main, viewGroup, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH)
            return new ItemSwipeWithActionWidthViewHolder(view);

        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ItemModelTwo model = itemModels.get(position);
        initializeViews(model, holder, position);

        if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    doDelete(holder.getAdapterPosition());
                    Log.e("@@Clicked", "First One");
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

                            tvInfo.setText(context.getString(R.string.dele_patient));

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


                            Log.e("@@Clicked", "delete One");
                        }
                    }
            );
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            Log.e("@@Clicked", "Second One");
                            doDelete(holder.getAdapterPosition());
                        }
                    }
            );
        }

    }

    public void getPatientId(int adapterPosition) {

        ItemModelTwo modelTwo = itemModels.get(adapterPosition);

        archivePatient(modelTwo.getId(),adapterPosition);

    }

    public void doDelete(int adapterPosition) {
        ItemModelTwo modelTwo = itemModels.get(adapterPosition);

        Log.e("@@doDelete", modelTwo.getName() + "");
        acceptRequest(modelTwo.getId(), adapterPosition);

    }

    private void archivePatient(final String id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_ARCHIEVE,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:archivepatient", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                itemModels.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(context.getString(R.string.delete_patient));

                               /* UpdatedData();*/
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
                params.put("archive", "Yes");

                Log.e("request archive patient", params.toString());
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
        return ITEM_TYPE_ACTION_WIDTH;
    }*/


    private void initializeViews(ItemModelTwo model, final RecyclerView.ViewHolder holder, int position) {

        int imageUrl = model.getImagePath();

        Glide.with(context)
                .load(imageUrl)
                .into(((ItemViewHolder) holder).imageView);


        ((ItemViewHolder) holder).name.setText(model.getName());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

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

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public class ItemViewHolderWithRecyclerWidth extends ItemViewHolder {

        @BindView(R.id.view_list_repo_action_delete)
        View mActionViewDelete;

        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewRefresh;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class ItemSwipeWithActionWidthViewHolder extends ItemViewHolder implements Extension {

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


    public void acceptRequest(final String patientId, final int pos) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.PATIENT_STATUS_CHANGE_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                itemModels.remove(pos);
                                notifyItemRemoved(pos);
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
// Reload current fragment
//                                cc.savePrefBoolean("fromPatientListUpdated",true);
//                                context.startActivity(new Intent(context, ActivityMenuPsychologist.class));
                                UpdatedData();


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
                params.put("patient_id", patientId + "");
                params.put("status", "Approved");

                Log.e("request profile data", params.toString());
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


    public void UpdatedData() {
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        progressDialog.show();

        final ProgressDialog finalProgressDialog = progressDialog;
        final ProgressDialog finalProgressDialog1 = progressDialog;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_PATIENT_LIST_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:profile data", response);
                        finalProgressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray dataArray = jsonObject.getJSONArray("patient_data");

                                ActivityPatientList.approved_listcontent.clear();
                                ActivityPatientList.pending_listcontent.clear();

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    Log.e("patientID", String.valueOf(jsonObject1.getInt("patient_id")));
                                    Log.e("patientStatus", jsonObject1.getString("status"));

                                    if (jsonObject1.getString("status").contains("Approved")) {

                                        ItemModelTwo model = new ItemModelTwo();

                                        model.setName(jsonObject1.getString("name"));
                                        model.setLname(jsonObject1.getString("last_name_initial"));
                                        model.setId(jsonObject1.getString("patient_id"));

                                        model.setName(model.getName() + " " + " " + model.getLname() + ".");
                                        ActivityPatientList.approved_listcontent.add(model);

                                    } else if (jsonObject1.getString("status").contains("Pending")) {
                                        ItemModelTwo model = new ItemModelTwo();

                                        model.setName(jsonObject1.getString("name"));
                                        model.setLname(jsonObject1.getString("last_name_initial"));
                                        model.setId(jsonObject1.getString("patient_id"));

                                        model.setName(model.getName() + " " + " " + model.getLname() + ".");
                                        ActivityPatientList.pending_listcontent.add(model);
                                    }
                                }

                                adapter.notifyDataSetChanged();

//                                RecyclerAdapter adapter = new RecyclerAdapter(context, approved_listcontent);
//                                ActivityPatientList.recyclerView = null;
//                                ActivityPatientList.recyclerView.setAdapter(adapter);
                               /* ActivityPatientList.recyclerView.notifyAll();

//                                PatientStatusAdapter adapter2 = new PatientStatusAdapter(context, pending_listcontent);
//                                ActivityPatientList.recyclerView2 = null;
                                ActivityPatientList.recyclerView2.notifyAll();*/

                            } else {
                                finalProgressDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                finalProgressDialog1.dismiss();
                cc.showToast(error.toString() + "");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                Log.e("request profile data", params.toString());
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

}
