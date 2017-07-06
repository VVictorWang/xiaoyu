package com.victor.myclient.datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/5/31.
 */

public class UserAcitivityInfo extends DataSupport {
    private String room;
    private String num;


    public void setRoom(String room) {
        this.room = room;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRoom() {
        return room;
    }

    public String getNum() {
        return num;
    }
}
