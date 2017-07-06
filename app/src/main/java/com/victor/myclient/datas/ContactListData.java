package com.victor.myclient.datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 17-5-4.
 */

public class ContactListData extends DataSupport {
    private int id;
    private String name;
    private String number;
    private String image_url;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImage_url() {
        return image_url;
    }
}
