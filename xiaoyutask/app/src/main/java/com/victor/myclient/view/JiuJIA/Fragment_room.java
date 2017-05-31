package com.victor.myclient.view.JiuJIA;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.Datas.UserAcitivityInfo;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-20.
 */

public class Fragment_room extends Fragment {
    private Activity activity;
    private View view;
    private FlowLayout layout;

    private RelativeLayout back;

    private List<UserAcitivityInfo> userAcitivityInfos;
    private boolean net_work, has_data = false;
    private String[] room_name = {"卧室1", "客厅", "厨房", "卧室2", "卫生间", "卧室3", "储物间", "饭厅", "其他"};
    Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                for (UserAcitivityInfo userAcitivityInfo : userAcitivityInfos) {
                    int room = Integer.parseInt(userAcitivityInfo.getRoom());
                    int number = Integer.parseInt(userAcitivityInfo.getNum());
                    if (number != 0) {
                        RecView recView = new RecView(activity, room_name[room], number);
//                        Random random = new Random();
                        // 设置彩色背景
                        GradientDrawable normalDrawable = new GradientDrawable();
                        normalDrawable.setShape(GradientDrawable.RECTANGLE);
                        int background = activity.getResources().getColor(R.color.my_background);
                        normalDrawable.setColor(background);

                        // 设置按下的灰色背景
                        GradientDrawable pressedDrawable = new GradientDrawable();
                        pressedDrawable.setShape(GradientDrawable.RECTANGLE);
                        pressedDrawable.setColor(Color.GRAY);

                        // 背景选择器
                        StateListDrawable stateDrawable = new StateListDrawable();
                        stateDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
                        stateDrawable.addState(new int[]{}, normalDrawable);
                        recView.setBackground(stateDrawable);
                        layout.addView(recView);
                    }
                }
            } else if (msg.what == 0x124) {
                Utils.showShortToast(activity, "没有数据");
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
        layout = (FlowLayout) view.findViewById(R.id.flow_layout);
      userAcitivityInfos = new ArrayList<>();
        back = (RelativeLayout) view.findViewById(R.id.fragment_room_back);
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
                userAcitivityInfos = gson.fromJson(info, new TypeToken<List<UserAcitivityInfo>>() {
                }.getType());
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

