package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.MyApplication;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;


public class ForgetPwdActivity extends BaseActivity {

    private android.widget.RelativeLayout forgetpasswordback;
    private Button forget;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected Activity getActivity() {
        return ForgetPwdActivity.this;
    }

    @Override
    protected void initView() {
        this.forgetpasswordback = (RelativeLayout) findViewById(R.id.forget_password_back);
        forget = (Button) findViewById(R.id.forget_password_button);
        name = (EditText) findViewById(R.id.input_number);
    }

    @Override
    public void onBackPressed() {
        ActivityManage.finishActivity(ForgetPwdActivity.this);
        super.onBackPressed();
    }

    private void initEvent() {
        forgetpasswordback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.finishActivity(ForgetPwdActivity.this);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                if (username.equals("")) {
                    name.setError("用户名不能为空");
                } else
                    sendEmail();
            }
        });
    }

    private void sendEmail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String json = Utils.sendRequest
                        ("http://139.196.40.97/OSAdmin-master/uploads/interface" +
                                "/send_password_mail.php?id=" + PrefUtils
                                .getValue(MyApplication.getContext(), GlobalData.PATIENTFAMILY_ID));
                Log.d("ok", json);
                if (json.contains("been sent")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showShortToast(getActivity(), "邮件发送成功");
                        }
                    });

                    ActivityManage.finishActivity(getActivity());
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.showShortToast(getActivity(), "邮件发送失败");
                        }
                    });
                }
            }
        }).start();
    }
}
