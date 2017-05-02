package com.example.franklin.myclient.view.Case.CaseLayout;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.franklin.myclient.CaseInfor;
import com.example.franklin.myclient.SomeUtils.Utils;
import com.example.franklin.myclient.view.Case.CaseDetailActivity;

import at.markushi.ui.CircleButton;
import demo.animen.com.xiaoyutask.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/23.
 */

public class CaseAdapter extends RecyclerView.Adapter<CaseAdapter.ViewHolder> {

    private List<CaseInfor> caseList = new ArrayList<>();
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorName;
        private TextView illnessName;
        private TextView dateText;
        private TextView patientName;
        private CircleButton circleButton;

        public ViewHolder(View view) {
            super(view);
            patientName = (TextView) view.findViewById(R.id.case_patient_name);
            doctorName = (TextView) view.findViewById(R.id.case_doctor_name);
            circleButton = (CircleButton) view.findViewById(R.id.circleImageView);
            illnessName = (TextView) view.findViewById(R.id.case_illness_name);
            dateText = (TextView) view.findViewById(R.id.case_date_text);
        }
    }

    public CaseAdapter(Context context, List<CaseInfor> caseList) {
        this.caseList = caseList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        CaseInfor aCase = caseList.get(position);
        holder.patientName.setText(aCase.getName());
        holder.doctorName.setText(aCase.getDoctorId());

        holder.illnessName.setText(aCase.getIllproblem());

        Date date = Utils.stringToDate(aCase.getCreationDate());
        holder.dateText.setText(Utils.dateToString(date));
        holder.circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CaseDetailActivity.class);
//                intent.putExtra()
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

}
