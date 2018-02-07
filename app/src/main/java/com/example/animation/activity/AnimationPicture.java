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
import com.example.animation.adapter.PictureListShow;
import com.example.animation.fragments.PictureListFragment;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

public class AnimationPicture extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager vpPictureShow;
    private TextView tvYandere;
    private TextView tvKonachan;
    //private TextView tvLolibooru;
    private TextView tvAnimation;
    private Button pictureSearch;
    private PictureListShow pictureListShowAdapter;
    private PictureListFragment yandereFragment;
    private PictureListFragment konachanFragment;
    //private PictureListFragment lolibooruFragment;
    private PictureListFragment animationFragment;
    private List<Fragment> pictureFragmentLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_picture);
        tvYandere = (TextView) findViewById(R.id.picture_yandere);
        tvYandere.setSelected(true);
        tvYandere.setOnClickListener(this);
        tvKonachan = (TextView) findViewById(R.id.picture_konachan);
        tvKonachan.setOnClickListener(this);
        pictureSearch = (Button) findViewById(R.id.picture_search);
        pictureSearch.setOnClickListener(this);
        //tvLolibooru = (TextView) findViewById(R.id.picture_lolibooru);
        //tvLolibooru.setOnClickListener(this);
        tvAnimation = (TextView) findViewById(R.id.picture_animation);
        tvAnimation.setOnClickListener(this);

        vpPictureShow = (ViewPager) findViewById(R.id.picture_show_vp);
        pictureFragmentLists = new ArrayList<>();
        yandereFragment = PictureListFragment.pictureInstance(PictureListFragment.YANDERE,"");
        konachanFragment = PictureListFragment.pictureInstance(PictureListFragment.KONACHAN,"");
        //lolibooruFragment = PictureListFragment.pictureInstance(PictureListFragment.LOLIBOORU);
        animationFragment = PictureListFragment.pictureInstance(PictureListFragment.ANIMATION,"");

        pictureFragmentLists.add(yandereFragment);
        pictureFragmentLists.add(konachanFragment);
        //pictureFragmentLists.add(lolibooruFragment);
        pictureFragmentLists.add(animationFragment);
        pictureListShowAdapter = new PictureListShow(getSupportFragmentManager(),pictureFragmentLists);
        vpPictureShow.setAdapter(pictureListShowAdapter);
        vpPictureShow.addOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "二次元图片");
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
                tvYandere.setSelected(true);
                tvKonachan.setSelected(false);
                //tvLolibooru.setSelected(false);
                tvAnimation.setSelected(false);
                break;
            case 1:
                tvYandere.setSelected(false);
                tvKonachan.setSelected(true);
                //tvLolibooru.setSelected(false);
                tvAnimation.setSelected(false);
                break;
            case 2:
                tvYandere.setSelected(false);
                tvKonachan.setSelected(false);
                //tvLolibooru.setSelected(true);
                tvAnimation.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picture_yandere:
                tvYandere.setSelected(true);
                tvKonachan.setSelected(false);
                //tvLolibooru.setSelected(false);
                tvAnimation.setSelected(false);
                vpPictureShow.setCurrentItem(0);
                break;
            case R.id.picture_konachan:
                tvYandere.setSelected(false);
                tvKonachan.setSelected(true);
                //tvLolibooru.setSelected(false);
                tvAnimation.setSelected(false);
                vpPictureShow.setCurrentItem(1);
                break;
            case R.id.picture_animation:
                tvYandere.setSelected(false);
                tvKonachan.setSelected(false);
                //tvLolibooru.setSelected(true);
                tvAnimation.setSelected(true);
                vpPictureShow.setCurrentItem(2);
                break;
            case R.id.picture_search:
                Intent intent = new Intent(AnimationPicture.this,SearchActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                break;
        }
    }
}
