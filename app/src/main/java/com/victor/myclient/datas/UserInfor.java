package com.victor.myclient.datas;

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
     * <p>
     * name：注册者姓名即患者家属姓名
     * patientId: 患者家属对应的患者id
     * mail： 患者家属邮箱
     * phone： 患者家属电话
     * image:
     */

    private String id;
    private String name;
    private String patientId;
    private String email;
    private String phone;
    private String image;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
