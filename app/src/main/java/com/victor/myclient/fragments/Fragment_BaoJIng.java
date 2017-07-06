package com.victor.myclient.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.datas.OneKeyWarning;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.adapters.BaojingInfoAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-4.
 */

public class Fragment_BaoJIng extends Fragment {
    private Activity activity;
    private View view;

    private RecyclerView recyclerView;
    private BaojingInfoAdapter adapter;
    private List<OneKeyWarning> oneKeyWarnings;
    private RelativeLayout back;
    private TextView no_data;

    private boolean net_work,has_data = false;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                no_data.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                InitData();
            } else if (msg.what == 0x124) {
                no_data.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        net_work = Utils.isNetWorkAvailabe(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.fragment_baojing, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        InitView();

        new FindBaojingListTask().execute();
        return view;
    }

    private void InitView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.baojing_information_list);
        no_data = (TextView) view.findViewById(R.id.fragment_baojing_no_data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        oneKeyWarnings = new ArrayList<>();
        back = (RelativeLayout) view.findViewById(R.id.fragment_baojing_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(activity);
            }
        });
        oneKeyWarnings = new ArrayList<>();
    }

    private void InitData() {
        adapter = new BaojingInfoAdapter(activity, oneKeyWarnings);
        recyclerView.setAdapter(adapter);
    }

    class FindBaojingListTask extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();
        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String infor = Utils.sendRequest(GlobalData.GET_ONEKEY_WARNING + Utils.getValue(activity, GlobalData.PATIENT_ID));
                if (!infor.contains("not_exist")) {
                    oneKeyWarnings = gson.fromJson(infor, new TypeToken<List<OneKeyWarning>>() {
                    }.getType());
                    DataSupport.deleteAll(OneKeyWarning.class);
                    for (OneKeyWarning oneKeyWarning : oneKeyWarnings) {
                        oneKeyWarning.save();
                    }
                    has_data = true;
                } else if (DataSupport.isExist(OneKeyWarning.class)) {
                    oneKeyWarnings = DataSupport.findAll(OneKeyWarning.class);
                    has_data = true;
                }

            } else if (DataSupport.isExist(OneKeyWarning.class)) {
                oneKeyWarnings = DataSupport.findAll(OneKeyWarning.class);
                has_data = true;
            }else
                has_data = false;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (has_data) {
                handler.sendEmptyMessage(0x123);
            } else {
                handler.sendEmptyMessage(0x124);
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
