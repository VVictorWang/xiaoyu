package com.example.franklin.myclient.view.Case;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.franklin.myclient.Datas.DoctorInfor;
import com.example.franklin.myclient.SomeUtils.BitmapDownLoader;
import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.Contact.BitmapUtil;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


public class DoctorActivity extends AppCompatActivity {

    private LinearLayout person_part;

    private String doctor_id;
    private DoctorInfor doctorInfor;
    private android.widget.ImageView backtomainimagebackgrounddoctorinfor;
    private TextView backtomainimagebackgrounddoctorinfortext;
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

    private Bitmap bitmap;

    private boolean net_work_available,has_data;
    private String name;
    private BitmapDownLoader bitmapDownLoader = new BitmapDownLoader();
    @Override
    protected void onResume() {
        super.onResume();
        new GetDoctorInfor().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_info);

        doctor_id = getIntent().getStringExtra("doctor_id");
        initView();
        initEvent();
        initData();
        new GetDoctorInfor().execute();
    }

    private void initView() {
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
        this.backtomainimagebackgrounddoctorinfortext = (TextView) findViewById(R.id.back_to_main_image_background_doctor_infor_text);
        this.backtomainimagebackgrounddoctorinfor = (ImageView) findViewById(R.id.back_to_main_image_background_doctor_infor);
        net_work_available = Utils.isNetWorkAvailabe(DoctorActivity.this);
    }

    private void initEvent() {
        backdoctordetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(DoctorActivity.this);
            }
        });
    }

    private void initData() {


    }


    class GetDoctorInfor extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {


            if (net_work_available) {
                doctorInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_DOCTOR_INFOR + doctor_id), DoctorInfor.class);
                bitmap = BitmapDownLoader.getBitmap("http://139.196.40.97/upload/doctorimage/" + doctorInfor.getImage());
//
                BitmapUtil.saveBitmapToSDCard(bitmap, GlobalData.DoctorIMage, doctorInfor.getName());
//                bitmapDownLoader.addBitmapToMemory(doctorInfor.getName(), bitmap);
                DataSupport.deleteAll(DoctorInfor.class);
                if (!doctorInfor.isSaved()) {
                   doctorInfor.save();
                }
                name = doctorInfor.getName();
                Utils.putValue(DoctorActivity.this, GlobalData.DoctorName, name);
                has_data = true;
            } else {
                if (DataSupport.isExist(DoctorInfor.class)) {
                    Log.e("exud", "dawrdfw");
                    List<DoctorInfor> doctorInfors = DataSupport.findAll(DoctorInfor.class);
                    name = Utils.getValue(DoctorActivity.this, GlobalData.DoctorName);
                    for (DoctorInfor doctorInfora : doctorInfors) {
                        if (doctorInfora.getName().equals(name)) {
                            doctorInfor = doctorInfora;
                            bitmap = BitmapUtil.getBitmap(GlobalData.DoctorIMage + "/" + doctorInfora.getName() + ".jpg");
                            break;
                        }
                    }

                    if (doctorInfor != null) {
                        has_data = true;
                    }
                    else
                        has_data = false;
                }else
                    has_data = false;


            }

//

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
                doctorinfointrodetail.setText(doctorInfor.getCases());
                persondetailimagedoctor.setImageBitmap(bitmap);
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
