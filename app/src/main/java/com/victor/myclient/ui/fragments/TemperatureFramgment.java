package com.victor.myclient.ui.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.victor.myclient.api.UserApi;
import com.victor.myclient.bean.HomeInfor;
import com.victor.myclient.ui.base.BaseFragment;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.utils.PrefUtils;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.widget.drawSmoothLine.BesselChart;
import com.victor.myclient.widget.drawSmoothLine.ChartData;
import com.victor.myclient.widget.drawSmoothLine.Point;
import com.victor.myclient.widget.drawSmoothLine.Series;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by victor on 17-5-3.
 */

public class TemperatureFramgment extends BaseFragment implements BesselChart.ChartListener {

    public static final String TAG = "@victor TempatureFramg";

    private static final String PATIENTID = "patientId";

    private RelativeLayout back;
    private TextView update;
    private TextView current_shi;
    private TextView time_text;
    private ImageView choose_left, choose_right;
    private BesselChart chart;
    private BarChart barChart;
    private ProgressDialog dialog;

    private List<Point> points = new ArrayList<>();

    private String dateString;
    private String patientId = null;

    public static TemperatureFramgment newInstance(String patientId) {
        Bundle args = new Bundle();
        args.putString(PATIENTID, patientId);
        TemperatureFramgment fragment = new TemperatureFramgment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jujia2;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            patientId = getArguments().getString(PATIENTID);
        }
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    protected void initView() {
        chart = (BesselChart) rootView.findViewById(R.id.shidu_line_chart);
        current_shi = (TextView) rootView.findViewById(R.id.current_shidu_text);
        barChart = (BarChart) rootView.findViewById(R.id.bar_shidu_chart);
        back = (RelativeLayout) rootView.findViewById(R.id.back_to_main_jujia);
        update = (TextView) rootView.findViewById(R.id.update_jujia);
        choose_left = (ImageView) rootView.findViewById(R.id.choose_data_jiujia_left);
        choose_right = (ImageView) rootView.findViewById(R.id.choose_data_jiujia_right);
        time_text = (TextView) rootView.findViewById(R.id.time_temparature);
        dialog = new ProgressDialog(activity);
        dialog.setMessage("正在加载中");
        chart.setSmoothness(0.4f);
        chart.setChartListener(this);
        chart.setSmoothness(0.33f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        Description description1 = new Description();
        description1.setText("");
        description1.setPosition(0, 0);
        barChart.setDescription(description1);
        barChart.setDrawBarShadow(true);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
        initEvent();

    }

    private void init() {
        Date date = new Date(System.currentTimeMillis());
        String current_data = Utils.DateToStringWithChinese(date);
        time_text.setText(current_data);
        dateString = Utils.dataTostringtem(date);
        getInfo();
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.finishActivity(activity);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateString = Utils.dataTostringtem(new Date(System
                        .currentTimeMillis()));
                time_text.setText(Utils.DateToStringWithChinese(new Date(System.currentTimeMillis
                        ())));
                getInfo();
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
                dateString = Utils.dataTostringtem(date);
                getInfo();
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
                    dateString = Utils.dataTostringtem(date);
                    time_text.setText(Utils.DateToStringWithChinese(date));
                    getInfo();
                }

            }
        });
    }

    private void refreshData(HomeInfor homeInfor) {
        List<Series> seriess = new ArrayList<>();
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
        chart.getData().setSeriesList(seriess);
        chart.refresh(true);

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
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
        current_shi.setText((int) homeInfor.getHumidity() + "%");
    }

    @Override
    public void onMove() {
    }

    private void getInfo() {
        dialog.show();
        Observable<HomeInfor> observable = UserApi.getInstance().getHomeInfo(Integer.valueOf
                (patientId != null ?
                        patientId : PrefUtils.getValue(activity, GlobalData.PATIENT_ID)), String
                .valueOf
                        (dateString));
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HomeInfor>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.showShortToast(activity, "没有数据");
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(HomeInfor homeInfor) {
                        refreshData(homeInfor);
                    }
                });
    }


}