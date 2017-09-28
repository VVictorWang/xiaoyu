package com.victor.myclient.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.victor.myclient.utils.MyActivityManager;
import com.victor.myclient.bean.CallRecord;
import com.victor.myclient.ui.activity.VideoActivity;
import com.victor.myclient.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by 小武哥 on 2017/4/29.
 */

public class CallRecordAdapter extends RecyclerView.Adapter<CallRecordAdapter.ViewHolder> {

    private List<CallRecord> callRecordList;
    private Context context;

    public CallRecordAdapter(Context context) {
        this.callRecordList = new ArrayList<>();
        this.context = context;
    }

    public void addItems(List<CallRecord> callRecords) {
        callRecordList.clear();
        callRecordList.addAll(callRecords);
        notifyDataSetChanged();
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
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
        holder.telephoneNum = num;
        String dateString = "";
        if (null != callRecord.getDate()) {
            Calendar mCalender = Calendar.getInstance();
            SimpleDateFormat formater = new SimpleDateFormat("MM月dd日");
            Date curDate = new Date(System.currentTimeMillis());
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
        holder.mRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("number", holder.telephoneNum);
                intent.putExtra("type", "patient");
                MyActivityManager.startActivity((Activity) context, intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return callRecordList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_record, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        return holder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView head;
        private TextView name;
        private TextView number;
        private String telephoneNum;
        private String xiaoyuNum;
        private TextView date;
        private ImageView callType;
        private RelativeLayout mRelativeLayout;

        public ViewHolder(View view) {
            super(view);
            head = (CircleImageView) view.findViewById(R.id.record_icon);
            name = (TextView) view.findViewById(R.id.record_title);
            number = (TextView) view.findViewById(R.id.record_number_list);
            date = (TextView) view.findViewById(R.id.call_time);
            callType = (ImageView) view.findViewById(R.id.call_type);
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.call_record);
        }
    }
}
