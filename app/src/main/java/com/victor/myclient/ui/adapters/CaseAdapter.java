package com.victor.myclient.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.myclient.data.CaseInfor;
import com.victor.myclient.ui.activity.CaseDetailActivity;
import com.victor.myclient.utils.Utils;
import com.victor.myclient.view.CircleImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/23.
 */

//病例列表Adapter
public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder> {

    private List<CaseInfor> caseList = new ArrayList<>();
    private Context context;

    public CaseAdapter(Context context) {
        caseList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .item_case, parent, false));
    }

    public void addItems(List<CaseInfor> caseInfors) {
        caseList.clear();
        caseList.addAll(caseInfors);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CaseInfor aCase = caseList.get(position);
        holder.patientName.setText(aCase.getName());
        holder.doctorName.setText("主治医生: " + aCase.getDoctorName());
        holder.illnessName.setText(aCase.getIllproblem());
        Date date = Utils.stringToDate(aCase.getDate());
        holder.dateText.setText(Utils.dateToString(date));
        holder.circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CaseDetailActivity.class);
                intent.putExtra("id", aCase.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorName;
        private TextView illnessName;
        private TextView dateText;
        private TextView patientName;
        private CircleImageView circleButton;

        ViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.case_patient_name);
            doctorName = (TextView) view.findViewById(R.id.case_doctor_name);
            circleButton = (CircleImageView) view.findViewById(R.id.circleImageView);
            illnessName = (TextView) view.findViewById(R.id.case_illness_name);
            dateText = (TextView) view.findViewById(R.id.case_date_text);
        }
    }

}
