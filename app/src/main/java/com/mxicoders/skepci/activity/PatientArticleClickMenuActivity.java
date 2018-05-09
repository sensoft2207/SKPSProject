package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.utils.AppController;

/**
 * Created by mxicoders on 20/7/17.
 */

public class PatientArticleClickMenuActivity extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    CommanClass cc;

    String articleID,articleTitle,articleBody,articlePhoto;

    TextView tvArticleViewTitle,tvArticleViewBody;

    NetworkImageView image;

    ProgressDialog pDialog;


    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    public PatientArticleClickMenuActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_article_click_patient);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(PatientArticleClickMenuActivity.this);


        articleID = cc.loadPrefString("article_patient_id");
        articleTitle = cc.loadPrefString("article_patient_title");
        articleBody = cc.loadPrefString("article_patient_body");
        articlePhoto = cc.loadPrefString("article_patient_image");

        tvArticleViewTitle = (TextView)findViewById(R.id.tv_article_view_title);
        tvArticleViewBody = (TextView)findViewById(R.id.tv_article_view_body);
        image = (NetworkImageView)findViewById(R.id.Image);

        tvArticleViewTitle.setText(articleTitle);
        tvArticleViewBody.setText(articleBody);
        image.setImageUrl(articlePhoto, imageLoader);

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        tooName.setText(getString(R.string.title_coupens));
        tooName.setPadding(75, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cc.loadPrefBoolean("isInactiveDevice") || cc.loadPrefBoolean("isDialogOpen") ){


            Log.e("IsLoginDevice","Psychologist...If");

            cc.savePrefBoolean("isInactiveDevice",false);

        }else {

            if (cc.loadPrefBoolean("Inactive")) {

                cc.savePrefBoolean("Inactive",false);

                cc.savePrefBoolean("isDialogOpen",true);

                cc.showInactiveDialog();
            }


            Log.e("IsLoginDevice","Psychologist...Else");
        }
    }
}
