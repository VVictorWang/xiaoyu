package com.victor.myclient.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.datas.UserInfor;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;


public class ForgetPwdActivity extends AppCompatActivity {

    private android.widget.RelativeLayout forgetpasswordback;
    private Button forget;
    private boolean network;
    private EditText name;
    private UserInfor mUserInfor;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "用户名错误";
                    break;
                case 2:
                    message = "用户不存在";
                    break;
                case 3:
                    message = "邮件已发送";
                    break;
                case 4:
                    message = "邮件发送失败";
                    break;
                default:
                    message = "用户名错误";
                    break;
            }
            Utils.showShortToast(ForgetPwdActivity.this, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ActivityManage.getInstance().pushActivity(ForgetPwdActivity.this);
        network = Utils.isNetWorkAvailabe(ForgetPwdActivity.this);
        InitViiew();
        InitEvent();
    }

    @Override
    public void onBackPressed() {
        Utils.finishActivity(ForgetPwdActivity.this);
        super.onBackPressed();
    }

    private void InitViiew() {
        this.forgetpasswordback = (RelativeLayout) findViewById(R.id.forget_password_back);
        forget = (Button) findViewById(R.id.forget_password_button);
        name = (EditText) findViewById(R.id.input_number);
    }

    private void InitEvent() {
        forgetpasswordback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(ForgetPwdActivity.this);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                if (username.equals("")) {
                    name.setError("用户名不能为空");
                } else
                    SendEmail(username);
            }
        });
    }

    private void SendEmail(final String user_name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (network) {
                        Gson gson = new Gson();
                        mUserInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_USR_INFOR +
                                "FamilyName=" + user_name), UserInfor.class);
                        String reply = Utils.sendRequest(GlobalData.FORGET_PASSWOD + mUserInfor
                                .getId());
                        if (reply.contains("Error")) {
                            handler.sendEmptyMessage(1);
                        } else if (reply.contains("Found")) {
                            handler.sendEmptyMessage(2);
                        } else if (reply.contains("been sent")) {
                            handler.sendEmptyMessage(3);
                        } else if (reply.contains("could not")) {
                            handler.sendEmptyMessage(4);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
