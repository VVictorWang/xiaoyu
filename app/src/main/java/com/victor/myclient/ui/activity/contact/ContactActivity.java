package com.victor.myclient.ui.activity.contact;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.victor.myclient.ActivityManage;
import com.victor.myclient.ui.adapters.FragmentAdapter;
import com.victor.myclient.ui.fragments.CallFragment;
import com.victor.myclient.ui.fragments.CallRecordFragment;
import com.victor.myclient.ui.fragments.ContactListFragment;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */


public class ContactActivity extends FragmentActivity {

    private ViewPager viewPager;
    private FragmentAdapter viewPageAdapter;
    private TabLayout tabLayout;
    private TabLayout.Tab one, two, three;
    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment_main);
        ActivityManage.getInstance().pushActivity(ContactActivity.this);
        initTab();
        initEvent();
    }


    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    private void initEvent() {
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
    private void initTab() {
        viewPager = (ViewPager) findViewById(R.id.contact_view_pager);
        viewPageAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPageAdapter.addFragment(new ContactListFragment());
        viewPageAdapter.addFragment(new CallFragment());
        viewPageAdapter.addFragment(new CallRecordFragment());
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
    }


}
