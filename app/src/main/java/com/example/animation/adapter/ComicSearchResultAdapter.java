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
import com.example.animation.db.ComicSearchResultList;
import com.example.animation.activity.ComicNumberActivity;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.R;

import java.util.List;

/**
 * Created by 刘通 on 2017/4/11.
 */

public class ComicSearchResultAdapter extends RecyclerView.Adapter<ComicSearchResultAdapter.ComicSearchResultHolder> {

    private List<ComicSearchResultList> resultLists;

    private Context context;

    public ComicSearchResultAdapter(List<ComicSearchResultList> resultLists){
            this.resultLists = resultLists;
    }

    @Override
    public ComicSearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        final ComicSearchResultHolder holder = new ComicSearchResultHolder(LayoutInflater.from(context).inflate(R.layout.comic_search_item,parent,false));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ComicSearchResultList resultList = resultLists.get(position);
                Intent intent = new Intent(context, ComicNumberActivity.class);
                intent.putExtra(ComicFragment.COMICURL,resultList.getComicUrl());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ComicSearchResultHolder holder, int position) {

        ComicSearchResultList resultList = resultLists.get(position);
        holder.comicSearchPages.setText(resultList.getComicPages());
        holder.comicSearchName.setText(resultList.getComicName());
        Glide.with(context).load(resultList.getComicImageUrl()).into(holder.comicSearchImage);
    }

    @Override
    public int getItemCount() {
        return resultLists.size();
    }

    class ComicSearchResultHolder extends RecyclerView.ViewHolder{

        TextView comicSearchPages;

        ImageView comicSearchImage;

        TextView comicSearchName;

        View view;

        public ComicSearchResultHolder(View itemView) {
            super(itemView);
            comicSearchPages = (TextView) itemView.findViewById(R.id.comic_searchPages);
            comicSearchImage = (ImageView) itemView.findViewById(R.id.comic_searchImage);
            comicSearchName = (TextView) itemView.findViewById(R.id.comic_searchNames);
            view = itemView;
        }
    }
}
