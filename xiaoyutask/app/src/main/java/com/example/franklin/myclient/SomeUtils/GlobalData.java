package com.example.franklin.myclient.SomeUtils;

/**
 * Created by victor on 2017/4/28.
 */

public interface GlobalData {
    String Login_status = "login_status";
    String Phone = "phone";
    String Img_URl = "tou_xiang";
    String UserInfo = "UserInfo";
    String NAME = "NAME";
    String PWD = "PWD";
    String User_ID = "User_ID";
    String USer_email = "email";
    String PATIENT_ID = "patient_id";
    String XIAO_YU = "xiaoyu";
    String DoctorName = "doctor_name";
    String DoctorIMage = "./DCIM/xiangce";
    String USER_IMAGE = "image";
    // JSON status
    String Info = "info";
    String Value = "data";
    String Result = "status";
    String NET_ERROR = "网络错误，请稍后再试！";
    // 主机地址
    String MAIN_ENGINE = "http://139.196.40.97/OSAdmin-master/uploads/interface/regloginpost.php?";
    String GET_USR_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/getPatientFamily.php?";
    String GET_PATIENT_CASE = "http://139.196.40.97/OSAdmin-master/uploads/interface/patientcases.php?patientId=";
    String GET_DOCTOR_INFOR = "http://139.196.40.97/OSAdmin-master/uploads/interface/doctorinfo.php?doctorId=";
    /******************************************************************
     getXiaoyuNum.php         根据数据类型和信息获取小鱼设备号

     type可为phone（手机号）或者id（患者id）
     data即为数据
     注意手机号可能不唯一
     例如： http://139.196.40.97/OSAdmin-master/uploads/interface/getXiaoyuNum.php?type=id&data=2
     返回值： [{"xiaoyuNum":"125"}]
     如：http://localhost/OSAdmin-master/uploads/interface/getXiaoyuNum.php?type=phone&data=13367379725
     返回值：[{"xiaoyuNum":"125"},{"xiaoyuNum":"1234567"},{"xiaoyuNum":"2312323"}]
     {"xiaoyuNum"："not_exist"}:  该小鱼设备不存在
     {"xiaoyuNum"："param_error"} : 参数错误
     /*******************************************************************/
    String GET_XIAO_YU_NUMBER = "http://139.196.40.97/OSAdmin-master/uploads/interface/getXiaoyuNum.php?";
    /***上传图像接口
     根据患者家属id
     以post方式上传图像  图片存在upload/patientfamilyimage里面
     将在getPatientFamily.php里面返回
     image:患者家属头像 前应加http://ip/upload/doctorimage/
     例如http://139.196.40.97/upload/patientfamilyimage/8525f03f5354d4386440947594872b1a0eb059de.jpg
     */
    String POST_IMAGE = "http://139.196.40.97/OSAdmin-master/uploads/interface/uploadPatientFamilyImage.php?";
    String CHANGE_PASSWORD = "http://139.196.40.97/OSAdmin-master/uploads/interface/userinfomgr.php?";
    // 发送验证码 codeType 1注册 2修改密码
    String SendCodeURL = "";
    // 用户注册
    String RegistURL = MAIN_ENGINE + "user/regigter";
    // 用户登录
    String Login_URL = MAIN_ENGINE + "user/login";
    // 更新用户信息
    String UpdateInfoURL = MAIN_ENGINE + "user/update_userinfo";
    // 更新用户信息
    String updateUserInfoURL = MAIN_ENGINE + "004";


}
