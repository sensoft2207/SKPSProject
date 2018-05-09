package com.mxicoders.skepci.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by sonali on 3/3/17.
 */
public class MyEditTextRegular extends EditText {

    public MyEditTextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditTextRegular(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/OPENSANS-REGULAR.TTF");
        setTypeface(tf, 1);

    }

}