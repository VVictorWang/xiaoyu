
package com.victor.myclient.activity;

import android.support.v7.app.AppCompatActivity;

import com.victor.myclient.service.PostClientIdService;
import com.victor.myclient.utils.GlobalData;

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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;
import com.igexin.sdk.PushManager;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.service.MyIntentService;
import com.victor.myclient.service.MyPushService;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.datas.UserInfor;
import com.google.gson.Gson;
import com.victor.myclient.view.CircleTextImageView;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;


/**
 * Created by victor on 2017/4/22.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    callothers.setImageDrawable(getResources().getDrawable(R.drawable
                            .infor_small_selected));
                    break;
                case 0x124:
                    callothers.setImageDrawable(getResources().getDrawable(R.drawable
                            .btn_bg_hujiao));
                    break;
                case 0x125:
                    jujiamain.setImageDrawable(getResources().getDrawable(R.drawable
                            .infor_small_selected));
                    break;
                case 0x126:
                    jujiamain.setImageDrawable(getResources().getDrawable(R.drawable
                            .btn_bg_hujiao));
                    break;
                case 0x127:
                    caseforpatient.setImageDrawable(getResources().getDrawable(R.drawable
                            .infor_small_selected));
                    break;
                case 0x128:
                    caseforpatient.setImageDrawable(getResources().getDrawable(R.drawable
                            .btn_bg_hujiao));
                    break;
                case 0x129:
                    setting.setImageDrawable(getResources().getDrawable(R.drawable
                            .infor_small_selected));
                    break;
                case 0x130:
                    setting.setImageDrawable(getResources().getDrawable(R.drawable
                            .btn_bg_hujiao));
                    break;
                case 0x131:
                    services.setImageDrawable(getResources().getDrawable(R.drawable
                            .infor_small_selected));
                    break;
                case 0x132:
                    services.setImageDrawable(getResources().getDrawable(R.drawable
                            .btn_bg_hujiao));
                    break;
                case 0x133:
                    Intent intent=new Intent(MainActivity.this, PostClientIdService.class);
                    startService(intent);
                    break;
                    
            }

        }
    };
    private de.hdodenhof.circleimageview.CircleImageView personimage;
    private android.widget.TextView nametext;
    private android.widget.TextView bangding_xiaoyu_number;
    private CircleTextImageView callothers, jujiamain, caseforpatient, setting;
    private CircleTextImageView services;
    private TextView time_call;

    private String xiaoyuNumber;
    private boolean net_work_available, has_data;
    private String type = "username";

    private UserInfor userInfor;
    private final static int REQUEST_PERMISSION = 1000;
    public static final String TAG = "@victor MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent() != null && getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }
        PackageManager packageManager = getPackageManager();
        boolean sdCardWritePermission = packageManager.checkPermission(Manifest.permission
                .WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean phoneStatePermission = packageManager.checkPermission(Manifest.permission
                .READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneStatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService.class);
        }

        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),
                MyIntentService.
                        class);

        ActivityManage.getInstance().pushActivity(MainActivity.this);
        initData();
        initView();
        initEvent();
        new getUserInfor().execute();
    }

    private void connectXiaoyu(String xiaoyuNumber, String user_name) {
        String name = user_name;
        String number = xiaoyuNumber;
        if (user_name == null) {
            name = "victor";
            number = "18774259685";
        }
        Log.d(TAG, "connectXiaoyu: user_name xiaoyunumber"+user_name+" "+xiaoyuNumber);
        NemoSDK.getInstance().connectNemo(name, number, new ConnectNemoCallback() {
            @Override
            public void onFailed(int i) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bangding_xiaoyu_number.setText("登录失败");
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                final String reslut = s;
                Log.d(TAG, "onSuccess: s="+s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bangding_xiaoyu_number.setText("绑定小鱼号: " + reslut);
                    }
                });
            }
        });
    }

    private void initData() {
        net_work_available = Utils.isNetWorkAvailabe(MainActivity.this);
    }

    private void initView() {
        this.callothers = (CircleTextImageView) findViewById(R.id.call_others);
        this.bangding_xiaoyu_number = (TextView) findViewById(R.id.bangding_xiaoyu_number);
        this.nametext = (TextView) findViewById(R.id.name_text);
        this.personimage = (CircleImageView) findViewById(R.id.person_image);
        services = (CircleTextImageView) findViewById(R.id.services);
        jujiamain = (CircleTextImageView) findViewById(R.id.jujia_main);
        caseforpatient = (CircleTextImageView) findViewById(R.id.case_for_patient);
        setting = (CircleTextImageView) findViewById(R.id.setting_default);
        time_call = (TextView) findViewById(R.id.time_call);
        setTime_call();
        userInfor = new UserInfor();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length == 2 || grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService
                        .class);
            } else {
                Utils.showShortToast(this, "建议您开启这些权限以便于获得更好的体验");
                PushManager.getInstance().initialize(this.getApplicationContext(), MyPushService
                        .class);
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
        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE + Utils.getValue
                (MainActivity.this, GlobalData.FAMILY_IMage));
        setTime_call();
    }

    private void initEvent() {
        setting.setOnClickListener(this);
        callothers.setOnClickListener(this);
        jujiamain.setOnClickListener(this);
        caseforpatient.setOnClickListener(this);
        services.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.case_for_patient:
                handler.sendEmptyMessage(0x127);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x128);
                    }
                }, 50);
                Intent intent = new Intent(MainActivity.this, CaseListActivity.class);
                intent.putExtra("id", userInfor.getPatientId());
                Utils.startActivity(MainActivity.this, intent);
                break;
            case R.id.jujia_main:
                handler.sendEmptyMessage(0x125);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x126);
                    }
                }, 50);
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
                handler.sendEmptyMessage(0x129);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x130);
                    }
                }, 50);
                Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
                intent1.putExtra("email", userInfor.getEmail());
                Utils.startActivity(MainActivity.this, intent1);
                break;
            case R.id.services:
                handler.sendEmptyMessage(0x131);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x132);
                    }
                }, 50);
                Intent intent2 = new Intent(MainActivity.this, ServiceHistoryActivity.class);
                intent2.putExtra("id", userInfor.getPatientId());
                Utils.startActivity(MainActivity.this, intent2);
                break;
            default:
                break;
        }
    }


    private class getUserInfor extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();

        @Override
        protected String doInBackground(String... params) {
            if (net_work_available ) {
                if (type.equals("username")) {
                    String info = Utils.sendRequest(GlobalData.GET_USR_INFOR +
                            "FamilyName=" + Utils.getValue(MainActivity.this, GlobalData.NAME) +
                            "&type=username");
                    if (!info.contains("not_exist")) {
                        userInfor = gson.fromJson(info, UserInfor.class);
                        has_data = true;
                    }else
                        has_data = false;
                } else if (type.equals("phone")) {
                    String info = Utils.sendRequest(GlobalData.GET_USR_INFOR +
                            "FamilyName=" + Utils.getValue(MainActivity.this, GlobalData.Phone) +
                            "&type=phone");
                    if (!info.contains("not_exist")) {
                        userInfor = gson.fromJson(info, UserInfor.class);
                        has_data = true;
                    }else
                        has_data = false;
                }
                if (has_data) {
                    Utils.putValue(MainActivity.this, GlobalData.NAME, userInfor.getName());
                    Utils.putValue(MainActivity.this, GlobalData.USer_email, userInfor.getEmail());
                    Utils.putValue(MainActivity.this, GlobalData.User_ID, userInfor.getId());
                    Utils.putValue(MainActivity.this, GlobalData.Phone, userInfor.getPhone());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENT_ID, userInfor.getPatientId());
                    Utils.putValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID, userInfor.getId());
                    Utils.putValue(MainActivity.this, GlobalData.FAMILY_IMage, userInfor.getImage());
                    Utils.putValue(MainActivity.this, GlobalData.XIAOYU_NAME, userInfor.getXiaoyuName
                            ());
                    Utils.putValue(MainActivity.this, GlobalData.XIAOYU_NUMBER, userInfor
                            .getXiaoyuNum());
                }
            } else {
                String name = Utils.getValue(MainActivity.this, GlobalData.NAME);
                if (!(name.equals(""))) {
                    userInfor.setPatientId(Utils.getValue(MainActivity.this, GlobalData
                            .PATIENT_ID));
                    userInfor.setEmail(Utils.getValue(MainActivity.this, GlobalData.USer_email));
                    userInfor.setPhone(Utils.getValue(MainActivity.this, GlobalData.Phone));
                    userInfor.setName(name);
                    userInfor.setXiaoyuName(Utils.getValue(MainActivity.this, GlobalData
                            .XIAOYU_NAME));
                    userInfor.setXiaoyuNum(Utils.getValue(MainActivity.this, GlobalData
                            .XIAOYU_NUMBER));
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
                    Log.d(TAG, "onPostExecute: ");
                    handler.sendEmptyMessage(0x133);
                    nametext.setText(userInfor.getName());
                    connectXiaoyu(userInfor.getXiaoyuNum(), userInfor.getXiaoyuName());
                    if (userInfor.getImage() != null) {
                        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE +
                                userInfor.getImage());
                    }
                }else
                    Utils.showShortToast(MainActivity.this, "用户数据不存在");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
