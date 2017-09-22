package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.DoorInfor;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.ui.fragments.BaoJingFragment;
import com.victor.myclient.ui.fragments.RoomFragment;
import com.victor.myclient.ui.fragments.TemperatureFramgmentt;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends BaseActivity implements View.OnClickListener {


    private TextView door_status;
    private DoorInfor doorInfor;
    private boolean net_work;
    private View room, person, baojing;
    private List<Fragment> mFragments = new ArrayList<>();
    private int currentIndex;
    private Fragment mCurrentFragment;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia);
        MyActivityManager.getInstance().pushActivity(JujiaActivity.this);
        initView();
        initFragment();
        net_work = Utils.isNetWorkAvailabe(JujiaActivity.this);
        new getDoorInfor().execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jujia;
    }

    @Override
    protected Activity getActivity() {
        return JujiaActivity.this;
    }

    @Override
    protected void initView() {
        room = findViewById(R.id.room_status_btn);
        room.setTag(0);
        room.setOnClickListener(this);
        person = findViewById(R.id.person_activity_btn);
        person.setTag(1);
        person.setOnClickListener(this);
        baojing = findViewById(R.id.baojing_info_btn);
        baojing.setTag(2);
        baojing.setOnClickListener(this);

        door_status = (TextView) room.findViewById(R.id.room_status_text);
        doorInfor = new DoorInfor();
    }

    private void initFragment() {
        mFragments.add(new TemperatureFramgmentt());
        mFragments.add(new RoomFragment());
        mFragments.add(new BaoJingFragment());
        room.setSelected(true);
        changeTab(0);

    }

    private void changeTab(int index) {
        currentIndex = index;
        room.setSelected(index == 0);
        person.setSelected(index == 1);
        baojing.setSelected(index == 3);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        Fragment fragment = mFragments.get(currentIndex);
        mCurrentFragment = fragment;
        if (!fragment.isAdded()) {
            transaction.add(R.id.frame, fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        changeTab((int) v.getTag());
    }


    class getDoorInfor extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected void onPostExecute(Void aVoid) {
            if (doorInfor.getStatus() == 1) {
                door_status.setText("开启");
            } else {
                door_status.setText("关闭");
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String door_infor = Utils.sendRequest(GlobalData.GET_ROOM_STATUS + PrefUtils
                        .getValue
                                (JujiaActivity.this, GlobalData.PATIENT_ID));
                if (!door_infor.contains("not_exist")) {
                    doorInfor = gson.fromJson(door_infor, DoorInfor.class);
                    PrefUtils.putIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS, doorInfor
                            .getStatus());
                }
            } else {
                doorInfor.setStatus(PrefUtils.getIntValue(JujiaActivity.this, GlobalData
                        .DOOR_STATUS));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

