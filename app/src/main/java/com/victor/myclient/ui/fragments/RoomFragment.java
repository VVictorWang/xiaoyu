package com.victor.myclient.ui.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.data.UserAcitivityInfo;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-20.
 */

public class RoomFragment extends BaseFragment {

    private RelativeLayout back;

    private List<UserAcitivityInfo> userAcitivityInfos;
    private boolean  has_data = false;
    private TextView bedroom1, bedroom2, bedroom3, washingroom, living_room, store_room,
            dining_room, other, kitchen_room;
    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos) {
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
                    }
                }
            } else if (msg.what == 0x124) {
            }
        }
    };



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetActivity().execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room_status;
    }

    @Override
    protected void initView() {
        userAcitivityInfos = new ArrayList<>();
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
                ActivityManage.finishActivity(activity);
            }
        });

    }


    private class GetActivity extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected void onPostExecute(Void aVoid) {
            if (has_data) {
                handler.sendEmptyMessage(0x123);
            } else
                handler.sendEmptyMessage(0x124);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (network) {
                String info = Utils.sendRequest(GlobalData.GET_ACTIVITIES + PrefUtils.getValue
                        (activity, GlobalData.PATIENT_ID));
                if (!info.contains("not_exist")) {
                    userAcitivityInfos = gson.fromJson(info, new
                            TypeToken<List<UserAcitivityInfo>>() {
                            }.getType());
                    DataSupport.deleteAll(UserAcitivityInfo.class);
                    for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos) {
                        userAcitivityInfo.save();
                    }
                    has_data = true;
                } else if (DataSupport.isExist(UserAcitivityInfo.class)) {
                    List<UserAcitivityInfo> userAcitivityInfos1 = DataSupport.findAll
                            (UserAcitivityInfo.class);
                    for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos1) {
                        boolean addabe = true;
                        for (UserAcitivityInfo userAcitivityInfo1 : userAcitivityInfos) {
                            if (userAcitivityInfo.getRoom().equals(userAcitivityInfo1.getRoom())) {
                                addabe = false;
                                break;
                            }
                        }
                        if (addabe) {
                            userAcitivityInfos.add(userAcitivityInfo);
                        }
                    }
                }
            } else if (DataSupport.isExist(UserAcitivityInfo.class)) {
                List<UserAcitivityInfo> userAcitivityInfos1 = DataSupport.findAll
                        (UserAcitivityInfo.class);
                for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos1) {
                    boolean addable = true;
                    for (UserAcitivityInfo userAcitivityInfo1 : userAcitivityInfos) {
                        if (userAcitivityInfo.getRoom().equals(userAcitivityInfo1.getRoom())) {
                            addable = false;
                            break;
                        }
                    }
                    if (addable) {
                        userAcitivityInfos.add(userAcitivityInfo);
                    }
                }
                has_data = true;
            } else
                has_data = false;
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

