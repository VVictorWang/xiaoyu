package com.victor.myclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.victor.myclient.fragments.FragmentCall;
import com.victor.myclient.fragments.FragmentCallRecord;
import com.victor.myclient.fragments.FragmentContactList;

/**
 * Created by victor on 2017/4/24.
 */

public class CallPageAdapter extends FragmentPagerAdapter {
    public CallPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new FragmentContactList();
        } else if (position == 1) {
            fragment = new FragmentCall();
        } else if (position == 2) {
            fragment = new FragmentCallRecord();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
