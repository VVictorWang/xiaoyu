package com.victor.myclient.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.Datas.UserAcitivityInfo;
import com.victor.myclient.Utils.GlobalData;
import com.victor.myclient.Utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-20.
 */

public class Fragment_room extends Fragment {
    private Activity activity;
    private View view;

    private RelativeLayout back;

    private List<UserAcitivityInfo> userAcitivityInfos;
    private boolean net_work, has_data = false;
    private String[] room_name = {"卧室1", "客厅", "厨房", "卧室2", "卫生间", "卧室3", "储物间", "饭厅", "其他"};
    private TextView bedroom1,bedroom2,bedroom3,washingroom,living_room,store_room,dining_room, other,kitchen_room;
    Handler handler = new Handler(){
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        net_work = Utils.isNetWorkAvailabe(activity);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.fragment_room_status, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        InitView();
        new GetActivity().execute();
        return view;
    }

    private void InitView() {
      userAcitivityInfos = new ArrayList<>();
        back = (RelativeLayout) view.findViewById(R.id.fragment_room_back);
        bedroom1 = (TextView) view.findViewById(R.id.bed_room1_text);
        bedroom2 = (TextView) view.findViewById(R.id.bed_room2_text);
        bedroom3 = (TextView) view.findViewById(R.id.bed_room3_text);
        washingroom = (TextView) view.findViewById(R.id.washing_room_text);
        living_room = (TextView) view.findViewById(R.id.living_room_text);
        store_room = (TextView) view.findViewById(R.id.store_room_text);
        dining_room = (TextView) view.findViewById(R.id.dinig_room_text);
        other = (TextView) view.findViewById(R.id.other_room_text);
        kitchen_room = (TextView) view.findViewById(R.id.kitchen_room_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(activity);
            }
        });


    }

    class GetActivity extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();
        @Override
        protected void onPostExecute(Void aVoid) {
            if (has_data) {
                handler.sendEmptyMessage(0x123);
            }else
                handler.sendEmptyMessage(0x124);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String info = Utils.sendRequest(GlobalData.GET_ACTIVITIES + Utils.getValue(activity, GlobalData.PATIENT_ID));
                if (!info.contains("not_exist")) {
                    userAcitivityInfos = gson.fromJson(info, new TypeToken<List<UserAcitivityInfo>>() {
                    }.getType());
                    DataSupport.deleteAll(UserAcitivityInfo.class);
                    for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos) {
                        userAcitivityInfo.save();
                    }
                    has_data = true;
                } else if (DataSupport.isExist(UserAcitivityInfo.class)) {
                    List<UserAcitivityInfo> userAcitivityInfos1 = DataSupport.findAll(UserAcitivityInfo.class);
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
            }} else if (DataSupport.isExist(UserAcitivityInfo.class)) {
                List<UserAcitivityInfo> userAcitivityInfos1 = DataSupport.findAll(UserAcitivityInfo.class);
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
            }else
                has_data = false;
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

