package com.example.franklin.myclient.view.Case;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.DoctorActivity;

import demo.animen.com.xiaoyutask.R;


public class CaseDetailActivity extends AppCompatActivity {

    private RelativeLayout back;
    private LinearLayout doctor_part;
    private Button see_doctor_tail;

    private String doctorId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_detail);
        doctorId = getIntent().getStringExtra("doctorid");
        initView();
        initEvent();
    }
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back_case_detail);
        doctor_part = (LinearLayout) findViewById(R.id.doctor_part_case_detail);
        see_doctor_tail = (Button) doctor_part.findViewById(R.id.doctor_part_button);

    }
    private void initEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(CaseDetailActivity.this);
            }
        });
        see_doctor_tail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(CaseDetailActivity.this,DoctorActivity.class);
            }
        });
    }
}
