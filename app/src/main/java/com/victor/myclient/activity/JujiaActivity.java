package com.victor.myclient.activity;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.adapters.FragmentAdapter;
import com.victor.myclient.datas.DoorInfor;
import com.victor.myclient.fragments.BaoJingFragment;
import com.victor.myclient.fragments.RoomFragment;
import com.victor.myclient.fragments.TemperatureFramgmentt;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends FragmentActivity {

    private android.support.v4.view.ViewPager jiujiaviewpager;
    private android.support.design.widget.TabLayout fragjiujia;

    private TextView door_status;
    private TabLayout.Tab one, two, three;
    View room_status_parent, person_status_parent, warning_infor_parent;
    private RelativeLayout room_status, person_status, warning_infor;
    private Drawable jiujia_normal, jiujia_selected;
    private DoorInfor doorInfor;
    private boolean net_work;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia);
        ActivityManage.getInstance().pushActivity(JujiaActivity.this);
        initView();
        net_work = Utils.isNetWorkAvailabe(JujiaActivity.this);
        initTab();
        new getDoorInfor().execute();
        InitEvent();
    }

    private void initView() {
        jiujia_normal = getResources().getDrawable(R.drawable.jiujia_viewpage_shape);
        jiujia_selected = getResources().getDrawable(R.drawable.jiujia_view_pager_selected);
        this.fragjiujia = (TabLayout) findViewById(R.id.frag_jiujia);
        this.jiujiaviewpager = (ViewPager) findViewById(R.id.jiujia_view_pager);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        room_status_parent = layoutInflater.inflate(R.layout.room_status, null);
        person_status_parent = layoutInflater.inflate(R.layout.person_activity_status, null);
        warning_infor_parent = layoutInflater.inflate(R.layout.magic_status, null);
        room_status = (RelativeLayout) room_status_parent.findViewById(R.id.room_status);
        person_status = (RelativeLayout) person_status_parent.findViewById(R.id.person_activity);
        warning_infor = (RelativeLayout) warning_infor_parent.findViewById(R.id.warning_view_pager);
        door_status = (TextView) room_status_parent.findViewById(R.id.room_status_text);
        doorInfor = new DoorInfor();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initTab() {
        FragmentAdapter viewPageAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new TemperatureFramgmentt());
        viewPageAdapter.addFragment(new RoomFragment());
        viewPageAdapter.addFragment(new BaoJingFragment());
        jiujiaviewpager.setAdapter(viewPageAdapter);
        fragjiujia.setupWithViewPager(jiujiaviewpager);
        one = fragjiujia.getTabAt(0);
        two = fragjiujia.getTabAt(1);
        three = fragjiujia.getTabAt(2);
        room_status.setBackground(jiujia_selected);
        person_status.setBackground(jiujia_normal);
        warning_infor.setBackground(jiujia_normal);
        one.setCustomView(room_status_parent);
        two.setCustomView(person_status_parent);
        three.setCustomView(warning_infor_parent);
        jiujiaviewpager.setCurrentItem(0);
    }

    private void InitEvent() {
        fragjiujia.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == fragjiujia.getTabAt(0)) {
                    room_status.setBackground(jiujia_selected);
                    tab.setCustomView(room_status_parent);
                } else if (tab == fragjiujia.getTabAt(1)) {
                    person_status.setBackground(jiujia_selected);
                    tab.setCustomView(person_status_parent);
                } else if (tab == fragjiujia.getTabAt(2)) {
                    warning_infor.setBackground(jiujia_selected);
                    tab.setCustomView(warning_infor_parent);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == fragjiujia.getTabAt(0)) {
                    room_status.setBackground(jiujia_normal);
                    tab.setCustomView(room_status_parent);
                } else if (tab == fragjiujia.getTabAt(1)) {
                    person_status.setBackground(jiujia_normal);
                    tab.setCustomView(person_status_parent);
                } else if (tab == fragjiujia.getTabAt(2)) {
                    warning_infor.setBackground(jiujia_normal);
                    tab.setCustomView(warning_infor_parent);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
            one.setCustomView(room_status_parent);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String door_infor = Utils.sendRequest(GlobalData.GET_ROOM_STATUS + Utils.getValue(JujiaActivity.this, GlobalData.PATIENT_ID));
                if (!door_infor.contains("not_exist")) {
                    doorInfor = gson.fromJson(door_infor, DoorInfor.class);
                    Utils.putIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS, doorInfor.getStatus());
                }
            } else {
                doorInfor.setStatus(Utils.getIntValue(JujiaActivity.this, GlobalData.DOOR_STATUS));
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

