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
import com.example.animation.db.ComicItem;
import com.example.animation.activity.ComicNumberActivity;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.R;

import java.util.List;

/**
 * Created by 刘通 on 2017/4/7.
 */

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.MyComicHolder> {

    private List<ComicItem> comicItemList;

    private Context comicContext;

    public ComicAdapter(List<ComicItem> comicList){
        this.comicItemList = comicList;

    }

    @Override
    public MyComicHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        comicContext = parent.getContext();
        final MyComicHolder comicHolder = new MyComicHolder(LayoutInflater.from(comicContext).inflate(R.layout.comic_item,parent,false));
        comicHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = comicHolder.getAdapterPosition();
                ComicItem comicItem = comicItemList.get(position);
                Intent intent = new Intent(comicContext, ComicNumberActivity.class);
                intent.putExtra(ComicFragment.COMICURL,comicItem.getComicUrl());
                comicContext.startActivity(intent);
            }
        });

        return comicHolder;
    }

    @Override
    public void onBindViewHolder(MyComicHolder holder, int position) {
        ComicItem comicItem = comicItemList.get(position);
        Glide.with(comicContext).load(comicItem.getBackgroundUrl()).error(R.drawable.error).into(holder.comicImageView);
        holder.comicUpdateClass.setText(comicItem.getUpdateClass());
        holder.comicName.setText(comicItem.getComicName());
        holder.comicIntroduction.setText(comicItem.getComicIntroduction());
        holder.comicAuthor.setText(comicItem.getComicAuthor());
    }

    @Override
    public int getItemCount() {
        return comicItemList.size();
    }

    class MyComicHolder extends RecyclerView.ViewHolder{

        ImageView comicImageView;

        TextView comicUpdateClass;

        TextView comicName;

        TextView comicIntroduction;

        TextView comicAuthor;

        View view;

        public MyComicHolder(View itemView) {
            super(itemView);
            comicImageView = (ImageView) itemView.findViewById(R.id.comic_image);
            comicUpdateClass = (TextView) itemView.findViewById(R.id.comic_updateClass);
            comicName = (TextView) itemView.findViewById(R.id.comic_name);
            comicIntroduction = (TextView) itemView.findViewById(R.id.comic_introduction);
            comicAuthor = (TextView) itemView.findViewById(R.id.comic_author);
            view = itemView;
        }
    }
}
