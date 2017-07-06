package com.victor.myclient.activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.activity.MainActivity;

import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private android.widget.EditText loginid;
    private android.widget.EditText loginpassword;
    private OkHttpClient client;
    private Response response;
    private String URL = "http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "登录成功";
                    break;
                case 0:
                    message = "参数错误";
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
                    message = "错误";
                    break;
            }
            Utils.showShortToast(LoginActivity.this, message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityManage.getInstance().pushActivity(LoginActivity.this);
        this.loginpassword = (EditText) findViewById(R.id.login_password);
        this.loginid = (EditText) findViewById(R.id.login_id);
        client = new OkHttpClient();
        loginid.setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.login_sign_up).setOnClickListener(this);
        findViewById(R.id.login_forget_psw).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                getResponse(loginid.getText().toString(), loginpassword.getText().toString());
                break;
            case R.id.login_sign_up:
                Utils.startActivity(LoginActivity.this, RegisterActivityFirst.class);
                break;
            case R.id.login_forget_psw:
                Utils.startActivity(LoginActivity.this, ForgetPassword.class);
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
                            Utils.putBooleanValue(LoginActivity.this, GlobalData.Login_status, true);
                            Utils.putValue(LoginActivity.this, GlobalData.NAME, username);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
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
