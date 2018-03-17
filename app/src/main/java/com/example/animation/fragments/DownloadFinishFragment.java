package com.example.animation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.activity.DownloadFinishComicPageListActivity;
import com.example.animation.db.DownloadComic;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 刘通 on 2018/3/15.
 */

public class DownloadFinishFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private List<DownloadComic> downloadComicList = new ArrayList<>();
    private Set<String> set = new HashSet<>();
    private DownloadFinishAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<DownloadComic> list = DataSupport.findAll(DownloadComic.class);
        for (DownloadComic item:list) {
            if (item.isDownloadFinish()) {
                if (!set.contains(item.getComicId())) {
                    set.add(item.getComicId());
                    downloadComicList.add(item);
                }
            }
        }
        adapter = new DownloadFinishAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_favourite,container,false);
        recyclerView =(RecyclerView) view.findViewById(R.id.my_favourityRecyclerview);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        textView = (TextView) view.findViewById(R.id.tv_myfavourity);
        if(downloadComicList.isEmpty()){
            textView.setText("还没有下载完成的漫画!!");
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void onDownloadClick(DownloadComic downloadComic,int position){
        Intent intent = new Intent(getActivity(), DownloadFinishComicPageListActivity.class);
        intent.putExtra("comicId",downloadComic.getComicId());
        getActivity().startActivity(intent);
    }

    class DownloadFinishAdapter extends RecyclerView.Adapter<DownloadFinishAdapter.DownloadFinishHolder>{

        @Override
        public DownloadFinishHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final DownloadFinishHolder holder = new DownloadFinishHolder(LayoutInflater.from(getActivity()).inflate(R.layout.comic_search_item,parent,false));
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDownloadClick(downloadComicList.get(holder.getAdapterPosition()),holder.getAdapterPosition());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(DownloadFinishHolder holder, int position) {
            Glide.with(getActivity()).load(downloadComicList.get(position).getComicImageUrl()).into(holder.ivDownloadFinish);
            holder.tvDownloadFinish.setText(downloadComicList.get(position).getComicName());
        }

        @Override
        public int getItemCount() {
            return downloadComicList.size();
        }

        class DownloadFinishHolder extends RecyclerView.ViewHolder{

            ImageView ivDownloadFinish;

            TextView tvDownloadFinish;

            View view;
            public DownloadFinishHolder(View itemView) {
                super(itemView);
                ivDownloadFinish = (ImageView) itemView.findViewById(R.id.comic_searchImage);
                tvDownloadFinish = (TextView) itemView.findViewById(R.id.comic_searchNames);
                view = itemView;
            }
        }
    }
}
