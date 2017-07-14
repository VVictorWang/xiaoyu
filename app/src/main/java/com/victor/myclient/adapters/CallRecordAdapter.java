package com.victor.myclient.adapters;
import android.support.v7.widget.RecyclerView;

import com.victor.myclient.datas.CallRecord;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.myclient.datas.CallRecord;

import de.hdodenhof.circleimageview.CircleImageView;
import demo.animen.com.xiaoyutask.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder> {

    private List<CallRecord> callRecordList;
    private Context context;

     class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView head;
        private TextView name;
        private TextView number;
        private String telephoneNum;
        private String xiaoyuNum;
        private TextView date;
        private ImageView callType;

         ViewHolder(View view) {
            super(view);
            head = (CircleImageView) view.findViewById(R.id.record_icon);
            name = (TextView) view.findViewById(R.id.record_title);
            number = (TextView) view.findViewById(R.id.record_number_list);
            date = (TextView) view.findViewById(R.id.call_time);
            callType = (ImageView) view.findViewById(R.id.call_type);
        }
    }

    public CallRecordAdapter(Context context, List<CallRecord> callRecordList) {
        this.callRecordList = callRecordList;
        this.context = context;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        CallRecord callRecord = callRecordList.get(position);

        holder.name.setText(callRecord.getName());
        if (CallRecord.CALL_IN == callRecord.getState()) {
            holder.callType.setImageResource(R.drawable.contact_call_in);
        } else if (CallRecord.CALL_OUT == callRecord.getState()) {
            holder.callType.setImageResource(R.drawable.contact_call_out);
        } else {
            holder.callType.setImageResource(R.drawable.contact_hang_off);
        }
        holder.telephoneNum = callRecord.getTelephoneNum();
        holder.xiaoyuNum = callRecord.getXiaoyuId();
        String num = "";
        if (null != callRecord.getTelephoneNum() && !"".equals(callRecord.getTelephoneNum())) {
            num = callRecord.getTelephoneNum();
        }
        if (null != callRecord.getXiaoyuId() && !"".equals(callRecord.getXiaoyuId())) {
            if (!num.equals("")) {
                num += "       ";
            }
            num += callRecord.getXiaoyuId();
        }
        holder.number.setText(num);
        if (holder.date.getText() == null || holder.date.getText().equals("")) {
            String dateString = "";
            if (null != callRecord.getDate()) {
                Calendar mCalender = Calendar.getInstance();
                SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日");
                Date curDate = new Date(System.currentTimeMillis());
                Date yesterday = curDate;
                mCalender.setTime(curDate);
                int day = mCalender.get(Calendar.DATE);
                mCalender.set(Calendar.DATE, day - 1);
                String yesterdayString = formater.format(mCalender.getTime());
                String curString = formater.format(curDate);
                String callString = formater.format(callRecord.getDate());
                if (curString.equals(callString)) {
                    dateString = new String("今天");
                } else if (yesterdayString.equals(callString)) {
                    dateString = new String("昨天");
                } else {
                    dateString = callString;
                }
            }
            holder.date.setText(dateString);
        }
    }

    @Override
    public int getItemCount() {
        return callRecordList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_record, parent, false);
        return new ViewHolder(view);
    }
}