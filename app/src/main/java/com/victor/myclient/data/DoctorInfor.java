package com.victor.myclient.data;

/**
 * Created by victor on 17-4-30.
 */

public class DoctorInfor {

    /**
     * id:医生id
     * phone:医生电话
     * name:医生姓名
     * mail:医生邮箱
     * sex: 医生性别
     * age: 医生年龄
     * image:医生头像  前应加http://ip/upload/doctorimage/
     * 例如http://139.196.40.97/upload/doctorimage/8525f03f5354d4386440947594872b1a0eb059de.jpg
     * hospital:医生所在医院
     * job_title:医生职称 （主任医生、主治医生....）
     * good_at : 擅长领域
     * cases：医生介绍
     * creationDate:添加时间
     * <p>
     * {"doctor"："not_exist"}: 未查询到该医生信息
     * {"data"："error"}      : 参数错误
     * <p>
     * <p>
     * id : 22
     * phone : 12345678901
     * name : 1231233
     * mail : 123@qq.com.cn
     * sex : 女
     * age : 34
     * image : 8525f03f5354d4386440947594872b1a0eb059de.jpg
     * hospital : 111
     * hospitalName : 111
     * job_title : 123
     * good_at : 123
     * cases : 123
     * creationDate : 2017-04-21 16:26:10
     */

    public int id;
    public String phone;
    public String name;
    public String mail;
    public String sex;
    public String age;
    public String image;
    public String hospital;
    public String hospitalName;
    public String job_title;
    public String good_at;
    public String cases;
    public String creationDate;

}
