package com.example.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.animation.Aaapter.ComicSearchResultAdapter;
import com.example.animation.Class.ComicSearchResultList;
import com.example.animation.Fragment.AnimationFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicSearchResult extends AppCompatActivity {

    private String comicName;

    private String comicSearchUrl;

    private List<ComicSearchResultList> resultLists = new ArrayList<>();

    private TextView comicNameView;

    private Button comicBackButton;

    private TextView comicSearchNoElementView;

    private RecyclerView comicSearchRecyclerview;

    private ComicSearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_search_result);
        Intent intent = getIntent();
        comicName = intent.getStringExtra(AnimationFragment.ANIMATION_NAME);
        comicSearchUrl = intent.getStringExtra(AnimationFragment.ANIMATION_URL);

        //Log.d("comicurl",comicSearchUrl);

        comicNameView = (TextView) findViewById(R.id.comic_searchName);         //搜索的標題
        comicBackButton = (Button) findViewById(R.id.comic_searchBack);         //返回按鍵
        comicSearchNoElementView = (TextView) findViewById(R.id.comic_searchFali);      //搜索失敗顯示的介面
        comicSearchRecyclerview = (RecyclerView) findViewById(R.id.comic_searchRecyclerview);       //recyclerview
        //初始化recyclerview
        GridLayoutManager layoutManager = new GridLayoutManager(ComicSearchResult.this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comicSearchRecyclerview.setLayoutManager(layoutManager);
        searchResultAdapter = new ComicSearchResultAdapter(resultLists);
        comicSearchRecyclerview.setAdapter(searchResultAdapter);

        comicNameView.setText(comicName);
        comicBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queryComicResult();
    }
        //查詢搜索結果，並顯示
    private void queryComicResult(){
            resultLists.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Document document = Jsoup.connect(comicSearchUrl).timeout(3000).post();
                    Element comicResults = document.select("div.content").get(0);
                    Elements comics = comicResults.select("li");
                    for(Element comic:comics){
                        ComicSearchResultList resultList = new ComicSearchResultList();
                        resultList.setComicUrl("https://nyaso.com" + comic.select("a").get(0).attr("href"));
                        resultList.setComicPages(comic.select("i").get(0).text());
                        resultList.setComicName(comic.select("span").get(0).text());
                        resultList.setComicImageUrl("https:" + comic.select("img").attr("src"));
                        resultLists.add(resultList);
                    }

                    /*for(int i = 0;i < resultLists.size();i++){
                        Log.d("url",resultLists.get(i).getComicUrl());
                        Log.d("pages",resultLists.get(i).getComicPages());
                        Log.d("name",resultLists.get(i).getComicName());
                        Log.d("imageurl",resultLists.get(i).getComicImageUrl());
                    }*/
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                ComicSearchResult.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resultLists.isEmpty()){
                            comicSearchNoElementView.setVisibility(View.VISIBLE);
                            comicSearchRecyclerview.setVisibility(View.GONE);
                            comicSearchNoElementView.setText("主人搜索不到喵  > _ < ");
                        }else {
                            comicSearchNoElementView.setVisibility(View.GONE);
                            comicSearchRecyclerview.setVisibility(View.VISIBLE);
                        }
                        //更新adapter
                        searchResultAdapter.notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

}

