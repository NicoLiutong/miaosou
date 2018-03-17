package com.example.animation.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.example.animation.R;

import java.io.File;
import java.util.List;

/**
 * Created by 刘通 on 2018/3/14.
 */

public class DownloadPageAdapter extends PagerAdapter {

    private List<String> urls;
    private Context context;

    public DownloadPageAdapter(List<String> urls,Context context){
        this.urls = urls;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.cosplay_image_item,container,false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.cosplay_image_item_pv);
        photoView.enable();
        Glide.with(context).load(new File(urls.get(position))).into(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view =(View) object;
        container.removeView(view);
    }
}
