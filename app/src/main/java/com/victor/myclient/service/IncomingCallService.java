package com.victor.myclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ainemo.sdk.otf.NemoReceivedCallListener;
import com.ainemo.sdk.otf.NemoSDK;
import com.victor.myclient.ui.activity.IncommingAcivity;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class IncomingCallService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NemoSDK.getInstance().setNemoReceivedCallListener(new NemoReceivedCallListener() {
            @Override
            public void onReceivedCall(String s, String s1, int i) {
                Intent inComingCall = new Intent(IncomingCallService.this, IncommingAcivity.class);
                inComingCall.putExtra("callerName", s);
                inComingCall.putExtra("callerNumber", s1);
                inComingCall.putExtra("callIndex", i);
                inComingCall.putExtra("isIncomingCall", true);
                inComingCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inComingCall);
            }
        });
    }
}
