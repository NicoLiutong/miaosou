package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.adapter.DividerItemDecoration;
import com.example.animation.adapter.NewsAdapter;
import com.example.animation.db.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2017/10/17.
 */

public class NewsFragment extends Fragment {

    public static final int NEWSNEWS = 1;
    public static final int NEWSHOT = 2;
    public static final int NEWSTUI = 3;
    private List<News> newsList;

    private static final String newNewsUrl = "https://ouo.us/news/new.html&p=";
    private static final String hotNewsUrl = "https://ouo.us/news/hot.html&p=";
    private static final String tuiNewsUrl = "https://ouo.us/news/tui.html&p=";
    private int page = 0;
    private boolean haveNext = true;
    private boolean isLoading = false;
    private TextView tvLoading;
    private RecyclerView newsRecycler;
    private NewsAdapter newsAdapter;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 0;
        haveNext = true;
        newsList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout,container,false);
        newsRecycler = (RecyclerView) view.findViewById(R.id.rv_news);
        tvLoading = (TextView) view.findViewById(R.id.tv_loading);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsRecycler.setLayoutManager(layoutManager);
        newsAdapter = new NewsAdapter(newsList);
        newsRecycler.setAdapter(newsAdapter);
        newsRecycler.addItemDecoration(new DividerItemDecoration(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        newsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!isLoading) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (!recyclerView.canScrollVertically(1)) {
                            loadData();
                        }
                    }
                }
            }
        });
    }

    public void setType(int type){
        this.type = type;
    }
    private void loadData(){
        if(haveNext){
            page = page + 1;
        }else {
            Toast.makeText(getContext(),"已经到底了",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case NewsFragment.NEWSNEWS:
                queryNews(newNewsUrl + page);
                break;
            case NewsFragment.NEWSHOT:
                queryNews(hotNewsUrl + page);
                break;
            case NewsFragment.NEWSTUI:
                queryNews(tuiNewsUrl + page);
                break;
            default:
                loadData();
                break;
        }

    }

    private void queryNews(final String newsUrl){
        isLoading = true;
        tvLoading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document newsDocument = Jsoup.connect(newsUrl).timeout(3000).post();
                    if(!newsDocument.select("div.pages").get(0).select("a").get(1).attr("href").equals("javascript:alert('已经是最后一页啦！')")){
                        haveNext = true;
                    }else {
                        haveNext = false;
                    }
                    Elements newsListElements = newsDocument.select("div.list-item");
                    for (Element newsElement:newsListElements){
                        String newsUrl = "https://ouo.us" + newsElement.select("a").get(0).attr("href");
                        String imageUrl = "https://ouo.us" + newsElement.select("img").get(0).attr("src");
                        String newsTitle = newsElement.select("h4").text();
                        String newsContent = newsElement.select("h5").text().split(" ")[1];
                        String newsInformation = newsElement.select("p").text();
                        String newsTime = newsInformation.split("/")[0];
                        String newsAuthor = newsInformation.split("/")[1];
                        String newsHot = newsInformation.split("/")[3];

                        /*Log.d("type",type + " ");
                        Log.d("page",page+" ");
                        Log.d("newsFragment",newsUrl+"\n"+imageUrl+"\n"+newsTitle+"\n"+newsContent+"\n"+newsTime+"\n"+newsAuthor+"\n"+newsHot);*/
                        News news = new News();
                        news.setNewsUrl(newsUrl);
                        news.setNewsTitle(newsTitle);
                        news.setNewsContent(newsContent);
                        news.setNewsAuthor(newsAuthor);
                        news.setNewsTime(newsTime);
                        news.setNewsHot(newsHot);
                        news.setNewsPictureUrl(imageUrl);
                        newsList.add(news);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                        tvLoading.setVisibility(View.GONE);
                        newsAdapter.notifyDataSetChanged();
                    }
                })
                ;
            }
        }.start();

    }
}
