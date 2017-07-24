package com.victor.myclient.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.loader.ImageLoader;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/28.
 */
public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width,
                             int height) {
        Glide.with(activity)
                .load(path)
                .placeholder(R.color.white)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

}