package com.mxicoders.skepci.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mxicoders.skepci.adapter.PatientChatListAdapter;
import com.mxicoders.skepci.adapter.RecyclerAdapter;

/**
 * Created by mxicoders on 11/8/17.
 */

public class ItemTouchHelperCallbackChat extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        PatientChatListAdapter.ItemViewHolder holder = (PatientChatListAdapter.ItemViewHolder) viewHolder;
        if (viewHolder instanceof PatientChatListAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof PatientChatListAdapter.ItemViewHolder)
            holder.mViewContent.setTranslationX(dX);
    }
}

