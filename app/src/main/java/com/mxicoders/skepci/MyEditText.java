package com.mxicoders.skepci;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by mxicoders on 30/8/17.
 */

public class MyEditText extends EditText {

    private int mPreviousCursorPosition;

    public MyEditText(Context context) {
        super(context);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        CharSequence text = getText();
        if (text != null) {
            if (selStart != selEnd) {
                setSelection(mPreviousCursorPosition, mPreviousCursorPosition);
                return;
            }
        }
        mPreviousCursorPosition = selStart;
        super.onSelectionChanged(selStart, selEnd);
    }
}
