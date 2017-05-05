package com.example.franklin.myclient.view.JiuJIA;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.BesselChart;
import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.Point;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends FragmentActivity{

    BesselChart chart;


//    private Chart mChart;

    Handler handler = new Handler();
    private BarChart barChart;
    private RelativeLayout back;
    private TextView update;

    private List<Point> points = new ArrayList<>();
    private android.support.v4.view.ViewPager jiujiaviewpager;
    private android.support.design.widget.TabLayout fragjiujia;

    private JiuJiaViewPageAdapter viewPageAdapter;
    private TabLayout.Tab one,two, three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia);
        initView();
        initTab();


    }

    private void initView() {
        this.fragjiujia = (TabLayout) findViewById(R.id.frag_jiujia);
        this.jiujiaviewpager = (ViewPager) findViewById(R.id.jiujia_view_pager);
    }

    private void  initTab(){
        viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
       jiujiaviewpager.setAdapter(viewPageAdapter);
        fragjiujia.setupWithViewPager(jiujiaviewpager);
        one = fragjiujia.getTabAt(0);
        two = fragjiujia.getTabAt(1);
        three = fragjiujia.getTabAt(2);
//        one.setIcon(getResources().getDrawable(R.drawable.room_status));
        one.setCustomView(R.layout.root_status);
        two.setCustomView(R.layout.person_activity_status);
        three.setCustomView(R.layout.magic_status);
        jiujiaviewpager.setCurrentItem(0);
//        two.setIcon(getResources().getDrawable(R.drawable.people_activity));
//        three.setIcon(getResources().getDrawable(R.drawable.magic_magic));

//        getSupportFragmentManager().beginTransaction().add(R.id.frag, fragmentContactList).show(fragmentContactList).commit();
    }


}

