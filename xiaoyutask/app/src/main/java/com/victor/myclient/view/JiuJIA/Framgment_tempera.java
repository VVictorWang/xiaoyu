package com.victor.myclient.view.JiuJIA;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.victor.myclient.Datas.DoorInfor;
import com.victor.myclient.Datas.HomeInfor;
import com.victor.myclient.SomeUtils.GlobalData;
import com.victor.myclient.SomeUtils.Utils;
import com.victor.myclient.view.JiuJIA.drawSmoothLine.BesselChart;
import com.victor.myclient.view.JiuJIA.drawSmoothLine.ChartData;
import com.victor.myclient.view.JiuJIA.drawSmoothLine.Point;
import com.victor.myclient.view.JiuJIA.drawSmoothLine.Series;

import org.litepal.crud.DataSupport;

import demo.animen.com.xiaoyutask.R;
import java.util.ArrayList;
import java.util.Date;
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
    private TextView current_shi;
    private ImageView choose_left, choose_right;
    private List<Point> points=new ArrayList<>();
    private boolean network_ava,has_data=false;
    private static final String TAG = "Framgment_tempera";
    private String data_string;
    private long data_number;
    private TextView time_text;
    private String current_data;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1234) {
                List<Series> seriess = new ArrayList<Series>();
                points.clear();
                float a[] = {3, 6, 9, 12, 15, 18, 21, 24};
                for (int i = 0; i < 8; i++) {
                    points.add(new Point( a[i], homeInfor.getTemperatures().get((int)(a[i]-1)), true));
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
        description1.setPosition(0,0);
        barChart.setDescription(description1);
        barChart.setDrawBarShadow(true);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
        current_shi.setText((int) homeInfor.getHumidity() + "%");
    }

    private void initView(){
        network_ava = Utils.isNetWorkAvailabe(activity);
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
        Date date = new Date(System.currentTimeMillis());
        data_string = Utils.dataTostringtem(date);
        data_number = Long.parseLong(data_string);
        Log.e(TAG, data_string);
        current_data = Utils.dataToStringWithChinese(date);
        time_text.setText(current_data);
        new GetHomeInforTask().execute(data_string);
        chart.setSmoothness(0.33f);
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
                data_number = Long.parseLong(Utils.dataTostringtem(new Date(System.currentTimeMillis())));
                time_text.setText((data_number / 10000) + "年" + ((data_number / 100) % 100) + "月" + (data_number % 100));
                new GetHomeInforTask().execute("" + data_number);
            }
        });
        choose_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long current = Long.parseLong(Utils.dataTostringtem(new Date(System.currentTimeMillis())));
                if (data_number == current) {
                    Utils.showShortToast(activity, "已经是今天的数据了");
                } else {
                    data_number++;
                    data_number = Utils.parsedataNumber(data_number,true);
                    Log.e(TAG, "data_number:  " + data_number);

                        time_text.setText((data_number / 10000) + "年" + ((data_number / 100) % 100) + "月" + (data_number % 100));
                        new GetHomeInforTask().execute("" + data_number);

                }

            }
        });
        choose_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data_number--;
                data_number = Utils.parsedataNumber(data_number, false);
                Log.e(TAG, "data_number:  " + data_number);
                time_text.setText((data_number / 10000) + "年" + ((data_number / 100) % 100) + "月" + (data_number % 100));
                new GetHomeInforTask().execute("" + data_number);
            }
        });
    }
    class GetHomeInforTask extends AsyncTask<String, Void, Void> {
        private Gson gson = new Gson();

        private ProgressDialog dialog = new ProgressDialog(activity);
        @Override
        protected Void doInBackground(String... params) {
            if (network_ava) {
                String date = params[0];
                String infor = Utils.sendRequest(GlobalData.GET_HOME_INFOR + Utils.getValue(activity, GlobalData.PATIENT_ID) + "&date=" + date);

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
                handler.sendEmptyMessage(0x1234);
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