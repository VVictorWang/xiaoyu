package com.victor.myclient.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 17-4-30.
 */

public class CaseInfor extends DataSupport{

    /**
     * id:病例id
     * name：患者名字
     * creationDate：病例创建时间
     patientId：患者id
     doctorId： 就诊医生id
     illproblem: 病情
     illresult:  诊断结果
     temperaturn: 病人测量体温
     blood_pressure: 病人测量血压值
     doctorName:医生姓名

     * id : 1
     * name : 小鱼测试
     * creationDate : 2017-04-24 22:13:40
     * patientId : 2
     * doctorId : 22
     * illproblem : dsfa
     * illresult : asdf
     * temperature : 38.3
     * blood_pressure : 93.2
     * doctorName : 1231233
     */
    private int id;
    private String name;
    private String sex;
    private String age;
    private String image;
    private String creationDate;
    private String patientId;
    private String doctorId;
    private String illproblem;
    private String illresult;
    private String temperature;
    private String blood_pressure;
    private String doctorName;
    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getIllproblem() {
        return illproblem;
    }

    public void setIllproblem(String illproblem) {
        this.illproblem = illproblem;
    }

    public String getIllresult() {
        return illresult;
    }

    public void setIllresult(String illresult) {
        this.illresult = illresult;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
