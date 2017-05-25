package com.victor.myclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.ainemo.sdk.otf.NemoSDKListener;
import com.ainemo.sdk.otf.VideoInfo;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.MyBitmapUtils;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.Datas.UserInfor;
import com.victor.myclient.Datas.XiaoYuNumber;
import com.victor.myclient.view.Case.CaseListActivity;
import com.victor.myclient.view.Contact.ContactActivity;
import com.victor.myclient.view.JiuJIA.JujiaActivity;
import com.victor.myclient.view.Setting.Setting_Activity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinkcool.circletextimageview.CircleTextImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.OkHttpClient;

/**
 * Created by victor on 2017/4/22.
 */


public class MainActivity extends AppCompatActivity {
    private OkHttpClient client;
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
    private CircleTextImageView caseforpatient;
    private CircleTextImageView callothers;
    private CircleTextImageView jujiamain;
    private CircleTextImageView settingdefault;

    private String image_url;
    private String user_name;
    private UserInfor userInfor;
    private List<XiaoYuNumber> xiaoYuNumbers;

    private boolean net_work_available,has_data;

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
        initData();
        initView();
        initEvent();
        NemoSDK.getInstance().connectNemo("victor", "18774259685", new ConnectNemoCallback() {
            @Override
            public void onFailed(int i) {
                Log.e(TAG,"fail");
            }

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "success");
            }
        });
        new GetUserInfor().execute(user_name);
    }

    private void initView() {
        this.settingdefault = (CircleTextImageView) findViewById(R.id.setting_default);
        this.jujiamain = (CircleTextImageView) findViewById(R.id.jujia_main);
        this.callothers = (CircleTextImageView) findViewById(R.id.call_others);
        this.caseforpatient = (CircleTextImageView) findViewById(R.id.case_for_patient);
        this.bangding_xiaoyu_number = (TextView) findViewById(R.id.bangding_xiaoyu_number);
        this.nametext = (TextView) findViewById(R.id.name_text);
        this.personimage = (CircleImageView) findViewById(R.id.person_image);
        if (image_url != null ) {
            this.personimage.setImageURI(Uri.fromFile(new File(image_url)));
        } else{
            this.personimage.setImageDrawable(getResources().getDrawable(R.drawable.person));
        }
        userInfor = new UserInfor();
        xiaoYuNumbers = new ArrayList<>();
        net_work_available= Utils.isNetWorkAvailabe(MainActivity.this);
    }

    private void resumeData() {

        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);
        personimage.setImageURI(Uri.fromFile(new File(Utils.getValue(MainActivity.this, GlobalData.Img_URl))));
        bitmapUtils.disPlay(personimage, GlobalData.GET_PATIENT_FAMILY_IMAGE + Utils.getValue(MainActivity.this, GlobalData.FAMILY_IMage));
    }

    private void initData() {
        image_url = Utils.getValue(MainActivity.this, GlobalData.Img_URl);
        user_name = Utils.getValue(MainActivity.this, GlobalData.NAME);
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
                String xiaoyu = Utils.sendRequest(GlobalData.GET_XIAO_YU_NUMBER + "type=phone&data=" + "13367379725");
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
                    bangding_xiaoyu_number.setText("绑定小鱼号: "+xiaoYuNumbers.get(0).getXiaoyuNum());
                    if (userInfor.getImage()!=null) {
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
