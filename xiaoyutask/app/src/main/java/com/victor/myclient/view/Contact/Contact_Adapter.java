package com.victor.myclient.view.Contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.victor.myclient.view.Contact.sortlist.SortModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by victor on 2017/4/24.
 */

public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.MyViewHoler> {
    private Context context;
    private List<SortModel> list = new ArrayList<>();
    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public static class MyViewHoler extends RecyclerView.ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
        private CircleImageView icon;
       private TextView number_text;

        public MyViewHoler(View itemView) {
            super(itemView);
            this.tvLetter =(TextView) itemView.findViewById(R.id.catalog);
            this.tvTitle = (TextView) itemView.findViewById(R.id.title);
            this.icon = (CircleImageView) itemView.findViewById(R.id.icon);
            this.number_text = (TextView) itemView.findViewById(R.id.number_list);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public Contact_Adapter(Context context, List<SortModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_phone_constacts, parent, false);
        MyViewHoler viewHoler = new MyViewHoler(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(MyViewHoler holder, int position) {
        int section = getSectionForPosition(position);
        final SortModel mContent = list.get(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
           holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText(mContent.getSortLetters());
        }else{
            holder.tvLetter.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(this.list.get(position).getName());
//        holder.icon.setText(this.list.get(position).getName());
//        holder.icon.setIconText(context,this.list.get(position).getName());
        holder.number_text.setText(this.list.get(position).getNumber());

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

}
