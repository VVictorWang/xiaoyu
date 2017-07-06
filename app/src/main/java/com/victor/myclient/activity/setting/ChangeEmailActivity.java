package com.victor.myclient.activity.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class ChangeEmailActivity extends AppCompatActivity {


    private ImageView backtomainimagebackgroundchangeemali;
    private TextView backtomainimagebackgroundchangeemalitext;
    private RelativeLayout changeemailbackback;
    private EditText bindemail;
    private EditText bindname;
    private EditText password;
    private Button finish;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "修改成功";
                    break;
                case 0:
                    message = "参数错误，为空";
                    break;
                case -1:
                    message = "用户名长度过长";
                    break;
                case -2:
                    message = "用户名不存在";
                    break;
                case -3:
                    message = "密码不正确";
                    break;
                case -4:
                    message = "邮箱格式不正确";
                    break;
                case -5:
                    message = "内部错误";
                    break;
                default:
                    message = "错误";
                    break;
            }
            Utils.showShortToast(ChangeEmailActivity.this, message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bind_email);
        ActivityManage activityManage = ActivityManage.getInstance();
        activityManage.pushActivity(ChangeEmailActivity.this);
        initView();
        initEvent();
    }

    private void initView() {
        this.finish = (Button) findViewById(R.id.finish_change_email);
        this.password = (EditText) findViewById(R.id.change_email_password);
        this.bindname = (EditText) findViewById(R.id.bind_name);
        this.bindemail = (EditText) findViewById(R.id.bind_email);
        this.changeemailbackback = (RelativeLayout) findViewById(R.id.change_email_back_back);
        this.backtomainimagebackgroundchangeemalitext = (TextView) findViewById(R.id.back_to_main_image_background_change_emali_text);
        this.backtomainimagebackgroundchangeemali = (ImageView) findViewById(R.id.back_to_main_image_background_change_emali);
    }

    private void initEvent() {
        changeemailbackback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bindemail.getText().toString();
                String password_text = password.getText().toString();
                String user_name = bindname.getText().toString();
                if (email.isEmpty()) {
                    bindemail.setError("绑定邮箱不能为空");
                } else if (password_text.isEmpty()) {
                    password.setError("密码不能为空");
                } else if (user_name.isEmpty()) {
                    bindname.setError("用户名不能为空");
                } else {
                    GetResponse(email, password_text, user_name);
                }
            }
        });

    }

    private void GetResponse(final String email, final String password, final String name) {
        final OkHttpClient client = new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("reqType", "chgemail");
                builder.add("username", name);
                builder.add("email", email);
                builder.add("password", password);
                RequestBody body = builder.build();
                try {
                    Request request = new Request.Builder().url(GlobalData.CHANGE_EMAIL_PASSORT).post(body).build();
                    Response response = client.newCall(request).execute();
                    String response_text = response.body().string();
                    switch (response_text) {
                        case "1":
                            handler.sendEmptyMessage(1);
                            Utils.putValue(ChangeEmailActivity.this, GlobalData.USer_email,email);
                            break;
                        case "2":
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
                        case "-4":
                            handler.sendEmptyMessage(-4);
                            break;
                        case "-5":
                            handler.sendEmptyMessage(-5);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
