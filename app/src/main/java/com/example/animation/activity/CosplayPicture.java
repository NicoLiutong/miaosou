package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.adapter.CosplayFragmentAdapter;
import com.example.animation.fragments.CosplayListFragment;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

public class CosplayPicture extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager vpPictureCosplay;
    private TextView tvChinaGirl;
    private TextView tvJdlingyuCos;
    private TextView tvJdlingyuMzitu;
    //private TextView tvBcy;
    private Button btCosplaySearch;
    //private TextView tvCosplayLa;
    private CosplayFragmentAdapter cosplayFragmentAdapter;
    private CosplayListFragment chinalGirlFragment;
    private CosplayListFragment jDLingYuCosFragment;
    private CosplayListFragment jDLingYuMzTuFragment;
    //private CosplayListFragment bcyFragment;
    //private CosplayListFragment cosplayLaFragment;
    private List<Fragment> cosplayFragmentLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosplay_picture);
        tvChinaGirl = (TextView) findViewById(R.id.picture_chinagirl);
        tvChinaGirl.setSelected(true);
        tvChinaGirl.setOnClickListener(this);
        tvJdlingyuCos = (TextView) findViewById(R.id.picture_jdlingyu_cos);
        tvJdlingyuCos.setOnClickListener(this);
        tvJdlingyuMzitu = (TextView) findViewById(R.id.picture_jdlingyu_mzitu);
        tvJdlingyuMzitu.setOnClickListener(this);
        //tvBcy = (TextView) findViewById(R.id.picture_bcy);
        //tvBcy.setOnClickListener(this);
        btCosplaySearch = (Button) findViewById(R.id.cosplay_search);
        btCosplaySearch.setOnClickListener(this);
        //tvCosplayLa = (TextView) findViewById(R.id.picture_cosplay_la);
        //tvCosplayLa.setOnClickListener(this);
        vpPictureCosplay = (ViewPager) findViewById(R.id.picture_cosplay_vp);
        vpPictureCosplay.setOffscreenPageLimit(0);
        cosplayFragmentLists = new ArrayList<>();

        chinalGirlFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.CHINAGIRL,"");
        jDLingYuCosFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.JDLINGYUCOS,"");
        jDLingYuMzTuFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.JDLINGYUMZITU,"");
        //bcyFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.BCY,"");
        //cosplayLaFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.COSPLAYLA);
        cosplayFragmentLists.add(chinalGirlFragment);
        cosplayFragmentLists.add(jDLingYuCosFragment);
        cosplayFragmentLists.add(jDLingYuMzTuFragment);
        //cosplayFragmentLists.add(bcyFragment);
        //cosplayFragmentLists.add(cosplayLaFragment);
        cosplayFragmentAdapter = new CosplayFragmentAdapter(getSupportFragmentManager(),cosplayFragmentLists);
        vpPictureCosplay.setAdapter(cosplayFragmentAdapter);
        vpPictureCosplay.addOnPageChangeListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "三次元图片");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                tvChinaGirl.setSelected(true);
                tvJdlingyuCos.setSelected(false);
                tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                break;
            case 1:
                tvChinaGirl.setSelected(false);
                tvJdlingyuCos.setSelected(true);
                tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                break;
            case 2:
                tvChinaGirl.setSelected(false);
                tvJdlingyuCos.setSelected(false);
                tvJdlingyuMzitu.setSelected(true);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                break;
            //case 3:
                //tvChinaGirl.setSelected(false);
                //tvJdlingyuCos.setSelected(false);
                //tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(true);
                //tvCosplayLa.setSelected(true);
                //break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picture_chinagirl:
                tvChinaGirl.setSelected(true);
                tvJdlingyuCos.setSelected(false);
                tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                vpPictureCosplay.setCurrentItem(0);
                break;
            case R.id.picture_jdlingyu_cos:
                tvChinaGirl.setSelected(false);
                tvJdlingyuCos.setSelected(true);
                tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                vpPictureCosplay.setCurrentItem(1);
                break;
            case R.id.picture_jdlingyu_mzitu:
                tvChinaGirl.setSelected(false);
                tvJdlingyuCos.setSelected(false);
                tvJdlingyuMzitu.setSelected(true);
                //tvBcy.setSelected(false);
                //tvCosplayLa.setSelected(false);
                vpPictureCosplay.setCurrentItem(2);
                break;
            //case R.id.picture_bcy:
                //tvChinaGirl.setSelected(false);
                //tvJdlingyuCos.setSelected(false);
                //tvJdlingyuMzitu.setSelected(false);
                //tvBcy.setSelected(true);
                //tvCosplayLa.setSelected(false);
                //vpPictureCosplay.setCurrentItem(3);
                //break;
            //case R.id.picture_cosplay_la:
                //tvChinaGirl.setSelected(false);
                //tvJdlingyuCos.setSelected(false);
                //tvJdlingyuMzitu.setSelected(false);
                //tvCosplayLa.setSelected(true);
                //vpPictureCosplay.setCurrentItem(3);
                //break;
            case R.id.cosplay_search:
                Intent intent = new Intent(CosplayPicture.this,SearchActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
                break;

        }
    }
}
