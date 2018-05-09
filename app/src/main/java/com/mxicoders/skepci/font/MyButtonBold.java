package com.mxicoders.skepci.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by mxicoders on 11/9/17.
 */

public class MyButtonBold extends Button {

    public MyButtonBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonBold(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/OPENSANS-BOLD.TTF");
        setTypeface(tf, 1);

    }

}
