package com.victor.myclient.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.bumptech.glide.Glide;
import com.igexin.sdk.PushManager;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.UserInfor;
import com.victor.myclient.service.MyIntentService;
import com.victor.myclient.service.MyPushService;
import com.victor.myclient.service.PostClientIdService;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.ui.contract.MainContract;
import com.victor.myclient.ui.presenter.MainPresenter;
import com.victor.myclient.utils.CheckUtils;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.CircleImageView;
import com.victor.myclient.widget.CircleTextImageView;

import java.util.Timer;
import java.util.TimerTask;

import demo.animen.com.xiaoyutask.R;


/**
 * Created by victor on 2017/4/22.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, MainContract.View {
    public static final String TAG = "@victor MainActivity";
    private final static int REQUEST_PERMISSION = 1000;
    private CircleImageView personimage;
    private android.widget.TextView nametext;
    private android.widget.TextView xiaoYuText;
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
            }

        }
    };
    private MainContract.Presenter mPresenter;
    private TextView time_call;
    private String type = "username";
    private UserInfor userInfor;


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

        mPresenter = new MainPresenter(this);
        initEvent();
        mPresenter.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Activity getActivity() {
        return MainActivity.this;
    }


    @Override
    protected void initView() {
        this.callothers = (CircleTextImageView) findViewById(R.id.call_others);
        this.xiaoYuText = (TextView) findViewById(R.id.bangding_xiaoyu_number);
        this.nametext = (TextView) findViewById(R.id.name_text);
        this.personimage = (CircleImageView) findViewById(R.id.person_image);
        services = (CircleTextImageView) findViewById(R.id.services);
        jujiamain = (CircleTextImageView) findViewById(R.id.jujia_main);
        caseforpatient = (CircleTextImageView) findViewById(R.id.case_for_patient);
        setting = (CircleTextImageView) findViewById(R.id.setting_default);
        time_call = (TextView) findViewById(R.id.time_call);
        setTime_call();
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
        int minute = PrefUtils.getIntValue(MainActivity.this, GlobalData.ECLIPSE_TIME) / 60;
        time_call.setText("小鱼通话时长: " + minute + "分钟");

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
                if (!CheckUtils.isNull(userInfor)) {
                    intent.putExtra("id", userInfor.getPatientId());
                }
                MyActivityManager.startActivity(getActivity(), intent);
                break;
            case R.id.jujia_main:
                handler.sendEmptyMessage(0x125);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x126);
                    }
                }, 50);
                MyActivityManager.startActivity(getActivity(), JujiaActivity.class);
                break;
            case R.id.call_others:
                handler.sendEmptyMessage(0x123);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x124);
                    }
                }, 50);
                MyActivityManager.startActivity(getActivity(), ContactActivity.class);
                break;
            case R.id.setting_default:
                handler.sendEmptyMessage(0x129);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x130);
                    }
                }, 50);
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                if (!CheckUtils.isNull(userInfor)) {
                    intent1.putExtra("email", userInfor.getEmail());
                }
                MyActivityManager.startActivity(getActivity(), intent1);
                break;
            case R.id.services:
                handler.sendEmptyMessage(0x131);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0x132);
                    }
                }, 50);
                Intent intent2 = new Intent(getActivity(), ServiceHistoryActivity.class);
                if (!CheckUtils.isNull(userInfor)) {
                    intent2.putExtra("id", userInfor.getPatientId());
                }
                MyActivityManager.startActivity(getActivity(), intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setName(String name) {
        nametext.setText(name);
    }

    @Override
    public void connectXiaoYu(String xiaoNum, String xiaoName) {
        String name = xiaoName;
        String number = xiaoNum;
        if (xiaoName == null) {
            name = "victor";
            number = "18774259685";
        }
        NemoSDK.getInstance().connectNemo(name, number, new ConnectNemoCallback() {
            @Override
            public void onFailed(int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xiaoYuText.setText("登录失败");
                    }
                });
            }

            @Override
            public void onSuccess(String s) {
                final String reslut = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xiaoYuText.setText("绑定小鱼号: " + reslut);
                    }
                });
            }
        });
    }

    @Override
    public void showImage(String imageUrl) {
        Glide.with(this).
                load(imageUrl).
                fitCenter().
                error(R.drawable.image_load_error).
                into(personimage);
    }

    @Override
    public void showToast(String info) {
        Utils.showShortToast(getActivity(), info);
    }

    @Override
    public void startClientService() {
        Intent intent = new Intent(getActivity(), PostClientIdService.class);
        startService(intent);
    }

    @Override
    public void setUserInfo(UserInfor myUserInfo) {
        userInfor = myUserInfo;
    }
}
