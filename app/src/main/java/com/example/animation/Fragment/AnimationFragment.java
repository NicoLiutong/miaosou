package com.example.animation.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.animation.Aaapter.AnimationAdapter;
import com.example.animation.Aaapter.DividerItemDecoration;
import com.example.animation.Class.AnimationItem;
import com.example.animation.R;
import com.example.animation.view.BounceBallView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2017/2/26.
 */

public class AnimationFragment extends Fragment {

    public static final String ANIMATION_NAME = "animation_name";

    public static final String ANIMATION_URL = "animation_url";

    private AlertDialog.Builder alertDialogBuilder;

    private AlertDialog alertDialog;

    private RecyclerView oneRecyclerView;

    private RecyclerView twoRecyclerView;

    private RecyclerView threeRecyclerView;

    private RecyclerView fourRecyclerView;

    private RecyclerView fiveRecyclerView;

    private RecyclerView sixRecyclerView;

    private RecyclerView sevenRecyclerView;

    private AnimationAdapter oneAnimationAdapter;

    private AnimationAdapter twoAnimationAdapter;

    private AnimationAdapter threeAnimationAdapter;

    private AnimationAdapter fourAnimationAdapter;

    private AnimationAdapter fiveAnimationAdapter;

    private AnimationAdapter sixAnimationAdapter;

    private AnimationAdapter sevenAnimationAdapter;

    private List<AnimationItem> oneAnimationItems = new ArrayList<>();

    private List<AnimationItem> twoAnimationItems = new ArrayList<>();

    private List<AnimationItem> threeAnimationItems = new ArrayList<>();

    private List<AnimationItem> fourAnimationItems = new ArrayList<>();

    private List<AnimationItem> fiveAnimationItems = new ArrayList<>();

    private List<AnimationItem> sixAnimationItems = new ArrayList<>();

    private List<AnimationItem> sevenAnimationItems = new ArrayList<>();

    private List<AnimationItem> dataAnimationItems;

    private LinearLayout linearAnimationList;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation_list,container,false);
        linearAnimationList = (LinearLayout) view.findViewById(R.id.linear_animationList);
        //對所有的recyclerview進行初始化
        oneRecyclerView = (RecyclerView) view.findViewById(R.id.oneAnimation_recycler);
        twoRecyclerView = (RecyclerView) view.findViewById(R.id.twoAnimation_recycler);
        threeRecyclerView = (RecyclerView) view.findViewById(R.id.threeAnimation_recycler);
        fourRecyclerView = (RecyclerView) view.findViewById(R.id.fourAnimation_recycler);
        fiveRecyclerView = (RecyclerView) view.findViewById(R.id.fiveAnimation_recycler);
        sixRecyclerView = (RecyclerView) view.findViewById(R.id.sixAnimation_recycler);
        sevenRecyclerView = (RecyclerView) view.findViewById(R.id.sevenAnimation_recycler);

        LinearLayoutManager oneLayoutManager = new LinearLayoutManager(getActivity());
        oneLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager twoLayoutManager = new LinearLayoutManager(getActivity());
        twoLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager threeLayoutManager = new LinearLayoutManager(getActivity());
        threeLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager fourLayoutManager = new LinearLayoutManager(getActivity());
        fourLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager fiveLayoutManager = new LinearLayoutManager(getActivity());
        fiveLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager sixLayoutManager = new LinearLayoutManager(getActivity());
        sixLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager sevenLayoutManager = new LinearLayoutManager(getActivity());
        sevenLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        oneRecyclerView.setLayoutManager(oneLayoutManager);
        twoRecyclerView.setLayoutManager(twoLayoutManager);
        threeRecyclerView.setLayoutManager(threeLayoutManager);
        fourRecyclerView.setLayoutManager(fourLayoutManager);
        fiveRecyclerView.setLayoutManager(fiveLayoutManager);
        sixRecyclerView.setLayoutManager(sixLayoutManager);
        sevenRecyclerView.setLayoutManager(sevenLayoutManager);

        oneAnimationAdapter = new AnimationAdapter(oneAnimationItems);
        twoAnimationAdapter = new AnimationAdapter(twoAnimationItems);
        threeAnimationAdapter = new AnimationAdapter(threeAnimationItems);
        fourAnimationAdapter = new AnimationAdapter(fourAnimationItems);
        fiveAnimationAdapter = new AnimationAdapter(fiveAnimationItems);
        sixAnimationAdapter = new AnimationAdapter(sixAnimationItems);
        sevenAnimationAdapter = new AnimationAdapter(sevenAnimationItems);

        oneRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        twoRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        threeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        fourRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        fiveRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        sixRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        sevenRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        oneRecyclerView.setNestedScrollingEnabled(false);
        twoRecyclerView.setNestedScrollingEnabled(false);
        threeRecyclerView.setNestedScrollingEnabled(false);
        fourRecyclerView.setNestedScrollingEnabled(false);
        fiveRecyclerView.setNestedScrollingEnabled(false);
        sixRecyclerView.setNestedScrollingEnabled(false);
        sevenRecyclerView.setNestedScrollingEnabled(false);

        oneRecyclerView.setAdapter(oneAnimationAdapter);
        twoRecyclerView.setAdapter(twoAnimationAdapter);
        threeRecyclerView.setAdapter(threeAnimationAdapter);
        fourRecyclerView.setAdapter(fourAnimationAdapter);
        fiveRecyclerView.setAdapter(fiveAnimationAdapter);
        sixRecyclerView.setAdapter(sixAnimationAdapter);
        sevenRecyclerView.setAdapter(sevenAnimationAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //獲取SharePreference
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        /*
        oneRecyclerView.setItemAnimator(new DefaultItemAnimator());
        twoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        threeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fourRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fiveRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sixRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sevenRecyclerView.setItemAnimator(new DefaultItemAnimator());*/
        //獲取數據進行顯示

        handAnimation();
    }

    /*

    如果沒有數據庫，進行查詢更新數據庫並顯示；否則直接從數據庫裏查詢顯示
    */
    public void handAnimation(){
        if(! DataSupport.isExist(AnimationItem.class)){
            queryAnimation();
        }else {
            showAnimationList();
        }
    }
    /*
    1、設置ProgressDialog顯示
    2、從忘了查詢數據，更新數據庫
    3、回到主線程，關閉ProgressDialog，並更新顯示
    */
    public void queryAnimation(){
        linearAnimationList.setVisibility(View.INVISIBLE);
        showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int i = 1;
                    Document document = Jsoup.connect("http://ouo.us/anime.html").timeout(3000).post();
                    //String dateName = pref.getString("datename","");
                    if(document != null && document.toString().length() != 0){
                        DataSupport.deleteAll(AnimationItem.class);
                        String date = document.select("option").get(0).text();

                        //Log.d("option",date);

                        //if(dateName.equals(date)){

                        //}else {
                        editor = pref.edit();
                        editor.putString("datename", date);
                        editor.apply();

                        //Log.d("setoption",pref.getString("datename",""));

                        Elements animationlists = document.getElementsByTag("tbody");
                        for (Element animationlist : animationlists) {
                            String week = "week" + i;
                            i++;
                            Elements animations = animationlist.select("tr");
                            for (Element animation : animations) {
                                Element name = animation.select("a.name").first();
                                Element type = animation.select("span").get(1);
                                Elements downloads = animation.select("a.tag");
                                AnimationItem animationItem = new AnimationItem();
                                animationItem.setWeek(week);
                                //Log.d("week", week);
                                animationItem.setAnimationItem(name.text());
                                //Log.d("name", name.text());
                                animationItem.setAnimationType(type.text());
                                //Log.d("type", type.text());
                                for (Element download : downloads) {
                                    if(download.text().equals("资讯")){
                                        animationItem.setAnimationInformationUrl("http://ouo.us" + download.attr("href"));
                                    }
                                    if (download.text().equals("在线")) {
                                        animationItem.setSeeOnlineUrl(download.attr("href"));
                                    }
                                    if (download.text().equals("下载")) {
                                        animationItem.setDownloadUrl(download.attr("href"));
                                        //Log.d("url",String.valueOf(download.attr("href")));
                                    }
                                }
                                animationItem.save();
                    }

                        }
                    }
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAnimationList();
                        closeProgressDialog();
                        linearAnimationList.setVisibility(View.VISIBLE);
                    }
                });
            }

        }.start();
    }

    private void showAnimationList(){
        oneAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week1").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            oneAnimationItems.add(dataAnimationItem);
        }
        oneAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();

        twoAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week2").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            twoAnimationItems.add(dataAnimationItem);
        }
        twoAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();

        threeAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week3").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            threeAnimationItems.add(dataAnimationItem);
        }
        threeAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();


        fourAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week4").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            fourAnimationItems.add(dataAnimationItem);
        }
        fourAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();

        fiveAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week5").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            fiveAnimationItems.add(dataAnimationItem);
        }
        fiveAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();

        sixAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week6").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            sixAnimationItems.add(dataAnimationItem);
        }
        sixAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();

        sevenAnimationItems.clear();
        dataAnimationItems = DataSupport.where("week=?", "week7").find(AnimationItem.class);
        for (AnimationItem dataAnimationItem : dataAnimationItems) {
            sevenAnimationItems.add(dataAnimationItem);
        }
        sevenAnimationAdapter.notifyDataSetChanged();
        dataAnimationItems.clear();
    }

    private void showProgressDialog(){
        if(alertDialogBuilder== null){
            alertDialogBuilder = new AlertDialog.Builder(getContext());
            View v = View.inflate(getContext(),R.layout.bounce_ball_view,null);
            BounceBallView ballView =(BounceBallView) v.findViewById(R.id.bounce_ball);
            ballView.start();
            alertDialogBuilder.setView(v);
            //progressDialog.setMessage("正在加载...");
            alertDialogBuilder.setCancelable(false);
        }
        alertDialog = alertDialogBuilder.show();
    }

    private void closeProgressDialog(){
        if(alertDialog != null){
            alertDialog.dismiss();
            alertDialogBuilder = null;
            alertDialog = null;
        }
    }
}
