package com.example.franklin.myclient.view.Case;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.franklin.myclient.DataBase.CaseListDataBase;
import com.example.franklin.myclient.SomeUtils.Utils;

import demo.animen.com.xiaoyutask.R;


public class CaseDetailActivity extends AppCompatActivity {

    private RelativeLayout back;
    private LinearLayout doctor_part;
    private LinearLayout person_part;
    private Button see_doctor_tail;
    private TextView doctor_name;
    private TextView date;
    private TextView name;
    private TextView sex_age,ill_problem,ill_detail,ill_result;

    private CaseListDataBase dataBase;
    private int id;
    private String doctor_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_detail);
        id = getIntent().getIntExtra("id", 1);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back_case_detail);
        doctor_part = (LinearLayout) findViewById(R.id.doctor_part_case_detail);
        person_part = (LinearLayout) findViewById(R.id.case_detail_person_part);
        see_doctor_tail = (Button) doctor_part.findViewById(R.id.doctor_part_button);
        doctor_name = (TextView) doctor_part.findViewById(R.id.doctor_part_name);
        date = (TextView) doctor_part.findViewById(R.id.doctor_part_time);
        name = (TextView) person_part.findViewById(R.id.person_detail_name);
        sex_age = (TextView) person_part.findViewById(R.id.person_detail_info);
        ill_problem = (TextView) person_part.findViewById(R.id.person_detail_reason);
        ill_result = (TextView) findViewById(R.id.case_detail_result);
        dataBase = new CaseListDataBase(CaseDetailActivity.this);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(CaseDetailActivity.this);
            }
        });
        see_doctor_tail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseDetailActivity.this, DoctorActivity.class);
                intent.putExtra("doctor_id", doctor_id);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Cursor co = dataBase.getAllItems();

        co.moveToFirst();
        do {
            int m = co.getInt(co.getColumnIndex(CaseListDataBase.DB_COLUMN_ID));
            if (m == id) {
                break;
            }

        } while (co.moveToNext());

        doctor_name.setText("主治医生  " + co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_DOCTOR_NAME)));
        date.setText(co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_CREATE_TIME)));
        name.setText(co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_PATIENTEN_NAME)));
        ill_problem.setText("病情: " + co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_ILL_PROBLEM)));
        ill_result.setText(co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_ILL_RESULT)));
        doctor_id = co.getString(co.getColumnIndex(CaseListDataBase.DB_COLUMN_DOCTOR_ID));
        Log.e("id: ", "" + id);
    }
}
