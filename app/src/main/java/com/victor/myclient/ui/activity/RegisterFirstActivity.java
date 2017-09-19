package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.UserInformation;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;


public class RegisterFirstActivity extends BaseActivity {


    private TextInputEditText enterpassword;
    private TextInputEditText enterpasswordagain;
    private Button firsttosecondregister;
    private TextInputEditText enterphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup_verifcode;
    }

    @Override
    protected Activity getActivity() {
        return RegisterFirstActivity.this;
    }

    @Override
    protected void initView() {
        this.enterphonenumber = (TextInputEditText) findViewById(R.id.enter_phone_number);
        this.firsttosecondregister = (Button) findViewById(R.id.first_to_second_register);
        this.enterpasswordagain = (TextInputEditText) findViewById(R.id.enter_password_again);
        this.enterpassword = (TextInputEditText) findViewById(R.id.enter_password);


    }

    @Override
    public void onBackPressed() {
        ActivityManage.finishActivity(RegisterFirstActivity.this);
        super.onBackPressed();
    }

    private void initEvent() {
        enterphonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterphonenumber.setCursorVisible(true);
            }
        });
        enterpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpassword.setCursorVisible(true);
            }
        });
        enterpasswordagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterpasswordagain.setCursorVisible(true);
            }
        });
        firsttosecondregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = enterphonenumber.getText().toString();
                String password = enterpassword.getText().toString();
                String pawword_again = enterpasswordagain.getText().toString();
                if (phone_number.isEmpty()) {
                    enterphonenumber.setError("手机号不能为空");
                } else if (!Utils.isMobileNO(phone_number)) {
                    enterphonenumber.setError("请输入正确格式的手机号");
                } else {
                    if (password.isEmpty()) {
                        enterpassword.setError("密码不能为空");
                    } else if (!password.equals(pawword_again)) {
                        enterpasswordagain.setError("两次输入密码不一致");
                    } else {

                        Intent intent = new Intent(RegisterFirstActivity.this,
                                RegisterLastActivity.class);
                        Bundle bundle = new Bundle();
                        UserInformation information = new UserInformation();
                        information.setPassword(password);
                        information.setPhone_number(phone_number);
                        bundle.putSerializable("user", information);
                        intent.putExtra("new_user", bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
