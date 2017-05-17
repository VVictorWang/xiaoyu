package com.victor.myclient.view.JiuJIA.drawSmoothLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Scroller;

import java.util.List;

/**
 * 贝塞尔曲线图
 *
 * @author tomkeyzhang（qitongzhang@anjuke.com）
 * @date :2014年5月4日
 */
class BesselChartView extends View {

    /**
     * 通用画笔
     */
    private Paint paint;
    /**
     * 曲线的路径，用于绘制曲线
     */
    private Path curvePath;
    /**
     * 曲线图绘制的计算信息
     */
    private BesselCalculator calculator;
    /**
     * 曲线图的样式
     */
    private ChartStyle style;
    /**
     * 曲线图的数据
     */
    private ChartData data;
    /**
     * 手势解析
     */
    private GestureDetector detector;
    /**
     * 是否绘制全部贝塞尔结点
     */
    private boolean drawBesselPoint;
    /**
     * 是否绘制所有的结点（false只绘制最大结点）
     */
    private boolean drawAllPoint;
    /**
     * 滚动计算器
     */
    private Scroller scroller;
    /**
     * 曲线图事件监听器
     */
    private BesselChart.ChartListener chartListener;

    public BesselChartView(Context context, ChartData data, ChartStyle style,
                           BesselCalculator calculator) {
        super(context);
        this.calculator = calculator;
        this.data = data;
        this.style = style;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.curvePath = new Path();
        this.drawBesselPoint = false;
        this.drawAllPoint = false;
        this.scroller = new Scroller(context);

        this.detector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            float lastScrollX = 0f;

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (Math.abs(distanceX / distanceY) > 1) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    BesselChartView.this.calculator.move(distanceX);
                    ViewCompat.postInvalidateOnAnimation(BesselChartView.this);
                    if (e1.getX() != lastScrollX) {
                        lastScrollX = e1.getX();
                        if (chartListener != null) {
                            chartListener.onMove();
                        }
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scroller.fling((int) BesselChartView.this.calculator.getTranslateX(), 0, (int) velocityX, 0,
                        -getWidth(), 0, 0, 0);
                ViewCompat.postInvalidateOnAnimation(BesselChartView.this);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                scroller.forceFinished(true);
                ViewCompat.postInvalidateOnAnimation(BesselChartView.this);
                return true;
            }
        });
    }


    public void animateScrollToEnd(int duration) {
        scroller.startScroll(0, 0, -calculator.xAxisWidth / 2, 0, duration);
    }

    public void setChartListener(BesselChart.ChartListener chartListener) {
        this.chartListener = chartListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            calculator.moveTo(scroller.getCurrX());
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data.getSeriesList().size() == 0) {
            return;
        }
        calculator.ensureTranslation();
        //canvas.translate(calculator.getTranslateX(), 0);
        drawBackGround(canvas);
        drawGrid(canvas);
        drawCurveAndPoints(canvas);
//    drawMarker(canvas);
        drawHorLabels(canvas);
        drawTemText(canvas);
    }


    /**
     * 绘制曲线图中的maker
     */
    private void drawMarker(Canvas canvas) {
        Marker marker = data.getMarker();
        if (marker != null) {
            paint.setAlpha(255);
            canvas.drawBitmap(marker.getBitmap(), null, marker
                            .updateRect(marker.getPoint().x, marker.getPoint().y, marker.getWidth(),
                                    marker.getHeight()),
                    paint);
        }
    }

    /**
     *
     */
    private void drawBackGround(Canvas canvas){

        paint.setColor(style.getBackgroundUpPartColor());
//    Path path=new Path();
//    path.moveTo(0,0);
//    path.lineTo((float) (calculator.height/2.0),0);
//    path.lineTo((float) (calculator.height/2.0),calculator.width);
//    path.lineTo(0,calculator.width);
//    path.close();
        canvas.drawRect(0,0,calculator.width, ((float) calculator.height/2),paint);
        paint.setColor(style.getBackgroundDownPartColor());
        canvas.drawRect(0, ((float) calculator.height/2),calculator.width,calculator.height,paint);
    }
    /**
     * 绘制网格线
     */
    private void drawGrid(Canvas canvas) {
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(style.getGridColor());
        paint.setAlpha(80);
        List<ChartData.Label> yLabels = data.getYLabels();
        // 先绘制两条水平线
//    float coordinateY = yLabels.get(0).y;
//    canvas.drawLine(0, coordinateY, calculator.xAxisWidth, coordinateY, paint);
//    coordinateY = yLabels.get(yLabels.size() - 1).y;
//    canvas.drawLine(0, coordinateY, calculator.xAxisWidth, coordinateY, paint);


        // 再绘制竖直线（标记最大值的竖直线）
        Paint paint1=new Paint();
        paint1.setDither(false);
        paint1.setStrokeWidth(20);

        paint1.setAlpha(255);

        for (Point point : calculator.maxPoints) {
            if (point != null && point.willDrawing && point.valueY > 0) {
                LinearGradient linearGradient = new LinearGradient(point.x, point.y, point.x,
                        (float) calculator.yAxisHeight, style.getVerticalLineColor(),
                        0x00ffffff,
                        TileMode.MIRROR);
                paint1.setShader(linearGradient);
                canvas.drawRect(point.x-5, point.y, point.x+5, calculator.yAxisHeight, paint1);
            }
        }
    }

    /**
     * 绘制曲线和结点
     */
    private void drawCurveAndPoints(Canvas canvas) {
        paint.setStrokeWidth(10);
        paint.setDither(false);

        for (Series series : data.getSeriesList()) {
            paint.setColor(series.getColor());
            curvePath.reset();
            List<Point> list = series.getBesselPoints();
            for (int i = 0; i < list.size(); i = i + 3) {
                if (i == 0) {
                    curvePath.moveTo(list.get(i).x, list.get(i).y);
                } else {
                    curvePath
                            .cubicTo(list.get(i - 2).x, list.get(i - 2).y, list.get(i - 1).x, list.get(i - 1).y,
                                    list.get(i).x, list.get(i).y);
                }
            }
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(curvePath, paint);// 绘制光滑曲线
            paint.setStyle(Paint.Style.FILL);
            if (drawAllPoint) {
                for (Point point :  series.getPoints()  ) {
                    if (point.willDrawing) {
                        paint.setAlpha(80);
                        canvas.drawCircle(point.x, point.y, 10, paint);
                        paint.setAlpha(180);
                        canvas.drawCircle(point.x, point.y, 25, paint);
                        paint.setAlpha(255);
                    }
                }
            }else {
                for (Point point : calculator.maxPoints ) {
                    if (point.willDrawing) {
                        paint.setAlpha(80);
                        canvas.drawCircle(point.x, point.y, 10, paint);
                        paint.setAlpha(180);
                        canvas.drawCircle(point.x, point.y, 15, paint);
                        paint.setAlpha(255);
                    }
                }
            }// 绘制结点
            if (drawBesselPoint) {
                for (Point point : series.getPoints()) {
                    if (!series.getPoints().contains(point)) {
                        paint.setColor(Color.BLUE);
                        paint.setAlpha(255);
                        canvas.drawCircle(point.x, point.y, 10, paint);
                    }
                }// 绘制贝塞尔控制结点
            }
        }
    }

    /**
     * 取消横轴实心线的绘制
     * 绘制横轴
     */
    private void drawHorLabels(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setColor(style.getHorizontalLabelTextColor());
        paint.setTextSize(style.getHorizontalLabelTextSize());
        paint.setTextAlign(Align.CENTER);
//    float endCoordinateX = calculator.xAxisWidth;
//    float coordinateY = getHeight() - calculator.xAxisHeight;
//    canvas.drawLine(0, coordinateY, endCoordinateX, coordinateY, paint);
        for (ChartData.Label label : data.getXLabels()) {
            // 绘制橫坐标文本
            canvas.drawText(label.text, label.x, label.y,
                    paint);
        }
    }
    /**
     *绘制最大最小温度
     * @param canvas
     */
    private void drawTemText(Canvas canvas){
        //最大值
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setColor(style.getMaxTemColor());
        paint.setTextSize(style.getMaxTemTextSize());
        paint.setTextAlign(Align.CENTER);
//    float endCoordinateX = calculator.xAxisWidth;
//    float coordinateY = getHeight() - calculator.xAxisHeight;
//    canvas.drawLine(0, coordinateY, endCoordinateX, coordinateY, paint);
        canvas.drawText(calculator.maxTemperature,data.getXLabels().get(1).x,100,paint);

        //上升值
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(style.getRaiseTemColor());
        paint.setTextSize(style.getRaiseTemTextSize());
        paint.setTextAlign(Align.CENTER);
//    float endCoordinateX = calculator.xAxisWidth;
//    float coordinateY = getHeight() - calculator.xAxisHeight;
//    canvas.drawLine(0, coordinateY, endCoordinateX, coordinateY, paint);
        canvas.drawText(calculator.raiseTemperature,(data.getXLabels().get(1).x+data.getXLabels().get(0).x)/2,200,paint);

        Path path=new Path();
        path.moveTo((data.getXLabels().get(1).x+data.getXLabels().get(0).x)/2-80,(float) (200-20*Math.sqrt(3)));
        path.lineTo((data.getXLabels().get(1).x+data.getXLabels().get(0).x)/2-100,200);
        path.lineTo((data.getXLabels().get(1).x+data.getXLabels().get(0).x)/2-60,200);
        path.close();
        canvas.drawPath(path,paint);
        //最小值
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(style.getMinTemColor());
        paint.setTextSize(style.getMinTemTextSize());
        paint.setTextAlign(Align.CENTER);
//    float endCoordinateX = calculator.xAxisWidth;
//    float coordinateY = getHeight() - calculator.xAxisHeight;
//    canvas.drawLine(0, coordinateY, endCoordinateX, coordinateY, paint);
        canvas.drawText(calculator.minTemperature,data.getXLabels().get(0).x,calculator.height/2+style.getMinTemTextSize()/2,paint);
    }
    public void updateSize() {
        LayoutParams lp = getLayoutParams();
        lp.height = calculator.height;
        lp.width = calculator.xAxisWidth;
        setLayoutParams(lp);
    }
    public void setDrawBesselPoint(boolean drawBesselPoint) {
        this.drawBesselPoint = drawBesselPoint;
    }

}