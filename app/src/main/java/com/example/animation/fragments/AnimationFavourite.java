package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.adapter.AnimationAdapter;
import com.example.animation.adapter.DividerItemDecoration;
import com.example.animation.db.AnimationItem;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2018/2/22.
 */

public class AnimationFavourite extends Fragment {
    private RecyclerView recyclerView;
    private AnimationAdapter adapter;
    private TextView textView;
    private List<AnimationItem> animationItems = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation_favourity,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.animation_favourite_rv);
        textView = (TextView) view.findViewById(R.id.animation_favourite_tv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        adapter = new AnimationAdapter(animationItems,1);
        recyclerView.setAdapter(adapter);
        List<AnimationItem> list = DataSupport.order("week asc").find(AnimationItem.class);
        for(AnimationItem item: list){
            if(item.isFavortiy())animationItems.add(item);
        }
        adapter.notifyDataSetChanged();
        if(animationItems.isEmpty()){
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.INVISIBLE);
        }
    }


}
