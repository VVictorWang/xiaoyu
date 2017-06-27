package com.victor.myclient;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.victor.myclient.activity.Login.LoginActivity;
import com.victor.myclient.Utils.GlobalData;
import com.victor.myclient.Utils.Utils;
import com.victor.myclient.activity.MainActivity;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/30.
 */
/*
* 判断是否已登陆来决定进入登陆界面还是主界面
* */
public class First extends AppCompatActivity {
    private boolean isLogin;  //是否是登录状态
    private int runcount; //运行次数
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            if (isLogin) {
                intent.setClass(First.this, MainActivity.class);
                //运行15次后重新输入密码登录
                if (runcount >= 15) {
                    isLogin = false;
                    Utils.putBooleanValue(First.this, GlobalData.Login_status, isLogin);
                }
            } else {
                intent.setClass(First.this, LoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            Utils.finishActivity(First.this);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ActivityManage.getInstance().pushActivity(First.this);
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
