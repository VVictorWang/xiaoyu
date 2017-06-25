package com.victor.myclient.Datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/5/31.
 */

public class OneKeyWarning extends DataSupport{
    private String sid;
    private String add_date;

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getSid() {
        return sid;
    }

    public String getAdd_date() {
        return add_date;
    }
}
