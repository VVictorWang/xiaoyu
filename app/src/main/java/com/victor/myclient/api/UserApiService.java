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


    @GET("uploads/interface/getPatientFamily.php")
    Observable<UserInfor> getUserInfo(@Query("FamilyName") String name, @Query("type") String type);


    @GET("uploads/interface/patientcases.php")
    Observable<List<CaseInfor>> getCaseInfo(@Query("patientId") int patientId);


    @POST("uploads/interface/regloginpost.php")
    Observable<Integer> register(@Body RequestBody requestBody);

    @POST("uploads/interface/regloginpost.php")
    Observable<Integer> login(@Body RequestBody requestBody);

    @GET("uploads/interface/doctorinfo.php")
    Observable<DoctorInfor> getDoctorInfo(@Query("doctorId") int doctorId);

    @GET("uploads/interface/getDoctorXiaoyuNum.php")
    Observable<DoctorXiaoYu> getDoctorXiaoyu(@Query("doctorId") int doctorId);


    @POST("uploads/interface/uploadPatientFamilyImage.php")
    Observable<MessageResponse> uploadImage(@Body RequestBody requestBody);

    @POST("uploads/interface/userinfomgr.php")
    Observable<Integer> changeEmail(@Body RequestBody body);

    @POST("uploads/interface/userinfomgr.php")
    Observable<Integer> changePwd(@Body RequestBody body);

    @GET("uploads/interface/homeinfo.php")
    Observable<HomeInfor> getHomeInfo(@Query("patientId") int patientId, @Query("date") String
            date);

    @GET("uploads/interface/doorinfo.php")
    Observable<DoorInfor> getDoorInfo(@Query("patientId") int patientId);

    @GET("uploads/interface/send_password_mail.php")
    Observable<MessageResponse> sendEmail(@Query("id") int id);

    @GET("uploads/interface/getOnekeyWaring.php")
    Observable<List<OneKeyWarning>> getOneKeyWarnning(@Query("patientId") int patientId);

    @GET("uploads/interface/getActivities.php")
    Observable<List<UserAcitivityInfo>> getUserActivities(@Query("patientId") int patientId);


}
