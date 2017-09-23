package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.DoorInfor;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.ui.fragments.RoomFragment;
import com.victor.myclient.ui.fragments.TemperatureFramgment;
import com.victor.myclient.ui.fragments.WarnningFragment;
import com.victor.myclient.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends BaseActivity implements View.OnClickListener {


    private TextView door_status;
    private View room, person, baojing;
    private List<Fragment> mFragments = new ArrayList<>();
    private Fragment mCurrentFragment;
    private String patientId = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            patientId = getIntent().getStringExtra("id");
        }
        super.onCreate(savedInstanceState);
        initFragment();
        getInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jujia;
    }

    @Override
    protected Activity getActivity() {
        return JujiaActivity.this;
    }

    @Override
    protected void initView() {
        room = findViewById(R.id.room_status_btn);
        room.setTag(0);
        room.setOnClickListener(this);
        person = findViewById(R.id.person_activity_btn);
        person.setTag(1);
        person.setOnClickListener(this);
        baojing = findViewById(R.id.baojing_info_btn);
        baojing.setTag(2);
        baojing.setOnClickListener(this);

        door_status = (TextView) room.findViewById(R.id.room_status_text);
    }

    private void initFragment() {
        mFragments.add(TemperatureFramgment.newInstance(patientId));
        mFragments.add(RoomFragment.newInstance(patientId));
        mFragments.add(WarnningFragment.newInstance(patientId));
        room.setSelected(true);
        changeTab(0);
    }

    private void changeTab(int index) {
        room.setSelected(index == 0);
        person.setSelected(index == 1);
        baojing.setSelected(index == 3);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        Fragment fragment = mFragments.get(index);
        mCurrentFragment = fragment;
        if (!fragment.isAdded()) {
            transaction.add(R.id.frame, fragment);
        } else {
            transaction.show(fragment);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        changeTab((int) v.getTag());
    }

    private void getInfo() {
        Observable<DoorInfor> observable = UserApi.getInstance().getDoorInfo(Integer.valueOf
                (patientId));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DoorInfor>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(getActivity(), "发生错误");
                    }

                    @Override
                    public void onNext(DoorInfor doorInfor) {
                        door_status.setText(doorInfor.getStatus() == 1 ? "开启" : "关闭");
                    }
                });
    }


}

