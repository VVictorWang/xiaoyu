package com.victor.myclient.data;

import org.litepal.crud.DataSupport;

/**
 * Created by Silver on 2017/7/13.
 */

public class ServiceHistory extends DataSupport {


    /**
     * name : 员工甲的哥哥3
     * address : 华师
     * serviceDatetime : 2017-07-13 09:32:00
     * serviceContent : 831b6b2ebb93490ba132d58f14af419543595831.jpg
     * patientId : 5
     */

    private String name;
    private String address;
    private String serviceDatetime;
    private String serviceContent;
    private String patientId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceDatetime() {
        return serviceDatetime;
    }

    public void setServiceDatetime(String serviceDatetime) {
        this.serviceDatetime = serviceDatetime;
    }

    public String getServiceContent() {
        return serviceContent;
    }

    public void setServiceContent(String serviceContent) {
        this.serviceContent = serviceContent;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
