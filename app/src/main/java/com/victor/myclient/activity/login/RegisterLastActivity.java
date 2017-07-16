package com.victor.myclient.activity.login;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.activity.MainActivity;
import com.victor.myclient.datas.UserInformation;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterLastActivity extends AppCompatActivity {



    private UserInformation information;
    private TextInputEditText registername;
    private TextInputEditText registeremail;
    private TextInputEditText registeridcard;
    private Button thirdtolastregister;
    private RadioButton selectman;
    private RadioButton selectwoman;

    private OkHttpClient client;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message;
            switch (msg.what) {
                case 1:
                    message = "注册成功";
                    break;
                case 2:
                    message = "参数错误 为空";
                    break;
                case 3:
                    message = "身份证长度过长";
                    break;
                case 4:
                    message = "身份证号不存在";
                    break;
                case 5:
                    message = "用户名长度过长";
                    break;
                case 6:
                    message = "用户名已注册";
                    break;
                case 7:
                    message = "两次密码不一致";
                    break;
                case 8:
                    message = "内部错误";
                    break;
                case 9:
                    message = "电话格式不正确";
                    break;
                case 10:
                    message = "邮箱格式不正确";
                    break;
                case 11:
                    message = "性别错误";
                    break;
                default:
                    message = "错误";
                    break;
            }
            Utils.showShortToast(RegisterLastActivity.this, message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_setdata);
        ActivityManage.getInstance().pushActivity(RegisterLastActivity.this);
        initView();
        initEvent();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("new_user");
        information = (UserInformation)bundle.getSerializable("user");
    }

    private void initView() {
        this.selectwoman = (RadioButton) findViewById(R.id.select_woman);
        this.selectman = (RadioButton) findViewById(R.id.select_man);
        this.thirdtolastregister = (Button) findViewById(R.id.third_to_last_register);
        this.registeridcard = (TextInputEditText) findViewById(R.id.register_id_card);
        this.registeremail = (TextInputEditText) findViewById(R.id.register_email);
        this.registername = (TextInputEditText) findViewById(R.id.register_name);
        client = new OkHttpClient();
    }
    private void initEvent(){
        registername.setCursorVisible(false);
        registername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registername.setCursorVisible(true);
            }
        });
        thirdtolastregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = registername.getText().toString();
                String sex = (selectman.isChecked()) ? "男" : "女";
                String email = registeremail.getText().toString();
                String id_card = registeridcard.getText().toString();
                if (name.isEmpty()) {
                    registername.setError("姓名不能为空");
                } else if ((!(selectman.isChecked())) && (!selectwoman.isChecked())) {
                    Toast.makeText(RegisterLastActivity.this, "请选择你的性别", Toast.LENGTH_SHORT);
                } else if (email.isEmpty()) {
                    registeremail.setError("邮箱不能为空");
                }else if (!Utils.isEmail(email)) {
                    registeremail.setError("邮箱格式不正确");
                }
                else {
                    if (id_card.isEmpty()) {
                        registeridcard.setError("身份证号不能为空");
                    } else {
                        information.setEmail(email);
                        information.setSex(sex);
                        information.setUsername(name);
                        information.setId_card(id_card);
                        getResponseByOkhttp(information);
                    }
                }
            }
        });
    }
    private void getResponseByOkhttp(final UserInformation information) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("reqType", "reg");
                builder.add("username", information.getUsername());
                builder.add("identityNum", information.getId_card());
                builder.add("password_cnfm", information.getPassword());
                builder.add("password", information.getPassword());
                builder.add("email", information.getEmail());
                builder.add("phone", information.getPhone_number());
                builder.add("sex", information.getSex());
                RequestBody requestBody = builder.build();
                try {
                    Request request = new Request.Builder().url(GlobalData.MAIN_ENGINE).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    String op = response.body().string();
                    Log.e("response, register:", op);
                    switch (op) {
                        case "1":
                            handler.sendEmptyMessage(1);
                            Utils.startActivity(RegisterLastActivity.this, MainActivity.class);
                            Utils.putBooleanValue(RegisterLastActivity.this, GlobalData.Login_status, true);
                            ActivityManage.getInstance().popAllActivity();
                            break;
                        case "0":
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
                        case "-6":
                            handler.sendEmptyMessage(8);
                            break;
                        case "-7":
                            handler.sendEmptyMessage(9);
                            break;
                        case "-8":
                            handler.sendEmptyMessage(10);
                            break;
                        case "-9":
                            handler.sendEmptyMessage(11);
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