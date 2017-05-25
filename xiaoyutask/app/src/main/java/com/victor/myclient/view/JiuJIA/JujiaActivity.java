package com.victor.myclient.view.JiuJIA;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import demo.animen.com.xiaoyutask.R;
/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends FragmentActivity {

    private android.support.v4.view.ViewPager jiujiaviewpager;
    private android.support.design.widget.TabLayout fragjiujia;
    private JiuJiaViewPageAdapter viewPageAdapter;
    private TabLayout.Tab one, two, three;
    View room_status_parent,person_status_parent,warning_infor_parent;
    private RelativeLayout room_status,person_status, warning_infor;
    private Drawable jiujia_normal, jiujia_selected;
    private int color_normal, color_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia);

        initView();
        initTab();
    InitEvent();

    }

    private void initView() {
        jiujia_normal = getResources().getDrawable(R.drawable.jiujia_viewpage_shape);
        jiujia_selected = getResources().getDrawable(R.drawable.jiujia_view_pager_selected);
        color_normal = getResources().getColor(R.color.my_background);
        color_selected = getResources().getColor(R.color.jiujia_selected);
        this.fragjiujia = (TabLayout) findViewById(R.id.frag_jiujia);
        this.jiujiaviewpager = (ViewPager) findViewById(R.id.jiujia_view_pager);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        room_status_parent= layoutInflater.inflate(R.layout.root_status,null);
        person_status_parent = layoutInflater.inflate(R.layout.person_activity_status, null);
        warning_infor_parent = layoutInflater.inflate(R.layout.magic_status, null);
        room_status =(RelativeLayout) room_status_parent.findViewById(R.id.room_status);
        person_status = (RelativeLayout) person_status_parent.findViewById(R.id.person_activity);
        warning_infor = (RelativeLayout) warning_infor_parent.findViewById(R.id.warning_view_pager);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initTab() {
        viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
        jiujiaviewpager.setAdapter(viewPageAdapter);
        fragjiujia.setupWithViewPager(jiujiaviewpager);
        one = fragjiujia.getTabAt(0);
        two = fragjiujia.getTabAt(1);
        three = fragjiujia.getTabAt(2);
        room_status.setBackground(jiujia_selected);
        person_status.setBackground(jiujia_normal);
        warning_infor.setBackground(jiujia_normal);

//        room_status.setBackgroundColor(color_selected);
//        person_status.setBackgroundColor(color_normal);
//        warning_infor.setBackgroundColor(color_normal);
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
//                    room_status.setBackgroundColor(color_selected);
                    tab.setCustomView(room_status_parent);
                } else if (tab == fragjiujia.getTabAt(1)) {
                    person_status.setBackground(jiujia_selected);
//                    person_status.setBackgroundColor(color_selected);
                    tab.setCustomView(person_status_parent);
                } else if (tab == fragjiujia.getTabAt(2)) {
                    warning_infor.setBackground(jiujia_selected);
//                    warning_infor.setBackgroundColor(color_selected);
                    tab.setCustomView(warning_infor_parent);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == fragjiujia.getTabAt(0)) {
                    room_status.setBackground(jiujia_normal);
//                    room_status.setBackgroundColor(color_normal);
                    tab.setCustomView(room_status_parent);
                } else if (tab == fragjiujia.getTabAt(1)) {
                    person_status.setBackground(jiujia_normal);
//                    person_status.setBackgroundColor(color_normal);
                    tab.setCustomView(person_status_parent);
                } else if (tab == fragjiujia.getTabAt(2)) {
                    warning_infor.setBackground(jiujia_normal);
//                    warning_infor.setBackgroundColor(color_normal);
                    tab.setCustomView(warning_infor_parent);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

