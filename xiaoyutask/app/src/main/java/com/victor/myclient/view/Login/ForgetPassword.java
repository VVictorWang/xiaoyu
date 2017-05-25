package com.victor.myclient.view.Login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.Utils;

import demo.animen.com.xiaoyutask.R;
import okhttp3.OkHttpClient;


public class ForgetPassword extends AppCompatActivity {

    private android.widget.RelativeLayout forgetpasswordback;
    private Button forget;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message;

            switch (msg.what) {
                case 1:
                    message = "参数错误";
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
                    message = "错误";
                    break;
            }
            Utils.showShortToast(ForgetPassword.this, message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        InitViiew();
        InitEvent();
    }

    private void InitViiew() {
        this.forgetpasswordback = (RelativeLayout) findViewById(R.id.forget_password_back);
        forget = (Button) findViewById(R.id.forget_password_button);
    }

    private void InitEvent() {
        forgetpasswordback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(ForgetPassword.this);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmail();
            }
        });
    }

    private void SendEmail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reply = Utils.sendRequest(GlobalData.FORGET_PASSWOD + Utils.getValue(ForgetPassword.this, GlobalData.User_ID));
                    if (reply.contains("Error")) {
                        handler.sendEmptyMessage(1);
                    } else if (reply.contains("Found")) {
                        handler.sendEmptyMessage(2);
                    } else if (reply.contains("been sent")) {
                        handler.sendEmptyMessage(3);
                    } else if (reply.contains("could not")) {
                        handler.sendEmptyMessage(4);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
