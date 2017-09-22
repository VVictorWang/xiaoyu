package com.victor.myclient.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.OneKeyWarning;
import com.victor.myclient.ui.adapters.BaojingInfoAdapter;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-4.
 */

public class BaoJingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private BaojingInfoAdapter adapter;
    private List<OneKeyWarning> oneKeyWarnings;
    private RelativeLayout back;
    private TextView no_data;
    Handler handler = new Handler() {
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
    private boolean has_data = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FindBaojingListTask().execute();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_baojing;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.baojing_information_list);
        no_data = (TextView) rootView.findViewById(R.id.fragment_baojing_no_data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        oneKeyWarnings = new ArrayList<>();
        back = (RelativeLayout) rootView.findViewById(R.id.fragment_baojing_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(activity);
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
            if (network) {
                String infor = Utils.sendRequest(GlobalData.GET_ONEKEY_WARNING + PrefUtils.getValue
                        (activity, GlobalData.PATIENT_ID));
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
            } else
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
