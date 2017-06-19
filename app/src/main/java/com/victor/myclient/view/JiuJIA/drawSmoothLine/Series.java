package com.victor.myclient.view.JiuJIA.drawSmoothLine;

import java.util.ArrayList;
import java.util.List;

public class Series {
    /** 序列曲线的标题 */
    private Title title;
    /** 序列曲线的颜色 */
    private int color;
    /** 序列点集合 */
    private List<Point> points;
    /** 贝塞尔曲线点 */
    private List<Point> besselPoints;

    /**
     * @param color 曲线的颜色
     * @param points 点集合
     */
    public Series(String title,int color, List<Point> points) {
        this.title=new Title(title, color);
        this.color = color;
        this.points = points;
        this.besselPoints = new ArrayList<Point>();
    }
    public Title getTitle() {
        return title;
    }
    public int getColor() {
        return color;
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Point> getBesselPoints() {
        return besselPoints;
    }

}