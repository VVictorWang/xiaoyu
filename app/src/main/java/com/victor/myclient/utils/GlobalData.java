package com.victor.myclient.utils;

/**
 * Created by victor on 2017/4/28.
 */

public interface GlobalData {
    String Login_status = "login_status";
    String Phone = "phone";
    String PATIENTFAMILY_ID = "patient_family_image";
    String Img_URl = "tou_xiang";
    String NAME = "NAME";
    String User_ID = "User_ID";
    String USer_email = "email";
    String PATIENT_ID = "patient_id";
    String DOOR_STATUS = "door_status";
    String DoctorName = "doctor_name";
    String FAMILY_IMage = "family_image";
    String MAIN_ENGINE = "http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?";
    String XIAOYU_NUMBER = "xiaoyu_number";
    String XIAOYU_NAME = "xiaoyu_name";
    String CLIENT_ID="clientId";
    String IP="139.196.40.97";
    String GET_USR_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/getPatientFamily.php?";
    String GET_PATIENT_CASE = "http://139.196.40.97/OSAdmin-master/uploads/interface/patientcases.php?patientId=";
    String GET_DOCTOR_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/doctorinfo.php?doctorId=";
    String GET_HOME_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/homeinfo.php?patientId=";
    String CHANGE_EMAIL_PASSORT = "http://139.196.40.97/OSAdmin-master/uploads/interface/userinfomgr.php";
    /*******************************************************************/
    String GET_XIAO_YU_NUMBER = "http://139.196.40.97/OSAdmin-master/uploads/interface/getXiaoyuNum.php?";
    String POST_IMAGE = "http://139.196.40.97/OSAdmin-master/uploads/interface/uploadPatientFamilyImage.php?";
    String CHANGE_PASSWORD = "http://139.196.40.97/OSAdmin-master/uploads/interface/userinfomgr.php?";
    String GET_PATIENT_FAMILY_IMAGE = "http://139.196.40.97/upload/patientfamilyimage/";
    String FORGET_PASSWOD = "http://139.196.40.97/OSAdmin-master/uploads/interface/send_password_mail.php?id=";
    String GET_ACTIVITIES = "http://139.196.40.97/OSAdmin-master/uploads/interface/getActivities.php?patientId=";
    String GET_ROOM_STATUS = "http://139.196.40.97/OSAdmin-master/uploads/interface/doorinfo.php?patientId=";
    String DRURATION_HOUR = "xiaoyu_duration_hour";
    String DRURATION_MINITE = "xiaoyu_duration_minute";
    String DRURATION_SECOND = "xiaoyu_duration_second";
    String GET_ONEKEY_WARNING = "http://139.196.40.97/OSAdmin-master/uploads/interface/getOnekeyWaring.php?patientId=";
    String GET_CALLING_IMAGE = "http://139.196.40.97/OSAdmin-master/uploads/interface/getCallingImage.php?xiaoyuNum=125";
    String GET_DOCTOR_XIAO_YU = "http://139.196.40.97/OSAdmin-master/uploads/interface/getDoctorXiaoyuNum.php?doctorId=";
    String GET_PATIENT_IMAGE = "http://139.196.40.97/upload/patientimage/";
    String GET_DOCTOR_IMAGE = "http://139.196.40.97/upload/doctorimage/";
    String GET_IMAGE="http://139.196.40.97/upload/serviceimage/";
    String GET_SERVICE_HISTORY="http://139.196.40.97/OSAdmin-master/uploads/interface/getServiceHistory.php?patientId=";
    String POST_CLIENTID="http://139.196.40.97/OSAdmin-master/uploads/getui/onekey_tuisong.php?patientId=";
}
