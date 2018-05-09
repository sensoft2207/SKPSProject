package com.mxicoders.skepci.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyButtonItalic  extends Button {

    public MyButtonItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonItalic(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/OPENSANS-ITALIC.TTF");
        setTypeface(tf, 1);

    }

}
