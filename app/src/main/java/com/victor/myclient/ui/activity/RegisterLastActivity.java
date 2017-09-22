package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.UserInformation;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class RegisterLastActivity extends BaseActivity {


    private UserInformation information;
    private TextInputEditText registername;
    private TextInputEditText registeremail;
    private TextInputEditText registeridcard;
    private Button thirdtolastregister;
    private RadioButton selectman;
    private RadioButton selectwoman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("new_user");
        information = (UserInformation) bundle.getSerializable("user");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup_setdata;
    }

    @Override
    protected Activity getActivity() {
        return RegisterLastActivity.this;
    }

    @Override
    protected void initView() {
        this.selectwoman = (RadioButton) findViewById(R.id.select_woman);
        this.selectman = (RadioButton) findViewById(R.id.select_man);
        this.thirdtolastregister = (Button) findViewById(R.id.third_to_last_register);
        this.registeridcard = (TextInputEditText) findViewById(R.id.register_id_card);
        this.registeremail = (TextInputEditText) findViewById(R.id.register_email);
        this.registername = (TextInputEditText) findViewById(R.id.register_name);
    }

    private void initEvent() {
        registername.setCursorVisible(false);
        registername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registername.setCursorVisible(true);
            }
        });
        thirdtolastregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registername.getText().toString();
                String sex = (selectman.isChecked()) ? "男" : "女";
                String email = registeremail.getText().toString();
                String id_card = registeridcard.getText().toString();
                if (name.isEmpty()) {
                    registername.setError("姓名不能为空");
                } else if ((!(selectman.isChecked())) && (!selectwoman.isChecked())) {
                    Utils.showShortToast(getActivity(), "请选择您的性别");
                } else if (email.isEmpty()) {
                    registeremail.setError("邮箱不能为空");
                } else if (!Utils.isEmail(email)) {
                    registeremail.setError("邮箱格式不正确");
                } else {
                    if (id_card.isEmpty()) {
                        registeridcard.setError("身份证号不能为空");
                    } else {
                        information.setEmail(email);
                        information.setSex(sex);
                        information.setUsername(name);
                        information.setId_card(id_card);
                        register(information);
                    }
                }
            }
        });
    }

    private void register(final UserInformation information) {
        Observable<Integer> observable = UserApi.getInstance().register(information.getUsername()
                , information.getId_card(), information.getPassword(), information.getPassword(),
                information.getEmail(), information.getPhone_number(), information.getSex());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        String message;
                        switch (integer) {
                            case 1:
                                message = "注册成功";
                                PrefUtils.putValue(RegisterLastActivity.this, GlobalData.NAME,
                                        information.getUsername());
                                MyActivityManager.startActivity(RegisterLastActivity.this, MainActivity
                                        .class);
                                PrefUtils.putBooleanValue(RegisterLastActivity.this, GlobalData
                                        .Login_status, true);
                                MyActivityManager.getInstance().popAllActivity();
                                break;
                            case -1:
                                message = "身份证长度过长";
                                break;
                            case -2:
                                message = "身份证号不存在";
                                break;
                            case -3:
                                message = "用户名长度过长";
                                break;
                            case -4:
                                message = "用户名已注册";
                                break;
                            case -5:
                                message = "两次密码不一致";
                                break;
                            case -7:
                                message = "电话格式不正确";
                                break;
                            case -8:
                                message = "邮箱格式不正确";
                                break;
                            case -9:
                                message = "性别错误";
                                break;
                            default:
                                message = "输入信息错误";
                                break;
                        }
                        Utils.showShortToast(getActivity(), message);
                    }
                });
    }
}