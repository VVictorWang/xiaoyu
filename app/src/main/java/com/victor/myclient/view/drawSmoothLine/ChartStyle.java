package com.victor.myclient.view.drawSmoothLine;

import android.graphics.Color;

/**
 * 曲线图整体的样式
 *
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年4月17日
 */
public class ChartStyle {


    /**
     * 竖直标记线的颜色
     */
    private int verticalLineColor;
    /**
     * 网格线颜色
     */
    private int gridColor;
    /**
     * 坐标轴分隔线宽度
     */
    private int axisLineWidth;

    /**
     * 横坐标文本大小
     */
    private float horizontalLabelTextSize;
    /**
     * 横坐标文本颜色
     */
    private int horizontalLabelTextColor;
    /**
     * 横坐标标题文本大小
     */
    private float horizontalTitleTextSize;
    /**
     * 横坐标标题文本颜色
     */
    private int horizontalTitleTextColor;
    /**
     * 横坐标标题文本左间距
     */
    private int horizontalTitlePaddingLeft;
    /**
     * 横坐标标题文本右间距
     */
    private int horizontalTitlePaddingRight;

    /**
     * 纵坐标文本大小
     */
    private float verticalLabelTextSize;
    /**
     * 纵坐标文本上下间距
     */
    private int verticalLabelTextPadding;
    /**
     * 纵坐标文本左右间距相对文本的比例
     */
    private float verticalLabelTextPaddingRate;
    /**
     * 纵坐标文本颜色
     */
    private int verticalLabelTextColor;
    /**
     * 图片背景上部分颜色
     */
    private int backgroundUpPartColor;


    /**
     * 图片背景下部分颜色
     */
    private int backgroundDownPartColor;


    /**
     * 最大温度最小温度提升温度的字体大小与颜色
     */
    private int maxTemColor;
    private int minTemColor;
    private int raiseTemColor;

    /**
     *
     */
    private int maxTemTextSize;
    private int minTemTextSize;


    private int raiseTemTextSize;


    public ChartStyle() {
        verticalLineColor = Color.WHITE;
        gridColor = Color.LTGRAY;
        horizontalTitleTextSize = 50;
        horizontalTitleTextColor = Color.GRAY;
        horizontalLabelTextSize = 50;
        horizontalLabelTextColor = 0xFFD2F4FD;
        verticalLabelTextSize = 20;
        verticalLabelTextPadding = 50;
        verticalLabelTextColor = Color.GRAY;
        verticalLabelTextPaddingRate = 0.2f;
        axisLineWidth = 2;
        horizontalTitlePaddingLeft = 20;
        horizontalTitlePaddingRight = 10;
        backgroundUpPartColor = 0xff24cffc;
        backgroundDownPartColor = 0xff1cbde7;
        maxTemColor = Color.WHITE;
        minTemColor = 0xFFD2F4FD;
        raiseTemColor = 0xFFD2F4FD;
        maxTemTextSize = 100;
        minTemTextSize = 50;
        raiseTemTextSize = 75;
    }

    public float getVerticalLabelTextSize() {
        return verticalLabelTextSize;
    }

    public void setVerticalLabelTextSize(float verticalLabelTextSize) {
        this.verticalLabelTextSize = verticalLabelTextSize;
    }

    public int getVerticalLabelTextPadding() {
        return verticalLabelTextPadding;
    }

    public void setVerticalLabelTextPadding(int verticalLabelTextPadding) {
        this.verticalLabelTextPadding = verticalLabelTextPadding;
    }

    public int getVerticalLabelTextColor() {
        return verticalLabelTextColor;
    }

    public void setVerticalLabelTextColor(int verticalLabelTextColor) {
        this.verticalLabelTextColor = verticalLabelTextColor;
    }

    public float getHorizontalLabelTextSize() {
        return horizontalLabelTextSize;
    }

    public void setHorizontalLabelTextSize(float horizontalLabelTextSize) {
        this.horizontalLabelTextSize = horizontalLabelTextSize;
    }

    public int getHorizontalLabelTextColor() {
        return horizontalLabelTextColor;
    }

    public void setHorizontalLabelTextColor(int horizontalLabelTextColor) {
        this.horizontalLabelTextColor = horizontalLabelTextColor;
    }

    public int getGridColor() {
        return gridColor;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    public int getVerticalLineColor() {
        return verticalLineColor;
    }

    public void setVerticalLineColor(int verticalLineColor) {
        this.verticalLineColor = verticalLineColor;
    }

    public float getHorizontalTitleTextSize() {
        return horizontalTitleTextSize;
    }

    public void setHorizontalTitleTextSize(float horizontalTitleTextSize) {
        this.horizontalTitleTextSize = horizontalTitleTextSize;
    }

    public int getHorizontalTitleTextColor() {
        return horizontalTitleTextColor;
    }

    public void setHorizontalTitleTextColor(int horizontalTitleTextColor) {
        this.horizontalTitleTextColor = horizontalTitleTextColor;
    }

    public float getVerticalLabelTextPaddingRate() {
        return verticalLabelTextPaddingRate;
    }

    public void setVerticalLabelTextPaddingRate(float verticalLabelTextPaddingRate) {
        this.verticalLabelTextPaddingRate = verticalLabelTextPaddingRate;
    }

    public int getAxisLineWidth() {
        return axisLineWidth;
    }

    public void setAxisLineWidth(int axisLineWidth) {
        this.axisLineWidth = axisLineWidth;
    }

    public int getHorizontalTitlePaddingLeft() {
        return horizontalTitlePaddingLeft;
    }

    public void setHorizontalTitlePaddingLeft(int horizontalTitlePaddingLeft) {
        this.horizontalTitlePaddingLeft = horizontalTitlePaddingLeft;
    }

    public int getHorizontalTitlePaddingRight() {
        return horizontalTitlePaddingRight;
    }

    public void setHorizontalTitlePaddingRight(int horizontalTitlePaddingRight) {
        this.horizontalTitlePaddingRight = horizontalTitlePaddingRight;
    }

    public int getBackgroundUpPartColor() {
        return backgroundUpPartColor;
    }

    public void setBackgroundUpPartColor(int backgroundUpPartColor) {
        this.backgroundUpPartColor = backgroundUpPartColor;
    }

    public int getBackgroundDownPartColor() {
        return backgroundDownPartColor;
    }

    public void setBackgroundDownPartColor(int backgroundDownPartColor) {
        this.backgroundDownPartColor = backgroundDownPartColor;
    }


    public int getMaxTemColor() {
        return maxTemColor;
    }

    public void setMaxTemColor(int maxTemColor) {
        this.maxTemColor = maxTemColor;
    }

    public int getMinTemColor() {
        return minTemColor;
    }

    public void setMinTemColor(int minTemColor) {
        this.minTemColor = minTemColor;
    }

    public int getRaiseTemColor() {
        return raiseTemColor;
    }

    public void setRaiseTemColor(int raiseTemColor) {
        this.raiseTemColor = raiseTemColor;
    }

    public int getMaxTemTextSize() {
        return maxTemTextSize;
    }

    public void setMaxTemTextSize(int maxTemTextSize) {
        this.maxTemTextSize = maxTemTextSize;
    }

    public int getMinTemTextSize() {
        return minTemTextSize;
    }

    public void setMinTemTextSize(int minTemTextSize) {
        this.minTemTextSize = minTemTextSize;
    }

    public int getRaiseTemTextSize() {
        return raiseTemTextSize;
    }

    public void setRaiseTemTextSize(int raiseTemTextSize) {
        this.raiseTemTextSize = raiseTemTextSize;
    }
}