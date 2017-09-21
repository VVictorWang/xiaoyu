package com.victor.myclient.api;

import com.victor.myclient.data.CaseInfor;
import com.victor.myclient.data.DoctorInfor;
import com.victor.myclient.data.DoctorXiaoYu;
import com.victor.myclient.data.DoorInfor;
import com.victor.myclient.data.HomeInfor;
import com.victor.myclient.data.MessageResponse;
import com.victor.myclient.data.OneKeyWarning;
import com.victor.myclient.data.UserAcitivityInfo;
import com.victor.myclient.data.UserInfor;
import com.victor.myclient.utils.GlobalData;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by victor on 9/20/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class UserApi {
    public static UserApi instance;
    private UserApiService mUserApiService;

    public UserApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalData.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mUserApiService = retrofit.create(UserApiService.class);
    }

    public static UserApi getInstance() {
        if (instance == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(12, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            instance = new UserApi(builder.build());
        }
        return instance;
    }

    public Observable<UserInfor> getUserInfo(String name, String type) {
        return mUserApiService.getUserInfo(name, type);
    }

    public Observable<List<CaseInfor>> getCaseInfo(int patientId) {
        return mUserApiService.getCaseInfo(patientId);
    }

    public Observable<Integer> register(String username, String identityNum, String password,
                                        String pass_cfn,
                                        String email, String phone, String sex) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("reqType", "reg");
        builder.add("username", username);
        builder.add("identityNum", identityNum);
        builder.add("password", password);
        builder.add("password_cnfm", pass_cfn);
        builder.add("email", email);
        builder.add("phone", phone);
        builder.add("sex", sex);
        RequestBody requestBody = builder.build();
        return mUserApiService.register(requestBody);
    }

    public Observable<Integer> login(String username, String password) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("reqType", "login");
        builder.add("username", username);
        builder.add("password", password);
        RequestBody requestBody = builder.build();
        return mUserApiService.login(requestBody);
    }

    public Observable<DoctorInfor> getDoctorInfo(int doctorId) {
        return mUserApiService.getDoctorInfo(doctorId);
    }

    public Observable<DoctorXiaoYu> getDoctorXiaoyu(int doxtorId) {
        return mUserApiService.getDoctorXiaoyu(doxtorId);
    }

    public Observable<MessageResponse> uploadImage(String imageurl, String patientId) {
        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/png");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File f = new File(imageurl);
        builder.addFormDataPart("patientFamilyImage", f.toString(), RequestBody.create
                (MEDIA_TYPE_JPG, f));
        builder.addFormDataPart("id", patientId);
        builder.setType(MultipartBody.FORM);
        RequestBody requestBody = builder.build();
        return mUserApiService.uploadImage(requestBody);
    }

    public Observable<Integer> changeEmail(String username, String email, String password) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("reqType", "chgemail");
        builder.add("username", username);
        builder.add("email", email);
        builder.add("password", password);
        RequestBody body = builder.build();
        return mUserApiService.changeEmail(body);
    }

    public Observable<Integer> changePwd(String username, String password_old, String
            password_new, String password_confirm) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("reqType", "chgpassword");
        builder.add("username", username);
        builder.add("password_old", password_old);
        builder.add("password_new", password_new);
        builder.add("password_cnfm", password_confirm);
        RequestBody body = builder.build();
        return mUserApiService.changePwd(body);
    }

    public Observable<HomeInfor> getHomeInfo(int patientId, String date) {
        return mUserApiService.getHomeInfo(patientId, date);
    }

    public Observable<DoorInfor> getDoorInfo(int patientId) {
        return mUserApiService.getDoorInfo(patientId);
    }

    private Observable<String> sendEmail(int id) {
        return mUserApiService.sendEmail(id);
    }

    public Observable<List<OneKeyWarning>> getOneKeyWarnning(int patientId) {
        return mUserApiService.getOneKeyWarnning(patientId);
    }

    public Observable<List<UserAcitivityInfo>> getUserActivities(int patientId) {
        return mUserApiService.getUserActivities(patientId);
    }
}
