package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.CheckUtils;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private android.widget.EditText loginid;
    private android.widget.EditText loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManage.getInstance().popAllActivity();
        super.onCreate(savedInstanceState);
        loginid.setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.login_sign_up).setOnClickListener(this);
        findViewById(R.id.login_forget_psw).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected Activity getActivity() {
        return LoginActivity.this;
    }

    @Override
    protected void initView() {
        this.loginpassword = (EditText) findViewById(R.id.login_password);
        this.loginid = (EditText) findViewById(R.id.login_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String usrname = loginid.getText().toString();
                String pwd = loginpassword.getText().toString();
                if (CheckUtils.isEmpty(usrname)) {
                    loginid.setError("用户名不能为空");
                } else if (CheckUtils.isEmpty(pwd)) {
                    loginpassword.setError("密码不能为空");
                } else {
                    getResponse(usrname, pwd);
                }
                break;
            case R.id.login_sign_up:
                ActivityManage.startActivity(LoginActivity.this, RegisterFirstActivity.class);
                break;
            case R.id.login_forget_psw:
                ActivityManage.startActivity(LoginActivity.this, ForgetPwdActivity.class);
                break;
            case R.id.login_id:
                loginid.setCursorVisible(true);
                break;
            default:
                break;
        }
    }

    private void getResponse(final String username, final String password) {

        Observable<Integer> observable = UserApi.getInstance().login(username, password);
        Subscription subscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        String message = "";
                        switch (integer) {
                            case 1:
                                PrefUtils.putBooleanValue(LoginActivity.this, GlobalData
                                                .Login_status,
                                        true);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                if (Utils.isMobileNO(username)) {
                                    PrefUtils.putValue(LoginActivity.this, GlobalData.Phone,
                                            username);
                                    intent.putExtra("type", "phone");
                                } else {
                                    PrefUtils.putValue(LoginActivity.this, GlobalData.NAME,
                                            username);
                                    intent.putExtra("type", "username");
                                }
                                startActivity(intent);
                                ActivityManage.getInstance().popAllActivity();
                                break;
                            case 0:
                                message = "用户名或者密码不正确";
                                break;
                            case -1:
                                message = "用户名长度过大";
                                break;
                            case -2:
                                message = "用户名不存在";
                                break;
                            case -3:
                                message = "密码错误";
                                break;
                            default:
                                message = "用户名或者密码不正确";
                                break;
                        }
                        if (!CheckUtils.isEmpty(message)) {
                            Utils.showShortToast(getActivity(), message);
                        }
                    }
                });

    }
}
