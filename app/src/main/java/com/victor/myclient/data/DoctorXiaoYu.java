package com.victor.myclient.data;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/6/5.
 */

public class DoctorXiaoYu extends DataSupport {
    private String xiaoyuNum;
    private int id;

    public String getXiaoyuNum() {
        return xiaoyuNum;
    }

    public void setXiaoyuNum(String xiaoyuNum) {
        this.xiaoyuNum = xiaoyuNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
