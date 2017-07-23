package com.victor.myclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

/**
 * Created by Silver on 2017/7/22.
 */

public class PostClientIdService extends Service {
    private String clientId;
    private String patientId;
    Handler handler=new Handler();
    private static final String TAG = "PostClientIdService";
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(clientId!=null&patientId!=null) {
                Log.d(TAG, "run: clientid shangchuanchenggong"+clientId+"patientid"+patientId);
                Utils.sendRequest(GlobalData.POST_CLIENTID + patientId + "&" + GlobalData.CLIENT_ID + clientId);
            }else{
                clientId= Utils.getValue(getBaseContext(),GlobalData.CLIENT_ID
                );
                patientId=Utils.getValue(getBaseContext(),GlobalData.PATIENT_ID);
                if(clientId!=null&patientId!=null) {
                    Utils.sendRequest(GlobalData.POST_CLIENTID + patientId + "&" + GlobalData.CLIENT_ID + clientId);
                }
            }
            handler.postDelayed(this, 5000);//10s
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable,10000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
