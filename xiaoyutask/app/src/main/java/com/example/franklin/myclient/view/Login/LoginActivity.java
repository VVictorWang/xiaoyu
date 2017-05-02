package com.example.franklin.myclient.view.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.view.MainActivity;
import com.example.franklin.myclient.SomeUtils.Utils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
//                loginid.setSelection(5);
                loginid.setCursorVisible(true);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginid.getText().toString();
                String password = loginpassword.getText().toString();

//                GetResponse(username,password);

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

    }

    private void GetResponse(final String username,final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder().url("http://139.196.40.97/OSAdmin-master/uploads/interface/reglogin.php?reqType"
                            +"=login&username="+username+"&password="+password).build();
                    Response response = client.newCall(request).execute();
                    Log.e("response:first ", response.body().string());
                } catch (Exception op) {
                    op.printStackTrace();
                }

            }
        }).start();
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
                    }
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
        }).start();

    }
}
