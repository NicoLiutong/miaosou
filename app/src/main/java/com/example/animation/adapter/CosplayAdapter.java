package com.example.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animation.R;
import com.example.animation.db.CosplayMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2017/12/26.
 */

public class CosplayAdapter  extends RecyclerView.Adapter<CosplayAdapter.CosplayHolder>  {

    private List<CosplayMessage> mCosplayMessages = new ArrayList<>();
    private Context mContext;
    private OnClickListener onClickListener;
    public CosplayAdapter(List<CosplayMessage> cosplayMessageList,OnClickListener onClickListener){
        mCosplayMessages = cosplayMessageList;
        this.onClickListener = onClickListener;
    }

    @Override
    public CosplayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        CosplayHolder holder = new CosplayHolder(LayoutInflater.from(mContext).inflate(R.layout.cosplay_picture_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final CosplayHolder holder, final int position) {
        Glide.with(mContext).load(mCosplayMessages.get(position).getImageUrl()).asBitmap().placeholder(R.drawable.ic_girl).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.ivImage);
        holder.tvTitle.setText(mCosplayMessages.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCosplayMessages.size();
    }

    public interface OnClickListener{
        void onClick(View v,int position);
    }

    public class CosplayHolder extends RecyclerView.ViewHolder{

        private View itemView;
        private ImageView ivImage;
        private TextView tvTitle;
        public CosplayHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivImage = (ImageView) itemView.findViewById(R.id.iv_cosplay_picture);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_cosplay_title);
        }
    }
}
