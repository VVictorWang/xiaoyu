package com.victor.myclient.utils;

import android.content.Context;

import com.victor.myclient.MyApplication;
import com.victor.myclient.bean.UserInfor;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class DataUtil {

    public static void saveUserInfo(UserInfor userInfor) {
        Context context = MyApplication.getContext();
        PrefUtils.putValue(context, GlobalData.NAME, userInfor.getName());
        PrefUtils.putValue(context, GlobalData.USer_email, userInfor
                .getEmail());
        PrefUtils.putValue(context, GlobalData.User_ID, userInfor.getId());
        PrefUtils.putValue(context, GlobalData.Phone, userInfor.getPhone());
        PrefUtils.putValue(context, GlobalData.PATIENT_ID, userInfor
                .getPatientId());
        PrefUtils.putValue(context, GlobalData.PATIENTFAMILY_ID, userInfor
                .getId());
        PrefUtils.putValue(context, GlobalData.FAMILY_IMage, userInfor
                .getImage());
        PrefUtils.putValue(context, GlobalData.XIAOYU_NAME, userInfor
                .getXiaoyuName
                        ());
        PrefUtils.putValue(context, GlobalData.XIAOYU_NUMBER, userInfor
                .getXiaoyuNum());
    }
}
