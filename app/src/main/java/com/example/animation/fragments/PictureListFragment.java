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
import com.example.animation.activity.PictureShowActivity;
import com.example.animation.adapter.PictureAdapter;
import com.example.animation.db.AnimationPicture;
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
 * Created by 刘通 on 2017/12/25.
 */

public class PictureListFragment extends Fragment implements PictureAdapter.OnClickListener,OnRefreshLoadmoreListener {

    public static final String YANDERE = "yandere";
    public static final String KONACHAN = "konachan";
    //public static final String LOLIBOORU = "lolibooru";
    public static final String ANIMATION = "animation";
    public static final String SEARCH = "search";

    private static final String TYPE = "type";
    private static final String NAME = "name";
    private String type;
    private final List<AnimationPicture> pictureList = new ArrayList<>();
    private RecyclerView picturesRv;
    private SmartRefreshLayout smartRefreshLayout;
    private PictureAdapter pictureAdapter;
    private String PictureListUrl;
    private String searchName;
    private int page = 1;

    public static final PictureListFragment pictureInstance(String type,String name){
        PictureListFragment pictureListFragment = new PictureListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE,type);
        bundle.putString(NAME,name);
        pictureListFragment.setArguments(bundle);
        return pictureListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(TYPE);
        searchName = getArguments().getString(NAME);
        switch (type){
            case YANDERE:
                PictureListUrl = "https://yande.re/post?page=";
                break;
            case KONACHAN:
                PictureListUrl = "http://konachan.net/post?page=";
                break;
            //case LOLIBOORU:
            //PictureListUrl = "https://lolibooru.moe/post?page=";
            //break;
            case ANIMATION:
                PictureListUrl = "https://anime-pictures.net/pictures/view_posts/";
                break;
            case SEARCH:
                PictureListUrl = "https://anime-pictures.net/pictures/view_posts/";
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_list,container,false);
        picturesRv = (RecyclerView) view.findViewById(R.id.rv_picture);
        smartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smart_refresh_layout_picture);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        picturesRv.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    switch (type){
                        case YANDERE:
                            creatYanderList(pictureDocument,pictureList);
                            break;
                        case KONACHAN:
                            creatKonachanList(pictureDocument,pictureList);
                            break;
                        //case LOLIBOORU:
                            //creatLolibooruList(pictureDocument,pictureList);
                            //break;
                        case ANIMATION:
                            creatAnimationList(pictureDocument,pictureList);
                            break;
                        case SEARCH:
                            creatAnimationList(pictureDocument,pictureList);
                            break;
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
                                if (pictureList.isEmpty()) {
                                    Toast.makeText(getContext(), "服务器暂无数据", Toast.LENGTH_SHORT).show();
                                }
                                smartRefreshLayout.finishLoadmore();
                                smartRefreshLayout.finishRefresh();
                                pictureAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        }.start();
    }

    private void creatYanderList(Document document,List<AnimationPicture> list){
        Elements pictureLists = document.select("a.thumb");
        for (Element picture:pictureLists){
            String pictureUrl = "https://yande.re" + picture.select("a").get(0).attr("href");
            String imageUrl = picture.select("img").get(0).attr("src");
            AnimationPicture animationPicture = new AnimationPicture();
            animationPicture.setPictureUrl(pictureUrl);
            animationPicture.setImageUrl(imageUrl);
            list.add(animationPicture);
        }
    }

    private void creatKonachanList(Document document,List<AnimationPicture> list){
        Elements pictureLists = document.select("a.thumb");
        for (Element picture:pictureLists){
            String pictureUrl = "http://konachan.net" + picture.select("a").get(0).attr("href");
            String imageUrl = "http:" + picture.select("img").get(0).attr("src");
            AnimationPicture animationPicture = new AnimationPicture();
            animationPicture.setPictureUrl(pictureUrl);
            animationPicture.setImageUrl(imageUrl);
            list.add(animationPicture);
        }
    }

    private void creatLolibooruList(Document document,List<AnimationPicture> list){
        Elements pictureLists = document.select("a.thumb");
        for (Element picture:pictureLists){
            String pictureUrl = "https://lolibooru.moe" + picture.select("a").get(0).attr("href");
            String imageUrl = picture.select("img").get(0).attr("src");
            AnimationPicture animationPicture = new AnimationPicture();
            animationPicture.setPictureUrl(pictureUrl);
            animationPicture.setImageUrl(imageUrl);
            list.add(animationPicture);
        }
    }

    private void creatAnimationList(Document document,List<AnimationPicture> list){
        Elements pictureLists = document.select("span.img_block_big");
        for (Element picture:pictureLists){
            if(!picture.text().endsWith("Log in!")){
                String pictureUrl = "https://anime-pictures.net" + picture.select("a").get(0).attr("href");
                String imageUrl ="https:" + picture.select("img").get(0).attr("src");
                //Log.d("pictureUrl",pictureUrl);
                //Log.d("imageUrl",imageUrl);
                AnimationPicture animationPicture = new AnimationPicture();
                animationPicture.setPictureUrl(pictureUrl);
                animationPicture.setImageUrl(imageUrl);
                list.add(animationPicture);
            }
        }
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(),PictureShowActivity.class);
        intent.putExtra(PictureShowActivity.PICTUREURL,pictureList.get(position).getPictureUrl());
        switch (type){
            case YANDERE:
                intent.putExtra(PictureShowActivity.TYPE,"yandere");
                break;
            case KONACHAN:
                intent.putExtra(PictureShowActivity.TYPE,"konachan");
                break;
            //case LOLIBOORU:
                //intent.putExtra(PictureShowActivity.TYPE,"lolibooru");
                //break;
            case ANIMATION:
                intent.putExtra(PictureShowActivity.TYPE,"animation");
                break;
            case SEARCH:
                intent.putExtra(PictureShowActivity.TYPE,"animation");
                break;
        }
        this.startActivity(intent);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page = page + 1;
        switch (type){
            case YANDERE:
                queryPictureUrl(PictureListUrl + page);
                break;
            //case LOLIBOORU:
                //queryPictureUrl(PictureListUrl + page);
                //break;
            case KONACHAN:
                queryPictureUrl(PictureListUrl + page + "&tags=");
                break;
            case ANIMATION:
                queryPictureUrl(PictureListUrl + page + "?lang=en");
                break;
            case SEARCH:
                queryPictureUrl(PictureListUrl + page + "?search_tag=" + searchName + "&order_by=date&ldate=0&lang=en");
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        pictureList.clear();
        switch (type){
            case YANDERE:
                queryPictureUrl(PictureListUrl + page);
                break;
            case KONACHAN:
                queryPictureUrl(PictureListUrl + page + "&tags=");
                break;
            //case LOLIBOORU:
            //queryPictureUrl(PictureListUrl + page);
            //break;
            case ANIMATION:
                page = 0;
                queryPictureUrl(PictureListUrl + page + "?lang=en");
                break;
            case SEARCH:
                page = 0;
                queryPictureUrl(PictureListUrl + page + "?search_tag=" + searchName + "&order_by=date&ldate=0&lang=en");
                break;
        }
    }

}
