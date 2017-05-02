package com.example.franklin.myclient.xiaoyu;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.ainemo.sdk.otf.NemoReceivedCallListener;
import com.ainemo.sdk.otf.NemoSDK;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class IncomingCallService extends Service{

  @Override
  public IBinder onBind(Intent intent){
    return null;
  }

  @Override
  public void onCreate(){
    super.onCreate();
    NemoSDK.getInstance().setNemoReceivedCallListener(new NemoReceivedCallListener() {
      @Override
      public void onReceivedCall(String s, String s1, int i) {
        Log.i(TAG, "onReceivedCall: name is "+s+" number is "+s1+" call index is "+i);
        Intent inComingCall=new Intent(IncomingCallService.this, BusinessActivity.class);
        inComingCall.putExtra("callerName",s);
        inComingCall.putExtra("callerNumber",s1);
        inComingCall.putExtra("callIndex",i);
        inComingCall.putExtra("isIncomingCall",true);
        inComingCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(inComingCall);
      }
    });
  }
}
