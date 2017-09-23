package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.victor.myclient.bean.CaseInfor;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.CircleImageView;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class CaseDetailActivity extends BaseActivity {

    private RelativeLayout back;
    private Button see_doctor_tail;
    private TextView doctor_name;
    private TextView date;
    private TextView name;
    private TextView sex_age, ill_problem, ill_result;
    private CircleImageView imageView;

    private String doctor_id;
    private int id = 1;
    private int patientId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 1);
            patientId = getIntent().getIntExtra(GlobalData.PATIENT_ID, -1);
        }
        super.onCreate(savedInstanceState);
        initEvent();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.case_detail;
    }

    @Override
    protected Activity getActivity() {
        return CaseDetailActivity.this;
    }


    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back_case_detail);
        LinearLayout doctor_part = (LinearLayout) findViewById(R.id.doctor_part_case_detail);
        LinearLayout person_part = (LinearLayout) findViewById(R.id.case_detail_person_part);
        see_doctor_tail = (Button) doctor_part.findViewById(R.id.doctor_part_button);
        doctor_name = (TextView) doctor_part.findViewById(R.id.doctor_part_name);
        date = (TextView) doctor_part.findViewById(R.id.doctor_part_time);
        name = (TextView) person_part.findViewById(R.id.person_detail_name);
        sex_age = (TextView) person_part.findViewById(R.id.person_detail_info);
        ill_problem = (TextView) findViewById(R.id.case_detail_detail);
        ill_result = (TextView) findViewById(R.id.case_detail_result);
        imageView = (CircleImageView) person_part.findViewById(R.id.person_detail_image);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(CaseDetailActivity.this);
            }
        });
        see_doctor_tail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaseDetailActivity.this, DoctorActivity.class);
                intent.putExtra("doctor_id", Integer.valueOf(doctor_id));
                MyActivityManager.startActivity(CaseDetailActivity.this, intent);
            }
        });
    }


    public void showData(CaseInfor caseInfor) {
        doctor_name.setText("主治医生  " + caseInfor.getDoctorName());
        date.setText(caseInfor.getDate());
        name.setText(caseInfor.getName());
        ill_problem.setText("病情: " + caseInfor.getIllproblem() + "\n体温: " + caseInfor
                .getTemperature() + "°C\n血压: " + caseInfor.getBlood_pressure() + "mmHg");
        ill_result.setText(caseInfor.getIllresult());
        sex_age.setText(caseInfor.getSex() + "    " + caseInfor.getAge() + "岁");
        doctor_id = caseInfor.getDoctorId();
        Utils.showImage(getActivity(), GlobalData.GET_PATIENT_IMAGE + caseInfor.getImage(),
                imageView);
    }

    private void initData() {
        String key = Utils.createAcacheKey("get-cases", patientId);
        Observable observable = RxUtil.rxCreateDiskObservable(key, new
                TypeToken<List<CaseInfor>>() {
                }.getType());
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<CaseInfor>, CaseInfor>() {
                    @Override
                    public CaseInfor call(List<CaseInfor> caseInfors) {
                        for (CaseInfor caseInfor : caseInfors) {
                            if (caseInfor.getId() == id) {
                                return caseInfor;
                            }
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<CaseInfor>() {
                    @Override
                    public void call(CaseInfor caseInfor) {
                        showData(caseInfor);
                    }
                });
    }
}
