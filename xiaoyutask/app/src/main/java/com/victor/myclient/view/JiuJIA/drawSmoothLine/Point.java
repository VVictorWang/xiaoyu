package com.victor.myclient.view.JiuJIA.drawSmoothLine;

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
    public int valueX;
    /**
     * 实际的Y数值
     */
    public int valueY;

    public Point() {
    }

    public Point(float x, float y, boolean willDrawing) {
        this.x = x;
        this.y = y;
        this.willDrawing = willDrawing;
    }

    public Point(int valueX, int valueY, boolean willDrawing) {
        this.valueX = valueX;
        this.valueY = valueY;
        this.willDrawing = willDrawing;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

}