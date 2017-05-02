package com.example.franklin.myclient;

/**
 * Created by victor on 17-4-30.
 */

public class DoctorInfor {
    /**
     id:医生id
     phone:医生电话
     name:医生姓名
     mail:医生邮箱
     image:医生头像  前应加http://ip/upload/doctorimage/
     例如http://139.196.40.97/upload/doctorimage/8525f03f5354d4386440947594872b1a0eb059de.jpg
     hospital:医生所在医院
     job_title:医生职称 （主任医生、主治医生....）
     good_at : 擅长领域
     cases：医生介绍
     creationDate:添加时间
     * creationDate : 2017-04-21 16:26:10
     */

    private String id;
    private String phone;
    private String password;
    private String name;
    private String mail;
    private String image;
    private String hospital;
    private String hospitalName;
    private String job_title;
    private String good_at;
    private String cases;
    private String creationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getGood_at() {
        return good_at;
    }

    public void setGood_at(String good_at) {
        this.good_at = good_at;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
