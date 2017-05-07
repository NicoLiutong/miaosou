package com.example.animation.Aaapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.animation.R;

import java.util.List;

/**
 * Created by 刘通 on 2017/4/18.
 */

public class ComicReadAdapter extends RecyclerView.Adapter<ComicReadAdapter.MyComicReadHolder> {

    private List<String> comicUrls;

    private Context context;

    public ComicReadAdapter(List<String> comicUrls){
        this.comicUrls = comicUrls;
    }

    @Override
    public MyComicReadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        final MyComicReadHolder holder = new MyComicReadHolder(LayoutInflater.from(context).inflate(R.layout.comic_read_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyComicReadHolder holder, int position) {
        String comicUrl = comicUrls.get(position);
        Glide.with(context).load(comicUrl).into(holder.readImageView);
    }

    @Override
    public int getItemCount() {
        return comicUrls.size();
    }

    class MyComicReadHolder extends RecyclerView.ViewHolder{

            ImageView readImageView;

        public MyComicReadHolder(View itemView) {
            super(itemView);
            readImageView = (ImageView) itemView.findViewById(R.id.comic_readImage);
        }
    }
}
