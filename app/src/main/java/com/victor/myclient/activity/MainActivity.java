package com.victor.myclient.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;
import com.igexin.sdk.PushManager;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.ServiceHistoryActivity;
import com.victor.myclient.datas.ServiceHistory;
import com.victor.myclient.service.MyIntentService;
import com.victor.myclient.service.MyPushService;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.datas.UserInfor;
import com.victor.myclient.activity.cases.CaseListActivity;
import com.victor.myclient.activity.contact.ContactActivity;
import com.victor.myclient.activity.setting.SettingActivity;
import com.google.gson.Gson;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.security.Permission;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/22.
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private static final String TAG = "MainActivity";
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
    private CircleTextImageView callothers;
    private CircleTextImageView services;
    private TextView time_call;

    private String xiaoyuNumber;
    private String user_name;
    private boolean net_work_available, has_data;

    private UserInfor userInfor;
    private final static int REQUEST_PERMISSION=1000;

    @Override
    protected void onResume() {
        super.onResume();
        net_work_available = Utils.isNetWorkAvailabe(MainActivity.this);
        resumeData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager packageManager = getPackageManager();
        boolean sdCardWritePermission = packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean phoneStatePermission=packageManager.checkPermission(Manifest.permission.READ_PHONE_STATE,getPackageName())== PackageManager.PERMISSION_GRANTED;

        if(Build.VERSION.SDK_INT>=23&&!sdCardWritePermission||!phoneStatePermission){
            requestPermission();
        }else {
            PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
        }

        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), MyIntentService.
                class);

        ActivityManage.getInstance().pushActivity(MainActivity.this);
        initData();
        initView();
        initEvent();
        NemoSDK.getInstance().connectNemo("vic", "18774259685", new ConnectNemoCallback() {
            @Override
            public void onFailed(int i) {

                Log.e(TAG, "fail");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bangding_xiaoyu_number.setText("登录失败");
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "success");
                xiaoyuNumber = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bangding_xiaoyu_number.setText("绑定小鱼号: " + xiaoyuNumber);
                    }
                });
            }
        });
        new getUserInfor().execute(user_name);
    }

    private void initData() {
        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);
        net_work_available = Utils.isNetWorkAvailabe(MainActivity.this);
    }

    private void initView() {
        this.callothers = (CircleTextImageView) findViewById(R.id.call_others);
        this.bangding_xiaoyu_number = (TextView) findViewById(R.id.bangding_xiaoyu_number);
        this.nametext = (TextView) findViewById(R.id.name_text);
        this.personimage = (CircleImageView) findViewById(R.id.person_image);
        this.services=(CircleTextImageView) findViewById(R.id.services);
        time_call = (TextView) findViewById(R.id.time_call);
        setTime_call();
        userInfor = new UserInfor();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length==2||grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
            }else{
                Utils.showShortToast(this,"建议您开启这些权限以便于获得更好的体验");
                PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
            }
        }
    }

    private void setTime_call() {
        int hour = Utils.getIntValue(MainActivity.this, GlobalData.DRURATION_HOUR);
        int minute = Utils.getIntValue(MainActivity.this, GlobalData.DRURATION_MINITE);
        int second = Utils.getIntValue(MainActivity.this, GlobalData.DRURATION_SECOND);
        minute = minute + hour * 60 + second / 60;
        time_call.setText("小鱼通话时长: " + minute + "分钟");

    }

    private void resumeData() {
        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);
        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE + Utils.getValue(MainActivity.this, GlobalData.FAMILY_IMage));
        setTime_call();
    }

    private void initEvent() {
        findViewById(R.id.setting_default).setOnClickListener(this);
        findViewById(R.id.call_others).setOnClickListener(this);
        findViewById(R.id.jujia_main).setOnClickListener(this);
        findViewById(R.id.case_for_patient).setOnClickListener(this);
        services.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.case_for_patient:
                Intent intent = new Intent(MainActivity.this, CaseListActivity.class);
                intent.putExtra("id", userInfor.getPatientId());
                startActivity(intent);
                break;
            case R.id.jujia_main:
                Utils.startActivity(MainActivity.this, JujiaActivity.class);
                break;
            case R.id.call_others:
                handler.sendEmptyMessage(0x123);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x124);
                    }
                }, 50);
                Utils.startActivity(MainActivity.this, ContactActivity.class);
                break;
            case R.id.setting_default:
                Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                intent1.putExtra("email", userInfor.getEmail());
                startActivity(intent1);
                break;
            case R.id.services:
                Intent intent2=new Intent(MainActivity.this, ServiceHistoryActivity.class);
                intent2.putExtra("id",userInfor.getPatientId());
                startActivity(intent2);
                break;
            default:
                break;
        }
    }


    private class getUserInfor extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();

        @Override
        protected String doInBackground(String... params) {
            if (net_work_available) {
                userInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_USR_INFOR + "FamilyName=" + user_name), UserInfor.class);
                String name = Utils.getValue(MainActivity.this, GlobalData.NAME);
                if (name == null || name != userInfor.getName()) {
                    Utils.putValue(MainActivity.this, GlobalData.NAME, userInfor.getName());
                    Utils.putValue(MainActivity.this, GlobalData.USer_email, userInfor.getEmail());
                    Utils.putValue(MainActivity.this, GlobalData.User_ID, userInfor.getId());
                    Utils.putValue(MainActivity.this, GlobalData.Phone, userInfor.getPhone());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENT_ID, userInfor.getPatientId());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID, userInfor.getId());
                    Utils.putValue(MainActivity.this, GlobalData.FAMILY_IMage, userInfor.getImage());
                }
//                String xiaoyu = Utils.sendRequest(GlobalData.GET_XIAO_YU_NUMBER + "type=phone&data=" + "13367379725");
//                if (xiaoyu.contains("not")) {
//                    XiaoYuNumber xiaoYuNumber = new XiaoYuNumber();
//                    xiaoYuNumber.setXiaoyuNum("暂无");
//                    xiaoYuNumbers.add(xiaoYuNumber);
//                } else {
//                    xiaoYuNumbers = gson2.fromJson(xiaoyu, new TypeToken<List<XiaoYuNumber>>() {
//                            }.getType()
//                    );
//
//                }
//                Utils.putValue(MainActivity.this, GlobalData.XIAO_YU, xiaoYuNumbers.get(0).getXiaoyuNum());
                has_data = true;
            } else {
                String name = Utils.getValue(MainActivity.this, GlobalData.NAME);
                if (name != null) {
                    userInfor.setPatientId(Utils.getValue(MainActivity.this, GlobalData.PATIENT_ID));
                    userInfor.setEmail(Utils.getValue(MainActivity.this, GlobalData.USer_email));
                    userInfor.setPhone(Utils.getValue(MainActivity.this, GlobalData.Phone));
                    userInfor.setName(name);
//                    XiaoYuNumber xiaoYuNumber = new XiaoYuNumber();
//                    xiaoYuNumber.setXiaoyuNum(Utils.getValue(MainActivity.this, GlobalData.XIAO_YU));
//                    xiaoYuNumbers.add(xiaoYuNumber);
                    has_data = true;
                } else {
                    has_data = false;
                }
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (has_data) {
                    nametext.setText(userInfor.getName());
                    if (userInfor.getImage() != null) {
                        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE + userInfor.getImage());
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
