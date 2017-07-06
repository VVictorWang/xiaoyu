package com.victor.myclient.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.NemoSDK;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyBitmapUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.datas.UserInfor;
import com.victor.myclient.activity.Case.CaseListActivity;
import com.victor.myclient.activity.Contact.ContactActivity;
import com.victor.myclient.activity.Setting.Setting_Activity;
import com.google.gson.Gson;
import com.thinkcool.circletextimageview.CircleTextImageView;

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
    private TextView time_call;

    private String xiaoyuNumber;
    private String user_name;
    private boolean net_work_available, has_data;

    private UserInfor userInfor;

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
        ActivityManage.getInstance().pushActivity(MainActivity.this);
        initData();
        initView();
        initEvent();
        NemoSDK.getInstance().connectNemo("vic", "18774259685", new ConnectNemoCallback() {
            @Override
            public void onFailed(int i) {

                Log.e(TAG,"fail");
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
                        bangding_xiaoyu_number.setText("绑定小鱼号: "+xiaoyuNumber);
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
        time_call = (TextView) findViewById(R.id.time_call);
        setTime_call();
        userInfor = new UserInfor();
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
                Intent intent1 = new Intent(MainActivity.this, Setting_Activity.class);
                intent1.putExtra("email", userInfor.getEmail());
                startActivity(intent1);
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
