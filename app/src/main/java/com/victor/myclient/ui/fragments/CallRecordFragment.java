package com.victor.myclient.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.victor.myclient.bean.CallRecord;
import com.victor.myclient.ui.adapters.CallRecordAdapter;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.Utils;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecordFragment extends BaseFragment {

    private CallRecordAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact_record;
    }

    @Override
    public void onResume() {
        super.onResume();
        getInfo();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getInfo();
    }

    @Override
    protected void initView() {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.record_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CallRecordAdapter(activity);
        recyclerView.setAdapter(adapter);
        RelativeLayout back = (RelativeLayout) rootView.findViewById(R.id.back_to_main_contact_list);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivityManager.finishActivity(activity);
            }
        });
    }

    private void getInfo() {
        Observable.from(DataSupport.findAll(CallRecord.class)).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toSortedList(new Func2<CallRecord, CallRecord, Integer>() {
                    @Override
                    public Integer call(CallRecord callRecord, CallRecord callRecord2) {
                        Date date1 = callRecord.getDate();
                        Date date2 = callRecord2.getDate();
                        if (date1.getTime() < date2.getTime()) {
                            return 1;
                        } else if (date1.getTime() > date2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                })
                .subscribe(new Observer<List<CallRecord>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(activity, "发生错误");
                    }

                    @Override
                    public void onNext(List<CallRecord> callRecords) {
                        adapter.addItems(callRecords);
                    }
                });
    }
}
