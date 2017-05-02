package com.example.franklin.myclient.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.franklin.myclient.drawSmoothLine.BesselChart;
import com.example.franklin.myclient.drawSmoothLine.BesselChart.ChartListener;
import com.example.franklin.myclient.drawSmoothLine.ChartData.LabelTransform;
import com.example.franklin.myclient.drawSmoothLine.Point;
import com.example.franklin.myclient.drawSmoothLine.Series;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import demo.animen.com.xiaoyutask.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by victor on 2017/4/24.
 */

public class JujiaActivity extends AppCompatActivity implements ChartListener{

    BesselChart chart;

//    private Chart mChart;

    Handler handler = new Handler();
    private BarChart barChart;
    private RelativeLayout back;
    private TextView update;

    private List<Point> points=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jujia2);
        initView();
        initEvent();
        initData();
        initData();
    }
    private void initData(){
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 10));
        entries.add(new Entry(1f, 20));
        entries.add(new Entry(2f, 23));
        entries.add(new Entry(3f, 18));
        entries.add(new Entry(4f, 45));
        entries.add(new Entry(5f, 30));
        entries.add(new Entry(6f, 35));
        entries.add(new Entry(7f, 16));
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        lineDataSet.setColors(Color.YELLOW, Color.BLUE, Color.DKGRAY);
        lineDataSet.setFillColor(Color.BLUE);
        lineDataSet.setCircleColor(255);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(6);
        lineDataSet.setFillAlpha(255);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(18);
        lineDataSet.setDrawValues(false);
        LineData lineData = new LineData(lineDataSet);
//        mChart.setData(lineData);
//        mChart.getLegend().setEnabled(false);
//        mChart.getAxisLeft().setEnabled(false);
//        mChart.getAxisRight().setEnabled(false);
//        mChart.getXAxis().setDrawGridLines(false);
//        mChart.getXAxis().setDrawAxisLine(false);

        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
            private String[] labels = new String[]{"03","06","09","12","15","18","21","24"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                float percent = value / axis.mAxisRange;
                return labels[(int) ((labels.length - 1) * percent)];
            }
        };
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTextColor(Color.WHITE);
//        xAxis.setTextSize(10);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(iAxisValueFormatter);
        Description description = new Description();
        description.setText("23");
        description.setTextSize(40);
        description.setPosition(140,225);
        description.setTextColor(Color.WHITE);
//        mChart.setDescription(description);
//
//        mChart.invalidate();
        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 45));
        barEntries.add(new BarEntry(1f, 34));
        barEntries.add(new BarEntry(2f, 67));
        barEntries.add(new BarEntry(3f, 20));
        barEntries.add(new BarEntry(4f, 78));
        barEntries.add(new BarEntry(5f, 25));
        barEntries.add(new BarEntry(6f, 10));
        barEntries.add(new BarEntry(7f, 35));
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setBarShadowColor(0xb1b2b288);
        barDataSet.setDrawValues(false);
        barDataSet.setColor(Color.BLUE);
        XAxis xAxis1 = barChart.getXAxis();
        xAxis1.setValueFormatter(iAxisValueFormatter);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTextColor(Color.BLUE);
        xAxis1.setTextSize(10);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        Description description1 = new Description();
        description1.setText("");
        description1.setPosition(0,0);
        barChart.setDescription(description1);
        barChart.setDrawBarShadow(true);

        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }

    private void initView(){
        chart = (BesselChart) findViewById(R.id.shidu_line_chart);

        barChart = (BarChart) findViewById(R.id.bar_shidu_chart);
        back = (RelativeLayout) findViewById(R.id.back_to_main_jujia);
        update = (TextView) findViewById(R.id.update_jujia);
        chart.setSmoothness(0.4f);
        chart.setChartListener(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                chart.setData(getChartData(true));
                getSeriesList(true);
                // chart.setDrawBesselPoint(true);
                chart.setSmoothness(0.33f);
            }
        }, 200);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Point 点的值（x,y）  x为横轴的值，y为纵轴的值，willingDrawing 为使用此点
     *
     * @param willDrawing true(draw) false(don't draw)
     */
    private void getSeriesList(boolean willDrawing) {


        List<Series> seriess = new ArrayList<Series>();

        Random random=new Random();
//        points.add(new Point(0,5,true));
//        points.add(new Point(1,0,true));
//        points.add(new Point(2,-1,true));
//        points.add(new Point(3,2,true));
//        points.add(new Point(4,1,true));
//        points.add(new Point(5,-1,true));
//        points.add(new Point(6,0,true));
//        points.add(new Point(7,3,true));

        for(int i=0;i<8;i++){
            points.add(new Point(i,random.nextInt(20),true));
        }
        seriess.add(new Series("温度",Color.WHITE,points));

        if (willDrawing) {
            chart.getData().setLabelTransform(new LabelTransform() {
                @Override
                public String verticalTransform(int valueY) {
                    return String.format("%d", valueY);
                }

                @Override
                public String horizontalTransform(int valueX) {
                    return String.format("%s", valueX*3+3 );
                }
                @Override
                public boolean labelDrawing(int valueX) {
                    return true;
                }
            });
        }
        chart.getData().setSeriesList(seriess);
        chart.refresh(true);
    }
    @Override
    public void onMove() {
        Log.d("zqt", "onMove");
    }
}

