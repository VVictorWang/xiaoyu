package com.victor.myclient.view.Case;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.Datas.CaseInfor;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.MyBitmapUtils;
import com.victor.myclient.SomeUtils.Utils;

import org.litepal.crud.DataSupport;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


public class CaseDetailActivity extends AppCompatActivity {

    private RelativeLayout back;
    private LinearLayout doctor_part;
    private LinearLayout person_part;
    private Button see_doctor_tail;
    private TextView doctor_name;
    private TextView date;
    private TextView name;
    private TextView sex_age, ill_problem, ill_detail, ill_result;
    private CircleImageView imageView;
    private Bitmap bitmap;

    private int id;
    private String doctor_id;


    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                imageView.setImageBitmap(bitmap);
            }
        }
    };

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
        imageView = (CircleImageView) person_part.findViewById(R.id.person_detail_image);
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
        if (DataSupport.isExist(CaseInfor.class)) {
            CaseInfor caseInfor = DataSupport.find(CaseInfor.class, (long) id);
            doctor_name.setText("主治医生  " + caseInfor.getDoctorName());
            date.setText(caseInfor.getCreationDate());
            name.setText(caseInfor.getName());
            ill_problem.setText("病情: " + caseInfor.getIllproblem());
            ill_result.setText(caseInfor.getIllresult());
            doctor_id = caseInfor.getDoctorId();
            bitmapUtils.disPlay(imageView, GlobalData.GET_PATIENT_IMAGE + caseInfor.getImage());
        } else {
            Utils.showShortToast(CaseDetailActivity.this, "没有数据");
        }

    }
}
