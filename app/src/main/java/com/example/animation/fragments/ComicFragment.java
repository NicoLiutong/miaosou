package com.example.animation.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.adapter.ComicAdapter;
import com.example.animation.db.ComicItem;
import com.example.animation.R;
import com.example.animation.view.BounceBallView;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2017/3/29.
 */

public class ComicFragment extends Fragment implements View.OnClickListener{

    public static final String COMICURL = "comicurl";   

    public static final String COMICREADURL = "comicreadurl";

    public static final String COMICREADEPISODES = "comicreadepisodes";

    public static final String COMICREADPAGE = "comicreadpage";

    private static final String hotUrl = "https://nyaso.com/comic.html?t=hot";

    private static final String advUrl = "https://nyaso.com/comic.html?t=adv";

    private static final String magicUrl = "https://nyaso.com/comic.html?t=magic";

    private static final String fightUrl = "https://nyaso.com/comic.html?t=fight";

    private static final String girlUrl = "https://nyaso.com/comic.html?t=girl";

    private static final String schoolUrl = "https://nyaso.com/comic.html?t=school";

    private static final String funnyUrl = "https://nyaso.com/comic.html?t=funny";

    private static final String techUrl = "https://nyaso.com/comic.html?t=tech";

    private static final String horrorUrl = "https://nyaso.com/comic.html?t=horror";

    private static final String randUrl = "https://nyaso.com/comic.html?t=rand";

    private String comicUrl;

    private String comicType;

    private TextView hotTextView;

    private TextView advTextView;

    private TextView magicTextView;

    private TextView fightTextView;

    private TextView girlTextView;

    private TextView schoolTextView;

    private TextView funnyTextView;

    private TextView techTextView;

    private TextView horrorTextView;

    private TextView randTextView;

    private AlertDialog.Builder alertDialogBuilder;

    private AlertDialog alertDialog;

    private RecyclerView comicRecyclerView;

    private ComicAdapter comicAdapter;

    private List<ComicItem> comicItemList = new ArrayList<>();

    private List<ComicItem> comicDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_list,container,false);
        comicRecyclerView = (RecyclerView)  view.findViewById(R.id.comic_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comicRecyclerView.setLayoutManager(layoutManager);
        comicAdapter = new ComicAdapter(comicItemList);
        comicRecyclerView.setNestedScrollingEnabled(false);
        comicRecyclerView.setAdapter(comicAdapter);

        hotTextView =(TextView) view.findViewById(R.id.hot);
        advTextView = (TextView) view.findViewById(R.id.adv);
        magicTextView = (TextView) view.findViewById(R.id.magic);
        fightTextView = (TextView) view.findViewById(R.id.fight);
        girlTextView = (TextView) view.findViewById(R.id.girl);
        schoolTextView = (TextView) view.findViewById(R.id.school);
        funnyTextView = (TextView) view.findViewById(R.id.funny);
        techTextView = (TextView) view.findViewById(R.id.tech);
        horrorTextView = (TextView) view.findViewById(R.id.horror);
        randTextView = (TextView) view.findViewById(R.id.rand);

        hotTextView.setOnClickListener(this);
        advTextView.setOnClickListener(this);
        magicTextView.setOnClickListener(this);
        fightTextView.setOnClickListener(this);
        girlTextView.setOnClickListener(this);
        schoolTextView.setOnClickListener(this);
        funnyTextView.setOnClickListener(this);
        techTextView.setOnClickListener(this);
        horrorTextView.setOnClickListener(this);
        randTextView.setOnClickListener(this);


        /*初始化
        1、將所有button的顏色初始化
        2、初始化comicurl
        3、將hot設置為默認點擊事件，並對其設置顏色
        4、更新列表顯示
        */
        setStartColor();
        comicUrl = hotUrl;
        hotTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        hotTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
        comicType = "hot";
        updateComicList();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase db = Connector.getDatabase();

    }

    @Override
    public void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(getActivity(), "漫画列表");
    }

    @Override
    public void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    //刷新列表，用於主界面的下拉刷新，先刪除所有comicType的數據，然後刷新數據
    public void refreshComic(){
            DataSupport.deleteAll(ComicItem.class,"comicType = ?",comicType);
            queryComic();
    }

    //更新列表，初始化時更新列表，如果數據庫中存在數據直接更新顯示；否則從網頁查詢並顯示
    private void updateComicList(){
        if(DataSupport.where("comicType = ?",comicType).find(ComicItem.class).isEmpty()){
            queryComic();
        }
        else {
            updateComic();
        }
    }


    /*查詢數據
   1、顯示ProgressDialog
   2、查詢數據更新列表
   */
    private void queryComic(){
        showProgressDialog();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Document document = Jsoup.connect(comicUrl).timeout(3000).post();
                    Elements comics = document.select("div.six");
                    for(Element comic : comics){
                        String comicUrl = "https://nyaso.com" + comic.select("a").get(0).attr("href");
                        String updateClass = comic.select("span").text();
                        String backgruondUrl = "https:" + comic.select("div.thumb").get(0).attr("style").split("\\(")[1].split("\\)")[0];
                        String comicName = comic.select("a").text();
                        String comicIntroduction = comic.select("h5").text();
                        String comicAuthor = comic.select("p").text();

                        /*Log.d("comicUrl",comicUrl);
                        Log.d("backgroundUrl",backgruondUrl);
                        Log.d("comocName",comicName);
                        Log.d("comicIntroduction",comicIntroduction);
                        Log.d("comicAuthor",comicAuthor);*/

                        ComicItem comicItem = new ComicItem();
                        comicItem.setComicUrl(comicUrl);
                        comicItem.setBackgroundUrl(backgruondUrl);
                        comicItem.setComicName(comicName);
                        comicItem.setComicIntroduction(comicIntroduction);
                        comicItem.setComicAuthor(comicAuthor);
                        comicItem.setUpdateClass(updateClass);
                        comicItem.setComicType(comicType);
                        comicItem.save();
                    }

                }
                catch (IOException e){
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateComic();
                        closeProgressDialog();
                    }
                });
            }
        }.start();
    }

    //更新顯示
    private void updateComic(){
        comicItemList.clear();
        comicDate = DataSupport.where("comicType = ?",comicType).find(ComicItem.class);
        for(ComicItem comicItem:comicDate){
            comicItemList.add(comicItem);
        }
        comicAdapter.notifyDataSetChanged();
        comicDate.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hot :
                setStartColor();
                comicUrl = hotUrl;
                hotTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                hotTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "hot";
                updateComicList();
                break;

            case R.id.adv :
                setStartColor();
                comicUrl = advUrl;
                advTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                advTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "adv";
                updateComicList();
                break;

            case R.id.magic :
                setStartColor();
                comicUrl = magicUrl;
                magicTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                magicTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "magic";
                updateComicList();
                break;

            case R.id.fight :
                setStartColor();
                comicUrl = fightUrl;
                fightTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                fightTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "fight";
                updateComicList();
                break;

            case R.id.girl :
                setStartColor();
                comicUrl = girlUrl;
                girlTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                girlTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "girl";
                updateComicList();
                break;

            case R.id.school :
                setStartColor();
                comicUrl = schoolUrl;
                schoolTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                schoolTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "school";
                updateComicList();
                break;

            case R.id.funny :
                setStartColor();
                comicUrl = funnyUrl;
                funnyTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                funnyTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "funny";
                updateComicList();
                break;

            case R.id.tech :
                setStartColor();
                comicUrl = techUrl;
                techTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                techTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "tech";
                updateComicList();
                break;

            case R.id.horror :
                setStartColor();
                comicUrl = horrorUrl;
                horrorTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                horrorTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "horror";
                updateComicList();
                break;

            case R.id.rand :
                setStartColor();
                comicUrl = randUrl;
                randTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                randTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.blue));
                comicType = "rand";
                updateComicList();
                break;
        }
    }



    private void setStartColor(){
        hotTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        hotTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        advTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        advTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        magicTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        magicTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        fightTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        fightTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        girlTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        girlTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        schoolTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        schoolTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        funnyTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        funnyTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        techTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        techTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        horrorTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        horrorTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));

        randTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.blue));
        randTextView.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));


    }

    private void showProgressDialog(){
        if(alertDialogBuilder == null){
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
