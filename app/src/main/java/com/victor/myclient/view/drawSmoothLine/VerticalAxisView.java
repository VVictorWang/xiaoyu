package com.victor.myclient.view.drawSmoothLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 纵轴
 *
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年5月4日
 */
public class VerticalAxisView extends View {
    public static final int POSITION_LEFT = 0;
    public static final int POSITION_RIGHT = 1;
    private int position = POSITION_RIGHT;
    private Paint paint;
    private ChartStyle style;
    private List<ChartData.Label> labels;
    private BesselCalculator calculator;

    public VerticalAxisView(Context context, List<ChartData.Label> labels, ChartStyle style,
                            BesselCalculator calculator) {
        super(context);
        this.calculator = calculator;
        this.labels = labels;
        this.style = style;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public void setStyle(ChartStyle style) {
        this.style = style;
    }

    public void setLabels(List<ChartData.Label> labels) {
        this.labels = labels;
    }

    public void setCalculator(BesselCalculator calculator) {
        this.calculator = calculator;
    }

    public void updateSize() {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = calculator.yAxisHeight;
        lp.width = calculator.yAxisWidth;
        setLayoutParams(lp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (labels.size() == 0)
//            return;
//        float coordinateX = 0;
//        if (position == POSITION_LEFT) {
//            coordinateX = calculator.yAxisWidth - style.getAxisLineWidth();
//        }
//        float startCoordinateY = labels.get(0).y;
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(style.getAxisLineWidth());
//        paint.setColor(style.getVerticalLabelTextColor());
//        paint.setTextSize(style.getVerticalLabelTextSize());
//        paint.setTextAlign(Align.CENTER);
//        canvas.drawLine(coordinateX, startCoordinateY, coordinateX, calculator.height
//                , paint);
//        for (Label label : labels) {
//            canvas.drawText(label.text, label.x, label.drawingY
//                    , paint);
//        }
    }
}