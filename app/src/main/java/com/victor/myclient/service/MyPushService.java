package com.victor.myclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igexin.sdk.GTServiceManager;
import com.igexin.sdk.PushManager;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import okhttp3.internal.Util;

/**
 * Created by Silver on 2017/7/11.
 */

public class MyPushService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        GTServiceManager.getInstance().onCreate(this);

        // com.getui.demo.DemoIntentService 为第三⽅⾃定义的推送服务事件接收类
    }

    @Override
    public void onDestroy() {
        GTServiceManager.getInstance().onDestroy();

        super.onDestroy();

    }

    @Nullable
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance().onBind(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        GTServiceManager.getInstance().onLowMemory();
    }
}
