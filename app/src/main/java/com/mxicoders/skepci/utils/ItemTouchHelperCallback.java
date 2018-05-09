package com.mxicoders.skepci.utils;

/**
 * Created by kuliza-195 on 11/30/16.
 */

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.mxicoders.skepci.adapter.RecyclerAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {




    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        /*Log.e("DirectionRecycler", String.valueOf(direction));

        return makeMovementFlags(0, ItemTouchHelper.START);*/

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        Log.e("DirectionRecycler", String.valueOf(direction));

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        RecyclerAdapter.ItemViewHolder holder = (RecyclerAdapter.ItemViewHolder) viewHolder;
        if (viewHolder instanceof RecyclerAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX = -holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof RecyclerAdapter.ItemViewHolder)
            holder.mViewContent.setTranslationX(dX);
    }
}
