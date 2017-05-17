package com.victor.myclient.view.Login;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import demo.animen.com.xiaoyutask.R;


public class RegisterActivityFirst extends AppCompatActivity {



    private TextInputEditText enterpassword;
    private TextInputEditText enterpasswordagain;
    private CheckBox checkBox;
    private Button firsttosecondregister;
    private TextInputEditText enterphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_verifcode);


        initView();
        initEvent();
    }
    private void  initView() {
        this.enterphonenumber = (TextInputEditText) findViewById(R.id.enter_phone_number);
        this.firsttosecondregister = (Button) findViewById(R.id.first_to_second_register);
        this.checkBox = (CheckBox) findViewById(R.id.checkBox);
        this.enterpasswordagain = (TextInputEditText) findViewById(R.id.enter_password_again);
        this.enterpassword = (TextInputEditText) findViewById(R.id.enter_password);


    }
    private void initEvent(){
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
                }
//                else if (!Utils.isMobileNO(phone_number)) {
//                    enterphonenumber.setError("请输入正确格式的手机号");
//                }
                else {
                    if (password.isEmpty()) {
                        enterpassword.setError("密码不能为空");
                    } else if (!password.equals(pawword_again)) {
                        enterpasswordagain.setError("两次输入密码不一致");
                    } else {

                        Intent intent = new Intent(RegisterActivityFirst.this, RegisterActivitySecond.class);
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
