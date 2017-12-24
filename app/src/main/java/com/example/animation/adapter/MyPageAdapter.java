package com.example.animation.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.animation.db.ComicViewPager;
import com.example.animation.fragments.ComicReadFragment;

import java.util.List;

/**
 * Created by 刘通 on 2017/8/8.
 */

public class MyPageAdapter extends FragmentStatePagerAdapter {

    private List<ComicViewPager> mComicViewPagerList;

    public MyPageAdapter(FragmentManager fm, List<ComicViewPager> comicViewPagerList) {
        super(fm);
        mComicViewPagerList = comicViewPagerList;
    }

    @Override
    public Fragment getItem(int position) {
        ComicViewPager comicViewPager = mComicViewPagerList.get(position);
        ComicReadFragment comicReadFragment = ComicReadFragment.newInstance(comicViewPager.getComicUrl(),comicViewPager.getCurrentPage(),comicViewPager.getPictureId(),comicViewPager.getType());
        return comicReadFragment;
    }

    @Override
    public int getCount() {
        return mComicViewPagerList.size();
    }

}
