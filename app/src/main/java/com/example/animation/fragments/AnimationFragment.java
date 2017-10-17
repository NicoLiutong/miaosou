package com.example.animation.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.animation.adapter.AnimationAdapter;
import com.example.animation.adapter.DividerItemDecoration;
import com.example.animation.db.AnimationItem;
import com.example.animation.R;
import com.example.animation.view.BounceBallView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 刘通 on 2017/2/26.
 */

public class AnimationFragment extends Fragment implements View.OnClickListener{

    public static final String ANIMATION_NAME = "animation_name";

    public static final String ANIMATION_URL = "animation_url";

    private AlertDialog.Builder alertDialogBuilder;

    //private ViewPager vpAnimationList;

    private TextView tv_monday,tv_tuesday,tv_wednesday,tv_thursday,tv_friday,tv_saturday,tv_sunday;

    private AlertDialog alertDialog;

    private RecyclerView mRecyclerView;

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

    private RelativeLayout relativeAnimationList;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private int week;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation_list,container,false);
        relativeAnimationList = (RelativeLayout) view.findViewById(R.id.relative_animationList);
        tv_monday = (TextView) view.findViewById(R.id.tv_monday);
        tv_monday.setOnClickListener(this);
        tv_tuesday = (TextView) view.findViewById(R.id.tv_tuesday);
        tv_tuesday.setOnClickListener(this);
        tv_wednesday = (TextView) view.findViewById(R.id.tv_wednesday);
        tv_wednesday.setOnClickListener(this);
        tv_thursday = (TextView) view.findViewById(R.id.tv_thursday);
        tv_thursday.setOnClickListener(this);
        tv_friday = (TextView) view.findViewById(R.id.tv_friday);
        tv_friday.setOnClickListener(this);
        tv_saturday = (TextView) view.findViewById(R.id.tv_saturday);
        tv_saturday.setOnClickListener(this);
        tv_sunday = (TextView) view.findViewById(R.id.tv_sunday);
        tv_sunday.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_animation_list);

        initRecyclerView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        week = getCalendarWeek();
        setTitle(week);
        setRecyclerViewAdapter(week);

        //獲取SharePreference
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        //獲取數據進行顯示
        handAnimation();
    }

    private int getCalendarWeek(){
        Calendar calendar = Calendar.getInstance();
        int i;
        i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i){
            case 1:return 6;
            case 2:return 0;
            case 3:return 1;
            case 4:return 2;
            case 5:return 3;
            case 6:return 4;
            case 7:return 5;
            default:return -1;
        }
    }

    private void setTitle(int position){
        switch (position){
            case 0:clearTextViewColor();
                tv_monday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 1:clearTextViewColor();
                tv_tuesday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 2:clearTextViewColor();
                tv_wednesday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 3:clearTextViewColor();
                tv_thursday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 4:clearTextViewColor();
                tv_friday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 5:clearTextViewColor();
                tv_saturday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
            case 6:clearTextViewColor();
                tv_sunday.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
                break;
        }
    }

    private void setRecyclerViewAdapter(int position){
        switch (position){
            case 0:mRecyclerView.setAdapter(oneAnimationAdapter);
                break;
            case 1:mRecyclerView.setAdapter(twoAnimationAdapter);
                break;
            case 2:mRecyclerView.setAdapter(threeAnimationAdapter);
                break;
            case 3:mRecyclerView.setAdapter(fourAnimationAdapter);
                break;
            case 4:mRecyclerView.setAdapter(fiveAnimationAdapter);
                break;
            case 5:mRecyclerView.setAdapter(sixAnimationAdapter);
                break;
            case 6:mRecyclerView.setAdapter(sevenAnimationAdapter);
                break;
        }
    }
    private void clearTextViewColor(){
        tv_monday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_thursday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_wednesday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_tuesday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_friday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_saturday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
        tv_sunday.setTextColor(ContextCompat.getColor(getContext(),R.color.gray));
    }

    private void initRecyclerView(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        oneAnimationAdapter = new AnimationAdapter(oneAnimationItems);
        twoAnimationAdapter = new AnimationAdapter(twoAnimationItems);
        threeAnimationAdapter = new AnimationAdapter(threeAnimationItems);
        fourAnimationAdapter = new AnimationAdapter(fourAnimationItems);
        fiveAnimationAdapter = new AnimationAdapter(fiveAnimationItems);
        sixAnimationAdapter = new AnimationAdapter(sixAnimationItems);
        sevenAnimationAdapter = new AnimationAdapter(sevenAnimationItems);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        mRecyclerView.setNestedScrollingEnabled(false);
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
        relativeAnimationList.setVisibility(View.INVISIBLE);
        showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int i = 1;
                    Document document = Jsoup.connect("http://ouo.us/anime.html").timeout(3000).post();
                    //String dateName = pref.getString("datename","");
                    if(document.select("title").get(0).text().equals("这里什么都木有喔！- 喵阅ouo.us")){

                    }else {
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
                        relativeAnimationList.setVisibility(View.VISIBLE);
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

        //animationPageAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_monday:
                setTitle(0);
                setRecyclerViewAdapter(0);
                break;
            case R.id.tv_tuesday:
                setTitle(1);
                setRecyclerViewAdapter(1);
                break;
            case R.id.tv_wednesday:
                setTitle(2);
                setRecyclerViewAdapter(2);
                break;
            case R.id.tv_thursday:
                setTitle(3);
                setRecyclerViewAdapter(3);
                break;
            case R.id.tv_friday:
                setTitle(4);
                setRecyclerViewAdapter(4);
                break;
            case R.id.tv_saturday:
                setTitle(5);
                setRecyclerViewAdapter(5);
                break;
            case R.id.tv_sunday:
                setTitle(6);
                setRecyclerViewAdapter(6);
                break;
        }
    }
}
