package com.example.franklin.myclient.view.Setting.Change;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.animen.com.xiaoyutask.R;


public class ChangeEmailActivity extends AppCompatActivity {

    private android.widget.ImageView backtomainimagebackgroundchangeemali;
    private android.widget.TextView backtomainimagebackgroundchangeemalitext;
    private RelativeLayout changeemailbackback;
    private android.widget.EditText bindname;
    private android.widget.Button signupbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bind_email);
     initView();
        initEvent();
    }
    private void initView(){
        this.signupbutton = (Button) findViewById(R.id.sign_up_button);
        this.bindname = (EditText) findViewById(R.id.bind_name);
        this.changeemailbackback = (RelativeLayout) findViewById(R.id.change_email_back_back);
    }
    private void initEvent(){
        changeemailbackback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
