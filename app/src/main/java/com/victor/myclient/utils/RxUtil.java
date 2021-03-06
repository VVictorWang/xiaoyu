package com.victor.myclient.utils;


import com.google.gson.Gson;
import com.victor.myclient.MyApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class RxUtil {


    public static <T> Observable rxCreateDiskObservable(final String key, final Class<T> tClass) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String json = ACache.get(MyApplication.getInstance()).getAsString(key);
                if (!CheckUtils.isEmpty(json)) {
                    subscriber.onNext(json);
                }
                subscriber.onCompleted();
            }
        }).map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return new Gson().fromJson(s, tClass);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static <T> Observable rxCreateDiskObservable(final String key, final Type type) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String json = ACache.get(MyApplication.getInstance()).getAsString(key);
                if (!CheckUtils.isEmpty(json)) {
                    subscriber.onNext(json);
                }
                subscriber.onCompleted();
            }
        }).map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return new Gson().fromJson(s, type);
            }
        }).subscribeOn(Schedulers.io());
    }


    public static <T> Observable.Transformer<T, T> rxCacheBeanHelper(final String key) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())//指定doOnNext执行线程是新线程
                        .doOnNext(new Action1<T>() {
                            @Override
                            public void call(final T data) {
                                Schedulers.io().createWorker().schedule(new Action0() {
                                    @Override
                                    public void call() {
                                        LogUtils.d("get data from network finish ,start cache...");
                                        ACache.get(MyApplication.getInstance())
                                                .put(key, new Gson().toJson(data));
                                        LogUtils.d("cache finish");
                                    }
                                });
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
