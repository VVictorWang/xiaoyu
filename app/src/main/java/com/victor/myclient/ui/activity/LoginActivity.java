package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "登录成功";
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
            Utils.showShortToast(LoginActivity.this, message);
        }
    };
    private android.widget.EditText loginid;
    private android.widget.EditText loginpassword;
    private OkHttpClient client;
    private Response response;
    private String URL = "http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManage.getInstance().popAllActivity();
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
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
                if (usrname.equals("")) {
                    loginid.setError("用户名不能为空");
                } else if (pwd.equals("")) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("reqType", "login");
                builder.add("username", username);
                builder.add("password", password);
                RequestBody requestBody = builder.build();
                try {
                    Request request = new Request.Builder().url(URL).post(requestBody).build();
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new Exception("not available");
                    }
                    String reply = response.body().string();
                    switch (reply) {
                        case "1":

                            PrefUtils.putBooleanValue(LoginActivity.this, GlobalData.Login_status,
                                    true);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            if (Utils.isMobileNO(username)) {
                                PrefUtils.putValue(LoginActivity.this, GlobalData.Phone, username);
                                intent.putExtra("type", "phone");
                            } else {
                                PrefUtils.putValue(LoginActivity.this, GlobalData.NAME, username);
                                intent.putExtra("type", "username");
                            }
                            startActivity(intent);
                            ActivityManage.getInstance().popAllActivity();
                            break;
                        case "0":
                            handler.sendEmptyMessage(0);
                            break;
                        case "-1":
                            handler.sendEmptyMessage(-1);
                            break;
                        case "-2":
                            handler.sendEmptyMessage(-2);
                            break;
                        case "-3":
                            handler.sendEmptyMessage(-3);
                            break;
                        default:
                            break;
                    }
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
        }).start();

    }
}
