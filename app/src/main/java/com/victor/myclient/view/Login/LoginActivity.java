package com.victor.myclient.view.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.view.MainActivity;
import com.victor.myclient.SomeUtils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private de.hdodenhof.circleimageview.CircleImageView loginhead;
    private android.widget.EditText loginid;
    private android.widget.EditText loginpassword;
    private android.widget.Button loginforgetpsw;
    private android.widget.Button loginsignup;
    private Button loginbutton;
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
        this.loginbutton = (Button) findViewById(R.id.login_button);
        this.loginsignup = (Button) findViewById(R.id.login_sign_up);
        this.loginforgetpsw = (Button) findViewById(R.id.login_forget_psw);
        this.loginpassword = (EditText) findViewById(R.id.login_password);
        this.loginid = (EditText) findViewById(R.id.login_id);
        this.loginhead = (CircleImageView) findViewById(R.id.login_head);
        client = new OkHttpClient();
        loginid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginid.setCursorVisible(true);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginid.getText().toString();
                String password = loginpassword.getText().toString();

                GetResponseBYOKhttp(username,password);
            }
        });

        loginsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivityFirst.class);
                startActivity(intent);
            }
        });
        loginforgetpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(LoginActivity.this, ForgetPassword.class);
            }
        });

    }


    private void GetResponseBYOKhttp(final String username,final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("reqType", "login");
                builder.add("username", username);
                builder.add("password", password);
                RequestBody requestBody = builder.build();
                try {
                    //是http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?
                    Request request = new Request.Builder().url(URL).post(requestBody).build();
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new Exception("not available");
                    }

                    String op = response.body().string();

                    Log.e("response: second ",op);
                    if (op.equals("1")) {
                        Utils.putBooleanValue(LoginActivity.this, GlobalData.Login_status, true);
                        Utils.putValue(LoginActivity.this, GlobalData.NAME, username);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else if (op.equals("0")) {
                        handler.sendEmptyMessage(0);
                    } else if (op.equals("-1")) {
                        handler.sendEmptyMessage(-1);
                    } else if (op.equals("-2")) {
                        handler.sendEmptyMessage(-2);
                    } else if (op.equals("-3")) {
                        handler.sendEmptyMessage(-3);
                    }
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
        }).start();

    }
}
