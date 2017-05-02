package com.example.franklin.myclient.view.Change;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.animen.com.xiaoyutask.R;


public class ChangeXiaoYuActivity extends AppCompatActivity {

    private android.widget.RelativeLayout changexiaoyunumberback;
    private android.support.design.widget.TextInputEditText changebindidcard;
    private android.widget.Button changexiaoyu;
    private android.support.design.widget.TextInputEditText changebindname;
    private RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_xiao_yu);
        initView();
        initEvent();
    }
    private void initView(){
        this.changebindname = (TextInputEditText) findViewById(R.id.change_bind_name);
        this.changexiaoyu = (Button) findViewById(R.id.change_xiao_yu);
        this.changebindidcard = (TextInputEditText) findViewById(R.id.change_bind_idcard);
        this.changexiaoyunumberback = (RelativeLayout) findViewById(R.id.change_xiao_yu_number_back);
        back = (RelativeLayout) findViewById(R.id.change_xiao_yu_number_back);
    }
    private void initEvent(){
        changexiaoyunumberback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changebindname.setCursorVisible(false);
        changebindname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changebindname.setCursorVisible(true);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
