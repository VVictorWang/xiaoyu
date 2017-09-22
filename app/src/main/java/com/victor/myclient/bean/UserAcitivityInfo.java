package com.victor.myclient.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/5/31.
 */

public class UserAcitivityInfo extends DataSupport {
    private String room;
    private String num;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
