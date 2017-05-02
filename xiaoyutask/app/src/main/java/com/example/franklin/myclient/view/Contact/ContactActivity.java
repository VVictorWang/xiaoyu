package com.example.franklin.myclient.view.Contact;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import demo.animen.com.xiaoyutask.R;
/**
 * Created by victor on 2017/4/24.
 */


public class ContactActivity extends FragmentActivity {

    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private TabLayout tabLayout;
    private TabLayout.Tab one,two, three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.contact_toolbar_sample);
//        toolbar.inflateMenu(R.menu.contact_menu);
//        setSupportActionBar(toolbar);

//        layoutManager = new LinearLayoutManager(ContactActivity.this);

        initTab();
        initEvent();
    }

    private void  initEvent(){
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_selected));
                    viewPager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_selected));
                    viewPager.setCurrentItem(1);
                } else if (tab == tabLayout.getTabAt(2)) {
                    three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_selected));
                    viewPager.setCurrentItem(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_nor));
                } else if (tab == tabLayout.getTabAt(1)) {
                    two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_nor));
                } else if (tab == tabLayout.getTabAt(2)) {
                    three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_nor));
                }

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void  initTab(){
        viewPager = (ViewPager) findViewById(R.id.contact_view_pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        tabLayout = (TabLayout) findViewById(R.id.frag);
        tabLayout.setupWithViewPager(viewPager);
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        three = tabLayout.getTabAt(2);
        one.setIcon(getResources().getDrawable(R.drawable.contact_bottom_first_selected));
        viewPager.setCurrentItem(0);
        two.setIcon(getResources().getDrawable(R.drawable.contact_bottom_second_nor));
        three.setIcon(getResources().getDrawable(R.drawable.contact_bottom_third_nor));

//        getSupportFragmentManager().beginTransaction().add(R.id.frag, fragmentContactList).show(fragmentContactList).commit();
    }



}
