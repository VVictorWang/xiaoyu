package com.victor.myclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.victor.myclient.MyApplication;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

/**
 * Created by Silver on 2017/7/22.
 */

public class PostClientIdService extends Service {
    private String clientId;
    private String userId;
    Handler mHandler;
    HandlerThread mHandlerThread;

    private static final String TAG = "PostClientIdService";

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
                        if (clientId != null && userId != null&&Utils.isNetWorkAvailabe(MyApplication.getContext())) {
                            String temp = Utils.sendRequest(GlobalData.POST_CLIENTID + userId +
                                    "&" + GlobalData.CLIENT_ID +"="+ clientId);

                            stopSelf();
                        } else {
                            clientId = Utils.getValue(getBaseContext(), GlobalData.CLIENT_ID
                            );
                            userId = Utils.getValue(getBaseContext(), GlobalData.User_ID);
                            if (clientId != null && userId != null&&Utils.isNetWorkAvailabe(MyApplication.getContext())) {
                                String temp = Utils.sendRequest(GlobalData.POST_CLIENTID + userId + "&" +
                                        GlobalData.CLIENT_ID  +"="+ clientId);
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
