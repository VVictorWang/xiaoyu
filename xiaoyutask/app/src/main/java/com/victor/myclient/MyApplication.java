package com.victor.myclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import org.litepal.LitePal;
import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.Settings;
import com.victor.myclient.xiaoyu.IncomingCallService;

/**
 * Created by victor on 2017/4/26.
 */

public class MyApplication extends Application{
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);

        NemoSDK nemoSDK = NemoSDK.getInstance();
        nemoSDK.init(this, settings);
        Intent intent = new Intent(this, IncomingCallService.class);
        startService(intent);
    }


    public static Context getContext() {
        return context;

    }
}
