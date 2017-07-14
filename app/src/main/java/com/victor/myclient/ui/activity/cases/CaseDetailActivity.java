package com.victor.myclient.ui.activity.cases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.base.BaseActivity;
import com.victor.myclient.ui.contract.CaseDetailContract;
import com.victor.myclient.datas.CaseInfor;
import com.victor.myclient.ui.presenter.CaseDetailPresenter;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


public class CaseDetailActivity extends BaseActivity implements CaseDetailContract.View{

    private RelativeLayout back;
    private LinearLayout doctor_part;
    private LinearLayout person_part;
    private Button see_doctor_tail;
    private TextView doctor_name;
    private TextView date;
    private TextView name;
    private TextView sex_age, ill_problem,  ill_result;
    private CircleImageView imageView;
    private CaseDetailContract.Presenter mPresenter;

    private String doctor_id;


    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", 1);
        mPresenter = new CaseDetailPresenter(this, id);
        mPresenter.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.case_detail;
    }

    @Override
    protected Activity getActivityToPush() {
        return CaseDetailActivity.this;
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back_case_detail);
        doctor_part = (LinearLayout) findViewById(R.id.doctor_part_case_detail);
        person_part = (LinearLayout) findViewById(R.id.case_detail_person_part);
        see_doctor_tail = (Button) doctor_part.findViewById(R.id.doctor_part_button);
        doctor_name = (TextView) doctor_part.findViewById(R.id.doctor_part_name);
        date = (TextView) doctor_part.findViewById(R.id.doctor_part_time);
        name = (TextView) person_part.findViewById(R.id.person_detail_name);
        sex_age = (TextView) person_part.findViewById(R.id.person_detail_info);
        ill_problem = (TextView) findViewById(R.id.case_detail_detail);
        ill_result = (TextView) findViewById(R.id.case_detail_result);
        imageView = (CircleImageView) person_part.findViewById(R.id.person_detail_image);
    }
    @Override
    protected void initEvent() {
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
                Utils.startActivity(CaseDetailActivity.this, intent);
            }
        });
    }

    @Override
    public void setPresenter(CaseDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showData(CaseInfor caseInfor) {
        doctor_name.setText("主治医生  " + caseInfor.getDoctorName());
        date.setText(caseInfor.getDate());
        name.setText(caseInfor.getName());
        ill_problem.setText("病情: " + caseInfor.getIllproblem() + "\n体温: " + caseInfor.getTemperature() + "°C\n血压: " + caseInfor.getBlood_pressure() + "mmHg");
        ill_result.setText(caseInfor.getIllresult());
        sex_age.setText(caseInfor.getSex() + "    " + caseInfor.getAge()+"岁");
        doctor_id = caseInfor.getDoctorId();
        bitmapUtils.disPlay(imageView, GlobalData.GET_PATIENT_IMAGE + caseInfor.getImage());
    }

    @Override
    public void showNoData() {
        Utils.showShortToast(CaseDetailActivity.this, "没有数据");
    }
}
