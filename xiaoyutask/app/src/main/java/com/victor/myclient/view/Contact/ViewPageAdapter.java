package com.victor.myclient.view.Contact;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by victor on 2017/4/24.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {
    public ViewPageAdapter(FragmentManager fm) {
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
