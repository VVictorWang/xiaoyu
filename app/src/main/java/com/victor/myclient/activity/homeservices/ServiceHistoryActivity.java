package com.victor.myclient.activity.homeservices;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.adapters.HistoryListAdapter;
import com.victor.myclient.datas.ServiceHistory;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;

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
    private List<ServiceHistory> list = new ArrayList<>();
    private GetServiceTask getServiceTask;
    private RecyclerView recyclerView;
    private HistoryListAdapter adapter;
    private Context context;
    private HistoryListAdapter.MyClickListener clickListener;
    private ImageView imageView;
    private RelativeLayout back;

    private static final String TAG = "ServiceHistoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = this;
        clickListener = this;
        patientId = Integer.parseInt(getIntent().getStringExtra("id"));
        android.util.Log.d(TAG, "onCreate: id=" + patientId);
        net_work = Utils.isNetWorkAvailabe(this);
        initView();
        initEvent();
        getServiceTask = new GetServiceTask();
        getServiceTask.execute();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        back = (RelativeLayout) findViewById(R.id.back_to_main_history);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        imageView = (ImageView) findViewById(R.id.imageView);
        list = new ArrayList<>();
        adapter = new HistoryListAdapter(list, this, this);
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(ServiceHistoryActivity.this);
            }
        });
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
        android.util.Log.d(TAG, "onItemClick: position =" + position);
        Glide.with(this).
                load(GlobalData.GET_IMAGE + list.get(position).getServiceContent()).
                fitCenter().
                error(R.drawable.backspace_number).
                into(imageView);
        recyclerView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (imageView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    class GetServiceTask extends AsyncTask<Void, Void, Void> {
        private Gson gson = new Gson();

        @Override
        protected Void doInBackground(Void... params) {
            if (net_work) {
                String sendRequest =
                        Utils.sendRequest(GlobalData.GET_SERVICE_HISTORY + patientId);
                android.util.Log.d(TAG, "doInBackground: sendRequest=" + sendRequest);
                if (!(sendRequest == null || sendRequest.contains("param_error") || sendRequest.contains("not_exist"))) {
                    list = gson.fromJson(sendRequest, new TypeToken<List<ServiceHistory>>() {}.getType());
//                    ServiceHistory serviceHistory = gson.fromJson(sendRequest, ServiceHistory.class);
//                    list = new ArrayList<>();
//                    list.add(serviceHistory);

                } else {
                    list.clear();
                }
                DataSupport.deleteAll(ServiceHistory.class);
                DataSupport.saveAll(list);
            } else {
                list = DataSupport.findAll(ServiceHistory.class);
            }
            if (!(list.isEmpty())) {
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
            adapter = new HistoryListAdapter(list, context, clickListener);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(aVoid);
        }
    }
}
