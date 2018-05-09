package com.mxicoders.skepci.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by sonali on 3/3/17.
 */
public class MyButtonRegular extends Button {

    public MyButtonRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyButtonRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButtonRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/OPENSANS-REGULAR.TTF");
        setTypeface(tf, 1);

    }

}