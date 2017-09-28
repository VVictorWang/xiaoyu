package com.victor.myclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

/**
 * Created by Silver on 2017/7/22.
 */

public class PostClientIdService extends Service {
    Handler mHandler;
    HandlerThread mHandlerThread;
    private String clientId;
    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        initBackThread();
    }

    @Override
    public void onDestroy() {
        mHandlerThread.quit();
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.sendEmptyMessageDelayed(0, 5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initBackThread() {
        mHandlerThread = new HandlerThread("posting");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                new Runnable() {
                    @Override
                    public void run() {
                        if (clientId != null & userId != null) {
                            String temp = Utils.sendRequest(GlobalData.POST_CLIENTID + userId +
                                    "&" + GlobalData.CLIENT_ID + clientId);
                            stopSelf();
                        } else {
                            clientId = PrefUtils.getValue(getBaseContext(), GlobalData.CLIENT_ID
                            );
                            userId = PrefUtils.getValue(getBaseContext(), GlobalData.User_ID);
                            if (clientId != null & userId != null) {
                                Utils.sendRequest(GlobalData.POST_CLIENTID + userId + "&" +
                                        GlobalData.CLIENT_ID + clientId);
                                stopSelf();
                            } else {
                                mHandler.sendEmptyMessageDelayed(0, 5000);
                            }
                        }

                    }
                }.run();
            }
        };
    }
}
