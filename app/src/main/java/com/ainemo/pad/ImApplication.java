package com.ainemo.pad;

import android.app.Application;
import android.content.Intent;

import com.ainemo.sdk.otf.NemoSDK;
import com.ainemo.sdk.otf.Settings;

public class ImApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Settings settings = new Settings("995ffeda2b164757fdd923d5211d5fd45f761303"); //请使用自己的企业i，否则无法运行demo
        NemoSDK nemoSDK = NemoSDK.getInstance();
        nemoSDK.init(this, settings);
        Intent intent = new Intent(this, IncomingCallService.class);
        startService(intent);
    }
}

