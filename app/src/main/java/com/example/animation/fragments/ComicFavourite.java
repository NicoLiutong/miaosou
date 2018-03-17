package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.adapter.ComicSearchResultAdapter;
import com.example.animation.db.ComicMessageItem;
import com.example.animation.db.ComicSearchResultList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2018/2/22.
 */

public class ComicFavourite extends Fragment {

    private List<ComicMessageItem> comicMessageItems;

    private List<ComicSearchResultList> myFavourityLists = new ArrayList<>();

    private RecyclerView myFavourityRecyclerview;

    private TextView tvHaveMyFavourity;

    private ComicSearchResultAdapter myFavourityAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_favourite,container,false);
        myFavourityRecyclerview = (RecyclerView) view.findViewById(R.id.my_favourityRecyclerview);
        tvHaveMyFavourity = (TextView) view.findViewById(R.id.tv_myfavourity);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //設置recyclerview的屬性
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myFavourityRecyclerview.setLayoutManager(layoutManager);
        myFavourityAdapter = new ComicSearchResultAdapter(myFavourityLists);
        myFavourityRecyclerview.setAdapter(myFavourityAdapter);
        //查詢並顯示favourity的數據
        myFavourityLists.clear();
        comicMessageItems = DataSupport.findAll(ComicMessageItem.class);
        for(ComicMessageItem comicMessageItem:comicMessageItems){
            if(comicMessageItem.isMyFavourity()) {
                ComicSearchResultList comicSearchResultList = new ComicSearchResultList();
                comicSearchResultList.setComicImageUrl(comicMessageItem.getComicImageUrl());
                comicSearchResultList.setComicName(comicMessageItem.getComicName());
                comicSearchResultList.setComicUrl(comicMessageItem.getComicUrl());

                /*Log.d("ComicImageUrl", comicMessageItem.getComicImageUrl());
                Log.d("ComicName", comicMessageItem.getComicName());
                Log.d("ComicUrl", comicMessageItem.getComicImageUrl());*/

                myFavourityLists.add(comicSearchResultList);
            }
        }
        myFavourityAdapter.notifyDataSetChanged();
        if(myFavourityLists.isEmpty()){
            tvHaveMyFavourity.setVisibility(View.VISIBLE);
        }else {
            tvHaveMyFavourity.setVisibility(View.INVISIBLE);
        }
    }
}
