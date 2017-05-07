package com.example.animation.Aaapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.Class.AnimationItem;
import com.example.animation.DownloadActivity;
import com.example.animation.Fragment.AnimationFragment;
import com.example.animation.InternetDisplay;
import com.example.animation.R;

import java.util.List;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class AnimationAdapter extends RecyclerView.Adapter<AnimationAdapter.MyAnimationHolder> {

    private List<AnimationItem> mAnimationItems;

    public AnimationAdapter(List<AnimationItem> animationItems){
        this.mAnimationItems = animationItems;
    }

    @Override
    public MyAnimationHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
       final MyAnimationHolder holder = new MyAnimationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.animation_item,parent,false));

        holder.seeOnlineCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AnimationItem animation = mAnimationItems.get(position);
                Intent intent = new Intent(parent.getContext(), InternetDisplay.class);
                intent.putExtra(AnimationFragment.ANIMATION_NAME,animation.getAnimationItem());
                intent.putExtra(AnimationFragment.ANIMATION_URL,animation.getSeeOnlineUrl());
                parent.getContext().startActivity(intent);
            }
        });

            holder.downloadCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    AnimationItem animation = mAnimationItems.get(position);
                    Intent intent = new Intent(parent.getContext(), DownloadActivity.class);
                    intent.putExtra(AnimationFragment.ANIMATION_NAME,animation.getAnimationItem());
                    intent.putExtra(AnimationFragment.ANIMATION_URL,animation.getDownloadUrl());
                    parent.getContext().startActivity(intent);
                }
            });

        holder.animationInformationCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AnimationItem animation = mAnimationItems.get(position);
                Intent intent = new Intent(parent.getContext(), InternetDisplay.class);
                intent.putExtra(AnimationFragment.ANIMATION_NAME,animation.getAnimationItem());
                intent.putExtra(AnimationFragment.ANIMATION_URL,animation.getAnimationInformationUrl());
                parent.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(MyAnimationHolder holder, int position) {
        AnimationItem animationItem = mAnimationItems.get(position);
        holder.animationItemText.setText(animationItem.getAnimationItem());
        //String type = animationItem.getAnimationType().split("/")[1].split("）")[0];
       //if(type.equals(" 新番")){
        //if(true){
            //Log.d("red","red");
         //   holder.animationTypeText.setTextColor(Color.parseColor("#FF4081"));
       //}
        holder.animationTypeText.setText(animationItem.getAnimationType());
        if(animationItem.getSeeOnlineUrl() != null){
            holder.seeOnlineCardview.setVisibility(View.VISIBLE);
        }else {
            holder.seeOnlineCardview.setVisibility(View.GONE);
        }
        if(animationItem.getDownloadUrl() != null){
            holder.downloadCardview.setVisibility(View.VISIBLE);
        }else {
            holder.downloadCardview.setVisibility(View.GONE);
        }
        if(animationItem.getAnimationInformationUrl() != null){
            holder.animationInformationCardview.setVisibility(View.VISIBLE);
        }else {
            holder.animationInformationCardview.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mAnimationItems.size();
    }

    class MyAnimationHolder extends RecyclerView.ViewHolder{

        TextView animationItemText;

        TextView animationTypeText;

        CardView downloadCardview;

        CardView seeOnlineCardview;

        CardView animationInformationCardview;

        public MyAnimationHolder(View itemView) {
            super(itemView);
            animationItemText = (TextView) itemView.findViewById(R.id.animation_item);
            animationTypeText = (TextView) itemView.findViewById(R.id.animation_type);
            downloadCardview = (CardView) itemView.findViewById(R.id.download_animationCardview);
            seeOnlineCardview = (CardView) itemView.findViewById(R.id.onlineSee_animationCardview);
            animationInformationCardview = (CardView) itemView.findViewById(R.id.animation_informationCardview);
        }
    }

}
