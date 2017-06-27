package com.victor.myclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.Settings;
import com.victor.myclient.service.IncomingCallService;

import org.litepal.LitePal;

/**
 * Created by victor on 2017/4/26.
 */

public class MyApplication extends Application{
    private static Context context;
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
      Settings settings = new Settings(""); //请使用自己的企业i，否则无法运行demo
        NemoSDK nemoSDK = NemoSDK.getInstance();
        nemoSDK.init(this, settings);
        Intent intent = new Intent(this, IncomingCallService.class);
        startService(intent);
    }
    public static Context getContext() {
        return context;

    }
}
