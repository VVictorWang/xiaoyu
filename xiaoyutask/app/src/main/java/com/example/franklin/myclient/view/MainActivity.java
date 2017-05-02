package com.example.franklin.myclient.view;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.UserInfor;
import com.example.franklin.myclient.view.Case.CaseListActivity;
import com.example.franklin.myclient.view.Contact.ContactActivity;
import com.google.gson.Gson;
import com.thinkcool.circletextimageview.CircleTextImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

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

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
        initData();
        new GetUserInfor().execute(user_name);
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
        client = new OkHttpClient();
        if (image_url != null || image_url.equals("")) {
            this.personimage.setImageURI(Uri.fromFile(new File(image_url)));
        } else if (image_url.equals("")) {
            this.personimage.setImageDrawable(getResources().getDrawable(R.drawable.person));
        }
    }

    private void resumeData() {
        image_url = Utils.getValue(MainActivity.this, GlobalData.Img_URl);
        if (image_url != null) {
            this.personimage.setImageURI(Uri.fromFile(new File(image_url)));
        }
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

        @Override
        protected String doInBackground(String... params) {
//            Log.e("data ", sendRequest());
            userInfor = gson.fromJson(Utils.sendRequest(GlobalData.GET_USR_INFOR+"FamilyName="+"admin"), UserInfor.class);

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
                nametext.setText(userInfor.getName());

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
