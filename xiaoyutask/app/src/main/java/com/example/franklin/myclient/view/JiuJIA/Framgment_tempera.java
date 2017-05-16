package com.example.franklin.myclient.view.JiuJIA;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.franklin.myclient.Datas.HomeInfor;
import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.BesselChart;
import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.ChartData;
import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.Point;
import com.example.franklin.myclient.view.JiuJIA.drawSmoothLine.Series;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import demo.animen.com.xiaoyutask.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 17-5-3.
 */

public class Framgment_tempera extends Fragment implements BesselChart.ChartListener{

    BesselChart chart;
    private Activity activity;
    private View layout;
    private BarChart barChart;
    private RelativeLayout back;
    private TextView update;
    private HomeInfor homeInfor;
    private List<Point> points=new ArrayList<>();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {
                List<Series> seriess = new ArrayList<Series>();
                for(int i=0;i<8;i++){
//                    points.add(new Point((float)i,(float) 20.5,true));
                    points.add(new Point((float)i, Float.parseFloat(homeInfor.getTemperatures().get(i)),true));
//                Log.e("value:", "" + Float.parseFloat(homeInfor.getTemperatures().get(i)));
                }
                seriess.add(new Series("温度",Color.WHITE,points));
                chart.getData().setLabelTransform(new ChartData.LabelTransform() {
                    @Override
                    public String verticalTransform(int valueY) {
                        return String.format("%d", valueY);
                    }

                    @Override
                    public String horizontalTransform(int valueX) {
                        return String.format("%s", valueX );
                    }
                    @Override
                    public boolean labelDrawing(int valueX) {
                        return true;
                    }
                });

//                initData();
                chart.getData().setSeriesList(seriess);
                chart.refresh(true);

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (layout == null) {
            layout = activity.getLayoutInflater().inflate(R.layout.activity_jujia2, null);
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        initView();
        initEvent();
        return layout;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    private void initData(){
        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
            private String[] labels = new String[]{"03","06","09","12","15","18","21","24"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                float percent = value / axis.mAxisRange;
                return labels[(int) ((labels.length) * percent)];
            }
        };
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            barEntries.add(new BarEntry((float) i, Float.parseFloat(homeInfor.getHumidityies().get(i))));
        }
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
        chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);

        barChart = (BarChart) layout.findViewById(R.id.bar_shidu_chart);
        back = (RelativeLayout) layout.findViewById(R.id.back_to_main_jujia);
        update = (TextView) layout.findViewById(R.id.update_jujia);
        chart.setSmoothness(0.4f);
        chart.setChartListener(this);
        new GetHomeInforTask().execute();
        chart.setSmoothness(0.33f);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new GetHomeInforTask().execute();
//            }
//        }, 500);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(activity);
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
        if (willDrawing) {
            new GetHomeInforTask().execute();
        }
    }

    class GetHomeInforTask extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();
        @Override
        protected Void doInBackground(Void... params) {
            String infor = Utils.sendRequest(GlobalData.GET_HOME_INFOR + Utils.getValue(activity, GlobalData.PATIENT_ID));
            homeInfor = gson.fromJson(infor, HomeInfor.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            handler.sendEmptyMessage(0x1234);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    public void onMove() {
        Log.d("zqt", "onMove");
    }
}
