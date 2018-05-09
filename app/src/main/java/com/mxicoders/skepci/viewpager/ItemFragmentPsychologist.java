package com.mxicoders.skepci.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ActivityMenuPsychologist;
import com.mxicoders.skepci.activity.ActivityPatientList;
import com.mxicoders.skepci.activity.ArticlesActivity;
import com.mxicoders.skepci.activity.QuestionariesActivity;

/**
 * Created by mxi on 27/11/17.
 */

public class ItemFragmentPsychologist extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";

    private int screenWidth;
    private int screenHeight;

    private int[] imageArray = new int[]{R.drawable.article_caro, R.drawable.patientlist_coro,
            R.drawable.ques_coro};

    public static Fragment newInstance(Context context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ItemFragmentPsychologist.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);

        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);

        //  textView.setText("Carousel item: " + postion);
        imageView.setLayoutParams(layoutParams);

        Glide.with(this)
                .load(imageArray[postion])
                .into(imageView);


        //imageView.setImageResource(imageArray[postion]);

        //handling click event
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (postion == 0){

                    Intent article = new Intent(getActivity(), ArticlesActivity.class);
                    startActivity(article);

                }else if (postion == 1){

                    Intent plist = new Intent(getActivity(), ActivityPatientList.class);
                    startActivity(plist);

                }else if (postion == 2){

                    Intent Ques = new Intent(getActivity(), QuestionariesActivity.class);
                    startActivity(Ques);

                }else {

                    Log.e("Nothing","...");
                }
            }
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
}

