package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.animation.R;
import com.example.animation.adapter.DividerItemDecoration;
import com.example.animation.adapter.NewsAdapter;
import com.example.animation.db.News;
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

/**
 * Created by 刘通 on 2017/10/17.
 */

public class NewsFragment extends Fragment implements OnRefreshLoadmoreListener{

    public static final int NEWSNEWS = 1;
    public static final int NEWSHOT = 2;
    public static final int NEWSTUI = 3;
    private static final String TYPE = "type";
    private List<News> newsList;

    private static final String newNewsUrl = "https://ouo.us/news/new.html&p=";
    private static final String hotNewsUrl = "https://ouo.us/news/hot.html&p=";
    private static final String tuiNewsUrl = "https://ouo.us/news/tui.html&p=";
    private int page = 0;
    private boolean haveNext = true;
    private RecyclerView newsRecycler;
    private SmartRefreshLayout smartRefreshLayout;
    private NewsAdapter newsAdapter;

    private int type = 0;

    public static NewsFragment newNewsFragment(int type){
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(TYPE,type);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(TYPE);
        page = 0;
        haveNext = true;
        newsList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout,container,false);
        newsRecycler = (RecyclerView) view.findViewById(R.id.rv_news);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smart_refresh_layout_news);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
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
        smartRefreshLayout.autoRefresh();
    }

    private void loadData(){
        if(haveNext){
            page = page + 1;
        }else {
            smartRefreshLayout.setLoadmoreFinished(true);
            smartRefreshLayout.finishLoadmore();
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
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document newsDocument = Jsoup.connect(newsUrl).timeout(3000).post();
                    if (!newsDocument.select("div.pages").get(0).select("a").get(1).attr("href").equals("javascript:alert('已经是最后一页啦！')")) {
                        haveNext = true;
                    } else {
                        haveNext = false;
                    }
                    Elements newsListElements = newsDocument.select("div.list-item");
                    for (Element newsElement : newsListElements) {
                        String newsUrl = "https://ouo.us" + newsElement.select("a").get(0).attr("href");
                        String imageUrl = "https://ouo.us" + newsElement.select("img").get(0).attr("src");
                        String newsTitle = newsElement.select("h4").text();
                        String newsContent = newsElement.select("h5").text().substring(2);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.finishLoadmore();
                            newsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }.start();

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadData();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 0;
        haveNext = true;
        smartRefreshLayout.setLoadmoreFinished(false);
        newsList.clear();
        loadData();
    }
}
