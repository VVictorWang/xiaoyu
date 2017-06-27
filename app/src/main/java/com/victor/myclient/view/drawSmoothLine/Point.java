package com.victor.myclient.view.drawSmoothLine;

/**
 * 结点
 *
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年4月17日
 */
public class Point {

    /**
     * 是否在图形中绘制出此结点
     */
    public boolean willDrawing;
    /**
     * 在canvas中的X坐标
     */
    public float x;
    /**
     * 在canvas中的Y坐标
     */
    public float y;
    /**
     * 实际的X数值
     */
    public float valueX;
    /**
     * 实际的Y数值
     */
    public float valueY;

    public Point() {
    }

    public Point( float valueX, float valueY,boolean willDrawing) {
        this.willDrawing = willDrawing;
        this.valueX = valueX;
        this.valueY = valueY;
    }

    //    public Point(int valueX, int valueY, boolean willDrawing) {
//        this.valueX = valueX;
//        this.valueY = valueY;
//        this.willDrawing = willDrawing;
//    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}