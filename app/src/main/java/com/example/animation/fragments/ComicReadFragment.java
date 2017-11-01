package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.example.animation.R;

/**
 * Created by 刘通 on 2017/11/1.
 */

public class ComicReadFragment extends Fragment {

    private static final String IMAGEURL = "imageurl";
    private String imageUrl;
    private PhotoView comicPhotoView;
    public static final ComicReadFragment newInstance(String imageUrl){
        ComicReadFragment comicReadFragment = new ComicReadFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(IMAGEURL,imageUrl);
        comicReadFragment.setArguments(bundle);
        return comicReadFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString(IMAGEURL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_photo_view,container,false);
        comicPhotoView = (PhotoView) view.findViewById(R.id.pv_comic_read);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getContext()).load(imageUrl).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).into(comicPhotoView);

    }
}
