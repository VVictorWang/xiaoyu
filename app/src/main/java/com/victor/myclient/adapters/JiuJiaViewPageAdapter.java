package com.victor.myclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.victor.myclient.fragments.Fragment_BaoJIng;
import com.victor.myclient.fragments.Fragment_room;
import com.victor.myclient.fragments.Framgment_tempera;

/**
 * Created by victor on 17-5-3.
 */

public class JiuJiaViewPageAdapter extends FragmentPagerAdapter {
    private Fragment temperature,room, baojin;

    public JiuJiaViewPageAdapter(FragmentManager fm) {
        super(fm);
        temperature = new Framgment_tempera();
        room = new Fragment_room();
        baojin = new Fragment_BaoJIng();
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return temperature;
        } else if (position == 1) {
            return room;
        } else if (position == 2) {
            return baojin;
        }
        return null;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
