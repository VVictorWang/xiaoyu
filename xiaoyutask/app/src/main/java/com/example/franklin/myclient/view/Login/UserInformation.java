package com.example.franklin.myclient.view.Login;

import java.io.Serializable;

/**
 * Created by victor on 2017/4/28.
 */

public class UserInformation implements Serializable {
    private String phone_number;
    private String password;
    private String username;
    private String sex;
    private String email;
    private String id_card;
    public void setId_card(String id_card) {
        this.id_card = id_card;
    }



    public String getId_card() {
        return id_card;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }
}