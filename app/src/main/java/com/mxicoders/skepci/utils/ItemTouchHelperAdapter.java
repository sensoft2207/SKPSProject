package com.mxicoders.skepci.utils;

/**
 * Created by mxi on 9/10/17.
 */

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
