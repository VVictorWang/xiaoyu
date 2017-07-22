package com.victor.myclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.victor.myclient.utils.GlobalData;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by Silver on 2017/7/15.
 */

public class ImageDetailActivity extends Activity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_detail);

        Intent intent=getIntent();
        imageView=(ImageView)findViewById(R.id.imageView);
        Glide.with(this).
                load(intent.getStringExtra("url")).
                fitCenter().
                error(R.drawable.image_load_error).
                into(imageView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
