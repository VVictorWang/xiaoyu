package com.victor.myclient.datas;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 2017/6/7.
 */

public class DoctorImage extends DataSupport {
    private String doctorimage;

    public String getDoctorimage() {
        return doctorimage;
    }

    public void setDoctorimage(String doctorimage) {
        this.doctorimage = doctorimage;
    }
}
