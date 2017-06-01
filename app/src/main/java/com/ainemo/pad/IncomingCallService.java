package com.ainemo.pad;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ainemo.sdk.otf.NemoReceivedCallListener;
import com.ainemo.sdk.otf.NemoSDK;

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
                Log.i("receive call", "name is " + s + " number is " + s1 + " call index is " + " i");
                Intent inComingCall = new Intent(IncomingCallService.this, BusinessActivity.class);
                inComingCall.putExtra("callerName", s);
                inComingCall.putExtra("callerNumber", s1);
                inComingCall.putExtra("callIndex", i);
                inComingCall.putExtra("isIncomingCall", true);
                inComingCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inComingCall);
            }
        });
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
