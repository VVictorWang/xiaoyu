package com.victor.myclient.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.UserInfor;
import com.victor.myclient.service.MyIntentService;
import com.victor.myclient.service.MyPushService;
import com.victor.myclient.service.PostClientIdService;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.CircleImageView;
import com.victor.myclient.view.CircleTextImageView;

import java.util.Timer;
import java.util.TimerTask;

import demo.animen.com.xiaoyutask.R;

import static com.victor.myclient.utils.PrefUtils.getValue;


/**
 * Created by victor on 2017/4/22.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "@victor MainActivity";
    private final static int REQUEST_PERMISSION = 1000;
    private MyBitmapUtils bitmapUtils = new MyBitmapUtils();
    private CircleImageView personimage;
    private android.widget.TextView nametext;
    private android.widget.TextView bangding_xiaoyu_number;
    private CircleTextImageView callothers, jujiamain, caseforpatient, setting;
    private CircleTextImageView services;
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
                    Intent intent = new Intent(MainActivity.this, PostClientIdService.class);
                    startService(intent);
                    break;

            }

        }
    };
    private TextView time_call;
    private String xiaoyuNumber;
    private boolean net_work_available, has_data;
    private String type = "username";
    private UserInfor userInfor;

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initView();
        initEvent();
        new getUserInfor().execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Activity getActivity() {
        return MainActivity.this;
    }

    private void connectXiaoyu(String xiaoyuNumber, String user_name) {
        String name = user_name;
        String number = xiaoyuNumber;
        if (user_name == null) {
            name = "victor";
            number = "18774259685";
        }
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

    @Override
    protected void initView() {
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
        int hour = PrefUtils.getIntValue(MainActivity.this, GlobalData.DRURATION_HOUR);
        int minute = PrefUtils.getIntValue(MainActivity.this, GlobalData.DRURATION_MINITE);
        int second = PrefUtils.getIntValue(MainActivity.this, GlobalData.DRURATION_SECOND);
        minute = minute + hour * 60 + second / 60;
        time_call.setText("小鱼通话时长: " + minute + "分钟");

    }

    private void resumeData() {
        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE + getValue
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
                ActivityManage.startActivity(MainActivity.this, intent);
                break;
            case R.id.jujia_main:
                handler.sendEmptyMessage(0x125);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x126);
                    }
                }, 50);
                ActivityManage.startActivity(MainActivity.this, JujiaActivity.class);
                break;
            case R.id.call_others:
                handler.sendEmptyMessage(0x123);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x124);
                    }
                }, 50);
                ActivityManage.startActivity(MainActivity.this, ContactActivity.class);
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
                ActivityManage.startActivity(MainActivity.this, intent1);
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
                ActivityManage.startActivity(MainActivity.this, intent2);
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
                if (type.equals("username")) {
                    String info = Utils.sendRequest(GlobalData.GET_USR_INFOR +
                            "FamilyName=" + getValue(MainActivity.this, GlobalData.NAME) +
                            "&type=username");
                    if (!info.contains("not_exist")) {
                        userInfor = gson.fromJson(info, UserInfor.class);
                        has_data = true;
                    } else
                        has_data = false;
                } else if (type.equals("phone")) {
                    String info = Utils.sendRequest(GlobalData.GET_USR_INFOR +
                            "FamilyName=" + getValue(MainActivity.this, GlobalData.Phone) +
                            "&type=phone");
                    if (!info.contains("not_exist")) {
                        userInfor = gson.fromJson(info, UserInfor.class);
                        has_data = true;
                    } else
                        has_data = false;
                }
                if (has_data) {
                    PrefUtils.putValue(MainActivity.this, GlobalData.NAME, userInfor.getName());
                    PrefUtils.putValue(MainActivity.this, GlobalData.USer_email, userInfor
                            .getEmail());
                    PrefUtils.putValue(MainActivity.this, GlobalData.User_ID, userInfor.getId());
                    PrefUtils.putValue(MainActivity.this, GlobalData.Phone, userInfor.getPhone());
                    PrefUtils.putValue(MainActivity.this, GlobalData.PATIENT_ID, userInfor
                            .getPatientId());
                    PrefUtils.putValue(MainActivity.this, GlobalData.PATIENTFAMILY_ID, userInfor
                            .getId());
                    PrefUtils.putValue(MainActivity.this, GlobalData.FAMILY_IMage, userInfor
                            .getImage
                                    ());
                    PrefUtils.putValue(MainActivity.this, GlobalData.XIAOYU_NAME, userInfor
                            .getXiaoyuName
                                    ());
                    PrefUtils.putValue(MainActivity.this, GlobalData.XIAOYU_NUMBER, userInfor
                            .getXiaoyuNum());
                }
            } else {
                String name = PrefUtils.getValue(MainActivity.this, GlobalData.NAME);
                if (!(name.equals(""))) {
                    userInfor.setPatientId(PrefUtils.getValue(MainActivity.this, GlobalData
                            .PATIENT_ID));
                    userInfor.setEmail(PrefUtils.getValue(MainActivity.this, GlobalData
                            .USer_email));
                    userInfor.setPhone(PrefUtils.getValue(MainActivity.this, GlobalData.Phone));
                    userInfor.setName(name);
                    userInfor.setXiaoyuName(PrefUtils.getValue(MainActivity.this, GlobalData
                            .XIAOYU_NAME));
                    userInfor.setXiaoyuNum(PrefUtils.getValue(MainActivity.this, GlobalData
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
                    handler.sendEmptyMessage(0x133);
                    nametext.setText(userInfor.getName());
                    connectXiaoyu(userInfor.getXiaoyuNum(), userInfor.getXiaoyuName());
                    if (userInfor.getImage() != null) {
                        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE +
                                userInfor.getImage());
                    }
                } else
                    Utils.showShortToast(MainActivity.this, "用户数据不存在");
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
