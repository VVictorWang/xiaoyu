package com.example.franklin.myclient.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.Case.CaseDetailActivity;

import demo.animen.com.xiaoyutask.R;


public class DoctorActivity extends AppCompatActivity {

    private RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_info);
        back = (RelativeLayout) findViewById(R.id.back_doctor_detail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(DoctorActivity.this);
            }
        });
    }
}
