package com.victor.myclient.view.JiuJIA.drawSmoothLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * 横向标题
 *
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年5月4日
 */
public class HorizontalLegendView extends View {
    private Paint paint;
    private ChartStyle style;
    private List<Title> titles;
    private BesselCalculator calculator;

    public HorizontalLegendView(Context context, List<Title> titles, ChartStyle style, BesselCalculator calculator) {
        super(context);
        this.titles = titles;
        this.style = style;
        this.calculator = calculator;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 绘制横轴的标题
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        Log.d("HorizontalLegendView onDraw");
//        if(titles.size()==0)
//            return;
//        paint.setTextAlign(Align.CENTER);
//        paint.setTextSize(style.getHorizontalTitleTextSize());
//        for (Title title : titles) {
//            Log.d("title=" + title.text);
//            paint.setColor(title.color);
//            paint.setTextAlign(Align.CENTER);
//            paint.setTextSize(style.getHorizontalTitleTextSize());
//            if (title instanceof Marker) {
//                Marker marker = (Marker) title;
//                canvas.drawBitmap(marker.getBitmap(), null,
//                        marker.updateRect(title.circleX, title.circleY, title.radius * 2, title.radius * 2), paint);
//            } else {
//                canvas.drawCircle(title.circleX, title.circleY, title.radius, paint);
//            }
//            paint.setAlpha(255);
//            canvas.drawText(title.text, title.textX, title.textY, paint);
//        }
    }

    public void updateHeight() {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = calculator.xTitleHeight;
        setLayoutParams(lp);
    }
}