package com.victor.myclient.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.activity.login.LoginActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/30.
 */
/*
* 判断是否已登陆来决定进入登陆界面还是主界面
* */
public class FirstActivity extends AppCompatActivity {
    private boolean isLogin;  //是否是登录状态
    private int runcount; //运行次数
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            if (isLogin) {
                intent.setClass(FirstActivity.this, MainActivity.class);
                //运行15次后重新输入密码登录
                if (runcount >= 15) {
                    isLogin = false;
                    Utils.putBooleanValue(FirstActivity.this, GlobalData.Login_status, isLogin);
                }
            } else {
                intent.setClass(FirstActivity.this, LoginActivity.class);
            }
            Utils.startActivity(FirstActivity.this, intent);
            Utils.finishActivity(FirstActivity.this);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ActivityManage.getInstance().pushActivity(FirstActivity.this);
        initData();
        if (isLogin) {
            getLogin();
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private void initData(){
        runcount = Utils.getIntValue(this, "RUN_COUNT");
        Utils.putIntValue(this, "RUN_COUNT", runcount++);
        isLogin = Utils.getBooleanValue(this, GlobalData.Login_status);
    }
    private void getLogin() {
        handler.sendEmptyMessageDelayed(0, 600);
    }
}
