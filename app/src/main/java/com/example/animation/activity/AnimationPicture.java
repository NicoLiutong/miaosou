package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.adapter.PictureAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationPicture extends AppCompatActivity implements PictureAdapter.OnClickListener,OnRefreshLoadmoreListener{

    private final List<List<String>> pictureList = new ArrayList<>();
    private RecyclerView picturesRv;
    private SmartRefreshLayout smartRefreshLayout;
    private PictureAdapter pictureAdapter;
    private static final String PictureListUrl = "https://yande.re/post?page=";
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_picture);
        picturesRv = (RecyclerView) findViewById(R.id.rv_picture);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh_layout_picture);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        LayoutManager layoutManager = new GridLayoutManager(this,3);
        picturesRv.setLayoutManager(layoutManager);
        pictureAdapter = new PictureAdapter(pictureList,this);
        picturesRv.setAdapter(pictureAdapter);
        if(!pictureList.isEmpty()){
            smartRefreshLayout.finishLoadmore();
            smartRefreshLayout.finishRefresh();
            pictureAdapter.notifyDataSetChanged();
        }else {
        smartRefreshLayout.autoRefresh();
        }
    }

    private void queryPictureUrl(final String pictureUrl){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document pictureDocument = Jsoup.connect(pictureUrl).timeout(3000).get();
                    Elements pictureLists = pictureDocument.select("a.thumb");
                    for (Element picture:pictureLists){
                        String pictureUrl = "https://yande.re" + picture.select("a").get(0).attr("href");
                        String imageUrl = picture.select("img").get(0).attr("src");
                        List<String> imageList = new ArrayList<String>();
                        imageList.add(pictureUrl);
                        imageList.add(imageUrl);
                        //Log.d("pictureUrl",pictureUrl);
                        //Log.d("imageUrl",imageUrl);
                        pictureList.add(imageList);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    page = page - 1;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pictureList.isEmpty()){
                            Toast.makeText(AnimationPicture.this,"服务器挂掉了，请稍后再试",Toast.LENGTH_SHORT).show();
                        }
                        smartRefreshLayout.finishLoadmore();
                        smartRefreshLayout.finishRefresh();
                        pictureAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }
    @Override
    public void onClick(View v,int positio) {
        Intent intent = new Intent(this,PictureShowActivity.class);
        intent.putExtra(PictureShowActivity.PICTUREURL,pictureList.get(positio).get(0));
        this.startActivity(intent);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        queryPictureUrl(PictureListUrl + page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        pictureList.clear();
        queryPictureUrl(PictureListUrl + page);
    }
}
