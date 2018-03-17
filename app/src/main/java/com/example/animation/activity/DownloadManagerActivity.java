package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.animation.R;
import com.example.animation.fragments.DownloadComicFragment;
import com.example.animation.fragments.DownloadFinishFragment;
import com.example.animation.fragments.DownloadManagerPicture;

import java.util.ArrayList;
import java.util.List;

public class DownloadManagerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private String[] title = {"动漫图片","cosplay图片","已完成","待下载"};
    private FragmentPagerAdapter adapter;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        toolbar = (Toolbar) findViewById(R.id.download_manager_toolbar);
        toolbar.setTitle("下载管理");
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tl_download_manager);
        viewPager = (ViewPager) findViewById(R.id.vp_download_manager);
        Intent intent = getIntent();
        index = intent.getIntExtra("page",0);
        fragmentList = new ArrayList<>();
        fragmentList.add(DownloadManagerPicture.downloadManagerPictureInstance(1));
        fragmentList.add(DownloadManagerPicture.downloadManagerPictureInstance(2));
        fragmentList.add(new DownloadFinishFragment());
        fragmentList.add(new DownloadComicFragment());
        adapter = new adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        tabLayout.setupWithViewPager(viewPager);

    }
    class adapter extends FragmentPagerAdapter {
        public adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
