package com.victor.myclient;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.datas.ServiceHistory;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.drawSmoothLine.Log;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by Silver on 2017/7/13.
 */

public class ServiceHistoryActivity extends Activity implements HistoryListAdapter.MyClickListener {
    private int patientId;
    private boolean net_work;
    private List<ServiceHistory> list;
    private GetServiceTask getServiceTask;
    private RecyclerView recyclerView;
    private HistoryListAdapter adapter;
    private Context context;
    private HistoryListAdapter.MyClickListener clickListener;

    private static final String TAG = "ServiceHistoryActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context=this;
        clickListener=this;
        patientId=Integer.parseInt(getIntent().getStringExtra("id"));
        net_work = Utils.isNetWorkAvailabe(this);
        initView();
        getServiceTask = new GetServiceTask();
        getServiceTask.execute();
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
//        adapter=new HistoryListAdapter(list,this,this);
//        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        if (getServiceTask != null) {
            getServiceTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(int position) {
        android.util.Log.d(TAG, "onItemClick: position ="+position);
    }

    class GetServiceTask extends AsyncTask<Void, Void, Void> {
        Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String s =
                        Utils.sendRequest(GlobalData.GET_SERVICE_HISTORY + patientId);
                if (!(s == null || s.contains("param_error") || s.contains("not_exist"))) {
                    list = gson.fromJson(s, new TypeToken<List<ServiceHistory>>() {
                    }.getType());
                } else {
                    list = null;
                }
                DataSupport.deleteAll(ServiceHistory.class);
                DataSupport.saveAll(list);
            } else {
                list = DataSupport.findAll(ServiceHistory.class);
            }
            if (list != null) {
                Collections.sort(list, new Comparator<ServiceHistory>() {
                    @Override
                    public int compare(ServiceHistory o1, ServiceHistory o2) {
                        if (o1.getServiceDatetime().compareTo(o2.getServiceDatetime()) < 0) {
                            return -1;
                        } else if (o1.getServiceDatetime().compareTo(o2.getServiceDatetime()) > 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter=new HistoryListAdapter(list,context,clickListener);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(aVoid);
        }
    }
}
