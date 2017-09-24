package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.ServiceHistory;
import com.victor.myclient.ui.adapters.HistoryListAdapter;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Silver on 2017/7/13.
 */

public class ServiceHistoryActivity extends BaseActivity implements HistoryListAdapter
        .MyClickListener {
    private int patientId;
    private List<ServiceHistory> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryListAdapter adapter;
    private RelativeLayout back_to_main;
    private TextView empty;
    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            patientId = Integer.parseInt(getIntent().getStringExtra("id"));
        }
        super.onCreate(savedInstanceState);
        getInfo();
        initEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected Activity getActivity() {
        return ServiceHistoryActivity.this;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        back_to_main = (RelativeLayout) findViewById(R.id.back_to_main_history);
        empty = (TextView) findViewById(R.id.empty);
        LinearLayoutManager manager = new LinearLayoutManager(ServiceHistoryActivity.this);
        recyclerView.setLayoutManager(manager);
        adapter = new HistoryListAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(ServiceHistoryActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ServiceHistoryActivity.this, ImageDetailActivity.class);
        intent.putExtra("url", GlobalData.GET_IMAGE + list.get(position).getServiceContent());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getInfo() {
        String key = Utils.createAcacheKey("getSearchHistory", patientId);
        Observable<List<ServiceHistory>> observable = UserApi.getInstance().getSeachHistory
                (patientId).compose(RxUtil.<List<ServiceHistory>>rxCacheBeanHelper(key));
        mSubscription = Observable.concat((Observable<List<ServiceHistory>>) RxUtil
                .rxCreateDiskObservable(key, new
                TypeToken<List<ServiceHistory>>() {
                }.getType()), observable).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<ServiceHistory>>() {
                    @Override
                    public void call(List<ServiceHistory> serviceHistories) {
                        Collections.sort(serviceHistories, new Comparator<ServiceHistory>() {
                            @Override
                            public int compare(ServiceHistory o1, ServiceHistory o2) {
                                if (o1.getServiceDatetime().compareTo(o2.getServiceDatetime()) <
                                        0) {
                                    return 1;
                                } else if (o1.getServiceDatetime().compareTo
                                        (o2.getServiceDatetime()) > 0) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                    }
                })
                .subscribe(new Observer<List<ServiceHistory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ServiceHistory> serviceHistories) {
                        if (serviceHistories.isEmpty()) {
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            adapter.addItems(serviceHistories);
                            list = serviceHistories;
                        }
                    }
                });
    }

}
