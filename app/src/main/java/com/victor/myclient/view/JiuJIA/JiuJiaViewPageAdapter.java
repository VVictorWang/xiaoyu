package com.victor.myclient.view.JiuJIA;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by victor on 17-5-3.
 */

public class JiuJiaViewPageAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"居家参数","人员活动","魔方感应"};
    public JiuJiaViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public JiuJiaViewPageAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new Framgment_tempera();
        } else if (position == 1) {
            return new Fragment_room();
        } else if (position == 2) {
            return new Fragment_BaoJIng();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
