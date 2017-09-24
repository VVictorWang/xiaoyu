package com.victor.myclient.api;

import com.victor.myclient.bean.CaseInfor;
import com.victor.myclient.bean.DoctorInfor;
import com.victor.myclient.bean.DoctorXiaoYu;
import com.victor.myclient.bean.DoorInfor;
import com.victor.myclient.bean.HomeInfor;
import com.victor.myclient.bean.MessageResponse;
import com.victor.myclient.bean.OneKeyWarning;
import com.victor.myclient.bean.ServiceHistory;
import com.victor.myclient.bean.UserAcitivityInfo;
import com.victor.myclient.bean.UserInfor;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by victor on 9/20/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public interface UserApiService {


    @GET("getPatientFamily.php")
    Observable<UserInfor> getUserInfo(@Query("FamilyName") String name, @Query("type") String type);


    @GET("patientcases.php")
    Observable<List<CaseInfor>> getCaseInfo(@Query("patientId") int patientId);


    @POST("regloginpost.php")
    Observable<Integer> register(@Body RequestBody requestBody);

    @POST("regloginpost.php")
    Observable<Integer> login(@Body RequestBody requestBody);

    @GET("doctorinfo.php")
    Observable<DoctorInfor> getDoctorInfo(@Query("doctorId") int doctorId);

    @GET("getDoctorXiaoyuNum.php")
    Observable<DoctorXiaoYu> getDoctorXiaoyu(@Query("doctorId") int doctorId);


    @POST("uploadPatientFamilyImage.php")
    Observable<MessageResponse> uploadImage(@Body RequestBody requestBody);

    @POST("userinfomgr.php")
    Observable<Integer> changeEmail(@Body RequestBody body);

    @POST("userinfomgr.php")
    Observable<Integer> changePwd(@Body RequestBody body);

    @GET("homeinfo.php")
    Observable<HomeInfor> getHomeInfo(@Query("patientId") int patientId, @Query("date") String
            date);

    @GET("doorinfo.php")
    Observable<DoorInfor> getDoorInfo(@Query("patientId") int patientId);

    @GET("send_password_mail.php")
    Observable<MessageResponse> sendEmail(@Query("id") int id);

    @GET("getOnekeyWaring.php")
    Observable<List<OneKeyWarning>> getOneKeyWarnning(@Query("patientId") int patientId);

    @GET("getActivities.php")
    Observable<List<UserAcitivityInfo>> getUserActivities(@Query("patientId") int patientId);

    @GET("getServiceHistory.php")
    Observable<List<ServiceHistory>> getSearchHistory(@Query("patientId") int patienId);
}
