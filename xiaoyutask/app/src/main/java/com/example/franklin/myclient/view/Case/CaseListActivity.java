package com.example.franklin.myclient.view.Case;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.franklin.myclient.CaseInfor;
import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.Case.CaseLayout.Case;
import com.example.franklin.myclient.view.Case.CaseLayout.CaseAdapter;
import com.example.franklin.myclient.view.Case.CaseLayout.CustomLayoutManager;
import com.example.franklin.myclient.view.Case.CaseLayout.DisplayUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import demo.animen.com.xiaoyutask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/27.
 */

public class CaseListActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    private RelativeLayout back;
    private String patientId;

    private List<CaseInfor> caseInfors;
    private CaseAdapter adapter ;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_list);
        patientId = getIntent().getStringExtra("id");
        patientId = "2";
        Log.e("patientid", patientId);
        DisplayUtils.init(this);//获取屏幕宽度高度信息
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        initView();
        initEvent();
        new CaseListTask().execute(patientId);

    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.case_list_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        CustomLayoutManager manager = new CustomLayoutManager();
        recyclerView.setLayoutManager(manager);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utils.finishActivity(CaseListActivity.this);
            }
        });
    }



    class CaseListTask extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();
        @Override

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                adapter = new CaseAdapter(CaseListActivity.this, caseInfors);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            caseInfors = gson.fromJson(Utils.sendRequest(GlobalData.GET_PATIENT_CASE + patientId), new TypeToken<List<CaseInfor>>() {
            }.getType());
            Log.e("caase", caseInfors.get(0).getCreationDate());
            return "ok";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog.show();
        }
    }
}
