package com.victor.myclient.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victor.myclient.ActivityManage;
import com.victor.myclient.datas.CaseInfor;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.adapters.CaseAdapter;
import com.victor.myclient.view.CustomLayoutManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/27.
 */

public class CaseListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private RelativeLayout back;
    private String patientId;
    private List<CaseInfor> caseInfors = new ArrayList<>();
    private CaseAdapter adapter;
    private ProgressDialog progressDialog;
    private boolean net_work_available, has_data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                Utils.showShortToast(CaseListActivity.this, "没有数据");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_list);
        ActivityManage.getInstance().pushActivity(CaseListActivity.this);
        patientId = getIntent().getStringExtra("id");
        if (patientId == null || patientId.equals("")) {
            Utils.showShortToast(CaseListActivity.this, "患者没有病例信息");
        } else {
            initView();
            initEvent();
            new CaseListTask().execute();
        }
    }
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.case_list_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLayoutManager manager = new CustomLayoutManager();
        recyclerView.setLayoutManager(manager);
        caseInfors = new ArrayList<>();
        net_work_available = Utils.isNetWorkAvailabe(CaseListActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
    }



    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.finishActivity(CaseListActivity.this);
            }
        });
    }

    private class CaseListTask extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && has_data) {
                adapter = new CaseAdapter(CaseListActivity.this, caseInfors);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } else {
                handler.sendEmptyMessage(0x123);
            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            if (net_work_available) {
                String info = Utils.sendRequest(GlobalData.GET_PATIENT_CASE + patientId);
                if (!info.contains("not_exist")) {
                    caseInfors = gson.fromJson(info, new TypeToken<List<CaseInfor>>() {
                    }.getType());
                    DataSupport.deleteAll(CaseInfor.class);
                    for (CaseInfor caseInfor : caseInfors) {
                        if (!caseInfor.isSaved()) {
                            caseInfor.save();
                        }
                    }
                    has_data = true;
                }

            } else {
                if (DataSupport.isExist(CaseInfor.class)) {
                    caseInfors = DataSupport.findAll(CaseInfor.class);
                    has_data = true;
                } else {
                    has_data = false;
                }
            }
            return "ok";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
    }
}
