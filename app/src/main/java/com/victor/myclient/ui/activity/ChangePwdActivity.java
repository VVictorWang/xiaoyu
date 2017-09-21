package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ChangePwdActivity extends BaseActivity {


    private RelativeLayout changexiaoyunumberback;
    private TextInputEditText changepasswordnew;
    private TextInputEditText changepasswordnewconfirm;
    private TextInputEditText changepasswordold;
    private Button changepasswordfinish;
    private TextInputEditText changepasswordusername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_xiao_yu;
    }

    @Override
    protected Activity getActivity() {
        return ChangePwdActivity.this;
    }

    @Override
    protected void initView() {
        this.changepasswordusername = (TextInputEditText) findViewById(R.id
                .change_password_username);
        this.changepasswordfinish = (Button) findViewById(R.id.change_password_finish);
        this.changepasswordold = (TextInputEditText) findViewById(R.id.change_password_old);
        this.changepasswordnewconfirm = (TextInputEditText) findViewById(R.id
                .change_password_new_confirm);
        this.changepasswordnew = (TextInputEditText) findViewById(R.id.change_password_new);
        this.changexiaoyunumberback = (RelativeLayout) findViewById(R.id
                .change_xiao_yu_number_back);
    }

    private void initEvent() {
        changexiaoyunumberback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.finishActivity(ChangePwdActivity.this);
            }
        });

        changepasswordfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = changepasswordusername.getText().toString();
                String password_old = changepasswordold.getText().toString();
                String password_new = changepasswordnew.getText().toString();
                String password_confirm = changepasswordnewconfirm.getText().toString();
                if (username.equals("")) {
                    changepasswordusername.setError("请输入用户名");
                } else if (password_old.equals("")) {
                    changepasswordold.setError("请输入原密码");
                } else if (password_new.equals("")) {
                    changepasswordnew.setError("请输入新密码");
                } else if (password_confirm.equals("")) {
                    changepasswordnewconfirm.setError("请再次输入新密码");
                } else if (!password_confirm.equals(password_new)) {
                    changepasswordnewconfirm.setError("两次输入密码不一致");
                } else {
                    changePassword(username, password_old, password_new, password_confirm);
                }
            }
        });

    }

    private void changePassword(final String username, final String password_old, final String
            password_new, final String password_confirm) {
        Observable<Integer> observable = UserApi.getInstance().changePwd(username, password_old,
                password_new, password_confirm);
        observable.observeOn(AndroidSchedulers.mainThread())
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
                        String message;
                        switch (integer) {
                            case 1:
                                message = "修改成功";
                                ActivityManage.startActivity(ChangePwdActivity.this, LoginActivity
                                        .class);
                                break;
                            case -1:
                                message = "用户名长度过长";
                                break;
                            case -2:
                                message = "用户名不存在";
                                break;
                            case -3:
                                message = "密码不正确";
                                break;
                            case -4:
                                message = "两次密码不一致";
                                break;
                            default:
                                message = "用户名或密码错误";
                                break;
                        }
                        Utils.showShortToast(ChangePwdActivity.this, message);
                    }
                });
    }
}
