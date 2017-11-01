package com.example.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2017/10/30.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureHolder> {

    private List<List<String>> imageUrlList = new ArrayList<>();
    private Context mContext;
    private PictureAdapter.OnClickListener onClickListener;
    public PictureAdapter(List<List<String>> imageUrlLists,PictureAdapter.OnClickListener onClickListener){
        this.imageUrlList = imageUrlLists;
        this.onClickListener = onClickListener;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        PictureHolder holder = new PictureHolder(LayoutInflater.from(mContext).inflate(R.layout.animation_picture_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, final int position) {
        Glide.with(mContext).load(imageUrlList.get(position).get(1)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.ivImage);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
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
        return imageUrlList.size();
    }

    public interface OnClickListener{
        void onClick(View v,int position);
    }

    public class PictureHolder extends RecyclerView.ViewHolder{

        private ImageView ivImage;
        public PictureHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_animation_picture);
        }
    }
}
