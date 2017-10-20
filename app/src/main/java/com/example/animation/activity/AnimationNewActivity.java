package com.example.animation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.adapter.NewsViewPagerAdapter;
import com.example.animation.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class AnimationNewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private ViewPager vpNews;
    private TextView tvNewNews;
    private TextView tvhotNews;
    private TextView tvtuiNews;
    private NewsViewPagerAdapter newsViewPagerAdapter;
    private NewsFragment newNewsFragment;
    private NewsFragment hotNewsFragment;
    private NewsFragment tuiNewsFragment;
    private List<Fragment> newsFragmentLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_new);
        tvNewNews = (TextView) findViewById(R.id.tv_bar_news_news);
        tvNewNews.setSelected(true);
        tvNewNews.setOnClickListener(this);
        tvhotNews = (TextView) findViewById(R.id.tv_bar_news_hot);
        tvhotNews.setOnClickListener(this);
        tvtuiNews = (TextView) findViewById(R.id.tv_bar_news_tui);
        tvtuiNews.setOnClickListener(this);
        vpNews = (ViewPager) findViewById(R.id.vp_news);
        newsFragmentLists = new ArrayList<>();
        newNewsFragment = new NewsFragment();
        newNewsFragment.setType(NewsFragment.NEWSNEWS);
        hotNewsFragment = new NewsFragment();
        hotNewsFragment.setType(NewsFragment.NEWSHOT);
        tuiNewsFragment = new NewsFragment();
        tuiNewsFragment.setType(NewsFragment.NEWSTUI);
        newsFragmentLists.add(newNewsFragment);
        newsFragmentLists.add(hotNewsFragment);
        newsFragmentLists.add(tuiNewsFragment);
        newsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(),newsFragmentLists);
        vpNews.setAdapter(newsViewPagerAdapter);
        vpNews.addOnPageChangeListener(this);
        vpNews.setOffscreenPageLimit(3);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                tvNewNews.setSelected(true);
                tvhotNews.setSelected(false);
                tvtuiNews.setSelected(false);
                break;
            case 1:
                tvNewNews.setSelected(false);
                tvhotNews.setSelected(true);
                tvtuiNews.setSelected(false);
                break;
            case 2:
                tvNewNews.setSelected(false);
                tvhotNews.setSelected(false);
                tvtuiNews.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_bar_news_news:
                tvNewNews.setSelected(true);
                tvhotNews.setSelected(false);
                tvtuiNews.setSelected(false);
                vpNews.setCurrentItem(0);
                break;
            case R.id.tv_bar_news_hot:
                tvNewNews.setSelected(false);
                tvhotNews.setSelected(true);
                tvtuiNews.setSelected(false);
                vpNews.setCurrentItem(1);
                break;
            case R.id.tv_bar_news_tui:
                tvNewNews.setSelected(false);
                tvhotNews.setSelected(false);
                tvtuiNews.setSelected(true);
                vpNews.setCurrentItem(2);
                break;
        }
    }
}
