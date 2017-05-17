package com.victor.myclient.view.JiuJIA;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import demo.animen.com.xiaoyutask.R;
/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends FragmentActivity {

    private android.support.v4.view.ViewPager jiujiaviewpager;
    private android.support.design.widget.TabLayout fragjiujia;
    private JiuJiaViewPageAdapter viewPageAdapter;
    private TabLayout.Tab one, two, three;

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

    private void initTab() {
        viewPageAdapter = new JiuJiaViewPageAdapter(getSupportFragmentManager());
        jiujiaviewpager.setAdapter(viewPageAdapter);
        fragjiujia.setupWithViewPager(jiujiaviewpager);
        one = fragjiujia.getTabAt(0);
        two = fragjiujia.getTabAt(1);
        three = fragjiujia.getTabAt(2);
        one.setCustomView(R.layout.root_status);
        two.setCustomView(R.layout.person_activity_status);
        three.setCustomView(R.layout.magic_status);
        jiujiaviewpager.setCurrentItem(0);
    }
}

