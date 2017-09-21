package com.victor.myclient.ui.presenter;

import com.victor.myclient.api.UserApi;
import com.victor.myclient.data.DoctorInfor;
import com.victor.myclient.data.DoctorXiaoYu;
import com.victor.myclient.ui.base.RxPresenter;
import com.victor.myclient.ui.contract.DoctorContract;
import com.victor.myclient.utils.RxUtil;
import com.victor.myclient.utils.Utils;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class DoctorPresenter extends RxPresenter implements DoctorContract.Presenter {

    private DoctorContract.View mView;

    public DoctorPresenter(DoctorContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getInfo() {
        if (mView.getDoctorId() != -1) {
            String key = Utils.createAcacheKey("get-doctor-info", mView.getDoctorId());
            Observable<DoctorInfor> observable = UserApi.getInstance().getDoctorInfo(mView
                    .getDoctorId())
                    .compose(RxUtil.<DoctorInfor>rxCacheBeanHelper(key));
            Subscription subscription = Observable.concat(RxUtil.rxCreateDiskObservable(key,
                    DoctorInfor.class), observable)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<DoctorInfor>() {
                        @Override
                        public void onCompleted() {
                            mView.dimissDialog();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(DoctorInfor doctorInfor) {
                            mView.showInfo(doctorInfor);
                        }
                    });
            addSubscribe(subscription);

            String xiaoYuKey = Utils.createAcacheKey("get-doctor-xiaoyu", mView.getDoctorId());
            Observable<DoctorXiaoYu> doctorXiaoYuObservable = UserApi.getInstance()
                    .getDoctorXiaoyu(mView.getDoctorId())
                    .compose(RxUtil.<DoctorXiaoYu>rxCacheBeanHelper(xiaoYuKey));
            Subscription subscription1 = Observable.concat(RxUtil.rxCreateDiskObservable
                    (xiaoYuKey, DoctorXiaoYu.class), doctorXiaoYuObservable)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<DoctorXiaoYu>() {
                        @Override
                        public void call(DoctorXiaoYu doctorXiaoYu) {
                            mView.setDoctorXiaoYu(doctorXiaoYu);
                        }
                    });
            addSubscribe(subscription1);
        }

    }

    @Override
    public void unscribe() {
        unSubscribe();
    }
}
