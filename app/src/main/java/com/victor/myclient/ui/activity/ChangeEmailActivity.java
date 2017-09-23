package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.victor.myclient.api.UserApi;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.CheckUtils;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ChangeEmailActivity extends BaseActivity {


    private RelativeLayout changeemailbackback;
    private EditText bindemail;
    private EditText bindname;
    private EditText password;
    private Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_bind_email;
    }

    @Override
    protected Activity getActivity() {
        return ChangeEmailActivity.this;
    }

    @Override
    protected void initView() {
        this.finish = (Button) findViewById(R.id.finish_change_email);
        this.password = (EditText) findViewById(R.id.change_email_password);
        this.bindname = (EditText) findViewById(R.id.bind_name);
        this.bindemail = (EditText) findViewById(R.id.bind_email);
        this.changeemailbackback = (RelativeLayout) findViewById(R.id.change_email_back_back);
    }

    private void initEvent() {
        changeemailbackback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(getActivity());
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bindemail.getText().toString();
                String password_text = password.getText().toString();
                String user_name = bindname.getText().toString();
                if (CheckUtils.isEmpty(email)) {
                    bindemail.setError("绑定邮箱不能为空");
                } else if (CheckUtils.isEmpty(password_text)) {
                    password.setError("密码不能为空");
                } else if (CheckUtils.isEmpty(user_name)) {
                    bindname.setError("用户名不能为空");
                } else if (!Utils.isEmail(email)) {
                    bindemail.setError("请输入正确格式的邮箱");
                } else {
                    getResponse(email, password_text, user_name);
                }
            }
        });

    }

    private void getResponse(final String email, final String password, final String name) {
        Observable<Integer> observable = UserApi.getInstance().changeEmail(name, email, password);
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
                        String message = "";
                        switch (integer) {
                            case 1:
                                message = "修改成功";
                                PrefUtils.putValue(ChangeEmailActivity.this, GlobalData.USer_email,
                                        email);
                                break;
                            case 0:
                                message = "输入项不能为空";
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
                                message = "邮箱格式不正确";
                                break;
                            default:
                                message = "用户名或密码错误";
                                break;
                        }
                        Utils.showShortToast(ChangeEmailActivity.this, message);
                    }
                });


    }
}
