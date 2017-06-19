package com.victor.myclient.view.JiuJIA.drawSmoothLine;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 内部使用
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年4月23日
 */
class Title {
    /**文本对应的坐标X*/
    public float textX;
    /**文本对应的坐标Y*/
    public float textY;
    /**文本*/
    public String text;
    /**圆点对应的坐标X*/
    public float circleX;
    /**圆点对应的坐标Y*/
    public float circleY;
    /**颜色*/
    public int color;
    /**圆点的半径*/
    public int radius;
    /**图形标注与文本的间距*/
    public int circleTextPadding;
    /**文本区域*/
    public Rect textRect=new Rect();
    public Title(String text, int color) {
        this.text = text;
        this.color = color;
    }
    public void updateTextRect(Paint paint,float maxWidth){
        int textWidth=textCircleWidth(paint);
        if(textWidth<=maxWidth)
            return;
        while(textWidth>maxWidth){
//            Log.d("zqt", "text="+text);
            text=text.substring(0, text.length()-1);
            textWidth=textCircleWidth(paint);
        }
        text=text.substring(0, text.length()-1)+"...";
        textCircleWidth(paint);
    }
    private int textCircleWidth(Paint paint){
        paint.getTextBounds(text, 0, text.length(), textRect);
        return textRect.width() + (radius + circleTextPadding) * 2;
    }
}