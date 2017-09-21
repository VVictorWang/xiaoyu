package com.victor.myclient.ui.presenter;

import com.victor.myclient.MyApplication;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.data.UserInfor;
import com.victor.myclient.ui.contract.MainContract;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        String name = "";
        if (mView.getType().equals("phone")) {
            name = PrefUtils.getValue(MyApplication.getContext(), GlobalData.Phone);
        } else {
            name = PrefUtils.getValue(MyApplication.getContext(), GlobalData.NAME);
        }
        Observable<UserInfor> userInforObservable = UserApi.getInstance().getUserInfo(name, mView
                .getType());
        userInforObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfor>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserInfor userInfor) {

                    }
                });
    }
}
