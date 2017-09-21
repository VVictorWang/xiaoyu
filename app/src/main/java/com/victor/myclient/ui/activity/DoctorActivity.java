package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.DoctorInfor;
import com.victor.myclient.data.DoctorXiaoYu;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.ui.contract.DoctorContract;
import com.victor.myclient.ui.presenter.DoctorPresenter;
import com.victor.myclient.utils.CheckUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.CircleImageView;

import demo.animen.com.xiaoyutask.R;


public class DoctorActivity extends BaseActivity implements DoctorContract.View {

    private static String doctorImageUrl = "http://139.196.40.97/upload/doctorimage/";

    private RelativeLayout backdoctordetail;
    private CircleImageView persondetailimagedoctor;
    private TextView persondetailnamedoctor;
    private TextView persondetailinfodoctor;
    private TextView persondetailjobdoctor;
    private TextView doctorpartnamedoctor;
    private TextView doctorparttimedoctor;
    private TextView doctorpartgoodat;
    private TextView doctorinfointrodetail;
    private ProgressDialog dialog;
    private Button make_call;

    private int doctor_id = -1;
    private DoctorXiaoYu doctorXiaoYu = null;

    private DoctorContract.Presenter mPresenter;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            doctor_id = getIntent().getIntExtra("doctor_id", -1);
        }
        super.onCreate(savedInstanceState);
        initEvent();
        mPresenter = new DoctorPresenter(this);
        dialog = new ProgressDialog(this);
        dialog.show();
        mPresenter.getInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.doctor_info;
    }

    @Override
    protected Activity getActivity() {
        return DoctorActivity.this;
    }

    @Override
    protected void initView() {
        doctorinfointrodetail = (TextView) findViewById(R.id.doctor_info_intro_detail);
        doctorpartgoodat = (TextView) findViewById(R.id.doctor_part_good_at);
        doctorparttimedoctor = (TextView) findViewById(R.id.doctor_part_time_doctor);
        doctorpartnamedoctor = (TextView) findViewById(R.id.doctor_part_name_doctor);
        persondetailjobdoctor = (TextView) findViewById(R.id.person_detail_job_doctor);
        persondetailinfodoctor = (TextView) findViewById(R.id.person_detail_info_doctor);
        persondetailnamedoctor = (TextView) findViewById(R.id.person_detail_name_doctor);
        persondetailimagedoctor = (CircleImageView) findViewById(R.id
                .person_detail_image_doctor);
        backdoctordetail = (RelativeLayout) findViewById(R.id.back_doctor_detail);
        make_call = (Button) findViewById(R.id.doctor_part_button_doctor);
    }

    private void initEvent() {
        backdoctordetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManage.finishActivity(DoctorActivity.this);
            }
        });
        make_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, VideoActivity.class);
                if (CheckUtils.isNull(doctorXiaoYu) || doctorXiaoYu.xiaoyuNum.equals("000")) {
                    Utils.showShortToast(DoctorActivity.this, "医生小鱼号不存在");
                } else {
                    intent.putExtra("number", doctorXiaoYu.xiaoyuNum);
                    intent.putExtra("type", "doctor");
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void setPresenter(DoctorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public int getDoctorId() {
        return doctor_id;
    }

    @Override
    public void showInfo(DoctorInfor doctorInfor) {
        persondetailnamedoctor.setText(doctorInfor.name);
        persondetailinfodoctor.setText(doctorInfor.sex + "   " + doctorInfor.age);
        persondetailjobdoctor.setText(doctorInfor.job_title);
        doctorpartnamedoctor.setText("所在医院: " + doctorInfor.hospital);
        doctorparttimedoctor.setText("联系邮箱: " + doctorInfor.mail);
        doctorpartgoodat.setText("擅长方向: " + doctorInfor.good_at);
        doctorinfointrodetail.setText("职称: " + doctorInfor.job_title + "\n其他: " +
                doctorInfor.cases);
        Utils.showImage(getActivity(), doctorImageUrl + doctorInfor
                .image, persondetailimagedoctor);
    }

    @Override
    public void dimissDialog() {
        dialog.dismiss();
    }

    @Override
    public void setDoctorXiaoYu(DoctorXiaoYu doctorXiaoyu) {
        doctorXiaoYu = doctorXiaoyu;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unscribe();
    }
}
