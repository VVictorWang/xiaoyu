package com.victor.myclient.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.CallRecord;
import com.victor.myclient.ui.adapters.CallRecordAdapter;
import com.victor.myclient.ui.base.BaseFragment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecordFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private CallRecordAdapter adapter;
    private List<CallRecord> list = new ArrayList<>();

    private RelativeLayout back;

    private RecyclerView.LayoutManager layoutManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                adapter = new CallRecordAdapter(activity, list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } else if (msg.what == 0x124) {
//                Utils.showShortToast(activity, "没有数据");
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_record;
    }

    @Override
    public void onResume() {
        super.onResume();
        new FindRecordsTask().execute(1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();


        new FindRecordsTask().execute(1);

    }

    @Override
    protected void initView() {


        recyclerView = (RecyclerView) rootView.findViewById(R.id.record_list);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        back = (RelativeLayout) rootView.findViewById(R.id.back_to_main_contact_list);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivityManager.finishActivity(activity);
            }
        });
    }

    public class FindRecordsTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... params) {
            if (DataSupport.isExist(CallRecord.class)) {
                list = DataSupport.findAll(CallRecord.class);
            } else
                return 0;
            if (list != null) {
                Collections.sort(list, new Comparator<CallRecord>() {
                    @Override
                    public int compare(CallRecord callRecord, CallRecord t1) {
                        Date date1 = callRecord.getDate();
                        Date date2 = t1.getDate();
                        if (date1.getTime() < date2.getTime()) {
                            return 1;
                        } else if (date1.getTime() > date2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }

                });

            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                handler.sendEmptyMessage(0x123);

            } else if (integer == 0) {
//                handler.sendEmptyMessage(0x124);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
