package com.example.franklin.myclient.view.Contact.Record;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

  static class ViewHolder extends RecyclerView.ViewHolder {

    private ImageView head;
    private TextView name;
    private TextView number;
    private String telephoneNum;
    private String xiaoyuNum;
    private TextView date;
    private ImageView callType;

    public ViewHolder(View view) {
      super(view);
      head = (ImageView) view.findViewById(R.id.record_icon);
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
    if (callRecord.getImageUrl() != null) {
      holder.head.setImageURI(Uri.parse(callRecord.getImageUrl()));
    }

    holder.name.setText(callRecord.getName());
    if (CallRecord.CALL_IN == callRecord.getState()) {
      holder.callType.setImageResource(R.drawable.call_image);
    } else if (CallRecord.CALL_OUT == callRecord.getState()) {
      holder.callType.setImageResource(R.drawable.call_image);
    } else {
      holder.callType.setImageResource(R.drawable.call_image);
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
}
