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
import com.example.animation.adapter.DepthPageTransformer;
import com.example.animation.adapter.MyPageAdapter;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.fragments.ComicReadFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicReadActivity extends AppCompatActivity {

    private String comicUrl;

    private String comicReadPage;

    private int currentReadPage = 1;

    private Button comicReadBack;

    private TextView tvComicReadPage;

    private List<Fragment> comicReadFragmentList = new ArrayList<>();
    private List<String> comicImageUrl = new ArrayList<>();

    private ViewPager viewPager;

    private TextView comicPagesText;

    private MyPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_read);
        Intent intent = getIntent();
        comicUrl = intent.getStringExtra(ComicFragment.COMICREADURL).replace(".html","_2.html");
        //Log.d("url",comicUrl);
        comicReadBack = (Button) findViewById(R.id.comic_readBack);
        tvComicReadPage = (TextView) findViewById(R.id.comic_readPage);
        comicPagesText = (TextView) findViewById(R.id.read_comic_pages);
        comicReadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.comic_read_pager);
        viewPager.setOffscreenPageLimit(2);
        pageAdapter = new MyPageAdapter(this.getSupportFragmentManager(),comicReadFragmentList);
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        queryComicUrl();
    }

    private void queryComicUrl(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document comicDocument = Jsoup.connect(comicUrl).timeout(3000).post();
                    Elements comicUrls = comicDocument.select("a.thumb");
                    for(int i = 0; i < comicUrls.size();i ++){
                        String comicUrl;
                        if(i != comicUrls.size()-1){
                            comicUrl = comicUrls.get(i).attr("href");
                        }else {
                            comicUrl = "http:" + comicUrls.get(i).attr("href");
                        }
                        comicImageUrl.add(comicUrl);
                        //Log.d("comurl",comicUrl);
                    }
                    comicReadPage = comicUrls.get(0).text().split(" ")[0];
                    //Log.d("compage",comicReadPage);
                }catch (IOException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setView();
                    }
                });
            }
        }.start();
    }

    private void setView(){
        tvComicReadPage.setText("第" + comicReadPage);
        comicPagesText.setText(currentReadPage + "/" + comicImageUrl.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentReadPage = position + 1;
                comicPagesText.setText(currentReadPage + "/" + comicImageUrl.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for(int i = 0;i < comicImageUrl.size();i ++){
            ComicReadFragment comicReadFragment = ComicReadFragment.newInstance(comicImageUrl.get(i));
            comicReadFragmentList.add(comicReadFragment);
        }
        pageAdapter.notifyDataSetChanged();
    }

}
