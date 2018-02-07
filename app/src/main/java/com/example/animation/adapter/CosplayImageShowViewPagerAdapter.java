package com.example.animation.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.example.animation.R;
import com.example.animation.Util.ACache;
import com.example.animation.db.CosplayImageMessage;

import java.util.List;

/**
 * Created by 刘通 on 2017/12/30.
 */

public class CosplayImageShowViewPagerAdapter extends PagerAdapter {

    private List<CosplayImageMessage> messages;
    private ACache aCache;
    private OnLongClick click;

    public CosplayImageShowViewPagerAdapter(List<CosplayImageMessage> messages,ACache aCache,OnLongClick longClick){
        this.messages = messages;
        this.aCache = aCache;
        this.click = longClick;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.cosplay_image_item,container,false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.cosplay_image_item_pv);
        photoView.enable();
        final String imageId ="bagBitmap" + messages.get(position).getImageId();
        final Bitmap bitmap = aCache.getAsBitmap(imageId);
        if(bitmap == null){
            photoView.setImageResource(R.drawable.ic_girl);
        }else {
            //Glide.with(container.getContext()).load(bitmap).asBitmap().into(photoView);
            photoView.setImageBitmap(bitmap);
        }
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click.OnLongClickLinstener(imageId,bitmap);
                return true;
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public interface OnLongClick{
        void OnLongClickLinstener(String imageId,Bitmap bitmap);
    }
}
