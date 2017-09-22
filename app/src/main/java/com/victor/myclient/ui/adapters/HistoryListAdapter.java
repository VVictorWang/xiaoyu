package com.victor.myclient.ui.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.victor.myclient.bean.ServiceHistory;
import com.victor.myclient.utils.GlobalData;
import com.victor.myclient.widget.CircleImageView;

import java.util.List;

import demo.animen.com.xiaoyutask.R;

/**
 * Created by Silver on 2017/7/13.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private List<ServiceHistory> list;
    private Context myContext;
    private LayoutInflater inflater;

    private MyClickListener myClickListener;

    public HistoryListAdapter(List<ServiceHistory> list, Context myContext, MyClickListener
            clickListener) {
        this.list = list;
        this.myContext = myContext;

        inflater = LayoutInflater.from(myContext);
        this.myClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_image_adapter, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ServiceHistory serviceHistory = list.get(position);
        holder.nameAndAddress.setText(serviceHistory.getName());

        holder.time.setText(serviceHistory.getServiceDatetime());
        holder.filePath = serviceHistory.getServiceContent();
        Glide.with(myContext).
                load(GlobalData.GET_IMAGE + serviceHistory.getServiceContent()).
                error(R.drawable.image_load_error).
                into(new GlideDrawableImageViewTarget(holder.imageView) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {

                        super.onLoadStarted(placeholder);
                        holder.progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {

                        super.onLoadFailed(e, errorDrawable);
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super
                            GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemClick(holder.getPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position,
                                 List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface MyClickListener {
        void onItemClick(int position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private CircleImageView imageView;

        private TextView nameAndAddress;
        private TextView time;
        private ProgressBar progressBar;

        private String filePath;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            nameAndAddress = (TextView) itemView.findViewById(R.id.item_filename);

            time = (TextView) itemView.findViewById(R.id.size_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}


