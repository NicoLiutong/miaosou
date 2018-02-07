package com.example.animation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.activity.CosplayListActivity;
import com.example.animation.adapter.CosplayAdapter;
import com.example.animation.db.CosplayMessage;
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
 * Created by 刘通 on 2017/12/26.
 */

public class CosplayListFragment  extends Fragment implements CosplayAdapter.OnClickListener,OnRefreshLoadmoreListener {

    public static final String CHINAGIRL = "chinagirl";
    public static final String JDLINGYUCOS = "jdlingyucos";
    public static final String JDLINGYUMZITU = "jdlingyumzitu";
    //public static final String BCY = "bcy";
    public static final String SEARCHRESULT = "searchresult";

    //public static final String COSPLAYLA = "cosplayla";
    private static final String TYPE = "type";
    private static final String SEARCHNAME = "searchname";
    private String type;
    private String searchName;

    private final List<CosplayMessage> cosplayMessageLists = new ArrayList<>();
    private RecyclerView cosplayRv;
    private SmartRefreshLayout smartRefreshLayout;
    private CosplayAdapter cosplayAdapter;
    private String cosplayListUrl;
    private int page = 1;

    public static final CosplayListFragment cosplayInstance(String type,String searchName){
        CosplayListFragment cosplayListFragment = new CosplayListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE,type);
        bundle.putString(SEARCHNAME,searchName);
        cosplayListFragment.setArguments(bundle);
        return cosplayListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE);
        searchName = getArguments().getString(SEARCHNAME);
        switch (type){
            case CHINAGIRL:
                cosplayListUrl = "http://www.chinagirlol.cc/forum-99-";
                break;
            case JDLINGYUCOS:
                cosplayListUrl = "http://www.jdlingyu.fun/cosplay/";
                break;
            case JDLINGYUMZITU:
                cosplayListUrl = "http://www.jdlingyu.fun/mzitu/";
                break;
            //case BCY:
                //cosplayListUrl = "https://bcy.net/coser/discover?&p=";
                //break;
            case SEARCHRESULT:
                cosplayListUrl = "http://www.jdlingyu.fun/page/";
                break;
            //case COSPLAYLA:
                //cosplayListUrl = "http://cosplay.la/photo/index/0-0-";
                //break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_cosplay_list,container,false);
        cosplayRv = (RecyclerView) view.findViewById(R.id.rv_cosplay);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smart_refresh_layout_cosplay);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        cosplayRv.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cosplayAdapter = new CosplayAdapter(cosplayMessageLists,this);
        cosplayRv.setAdapter(cosplayAdapter);
        if(!cosplayMessageLists.isEmpty()){
            smartRefreshLayout.finishLoadmore();
            smartRefreshLayout.finishRefresh();
            cosplayAdapter.notifyDataSetChanged();
        }else {
            smartRefreshLayout.autoRefresh();
        }
    }

    private void queryPictureUrl(final String cosplayListUrl){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document cosplayDocument = Jsoup.connect(cosplayListUrl).get();
                    switch (type){
                        case CHINAGIRL:
                            creatChinaGirlList(cosplayDocument,cosplayMessageLists);
                            break;
                        case JDLINGYUCOS:
                            creatJDLingYuList(cosplayDocument,cosplayMessageLists);
                            break;
                        case JDLINGYUMZITU:
                            creatJDLingYuList(cosplayDocument,cosplayMessageLists);
                            break;
                        //case BCY:
                            //creatBcyList(cosplayDocument,cosplayMessageLists);
                            //break;
                        case SEARCHRESULT:
                            creatJDLingYuList(cosplayDocument,cosplayMessageLists);
                            break;
                        //case COSPLAYLA:
                            //creatCosplayLa(cosplayDocument,cosplayMessageLists);
                            //break;
                    }

                }catch (IOException e){
                    e.printStackTrace();
                    page = page - 1;
                }
                if(getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (this != null) {
                                if (cosplayMessageLists.isEmpty()) {
                                    Toast.makeText(getContext(), "服务器挂掉了，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                                smartRefreshLayout.finishLoadmore();
                                smartRefreshLayout.finishRefresh();
                                cosplayAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void creatChinaGirlList(Document document,List<CosplayMessage> list){
        Elements cosplayLists = document.select("div.c");
        for (Element cosplayItem : cosplayLists){
            CosplayMessage message = new CosplayMessage();
            message.setPictureUrl(cosplayItem.select("a").get(0).attr("href"));
            //Log.d("chinalgirl-pictureurl",message.getPictureUrl());
            message.setImageUrl("http://www.chinagirlol.cc/" + cosplayItem.select("img").get(0).attr("src"));
            //Log.d("chinalgirl-imageurl",message.getImageUrl());
            message.setTitle(cosplayItem.select("a").get(0).attr("title"));
            //Log.d("chinalgirl-title",message.getTitle());
            list.add(message);
        }
    }

    private void creatJDLingYuList(Document document,List<CosplayMessage> list) {
        Elements cosplayLists = document.select("div.pin-coat");
        for (Element cosplayItem : cosplayLists) {
            CosplayMessage message = new CosplayMessage();
            message.setPictureUrl(cosplayItem.select("a").get(0).attr("href"));
            //Log.d("jdlingyu-pictureurl",message.getPictureUrl());
            message.setImageUrl(cosplayItem.select("img").get(0).attr("original"));
            //Log.d("jdling-imageurl",message.getImageUrl());
            message.setTitle(cosplayItem.select("span").get(0).text());
            //Log.d("jdling-title",message.getTitle());
            list.add(message);
        }
    }

    /*private void creatBcyList(Document document,List<CosplayMessage> list){
        Element cosplay = document.select("div.lwf").get(0);
        Elements cosplayLists = cosplay.select("li");
        for (Element cosplayItem : cosplayLists){
            CosplayMessage message = new CosplayMessage();
            message.setPictureUrl(cosplayItem.select("a").get(0).attr("href"));
            //Log.d("bcy-pictureurl",message.getPictureUrl());
            String imageUrl = cosplayItem.select("img").get(0).attr("src");
            message.setImageUrl(imageUrl.substring(0,imageUrl.lastIndexOf("/")));
            //Log.d("bcy-imageurl",message.getImageUrl());
            message.setTitle(cosplayItem.select("a").get(0).attr("title"));
            //Log.d("bcy-title",message.getTitle());
            list.add(message);
        }
    }*/

    /*private void creatSearchList(Document document,List<CosplayMessage> list){
        Element cosplay = document.select("ul.grid__inner").get(0);
        Elements cosplayLists = cosplay.select("li");
        for (Element cosplayItem : cosplayLists){
            CosplayMessage message = new CosplayMessage();
            message.setPictureUrl("https://bcy.net" + cosplayItem.select("a").get(0).attr("href"));
            //Log.d("search-pictureurl",message.getPictureUrl());
            String imageUrl = cosplayItem.select("img").get(0).attr("src");
            message.setImageUrl(imageUrl.substring(0,imageUrl.lastIndexOf("/")));
            //Log.d("search-imageurl",message.getImageUrl());
            message.setTitle(cosplayItem.select("a").get(0).attr("title"));
            //Log.d("search-title",message.getTitle());
            list.add(message);
        }
    }*/

    /*private void creatCosplayLa(Document document, List<CosplayMessage> list){
        Elements cosplayLists = document.select("li.font12");
        for (Element cosplayItem : cosplayLists) {
            CosplayMessage message = new CosplayMessage();
            message.setPictureUrl("http://cosplay.la" + cosplayItem.select("a").get(0).attr("href"));
            //Log.d("cosplayla-pictureurl",message.getPictureUrl());
            message.setImageUrl(cosplayItem.select("img").get(0).attr("src").split("\\?")[0]);
            //Log.d("cosplayla-imageurl",message.getImageUrl());
            message.setTitle(cosplayItem.select("a").get(1).text());
            //Log.d("cosplayla_title",message.getTitle());
            list.add(message);
        }
    }*/


    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(), CosplayListActivity.class);
        intent.putExtra(CosplayListActivity.COSPLAYIMAGEURL,cosplayMessageLists.get(position).getPictureUrl());
        switch (type){
            case CHINAGIRL:
                intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"chinagirl");
                break;
            case JDLINGYUCOS:
                intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"jdlingyucos");
                break;
            case JDLINGYUMZITU:
                intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"jdlingyumztu");
                break;
            //case BCY:
                //intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"bcy");
                //break;
            case SEARCHRESULT:
                intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"jdlingyucos");
                break;
            //case COSPLAYLA:
                //intent.putExtra(CosplayListActivity.COSPLAYIMAGETYPE,"cosplayla");
                //break;
        }
        this.startActivity(intent);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        switch (type){
            case CHINAGIRL:
                queryPictureUrl(cosplayListUrl + page + ".html");
                break;
            case JDLINGYUCOS:
                if(page == 1){
                    queryPictureUrl(cosplayListUrl);
                }else {
                queryPictureUrl(cosplayListUrl + "page/" + page + "/");
                }
                break;
            case JDLINGYUMZITU:
                if(page == 1){
                    queryPictureUrl(cosplayListUrl);
                }else {
                    queryPictureUrl(cosplayListUrl + "page/" + page + "/");
                }
                break;
            //case BCY:
                //queryPictureUrl(cosplayListUrl + page);
                //break;
            case SEARCHRESULT:
                queryPictureUrl(cosplayListUrl + page + "/?s=" + searchName);
                break;
            //case COSPLAYLA:
                //queryPictureUrl(cosplayListUrl + page);
                //break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        cosplayMessageLists.clear();
        switch (type){
            case CHINAGIRL:
                queryPictureUrl(cosplayListUrl + page + ".html");
                break;
            case JDLINGYUCOS:
                if(page == 1){
                    queryPictureUrl(cosplayListUrl);
                }else {
                    queryPictureUrl(cosplayListUrl + "page/" + page + "/");
                }
                break;
            case JDLINGYUMZITU:
                if(page == 1){
                    queryPictureUrl(cosplayListUrl);
                }else {
                    queryPictureUrl(cosplayListUrl + "page/" + page + "/");
                }
                break;
            //case BCY:
                //queryPictureUrl(cosplayListUrl + page);
                //break;
            case SEARCHRESULT:
                queryPictureUrl(cosplayListUrl + page + "/?s=" + searchName);
                break;
            //case COSPLAYLA:
                //queryPictureUrl(cosplayListUrl + page);
                //break;
        }
    }

}

