package com.victor.myclient.data;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by victor on 17-5-16.
 */

public class HomeInfor extends DataSupport {
    /**
     * id：设备排序
     * model:设备类型 有以下几种 sensor_ht ： "温度传感器",
     * $deviceMode = array( "switch" => "一键呼入装置","plug" => "PLUG装置", "sensor_ht" => "温度传感器",
     * "motion" => "人体红外传感器", "magnet" => "门磁传感器","magic" => "魔方感应器", "vedio" => "视频健康器",
     * "safemode" => "安防模式");
     * <p>
     * sid：设备id
     * temperature: 温度 25.08 格式有误 前端再改
     * humidity   ：湿度
     * add_date   ：添加时间
     * temperatures :温度数组 [0,0,0,0,0,0,0,"2257","2306","2358","2409","2416","2459","2506","2512",
     * 0,0,0,0,0,0,0,0,0]
     * 为0代表没有数据  从左到右为0点到23点数据
     * humidityies ：湿度数组
     * <p>
     * {"sensor_ht": "param_error"} : 参数错误
     * {"sensor_ht": "not_exist"}   : 该设备不存在
     * {"sensor_ht": "data_not_exist"} :该设备还未上传数据
     * <p>
     * id : 3
     * model : sensor_ht
     * short_id : 63883
     * sid : 158d0001531af0
     * temperature : 2512
     * humidity : 6128
     * add_date : 2017-05-09 14:57:22
     * temperatures : [0,0,0,0,0,0,0,"2257","2306","2358","2409","2416","2459","2506","2512",0,0,
     * 0,0,0,0,0,0,0]
     * humidityies : [0,0,0,0,0,0,0,"6752","6637","6503","6432","6417","6345","6185","6128",0,0,
     * 0,0,0,0,0,0,0]
     */

    private int id;
    private String model;
    private String short_id;
    private String sid;
    private String temperature;
    private float humidity;
    private String add_date;
    private List<Float> temperatures;
    private List<Float> humidityies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getShort_id() {
        return short_id;
    }

    public void setShort_id(String short_id) {
        this.short_id = short_id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public List<Float> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Float> temperatures) {
        this.temperatures = temperatures;
    }

    public List<Float> getHumidityies() {
        return humidityies;
    }

    public void setHumidityies(List<Float> humidityies) {
        this.humidityies = humidityies;
    }
}
