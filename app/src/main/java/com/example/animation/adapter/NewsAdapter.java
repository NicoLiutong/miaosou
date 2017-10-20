package com.example.animation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.activity.BasicWebActivity;
import com.example.animation.db.News;
import com.example.animation.fragments.AnimationFragment;

import java.util.List;

/**
 * Created by 刘通 on 2017/10/19.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private List<News> mNewsList;
    private Context mContext;

    public NewsAdapter(List<News> newsList){
        mNewsList = newsList;
    }
    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        NewsHolder holder = new NewsHolder(LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        final News news = mNewsList.get(position);
        holder.tvTitle.setText(news.getNewsTitle());
        holder.tvContent.setText(news.getNewsContent());
        holder.tvAuthor.setText(news.getNewsAuthor());
        holder.tvTime.setText(news.getNewsTime());
        holder.tvHot.setText(news.getNewsHot());
        Glide.with(mContext).load(news.getNewsPictureUrl()).into(holder.imageNews);
        holder.vNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BasicWebActivity.class);
                intent.putExtra(AnimationFragment.ANIMATION_URL,news.getNewsUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private View vNews;
        private ImageView imageNews;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvAuthor;
        private TextView tvTime;
        private TextView tvHot;

        public NewsHolder(View itemView) {
            super(itemView);
            vNews = itemView;
            imageNews = (ImageView) itemView.findViewById(R.id.iv_news_picture);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_news_content);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_news_man);
            tvTime = (TextView) itemView.findViewById(R.id.tv_news_time);
            tvHot = (TextView) itemView.findViewById(R.id.tv_news_hot);
        }
    }
}
