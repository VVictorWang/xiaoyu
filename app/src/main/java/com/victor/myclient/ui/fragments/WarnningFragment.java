package com.victor.myclient.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.OneKeyWarning;
import com.victor.myclient.ui.adapters.WarnningInfoAdapter;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 17-5-4.
 */

public class WarnningFragment extends BaseFragment {

    private static String PATIENTID = "patientId";
    private RecyclerView recyclerView;
    private WarnningInfoAdapter adapter;
    private RelativeLayout back;
    private TextView no_data;
    private String patientId = null;

    public static WarnningFragment newInstance(String patientId) {
        Bundle args = new Bundle();
        args.putString(PATIENTID, patientId);
        WarnningFragment fragment = new WarnningFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getString(PATIENTID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getInfo();
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
        back = (RelativeLayout) rootView.findViewById(R.id.fragment_baojing_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(activity);
            }
        });
        adapter = new WarnningInfoAdapter(activity);
        recyclerView.setAdapter(adapter);
    }


    private void getInfo() {
        String key = Utils.createAcacheKey("get-onekey-warnning", patientId);
        Observable<List<OneKeyWarning>> oneKeyWarningObservable = UserApi.getInstance()
                .getOneKeyWarnning(Integer.valueOf(patientId != null ? patientId : PrefUtils
                        .getValue(activity, GlobalData.PATIENT_ID))).compose(RxUtil
                        .<List<OneKeyWarning>>rxCacheBeanHelper(key));
        Observable.concat(oneKeyWarningObservable, (Observable<List<OneKeyWarning>>) RxUtil
                .rxCreateDiskObservable(key, new
                TypeToken<List<OneKeyWarning>>() {
                }.getType())).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<OneKeyWarning>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(activity, "设备不存在");
                    }

                    @Override
                    public void onNext(List<OneKeyWarning> oneKeyWarnings) {
                        if (oneKeyWarnings.isEmpty()) {
                            no_data.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        } else {
                            adapter.addItems(oneKeyWarnings);
                        }
                    }
                });

    }
}

