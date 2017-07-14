package com.victor.myclient.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 7/7/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


}
