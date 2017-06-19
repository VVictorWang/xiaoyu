package com.victor.myclient.view.Contact.sortlist;

/**
 * Created by victor on 2017/4/23.
 */

public class SortModel {



    private String name;   //显示的数�?
    private String sortLetters;  //显示数据拼音的首字母
    private String number;

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
