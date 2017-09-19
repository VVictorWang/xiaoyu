package com.victor.myclient.data;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/5/29.
 */

public class DoorInfor extends DataSupport {
    private String sid;
    private int status;
    private String add_date;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }
}
