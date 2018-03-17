package com.example.animation.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.db.DownloadComicSelectList;

import java.util.List;

/**
 * Created by 刘通 on 2018/3/6.
 */

public class SelectComicDownloadAdapter extends RecyclerView.Adapter<SelectComicDownloadAdapter.MySelectComicDownloadHolder> {

    private OnDownloadComicListener mListener;
    private List<DownloadComicSelectList> mLists;
    private Context context = null;
    public SelectComicDownloadAdapter(OnDownloadComicListener listener,List<DownloadComicSelectList> lists){
        mListener = listener;
        mLists = lists;
    }
    @Override
    public MySelectComicDownloadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        MySelectComicDownloadHolder holder = new MySelectComicDownloadHolder(LayoutInflater.from(context).inflate(R.layout.model_adapter_comic_download_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MySelectComicDownloadHolder holder, final int position) {
        holder.tvComicSelectDownload.setText(mLists.get(position).getComicPages());
        if(mListener!=null&&!mLists.get(position).isDownload()){
            holder.tvComicSelectDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemOnClick(holder.tvComicSelectDownload,position);
                }
            });
        }else {
            holder.tvComicSelectDownload.setTextColor(ContextCompat.getColor(context,R.color.gray));
            holder.tvComicSelectDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"该集已经下载或在下载列表中",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    class MySelectComicDownloadHolder extends RecyclerView.ViewHolder{
        private TextView tvComicSelectDownload;
        public MySelectComicDownloadHolder(View itemView) {
            super(itemView);
            tvComicSelectDownload = (TextView) itemView.findViewById(R.id.tv_comic_download_item_pages);
        }
    }
    public interface OnDownloadComicListener{
        void itemOnClick(View view,int position);
    }
}
