package com.victor.myclient.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.log.L;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.victor.myclient.datas.HomeInfor;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.drawSmoothLine.BesselChart;
import com.victor.myclient.view.drawSmoothLine.ChartData;
import com.victor.myclient.view.drawSmoothLine.Point;
import com.victor.myclient.view.drawSmoothLine.Series;

import org.litepal.crud.DataSupport;

import demo.animen.com.xiaoyutask.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.data;

/**
 * Created by victor on 17-5-3.
 */

public class TemperatureFramgmentt extends Fragment implements BesselChart.ChartListener {

    public static final String TAG = "@victor TempatureFramg";
    private Activity activity;
    private RelativeLayout back;
    private View layout;
    private TextView update;
    private TextView current_shi;
    private TextView time_text;
    private ImageView choose_left, choose_right;
    private BesselChart chart;
    private BarChart barChart;

    private HomeInfor homeInfor;
    private List<Point> points = new ArrayList<>();
    private boolean network_ava, has_data = false;
    private long data_number;
    private int count = 1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {
                List<Series> seriess = new ArrayList<Series>();
                points.clear();
                float a[] = {3, 6, 9, 12, 15, 18, 21, 24};
                for (int i = 0; i < 8; i++) {
                    points.add(new Point(a[i], homeInfor.getTemperatures().get((int) (a[i] - 1)),
                            true));
                }
                seriess.add(new Series("温度", Color.WHITE, points));
                chart.getData().setLabelTransform(new ChartData.LabelTransform() {
                    @Override
                    public String verticalTransform(int valueY) {
                        return String.format("%d", valueY);
                    }

                    @Override
                    public String horizontalTransform(int valueX) {
                        return String.format("%s", valueX);
                    }

                    @Override
                    public boolean labelDrawing(int valueX) {
                        return true;
                    }
                });

                initData();
                chart.getData().setSeriesList(seriess);
                chart.refresh(true);

            } else if (msg.what == 0x123) {
                Utils.showShortToast(activity, "没有数据");
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (count < 2) {
            init();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        count++;
    }


    private void initView() {
        chart = (BesselChart) layout.findViewById(R.id.shidu_line_chart);
        current_shi = (TextView) layout.findViewById(R.id.current_shidu_text);
        barChart = (BarChart) layout.findViewById(R.id.bar_shidu_chart);
        back = (RelativeLayout) layout.findViewById(R.id.back_to_main_jujia);
        update = (TextView) layout.findViewById(R.id.update_jujia);
        choose_left = (ImageView) layout.findViewById(R.id.choose_data_jiujia_left);
        choose_right = (ImageView) layout.findViewById(R.id.choose_data_jiujia_right);
        time_text = (TextView) layout.findViewById(R.id.time_temparature);
        chart.setSmoothness(0.4f);
        chart.setChartListener(this);
        chart.setSmoothness(0.33f);
    }

    private void init() {
        network_ava = Utils.isNetWorkAvailabe(activity);
        Date date = new Date(System.currentTimeMillis());
        String current_data = Utils.DateToStringWithChinese(date);
        time_text.setText(current_data);

        String data_string = Utils.dataTostringtem(date);
        data_number = Long.parseLong(data_string);
        new getHomeInforTask().execute(data_string);
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
                data_number = Long.parseLong(Utils.dataTostringtem(new Date(System
                        .currentTimeMillis())));
                time_text.setText((data_number / 10000) + "年" + ((data_number / 100) % 100) + "月"
                        + (data_number % 100) + "日");
                new getHomeInforTask().execute("" + data_number);
            }
        });
        choose_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now = time_text.getText().toString();
                Calendar calendar = Calendar.getInstance();
                Date date = Utils.stringToDateWithChinese(now);
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();
                time_text.setText(Utils.DateToStringWithChinese(date));
                String data_string = Utils.dataTostringtem(date);
                data_number = Long.parseLong(data_string);
                new getHomeInforTask().execute("" + data_number);
            }
        });
        choose_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String now = time_text.getText().toString();
                Date date = new Date(System.currentTimeMillis());
                String data_string = Utils.DateToStringWithChinese(date);
                if (now.equals(data_string)) {
                    Utils.showShortToast(activity, "已经是今天的数据了");
                } else {
                    Calendar calendar = Calendar.getInstance();
                    Date date1 = Utils.stringToDateWithChinese(now);
                    calendar.setTime(date1);
                    calendar.add(Calendar.DATE, 1);
                    date = calendar.getTime();
                    data_string = Utils.dataTostringtem(date);
                    data_number = Long.parseLong(data_string);
                    time_text.setText(Utils.DateToStringWithChinese(date));
                    new getHomeInforTask().execute("" + data_number);
                }

            }
        });
    }

    private void initData() {
        IAxisValueFormatter iAxisValueFormatter = new IAxisValueFormatter() {
            private String[] labels = new String[]{"03", "06", "09", "12", "15", "18", "21", "24"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                float percent = value / axis.mAxisRange;
                return labels[(int) ((labels.length) * percent)];
            }
        };
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            barEntries.add(new BarEntry((float) i, homeInfor.getHumidityies().get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setBarShadowColor(0xb1b2b288);
        barDataSet.setDrawValues(false);
        int background = activity.getResources().getColor(R.color.my_background);
        barDataSet.setColor(background);
        int shadow = activity.getResources().getColor(R.color.line_top);
        barDataSet.setBarShadowColor(shadow);
        XAxis xAxis1 = barChart.getXAxis();
        xAxis1.setValueFormatter(iAxisValueFormatter);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTextColor(background);
        xAxis1.setTextSize(10);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        Description description1 = new Description();
        description1.setText("");
        description1.setPosition(0, 0);
        barChart.setDescription(description1);
        barChart.setDrawBarShadow(true);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
        current_shi.setText((int) homeInfor.getHumidity() + "%");
    }

    private class getHomeInforTask extends AsyncTask<String, Void, Void> {
        private Gson gson = new Gson();
        private ProgressDialog dialog = new ProgressDialog(activity);

        @Override
        protected Void doInBackground(String... params) {
            if (network_ava) {
                String date = params[0];
                String infor = Utils.sendRequest(GlobalData.GET_HOME_INFOR + Utils.getValue
                        (activity, GlobalData.PATIENT_ID) + "&date=" + date);
                homeInfor = gson.fromJson(infor, HomeInfor.class);
                DataSupport.deleteAll(HomeInfor.class);
                homeInfor.saveAsync();
                has_data = true;
            } else {
                if (DataSupport.isExist(HomeInfor.class)) {
                    List<HomeInfor> homeInfors = new ArrayList<>();
                    homeInfors = DataSupport.findAll(HomeInfor.class);
                    if (homeInfors != null) {
                        homeInfor = homeInfors.get(0);
                        has_data = true;
                    }
                } else {
                    has_data = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (has_data) {
                if (homeInfor.getHumidityies() != null && homeInfor.getTemperatures() != null) {
                    handler.sendEmptyMessage(0x1234);
                } else {
                    handler.sendEmptyMessage(0x123);
                }
            } else {
                handler.sendEmptyMessage(0x123);
            }
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("正在加载中...");
            dialog.show();
            super.onPreExecute();
        }
    }

    @Override
    public void onMove() {
        Log.d("zqt", "onMove");
    }
}