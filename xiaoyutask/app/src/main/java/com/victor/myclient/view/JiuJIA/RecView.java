package com.victor.myclient.view.JiuJIA;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.util.Random;

/**
 * Created by victor on 17-5-20.
 */

public class RecView extends View {
    private Paint paint;
    private int width;
    private int height;

    private String roomid;
    private int number;
    public RecView(Context context) {

        super(context);
    }

    public RecView(Context context, String roomid, int number) {
        super(context);
        this.roomid = roomid;
        this.number = number;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint = new Paint();
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,width,height,paint);
        paint.setTextSize(28);
        canvas.drawText("房间编号：" + roomid, 10, 20, paint);
        Paint paint1 = new Paint();
        paint1.setColor(Color.RED);
        paint1.setStyle(Paint.Style.FILL);
        for (int i = 1; i <=number ; i++) {
            float x = (float) Math.random() * width;
            float y = (float) Math.random() * height;
            if (y < 30) {
                y += 30;
            }
            canvas.drawCircle(x,y,10,paint1);
        }


    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(100, widthMeasureSpec);
        int height = getMySize(100, heightMeasureSpec);
        if (width < height) {
            height = width;
        } else {
            width = height;
        }

        setMeasuredDimension(300, 300);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mySize = defaultSize;

        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED: {//如果没有指定大小，就设置为默认大小
                mySize = defaultSize;
                break;
            }
            case MeasureSpec.AT_MOST: {//如果测量模式是最大取值为size
                //我们将大小取最大值,你也可以取其他值
                mySize = size;
                break;
            }
            case MeasureSpec.EXACTLY: {//如果是固定的大小，那就不要去改变它
                mySize = size;
                break;
            }
        }
        return mySize;
    }

}
