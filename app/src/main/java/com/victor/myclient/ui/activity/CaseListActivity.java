package com.victor.myclient.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.CaseInfor;
import com.victor.myclient.ui.adapters.CaseAdapter;
import com.victor.myclient.ui.base.BaseActivity;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.CustomLayoutManager;

import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
        String key = Utils.createAcacheKey("get-cases", patientId);
        Observable<List<CaseInfor>> observable = UserApi.getInstance().getCaseInfo(Integer
                .valueOf(patientId)).compose(RxUtil.<List<CaseInfor>>rxCacheBeanHelper(key));
        mSubscription = Observable.concat((Observable<List<CaseInfor>>) RxUtil
                .rxCreateDiskObservable(key, new TypeToken<List<CaseInfor>>() {
        }.getType()), observable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<CaseInfor>>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
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
