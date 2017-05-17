package com.victor.myclient.view.JiuJIA;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-4.
 */

public class Fragment_BaoJIng extends Fragment {
    private Activity activity;
    private List<BaojingInfor> baojingInfors;
    private View view;

    private RecyclerView recyclerView;
    private BaojingAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.fragment_baojing, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        InitView();
        InitData();
        return view;
    }

    private void InitView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.baojing_information_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        baojingInfors = new ArrayList<>();
    }

    private void InitData() {
        BaojingInfor baojingInfor = new BaojingInfor();
        baojingInfor.setNumber("390284");
        baojingInfor.setTime("2016/324");
        baojingInfors.add(baojingInfor);
        adapter = new BaojingAdapter(activity, baojingInfors);
        recyclerView.setAdapter(adapter);
    }

    class FindBaojingListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
