package com.example.animation.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.db.ComicMessageItem;
import com.example.animation.db.ComicNumberList;
import com.example.animation.activity.ComicReadActivity;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.R;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 刘通 on 2017/4/10.
 */

public class ComicNumberAdapter extends RecyclerView.Adapter<ComicNumberAdapter.MyComicNumberHolder> {

    private List<ComicNumberList> comicNumberLists;

    private Context context;

    public ComicNumberAdapter(List<ComicNumberList> comicNumberLists){
        this.comicNumberLists = comicNumberLists;
    }

    @Override
    public MyComicNumberHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        context = parent.getContext();
        final MyComicNumberHolder comicNumberHolder = new MyComicNumberHolder(LayoutInflater.from(context).inflate(R.layout.comic_number_recyclerview,parent,false));
        comicNumberHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = comicNumberHolder.getAdapterPosition();
                ComicNumberList comicNumberList = comicNumberLists.get(position);
                comicNumberHolder.comicNumberCardview.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

                ContentValues values = new ContentValues();
                values.put("readNow",comicNumberList.getComicPages());
                DataSupport.updateAll(ComicMessageItem.class,values,"comicUrl = ?",comicNumberList.getComicUrl());

                //Log.d("readnow",DataSupport.where("comicUrl = ?",comicNumberList.getComicUrl()).find(ComicMessageItem.class).get(0).getReadNow());

                Intent intent = new Intent(context, ComicReadActivity.class);
                intent.putExtra(ComicFragment.COMICREADURL,comicNumberList.getComicPagesUrl());
                context.startActivity(intent);
            }
        });
        return comicNumberHolder;
    }

    @Override
    public void onBindViewHolder(MyComicNumberHolder holder, int position) {
        ComicNumberList comicNumberList = comicNumberLists.get(position);
        holder.comicNumberPages.setText(comicNumberList.getComicPages());
        holder.comicNumberEpisodes.setText(comicNumberList.getComicEpisodes());
       if(comicNumberList.isComicLastRead()){
            holder.comicNumberCardview.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            holder.comicNumberCardview.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        }
    }

    @Override
    public int getItemCount() {
        return comicNumberLists.size();
    }

    class MyComicNumberHolder extends RecyclerView.ViewHolder{

        TextView comicNumberPages;

        TextView comicNumberEpisodes;

        CardView comicNumberCardview;

        View view;

        public MyComicNumberHolder(View itemView) {
            super(itemView);
            comicNumberPages = (TextView) itemView.findViewById(R.id.comic_numberPages);
            comicNumberEpisodes = (TextView) itemView.findViewById(R.id.comic_numberEpisodes);
            comicNumberCardview = (CardView) itemView.findViewById(R.id.comic_numberPagesCardview);
            view = itemView;
        }
    }
}
