package com.victor.myclient.view.JiuJIA;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.myclient.Datas.OneKeyWarning;

import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 17-5-4.
 */

public class BaojingAdapter extends RecyclerView.Adapter<BaojingAdapter.ViewHolder> {
    private Context context;
    private List<OneKeyWarning> oneKeyWarnings;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView number;
        private TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.baojing_she_bei_number);
            time = (TextView) itemView.findViewById(R.id.baojing_time);
        }
    }

    public BaojingAdapter(Context context, List<OneKeyWarning> oneKeyWarnings) {
        this.context = context;
        this.oneKeyWarnings = oneKeyWarnings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.baojing_adpter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       OneKeyWarning oneKeyWarning = oneKeyWarnings.get(position);
        holder.time.setText("报警时间: " + oneKeyWarning.getAdd_date());
        holder.number.setText("设备编号: " + oneKeyWarning.getSid());
    }

    @Override
    public int getItemCount() {
        return oneKeyWarnings.size();
    }
}
