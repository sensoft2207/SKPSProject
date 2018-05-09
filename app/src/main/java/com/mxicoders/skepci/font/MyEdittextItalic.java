package com.mxicoders.skepci.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyEdittextItalic extends EditText {

    public MyEdittextItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEdittextItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEdittextItalic(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/OPENSANS-ITALIC.TTF");
        setTypeface(tf, 1);

    }

}
