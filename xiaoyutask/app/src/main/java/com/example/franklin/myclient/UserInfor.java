package com.example.franklin.myclient;

/**
 * Created by victor on 17-4-30.
 */

public class UserInfor {
    /**
     * name : admin
     * password : 098f6bcd4621d373cade4e832627b4f6
     * patientId : 2
     * email : test@qq.com
     * phone : 12345678
     * \
     *
     * name：注册者姓名即患者家属姓名
     patientId: 患者家属对应的患者id
     mail： 患者家属邮箱
     phone： 患者家属电话
     */

    private String name;
    private String password;
    private String patientId;
    private String email;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
