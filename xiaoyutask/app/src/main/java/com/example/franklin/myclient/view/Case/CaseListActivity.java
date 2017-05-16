package com.example.franklin.myclient.view.Case;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.franklin.myclient.DataBase.CaseListDataBase;
import com.example.franklin.myclient.Datas.CaseInfor;
import com.example.franklin.myclient.SomeUtils.GlobalData;
import com.example.franklin.myclient.SomeUtils.Utils;
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
    private boolean status;
    private CaseListDataBase dataBase;

    private boolean net_work_available,has_data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_list);
        patientId = getIntent().getStringExtra("id");
        if (patientId == null || patientId.equals("")) {
            Utils.showShortToast(CaseListActivity.this, "此用户没有绑定患者");
            Utils.finishActivity(CaseListActivity.this);
        } else {
            Log.e("patientid", patientId);
            DisplayUtils.init(this);//获取屏幕宽度高度信息
            initView();
            initEvent();
            new CaseListTask().execute(patientId);
        }
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.case_list_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        CustomLayoutManager manager = new CustomLayoutManager();
        recyclerView.setLayoutManager(manager);
        dataBase = new CaseListDataBase(CaseListActivity.this);
        status = false;
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



    class CaseListTask extends AsyncTask<String, Void, String> {
        private Gson gson = new Gson();
        @Override

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && has_data) {
                adapter = new CaseAdapter(CaseListActivity.this, caseInfors);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            if (!net_work_available && !dataBase.isEmpty()) {
                Cursor c = dataBase.getAllItems();
                c.moveToFirst();
                do {
                    CaseInfor caseInfor = new CaseInfor();
                    caseInfor.setId(c.getInt(c.getColumnIndex(CaseListDataBase.DB_COLUMN_ID)));
                    caseInfor.setDoctorId(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_DOCTOR_ID)));
                    caseInfor.setPatientId(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUNM_PATIETENT_ID)));
                    caseInfor.setBlood_pressure(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_BLODD_PRESSURE)));
                    caseInfor.setCreationDate(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_CREATE_TIME)));
                    caseInfor.setDoctorName(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_DOCTOR_NAME)));
                    caseInfor.setName(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_PATIENTEN_NAME)));
                    caseInfor.setIllproblem(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_ILL_PROBLEM)));
                    caseInfor.setIllresult(c.getString(c.getColumnIndex(CaseListDataBase.DB_COLUMN_ILL_RESULT)));
                    caseInfors.add(caseInfor);
                } while (c.moveToNext());
                has_data = true;
            } else if (net_work_available) {
                caseInfors = gson.fromJson(Utils.sendRequest(GlobalData.GET_PATIENT_CASE + patientId), new TypeToken<List<CaseInfor>>() {
                }.getType());

                dataBase.update();
                for (CaseInfor caseInfor : caseInfors) {
                    dataBase.insert(caseInfor.getId(), caseInfor.getName(), caseInfor.getCreationDate(), caseInfor.getPatientId(), caseInfor.getDoctorId(), caseInfor.getIllproblem(), caseInfor.getIllresult(), caseInfor.getTemperature(), caseInfor.getBlood_pressure(), caseInfor.getDoctorName());
                }
                has_data = true;
            } else if (!net_work_available && dataBase.isEmpty()) {
                has_data = false;
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
