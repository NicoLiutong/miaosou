package com.example.animation.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.activity.BasicWebActivity;
import com.example.animation.db.DownloadItem;
import com.example.animation.fragments.AnimationFragment;
import com.example.animation.R;

import java.util.List;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyDownloadHolder> {

    private List<DownloadItem> mDownloadItem;

    public DownloadAdapter(List<DownloadItem> downloadItem){
        this.mDownloadItem = downloadItem;
    }

    @Override
    public MyDownloadHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final MyDownloadHolder holder = new MyDownloadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item,parent,false));
        holder.downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                DownloadItem download = mDownloadItem.get(position);
                Intent intent = new Intent(parent.getContext(), BasicWebActivity.class);
                intent.putExtra(AnimationFragment.ANIMATION_URL,download.getDownloadUrl());
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyDownloadHolder holder, int position) {
        DownloadItem downloadItem = mDownloadItem.get(position);
        //holder.downloadNumberText.setText(downloadItem.getDownloadNumber());
        holder.downloadItemText.setText(downloadItem.getDownloadItem());
        holder.downloadMessageText.setText(downloadItem.getDownloadMessage());
        
    }

    @Override
    public int getItemCount() {
        return mDownloadItem.size();
    }

    class MyDownloadHolder extends RecyclerView.ViewHolder {
        //TextView downloadNumberText;

        TextView downloadItemText;

        TextView downloadMessageText;

        View downloadView;

        public MyDownloadHolder(View itemView) {
            super(itemView);
            downloadView = itemView;
            //downloadNumberText = (TextView) itemView.findViewById(R.id.download_number);
            downloadItemText = (TextView) itemView.findViewById(R.id.download_item);
            downloadMessageText = (TextView) itemView.findViewById(R.id.download_message);


        }
    }
}
