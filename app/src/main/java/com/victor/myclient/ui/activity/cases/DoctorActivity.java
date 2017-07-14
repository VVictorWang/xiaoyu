package com.victor.myclient.ui.activity.cases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.base.BaseActivity;
import com.victor.myclient.datas.DoctorInfor;
import com.victor.myclient.datas.DoctorXiaoYu;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.Utils;
import com.google.gson.Gson;
import com.victor.myclient.ui.activity.xiaoyu.VideoActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


public class DoctorActivity extends BaseActivity {

    private String doctor_id;
    private DoctorInfor doctorInfor;
    private RelativeLayout backdoctordetail;
    private de.hdodenhof.circleimageview.CircleImageView persondetailimagedoctor;
    private TextView persondetailnamedoctor;
    private TextView persondetailinfodoctor;
    private TextView persondetailjobdoctor;
    private TextView doctorpartnamedoctor;
    private TextView doctorparttimedoctor;
    private TextView doctorpartgoodat;
    private TextView doctorinfointrodetail;
    private ProgressDialog dialog;
    private Button make_call;

    private boolean net_work_available, has_data;
    private String name;
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private DoctorXiaoYu doctorXiaoYu = new DoctorXiaoYu();
    private static final String TAG = "DoctorActivity";

    @Override
    protected void onResume() {
        super.onResume();
        new GetDoctorInfor().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctor_id = getIntent().getStringExtra("doctor_id");
        new GetDoctorInfor().execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.doctor_info;
    }

    @Override
    protected Activity getActivityToPush() {
        return DoctorActivity.this;
    }

    @Override
    protected void initView() {
        dialog = new ProgressDialog(this);
        dialog.show();
        this.doctorinfointrodetail = (TextView) findViewById(R.id.doctor_info_intro_detail);
        this.doctorpartgoodat = (TextView) findViewById(R.id.doctor_part_good_at);
        this.doctorparttimedoctor = (TextView) findViewById(R.id.doctor_part_time_doctor);
        this.doctorpartnamedoctor = (TextView) findViewById(R.id.doctor_part_name_doctor);
        this.persondetailjobdoctor = (TextView) findViewById(R.id.person_detail_job_doctor);
        this.persondetailinfodoctor = (TextView) findViewById(R.id.person_detail_info_doctor);
        this.persondetailnamedoctor = (TextView) findViewById(R.id.person_detail_name_doctor);
        this.persondetailimagedoctor = (CircleImageView) findViewById(R.id.person_detail_image_doctor);
        this.backdoctordetail = (RelativeLayout) findViewById(R.id.back_doctor_detail);
        make_call = (Button) findViewById(R.id.doctor_part_button_doctor);
        net_work_available = Utils.isNetWorkAvailabe(DoctorActivity.this);
    }

    @Override
    protected void initEvent() {
        backdoctordetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(DoctorActivity.this);
            }
        });
        make_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorActivity.this, VideoActivity.class);
                if (doctorXiaoYu.getXiaoyuNum().equals("000")) {
                    Utils.showShortToast(DoctorActivity.this, "医生小鱼号不存在");
                } else {
                    intent.putExtra("number", doctorXiaoYu.getXiaoyuNum());
                    intent.putExtra("type", "doctor");
                    startActivity(intent);
                }
            }
        });
    }

    private class GetDoctorInfor extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {


            if (net_work_available) {
                String infor = Utils.sendRequest(GlobalData.GET_DOCTOR_INFOR + doctor_id);
                if (!infor.contains("not_exist")) {
                    doctorInfor = gson.fromJson(infor, DoctorInfor.class);
                    String xiaoyu = Utils.sendRequest(GlobalData.GET_DOCTOR_XIAO_YU + doctor_id);
                    if (xiaoyu.contains("not_exist")) {
                        doctorXiaoYu.setXiaoyuNum("000");
                    } else {
                        doctorXiaoYu = gson.fromJson(Utils.sendRequest(GlobalData.GET_DOCTOR_XIAO_YU + doctor_id), DoctorXiaoYu.class);
                    }
                    DataSupport.deleteAll(DoctorInfor.class);
                    if (!doctorInfor.isSaved()) {
                        doctorInfor.saveAsync();
                    }
                    if (!doctorXiaoYu.isSaved()) {
                        doctorXiaoYu.saveAsync();
                    }
                    name = doctorInfor.getName();
                    Utils.putValue(DoctorActivity.this, GlobalData.DoctorName, name);
                    has_data = true;
                } else {
                    has_data = false;
                }

            } else {
                if (DataSupport.isExist(DoctorInfor.class)) {
                    List<DoctorInfor> doctorInfors = DataSupport.findAll(DoctorInfor.class);
                    name = Utils.getValue(DoctorActivity.this, GlobalData.DoctorName);
                    for (DoctorInfor doctorInfora : doctorInfors) {
                        if (doctorInfora.getName().equals(name)) {
                            doctorInfor = doctorInfora;
                            break;
                        }
                    }
                    if (doctorInfor != null)
                        has_data = true;
                    else
                        has_data = false;
                } else
                    has_data = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (has_data) {
                persondetailnamedoctor.setText(doctorInfor.getName());
                persondetailinfodoctor.setText(doctorInfor.getSex() + "   " + doctorInfor.getAge());
                persondetailjobdoctor.setText(doctorInfor.getJob_title());
                doctorpartnamedoctor.setText("所在医院: " + doctorInfor.getHospital());
                doctorparttimedoctor.setText("联系邮箱: " + doctorInfor.getMail());
                doctorpartgoodat.setText("擅长方向: " + doctorInfor.getGood_at());
                doctorinfointrodetail.setText("职称: " + doctorInfor.getJob_title() + "\n其他: " + doctorInfor.getCases());
                Log.e(TAG, "http://139.196.40.97/upload/doctorimage/" + doctorInfor.getImage());
                bitmapUtils.disPlay(persondetailimagedoctor, "http://139.196.40.97/upload/doctorimage/" + doctorInfor.getImage());
            }
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
    }
}
