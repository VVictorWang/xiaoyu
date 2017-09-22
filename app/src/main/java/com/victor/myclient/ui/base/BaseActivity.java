package com.victor.myclient.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.victor.myclient.utils.MyActivityManager;

/**
 * Created by victor on 9/19/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        MyActivityManager.getInstance().pushActivity(getActivity());
        initView();
    }

    abstract protected int getLayoutId();

    abstract protected Activity getActivity();

    abstract protected void initView();
}
