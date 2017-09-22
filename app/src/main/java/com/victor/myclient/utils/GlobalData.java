package com.victor.myclient.utils;

/**
 * Created by victor on 2017/4/28.
 */

public class GlobalData {
    public static String Login_status = "login_status";
    public static String Phone = "phone";
    public static String PATIENTFAMILY_ID = "patient_family_image";
    public static String NAME = "NAME";
    public static String User_ID = "User_ID";
    public static String USer_email = "email";
    public static String PATIENT_ID = "patient_id";
    public static String DOOR_STATUS = "door_status";
    public static String FAMILY_IMage = "family_image";
    public static String ECLIPSE_TIME = "eclipse_time";
    public static String BASE_URL = "http://139.196.40.97/OSAdmin-master/";
    public static String XIAOYU_NUMBER = "xiaoyu_number";
    public static String XIAOYU_NAME = "xiaoyu_name";
    public static String CLIENT_ID = "clientId";


    public static String GET_HOME_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/homeinfo" +
            ".php?patientId=";


    public static String GET_PATIENT_FAMILY_IMAGE =
            "http://139.196.40.97/upload/patientfamilyimage/";
    public static String GET_ACTIVITIES = "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/getActivities" +
            ".php?patientId=";
    public static String GET_ROOM_STATUS = "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/doorinfo" +
            ".php?patientId=";
    public static String GET_ONEKEY_WARNING =
            "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/getOnekeyWaring.php?patientId=";
    public static String GET_CALLING_IMAGE =
            "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/getCallingImage.php?xiaoyuNum=125";

    public static String GET_PATIENT_IMAGE = "http://139.196.40.97/upload/patientimage/";
    public static String GET_DOCTOR_IMAGE = "http://139.196.40.97/upload/doctorimage/";
    public static String GET_IMAGE = "http://139.196.40.97/upload/serviceimage/";
    public static String GET_SERVICE_HISTORY =
            "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/getServiceHistory.php?patientId=";
    public static String POST_CLIENTID = "http://139.196.40.97/OSAdmin-master/uploads/interface" +
            "/add_clientId" +
            ".php?id=";
}
