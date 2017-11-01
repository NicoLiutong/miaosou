package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.animation.R;
import com.example.animation.adapter.PictureAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationPicture extends AppCompatActivity implements PictureAdapter.OnClickListener {

    private List<List<String>> pictureList = new ArrayList<>();
    private RecyclerView picturesRv;
    private PictureAdapter pictureAdapter;
    private static final String PictureListUrl = "https://yande.re/post?page=";
    private int page = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_picture);
        picturesRv = (RecyclerView) findViewById(R.id.rv_picture);
        LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        picturesRv.setLayoutManager(layoutManager);
        pictureAdapter = new PictureAdapter(pictureList,this);
        picturesRv.setAdapter(pictureAdapter);
        picturesRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!isLoading) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (!recyclerView.canScrollVertically(1)) {
                            page = page + 1;
                            queryPictureUrl(PictureListUrl + page);
                        }
                    }
                }
            }
        });
            queryPictureUrl(PictureListUrl + page);
    }

    private void queryPictureUrl(final String pictureUrl){
        isLoading = true;
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
                        Log.d("pictureUrl",pictureUrl);
                        Log.d("imageUrl",imageUrl);
                        pictureList.add(imageList);
                    }


                }catch (IOException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
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
}
