package com.example.animation.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.animation.R;
import com.example.animation.fragments.AnimationFavourite;
import com.example.animation.fragments.ComicFavourite;

import java.util.ArrayList;
import java.util.List;

public class MyFavourityActivity extends AppCompatActivity {

    private Button backButton;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] title = {"漫画","动画"};
    private List<Fragment> fragmentList;
    private adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourity);
        backButton = (Button) findViewById(R.id.my_favourityBack);
        tabLayout = (TabLayout) findViewById(R.id.tl_favourity);
        viewPager = (ViewPager) findViewById(R.id.vp_favourite);
        fragmentList = new ArrayList<>();
        fragmentList.add(new ComicFavourite());
        fragmentList.add(new AnimationFavourite());
        adapter = new adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
