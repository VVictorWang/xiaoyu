package com.victor.myclient;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.victor.myclient.view.Login.LoginActivity;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.MainActivity;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/30.
 */

public class First extends AppCompatActivity {
    private boolean isLogin;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            if (isLogin) {
                intent.setClass(First.this, MainActivity.class);
            } else {
                intent.setClass(First.this, LoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initData();
        if (isLogin) {

            getLogin();
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private void initData(){
        int Runcount = Utils.getIntValue(this, "RUN_COUNT");
        Utils.putIntValue(this, "RUN_COUNT", Runcount++);
        isLogin = Utils.getBooleanValue(this, GlobalData.Login_status);

    }
    private void getLogin() {
        handler.sendEmptyMessageDelayed(0, 600);
    }
}
