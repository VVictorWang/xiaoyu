package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/30.
 */
/*
* 判断是否已登陆来决定进入登陆界面还是主界面
* */
public class FirstActivity extends BaseActivity {
    private int second = 2000;
    private boolean isLogin;  //是否是登录状态
    private int runcount; //运行次数
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Intent intent = new Intent();
            if (isLogin) {
                intent.setClass(FirstActivity.this, MainActivity.class);
                //运行15次后重新输入密码登录
                if (runcount >= 15) {
                    isLogin = false;
                    PrefUtils.putBooleanValue(FirstActivity.this, GlobalData.Login_status, isLogin);
                }
            } else {
                intent.setClass(FirstActivity.this, LoginActivity.class);
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MyActivityManager.startActivity(FirstActivity.this, intent);
                    MyActivityManager.finishActivity(FirstActivity.this);
                }

            }, second);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_first;
    }

    @Override
    protected Activity getActivity() {
        return FirstActivity.this;
    }

    @Override
    protected void initView() {

    }

    private void initData() {
        runcount = PrefUtils.getIntValue(this, "RUN_COUNT");
        PrefUtils.putIntValue(this, "RUN_COUNT", runcount++);
        isLogin = PrefUtils.getBooleanValue(this, GlobalData.Login_status);
        if (runcount == 1) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(FirstActivity.this, WelcomeActivity.class);
                    intent.putExtra("login", isLogin);
                    MyActivityManager.startActivity(FirstActivity.this, intent);
                }

            }, second);

        } else {
            if (isLogin) {
                getLogin();
            } else {
                handler.sendEmptyMessage(0);
            }
        }
    }

    private void getLogin() {
        handler.sendEmptyMessageDelayed(0, 600);
    }
}
