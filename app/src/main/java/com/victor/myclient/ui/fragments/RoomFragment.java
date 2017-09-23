package com.victor.myclient.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.UserAcitivityInfo;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 17-5-20.
 */

public class RoomFragment extends BaseFragment {

    private RelativeLayout back;

    private TextView bedroom1, bedroom2, bedroom3, washingroom, living_room, store_room,
            dining_room, other, kitchen_room;


    private String patientId = null;


    public static RoomFragment newInstance(String patientId) {
        Bundle args = new Bundle();
        args.putString(GlobalData.PATIENT_ID, patientId);
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            patientId = getArguments().getString(GlobalData.PATIENT_ID);
        }
        super.onActivityCreated(savedInstanceState);
        getInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room_status;
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) rootView.findViewById(R.id.fragment_room_back);
        bedroom1 = (TextView) rootView.findViewById(R.id.bed_room1_text);
        bedroom2 = (TextView) rootView.findViewById(R.id.bed_room2_text);
        bedroom3 = (TextView) rootView.findViewById(R.id.bed_room3_text);
        washingroom = (TextView) rootView.findViewById(R.id.washing_room_text);
        living_room = (TextView) rootView.findViewById(R.id.living_room_text);
        store_room = (TextView) rootView.findViewById(R.id.store_room_text);
        dining_room = (TextView) rootView.findViewById(R.id.dinig_room_text);
        other = (TextView) rootView.findViewById(R.id.other_room_text);
        kitchen_room = (TextView) rootView.findViewById(R.id.kitchen_room_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(activity);
            }
        });

    }

    private void getInfo() {
        Observable<List<UserAcitivityInfo>> observable = UserApi.getInstance()
                .getUserActivities(Integer.valueOf(patientId != null ? patientId : PrefUtils
                        .getValue(activity, GlobalData.PATIENT_ID)));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<UserAcitivityInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(activity, "没有数据");
                    }

                    @Override
                    public void onNext(List<UserAcitivityInfo> userAcitivityInfos) {
                        refreshDate(userAcitivityInfos);
                    }
                });


    }

    private void refreshDate(List<UserAcitivityInfo> userAcitivityInfos) {
        Observable.from(userAcitivityInfos).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserAcitivityInfo>() {
                    @Override
                    public void call(UserAcitivityInfo userAcitivityInfo) {
                        int room = Integer.parseInt(userAcitivityInfo.getRoom());
                        int number = Integer.parseInt(userAcitivityInfo.getNum());
                        switch (room) {
                            case 0:
                                bedroom1.setText("" + number);
                                break;
                            case 1:
                                living_room.setText("" + number);
                                break;
                            case 2:
                                kitchen_room.setText("" + number);
                                break;
                            case 3:
                                bedroom2.setText("" + number);
                                break;
                            case 4:
                                washingroom.setText("" + number);
                                break;
                            case 5:
                                bedroom3.setText("" + number);
                                break;
                            case 6:
                                store_room.setText("" + number);
                                break;
                            case 7:
                                dining_room.setText("" + number);
                                break;
                            case 8:
                                other.setText("" + number);
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


}

