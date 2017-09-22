package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.CaseInfor;
import com.victor.myclient.ui.adapters.CaseAdapter;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.CustomLayoutManager;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 2017/4/27.
 */

public class CaseListActivity extends BaseActivity {
    public static final String TAG = "@victor CaseListvity";
    public RecyclerView recyclerView;
    private RelativeLayout back;
    private String patientId;
    private CaseAdapter adapter;
    private ProgressDialog progressDialog;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            patientId = getIntent().getStringExtra("id");
        }
        super.onCreate(savedInstanceState);
        if (patientId == null || patientId.equals("")) {
            Utils.showShortToast(CaseListActivity.this, "患者没有病例信息");
        } else {
            initEvent();
            getInfo();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.case_list;
    }

    @Override
    protected Activity getActivity() {
        return CaseListActivity.this;
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.case_list_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLayoutManager manager = new CustomLayoutManager();
        recyclerView.setLayoutManager(manager);
        adapter = new CaseAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }


    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(CaseListActivity.this);
            }
        });
    }

    private void getInfo() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        Observable<List<CaseInfor>> observable = UserApi.getInstance().getCaseInfo(Integer
                .valueOf(patientId));
        mSubscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<CaseInfor>>() {
                    @Override
                    public void call(List<CaseInfor> caseInfors) {
                        Observable.from(caseInfors).subscribeOn(Schedulers.io())
                                .map(new Func1<CaseInfor, Void>() {

                                    @Override
                                    public Void call(CaseInfor caseInfor) {
                                        if (!caseInfor.isSaved()) {
                                            caseInfor.save();
                                        }
                                        return null;
                                    }
                                }).subscribe();
                    }
                })
                .subscribe(new Observer<List<CaseInfor>>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(getActivity(), "获取信息失败");
                    }

                    @Override
                    public void onNext(List<CaseInfor> caseInfors) {
                        adapter.addItems(caseInfors);
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
}
