package com.mxicoders.skepci.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.mxicoders.skepci.adapter.PatientArchievedAdapter;

/**
 * Created by mxicoders on 15/7/17.
 */

public class ItemTouchHelperCallbackPatientArchieved extends ItemTouchHelperExtension.Callback {

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
        PatientArchievedAdapter.ItemViewHolder holder = (PatientArchievedAdapter.ItemViewHolder) viewHolder;
        if (viewHolder instanceof PatientArchievedAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof PatientArchievedAdapter.ItemViewHolder)
            holder.mViewContent.setTranslationX(dX);
    }
}
