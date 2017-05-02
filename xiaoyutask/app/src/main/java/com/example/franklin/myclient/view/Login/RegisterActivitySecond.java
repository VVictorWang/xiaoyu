package com.example.franklin.myclient.view.Login;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivitySecond extends AppCompatActivity {



    private UserInformation information;
    private CircleImageView loginhead;
    private TextInputEditText registername;
    private TextInputEditText registeremail;
    private TextInputEditText registeridcard;
    private Button thirdtolastregister;
    private RadioButton selectman;
    private RadioButton selectwoman;

    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_setdata);

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
        this.loginhead = (CircleImageView) findViewById(R.id.login_head);
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
                    Toast.makeText(RegisterActivitySecond.this, "请选择你的性别", Toast.LENGTH_SHORT);
                } else if (email.isEmpty()) {
                    registeremail.setError("邮箱不能为空");
                } else if (Utils.isEmail(email)) {
                    registeremail.setError("请输入正确的邮箱格式");
                } else {
                    if (id_card.isEmpty()) {
                        registeridcard.setError("身份证号不能为空");
                    } else {
                        information.setEmail(email);
                        information.setSex(sex);
                        information.setUsername(name);
                        information.setId_card(id_card);
                        GetResponseBYOKhttp(information);
                    }
                }
            }
        });
    }
    private void GetResponseBYOKhttp(final UserInformation information) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("usrname:  ", information.getUsername());
                Log.e("password:  ", information.getPassword());
                Log.e("identyNum: ", information.getId_card());
                Log.e("email: ", information.getEmail());
                Log.e("phone: ", information.getPhone_number());
                Log.e("sex: ", information.getSex());
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
                    //是http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?
                    Request request = new Request.Builder().url(GlobalData.MAIN_ENGINE).post(requestBody).build();
                    Response response = client.newCall(request).execute();
                    Log.e("response: second ", response.body().string());
                    String op = response.body().string();
                    Log.e("response, register:", op);
                    if (op.equals("1")) {
                        Utils.showShortToast(RegisterActivitySecond.this, "注册成");
                        Utils.startActivity(RegisterActivitySecond.this, MainActivity.class);
                    }
                } catch (Exception p) {
                    p.printStackTrace();
                }
            }
        }).start();

    }
}