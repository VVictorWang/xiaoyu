package com.example.franklin.myclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.franklin.myclient.SomeUtils.BitmapDownLoader;
import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.Datas.UserInfor;
import com.example.franklin.myclient.Datas.XiaoYuNumber;
import com.example.franklin.myclient.view.Case.CaseListActivity;
import com.example.franklin.myclient.view.Contact.BitmapUtil;
import com.example.franklin.myclient.view.Contact.ContactActivity;
import com.example.franklin.myclient.view.JiuJIA.JujiaActivity;
import com.example.franklin.myclient.view.Setting.Setting_Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by victor on 2017/4/22.
 */


public class MainActivity extends AppCompatActivity {
    private OkHttpClient client;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                callothers.setImageDrawable(getResources().getDrawable(R.drawable.infor_small_selected));
            } else if (msg.what == 0x124) {
                callothers.setImageDrawable(getResources().getDrawable(R.drawable.infor_small));
            }
        }
    };
    private de.hdodenhof.circleimageview.CircleImageView personimage;
    private android.widget.TextView nametext;
    private android.widget.TextView bangding_xiaoyu_number;
    private android.widget.TextView timecall;
    private CircleTextImageView caseforpatient;
    private CircleTextImageView callothers;
    private CircleTextImageView jujiamain;
    private CircleTextImageView settingdefault;

    private String image_url;
    private String user_name;
    private UserInfor userInfor;
    private List<XiaoYuNumber> xiaoYuNumbers;

    private boolean net_work_available,has_data;
    private Bitmap bitmap;
    private BitmapDownLoader  downLoader = new BitmapDownLoader();

    @Override
    protected void onResume() {
        super.onResume();
        net_work_available = Utils.isNetWorkAvailabe(MainActivity.this);
        resumeData();
//        new GetUserInfor().execute(user_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
        new GetUserInfor().execute(user_name);
    }

    private void initView() {
        this.settingdefault = (CircleTextImageView) findViewById(R.id.setting_default);
        this.jujiamain = (CircleTextImageView) findViewById(R.id.jujia_main);
        this.callothers = (CircleTextImageView) findViewById(R.id.call_others);
        this.caseforpatient = (CircleTextImageView) findViewById(R.id.case_for_patient);
        this.timecall = (TextView) findViewById(R.id.time_call);
        this.bangding_xiaoyu_number = (TextView) findViewById(R.id.bangding_xiaoyu_number);
        this.nametext = (TextView) findViewById(R.id.name_text);
        this.personimage = (CircleImageView) findViewById(R.id.person_image);
//        if (image_url != null || image_url.equals("")) {
//            this.personimage.setImageURI(Uri.fromFile(new File(image_url)));
//        } else if (image_url.equals("")) {
//            this.personimage.setImageDrawable(getResources().getDrawable(R.drawable.person));
//        }
        userInfor = new UserInfor();
        xiaoYuNumbers = new ArrayList<>();
        net_work_available= Utils.isNetWorkAvailabe(MainActivity.this);
    }

    private void resumeData() {

        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);
        personimage.setImageURI(Uri.fromFile(new File(Utils.getValue(MainActivity.this, GlobalData.Img_URl))));
//        personimage.setImageBitmap(BitmapUtil.getBitmap(GlobalData.DoctorIMage + Utils.getValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID)));
    }

    private void initData() {
//        image_url = Utils.getValue(MainActivity.this, GlobalData.Img_URl);
        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);

//        personimage.setImageBitmap(BitmapUtil.getBitmap(GlobalData.DoctorIMage + Utils.getValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID)));
    }

    private void initEvent() {
        settingdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Setting_Activity.class);
                intent.putExtra("email", userInfor.getEmail());
                startActivity(intent);
            }
        });
        callothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(0x123);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x124);
                    }
                }, 50);

                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
        jujiamain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JujiaActivity.class);
                startActivity(intent);
            }
        });
        caseforpatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CaseListActivity.class);
                intent.putExtra("id", userInfor.getPatientId());
                startActivity(intent);
            }
        });
    }


    class GetUserInfor extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();
        private Gson gson2 = new Gson();

        @Override
        protected String doInBackground(String... params) {
//            Log.e("data ", sendRequest());
//            if (!Utils.isNetWorkAvailabe(MainActivity.this)) {
            if (net_work_available) {
                userInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_USR_INFOR + "FamilyName=" + user_name), UserInfor.class);
                String name = Utils.getValue(MainActivity.this, GlobalData.NAME);
                if (name == null || name != userInfor.getName()) {
                    Utils.putValue(MainActivity.this, GlobalData.NAME, userInfor.getName());
//                Utils.putValue(MainActivity.this, GlobalData.Img_URl, userInfor.getImage());
                    Utils.putValue(MainActivity.this, GlobalData.USer_email, userInfor.getEmail());
                    Utils.putValue(MainActivity.this, GlobalData.User_ID, userInfor.getId());
                    Utils.putValue(MainActivity.this, GlobalData.Phone, userInfor.getPhone());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENT_ID, userInfor.getPatientId());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID, userInfor.getId());

                }
                String image_url = userInfor.getImage();
                if (image_url != null && !image_url.equals("null")) {

                        bitmap = BitmapDownLoader.getBitmap(GlobalData.GET_PATIENT_FAMILY_IMAGE + image_url);
                    BitmapUtil.saveBitmapToSDCard(bitmap, GlobalData.DoctorIMage, "familyimage" + userInfor.getId());
                    downLoader.addBitmapToMemory("familyimage" + userInfor.getId(), bitmap);
                }

                String xiaoyu = Utils.sendRequest(GlobalData.GET_XIAO_YU_NUMBER + "type=phone&data=" + "13367379725");
                Log.e("xiaoyu: ", xiaoyu);
                if (xiaoyu.contains("not")) {
                    XiaoYuNumber xiaoYuNumber = new XiaoYuNumber();
                    xiaoYuNumber.setXiaoyuNum("暂无");
                    xiaoYuNumbers.add(xiaoYuNumber);
                } else {
                    xiaoYuNumbers = gson2.fromJson(xiaoyu, new TypeToken<List<XiaoYuNumber>>() {
                            }.getType()
                    );
//
                }
                Utils.putValue(MainActivity.this, GlobalData.XIAO_YU, xiaoYuNumbers.get(0).getXiaoyuNum());
                Log.e("xiaoyunum: ", xiaoYuNumbers.toString());
                has_data = true;
            } else {
                String name = Utils.getValue(MainActivity.this, GlobalData.NAME);
                if (name != null) {
                    userInfor.setPatientId(Utils.getValue(MainActivity.this, GlobalData.PATIENT_ID));
                    userInfor.setEmail(Utils.getValue(MainActivity.this, GlobalData.USer_email));
                    userInfor.setPhone(Utils.getValue(MainActivity.this, GlobalData.Phone));
                    userInfor.setName(name);
                    XiaoYuNumber xiaoYuNumber = new XiaoYuNumber();
                    xiaoYuNumber.setXiaoyuNum(Utils.getValue(MainActivity.this, GlobalData.XIAO_YU));
                    xiaoYuNumbers.add(xiaoYuNumber);
                    bitmap = downLoader.getBitmapFromMemCache("familyimage" + Utils.getValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID));
                    has_data = true;
                } else {
                    has_data = false;
                }

            }

//
//            try {
////                Request request = new Request.Builder().url(GlobalData.GET_USR_INFOR + "FamilyName=" + user_name).build();
////                Response response = client.newCall(request).execute();
////                Log.e("url: ", GlobalData.GET_USR_INFOR + "FamilyName=" + user_name);
////                UserInfor userInfor = gson.fromJson(response.body().charStream(), UserInfor.class);
////                Log.e("id: ", userInfor.getPatientId());
////
////                Log.e("userinfor", response.body().charStream().toString());
//            } catch (Exception op) {
//                op.printStackTrace();
//            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (has_data) {
                    nametext.setText(userInfor.getName());
                    bangding_xiaoyu_number.setText("绑定小鱼号: "+xiaoYuNumbers.get(0).getXiaoyuNum());
                    if (bitmap != null) {
                        personimage.setImageBitmap(bitmap);
                    } else {
                        bitmap = BitmapUtil.getBitmap(GlobalData.DoctorIMage + "familyimage" + userInfor.getName());
                        if (bitmap != null) {

                            personimage.setImageBitmap(bitmap);
                        }
                        else{
                            BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.person);
                            bitmap = drawable.getBitmap();
                            personimage.setImageBitmap(bitmap);
                            BitmapUtil.saveDrawble(MainActivity.this, R.drawable.person, "familyimage" + userInfor.getName());
                            Utils.putValue(MainActivity.this, GlobalData.Img_URl, Environment.getExternalStorageDirectory() + "familyimage" + userInfor.getName());
                        }
                    }

                }

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
