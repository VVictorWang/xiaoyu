package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChangePwdActivity extends BaseActivity {


    private RelativeLayout changexiaoyunumberback;
    private TextInputEditText changepasswordnew;
    private TextInputEditText changepasswordnewconfirm;
    private TextInputEditText changepasswordold;
    private Button changepasswordfinish;
    private TextInputEditText changepasswordusername;
    private OkHttpClient client;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "修改密码成功";
                    break;
                case 2:
                    message = "参数错误";
                    break;
                case 3:
                    message = "用户名长度过长";
                    break;
                case 4:
                    message = "用户名不存在";
                    break;
                case 5:
                    message = "密码不正确";
                    break;
                case 6:
                    message = "两次密码不一致";
                    break;
                case 7:
                    message = "内部错误";
                    break;
                default:
                    message = "错误";
            }
            Utils.showShortToast(ChangePwdActivity.this, message);
        }
    };

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
        client = new OkHttpClient();
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
                    ChangePassword(username, password_old, password_new, password_confirm);
                }
            }
        });

    }

    private void ChangePassword(final String username, final String password_old, final String
            password_new, final String password_confirm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("reqType", "chgpassword");
                builder.add("username", username);
                builder.add("password_old", password_old);
                builder.add("password_new", password_new);
                builder.add("password_cnfm", password_confirm);
                try {
                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder().url(GlobalData.CHANGE_PASSWORD).post
                            (requestBody).build();
                    Response response = client.newCall(request).execute();
                    String op = response.body().string();
                    switch (op) {
                        case "1":
                            handler.sendEmptyMessage(1);
                            ActivityManage.startActivity(ChangePwdActivity.this, LoginActivity
                                    .class);
                            break;
                        case "2":
                            handler.sendEmptyMessage(2);
                            break;
                        case "-1":
                            handler.sendEmptyMessage(3);
                            break;
                        case "-2":
                            handler.sendEmptyMessage(4);
                            break;
                        case "-3":
                            handler.sendEmptyMessage(5);
                            break;
                        case "-4":
                            handler.sendEmptyMessage(6);
                            break;
                        case "-5":
                            handler.sendEmptyMessage(7);
                            break;
                        default:
                            break;
                    }

                } catch (Exception po) {
                    po.printStackTrace();

                }
            }
        }).start();
    }
}
