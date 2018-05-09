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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.Extension;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.EditNoteActivity;
import com.mxicoders.skepci.activity.NotesPatientMenuActivity;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;
import com.mxicoders.skepci.utils.ItemModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mxicoders on 21/7/17.
 */

public class NotesPatientMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    CommanClass cc;

    private List<ItemModel> itemModels;
    private Context context;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;

    Dialog dialog;

    String title = "";

    public NotesPatientMenuAdapter(Context context, List<ItemModel> wallTalls) {

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

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patientlist_notes_item_main, viewGroup, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH)
            return new ItemSwipeWithActionWidthViewHolder(view);

        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ItemModel model = itemModels.get(position);
        initializeViews(model, holder, position);

        if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    editNote(holder.getAdapterPosition());
                }
            });
        } if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog = new Dialog(context);
                            dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.patient_list_delete_dialog);
                            dialog.show();

                            TextView tvYes = (TextView)dialog.findViewById(R.id.tv_yes_dialog);
                            TextView tvNo = (TextView)dialog.findViewById(R.id.tv_no_dialog);
                            ImageView closee = (ImageView)dialog.findViewById(R.id.close);

                            closee.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });

                            tvYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getNoteId(holder.getAdapterPosition());
                                }
                            });

                            tvNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });
                        }
                    }

            );
            viewHolder.mActionViewEdit.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editNote(holder.getAdapterPosition());

                        }
                    }

            );
            viewHolder.mActionViewMove.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                                   }
                    }

            );
        }

    }

    public void getNoteId(int adapterPosition) {

        ItemModel modelTwo = itemModels.get(adapterPosition);

        cc.savePrefString("note_id_main",modelTwo.getNoteid());

        archieveNote(modelTwo.getNoteid(),adapterPosition);

    }

    public void editNote(int adapterPosition){

        ItemModel modelTwo = itemModels.get(adapterPosition);

        cc.savePrefString("note_id_main",modelTwo.getNoteid());
        cc.savePrefString("note_title",modelTwo.getName());
        cc.savePrefString("note_body",modelTwo.getBody());

        Intent edit = new Intent(context, EditNoteActivity.class);
        context.startActivity(edit);
    }

   /* @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return ITEM_TYPE_ACTION_WIDTH_NO_SPRING;
        }
        return ITEM_TYPE_ACTION_WIDTH;
    }*/



    private void initializeViews(ItemModel model, final RecyclerView.ViewHolder holder, int position) {

        int imageUrl = model.getImagePath();

       /* Glide.with(context)
                .load(imageUrl)
                .into(((ItemViewHolder)holder).imageView);*/


        ((ItemViewHolder)holder).name.setText(model.getName());
        ((ItemViewHolder)holder).date.setText(model.getD_day());
        ((ItemViewHolder)holder).day.setText(model.getD_date());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv_date_list)
        TextView day;
        @BindView(R.id.tv_day_list)
        TextView date;


        @BindView(R.id.ln_open)
        LinearLayout ln_open;


       /* @BindView(R.id.imageView)
        ImageView imageView;
*/
        @BindView(R.id.view_list_repo_action_container)
        public
        View mActionContainer;

        @BindView(R.id.view_list_main_content)
        public
        View mViewContent;

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

            /*Intent intent = new Intent(context, ActivityMenuPatientList.class);
            context.startActivity(intent);*/

        }
    }



    class ItemViewHolderWithRecyclerWidth extends ItemViewHolder {

        @BindView(R.id.view_list_repo_action_delete)
        View mActionViewEdit;
        @BindView(R.id.view_list_repo_action_update)
        View mActionViewDelete;
        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewMove;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends ItemViewHolder implements Extension {

        @BindView(R.id.view_list_repo_action_delete)
        View mActionViewEdit;
        @BindView(R.id.view_list_repo_action_update)
        View mActionViewDelete;
        @BindView(R.id.view_list_repo_action_upda)
        View mActionViewMove;

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

    private void archieveNote(final String noteid, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.please_wait));
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.ARCHIEVE_NOTE_BY_PSYCHOLOGIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:archieve note", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                itemModels.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

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
                AndyUtils.showToast(context,context.getString(R.string.no_internet));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("psycologist_user_id", cc.loadPrefString("user_id"));
                params.put("note_id", noteid + "");
                params.put("is_archive", "Yes");

                Log.e("request archieve note", params.toString());
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

    /*private void UpdatedData() {

        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        final ProgressDialog finalProgressDialog = progressDialog;
        final ProgressDialog finalProgressDialog1 = progressDialog;

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.NOTE_LIST,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:note data", response);
                        finalProgressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray dataArray = jsonObject.getJSONArray("note_list");

                                NotesPsychologistActivity.note_list.clear();

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    ItemModel model = new ItemModel();

                                    model.setName(jsonObject1.getString("title"));
                                    model.setBody(jsonObject1.getString("body"));
                                    model.setNoteid(jsonObject1.getString("id"));

                                    model.setName(model.getName());

                                    NotesPsychologistActivity.note_list.add(model);

                                }

                               *//* NotesPsychologistActivity.recyclerView.notify();*//*


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
    }*/
}