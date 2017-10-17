package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 刘通 on 2017/10/17.
 */

public class NewsFragment extends Fragment {

    private static final int NEWSNEWS = 1;
    private static final int NEWSHOT = 2;
    private static final int NEWSTUI = 3;

    private int type;

    public NewsFragment(int type){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
