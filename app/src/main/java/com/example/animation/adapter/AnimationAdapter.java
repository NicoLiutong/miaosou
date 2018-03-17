package com.example.animation.adapter;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.activity.BasicWebActivity;
import com.example.animation.activity.DownloadActivity;
import com.example.animation.db.AnimationItem;
import com.example.animation.fragments.AnimationFragment;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class AnimationAdapter extends RecyclerView.Adapter<AnimationAdapter.MyAnimationHolder> {

    private List<AnimationItem> mAnimationItems;
    private int type;//0为主界面，1为收藏

    public AnimationAdapter(List<AnimationItem> animationItems,int type){
        this.mAnimationItems = animationItems;
        this.type = type;
    }

    @Override
    public MyAnimationHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
       final MyAnimationHolder holder = new MyAnimationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.animation_item,parent,false));

        holder.favoyriteCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AnimationItem animation = mAnimationItems.get(position);
                if(animation.isFavortiy()){
                    animation.setFavortiy(false);
                    holder.favouriteTexiview.setText("未收藏");
                    ContentValues values = new ContentValues();
                    values.put("isFavortiy",false);
                    DataSupport.updateAll(AnimationItem.class,values,"animationItem=?",animation.getAnimationItem());
                }else {
                    animation.setFavortiy(true);
                    holder.favouriteTexiview.setText("已收藏");
                    ContentValues values = new ContentValues();
                    values.put("isFavortiy",true);
                    DataSupport.updateAll(AnimationItem.class,values,"animationItem=?",animation.getAnimationItem());
                }
            }
        });
        holder.seeOnlineCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AnimationItem animation = mAnimationItems.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(animation.getSeeOnlineUrl()));
                //Intent intent = new Intent(parent.getContext(), BasicWebActivity.class);
                //intent.putExtra(AnimationFragment.ANIMATION_URL,animation.getSeeOnlineUrl());
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
                Intent intent = new Intent(parent.getContext(), BasicWebActivity.class);
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
        if(animationItem.isFavortiy()){
            holder.favouriteTexiview.setText("已收藏");
        }else {
            holder.favouriteTexiview.setText("未收藏");
        }
        //String type = animationItem.getAnimationType().split("/")[1].split("）")[0];
       //if(type.equals(" 新番")){
        //if(true){
            //Log.d("red","red");
         //   holder.animationTypeText.setTextColor(Color.parseColor("#FF4081"));
       //}
        //holder.animationTypeText.setText(animationItem.getAnimationType());
        if(type==0){
            holder.animationTypeText.setVisibility(View.INVISIBLE);
        }else if(type==1){
            switch (animationItem.getWeek()){
                case "week1":
                    holder.animationTypeText.setText("星期一");
                    break;
                case "week2":
                    holder.animationTypeText.setText("星期二");
                    break;
                case "week3":
                    holder.animationTypeText.setText("星期三");
                    break;
                case "week4":
                    holder.animationTypeText.setText("星期四");
                    break;
                case "week5":
                    holder.animationTypeText.setText("星期五");
                    break;
                case "week6":
                    holder.animationTypeText.setText("星期六");
                    break;
                case "week7":
                    holder.animationTypeText.setText("星期天");
                    break;
            }
        }


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

        CardView favoyriteCardview;

        TextView favouriteTexiview;

        CardView downloadCardview;

        CardView seeOnlineCardview;

        CardView animationInformationCardview;

        public MyAnimationHolder(View itemView) {
            super(itemView);
            animationItemText = (TextView) itemView.findViewById(R.id.animation_item);
            animationTypeText = (TextView) itemView.findViewById(R.id.animation_type);
            favoyriteCardview = (CardView) itemView.findViewById(R.id.animation_favourity);
            favouriteTexiview = (TextView) itemView.findViewById(R.id.animation_favourity_text);
            downloadCardview = (CardView) itemView.findViewById(R.id.download_animationCardview);
            seeOnlineCardview = (CardView) itemView.findViewById(R.id.onlineSee_animationCardview);
            animationInformationCardview = (CardView) itemView.findViewById(R.id.animation_informationCardview);
        }
    }

}
