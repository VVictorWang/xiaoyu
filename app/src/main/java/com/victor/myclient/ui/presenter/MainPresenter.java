package com.victor.myclient.ui.presenter;

import com.victor.myclient.MyApplication;
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.UserInfor;
import com.victor.myclient.ui.activity.SettingActivity;
import com.victor.myclient.ui.contract.MainContract;
import com.victor.myclient.utils.DataUtil;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class MainPresenter implements MainContract.Presenter, SettingActivity.OnAvatarChanged {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
        SettingActivity.setOnAvatarChanged(this);
    }

    @Override
    public void start() {
        String name;
        if (mView.getType().equals("phone")) {
            name = PrefUtils.getValue(MyApplication.getContext(), GlobalData.Phone);
        } else {
            name = PrefUtils.getValue(MyApplication.getContext(), GlobalData.NAME);
        }
        String key = Utils.createAcacheKey("getFamilyInfo", name);
        Observable<UserInfor> userInforObservable = UserApi.getInstance().getUserInfo(name, mView
                .getType()).compose(RxUtil.<UserInfor>rxCacheBeanHelper(key));
        Subscription subscription = Observable.concat(RxUtil.rxCreateDiskObservable(key,
                UserInfor.class), userInforObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfor>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showToast("错误");
                    }

                    @Override
                    public void onNext(UserInfor userInfor) {
                        DataUtil.saveUserInfo(userInfor);
                        mView.setUserInfo(userInfor);
                        mView.setName(userInfor.name);
                        mView.connectXiaoYu(userInfor.xiaoyuNum, userInfor.xiaoyuName);
                        mView.showImage(GlobalData.GET_PATIENT_FAMILY_IMAGE + userInfor.image);
                        mView.startClientService();
                    }
                });
    }

    @Override
    public void OnImageChanged() {
        String name = PrefUtils.getValue(MyApplication.getContext(), GlobalData.NAME);
        String key = Utils.createAcacheKey("getFamilyInfo", name);
        Observable<UserInfor> userInforObservable = UserApi.getInstance().getUserInfo(name,
                "username").compose(RxUtil.<UserInfor>rxCacheBeanHelper(key));
        Subscription subscription = Observable.concat(RxUtil.rxCreateDiskObservable(key,
                UserInfor.class), userInforObservable)
                .observeOn(AndroidSchedulers.mainThread())
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
                        DataUtil.saveUserInfo(userInfor);
                        mView.showImage(GlobalData.GET_PATIENT_FAMILY_IMAGE + userInfor.image);
                    }
                });
    }
}
