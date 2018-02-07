package com.example.animation.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.db.HistorySearch;
import com.example.animation.fragments.AnimationFragment;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener,AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{

    private static final int ANIMATION = 0;

    private static final int COMIC = 1;

    private static final int MUSIC = 2;

    private static final int PICTURE = 3;

    private static final int COSPLAY = 4;

    private Button searchBackButton;

    private EditText searchEditText;

    private ListView searchListView;

    private TextView searchClear;

    private Spinner searchSpinner;

    private ArrayAdapter<String> adapter;

    private ArrayAdapter spinnerAdapter;

    private List<String> spinnerList = new ArrayList<>();

    private List<String> historySearchList = new ArrayList<>();

    private List<HistorySearch> historySearches;

    private final String animationUrl = "https://nyaso.com/dong/";

    private final String comicUrl = "https://nyaso.com/man/";

    private final String musicUrl = "https://nyaso.com/yin/";

    private final String pictureUrl = "https://anime-pictures.net/pictures/view_posts/0?search_tag=";

    private final String cosplayUrl = "";

    private String searchUrl;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        historySearches = DataSupport.findAll(HistorySearch.class);
        if(historySearches.size() != 0 && historySearches.get(0).date == null){
            DataSupport.deleteAll(HistorySearch.class);
        }

        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);

        searchBackButton = (Button) findViewById(R.id.search_back);
        searchBackButton.setOnClickListener(this);
        searchEditText = (EditText) findViewById(R.id.et_search);           //搜素輸入框
        searchEditText.setOnKeyListener(this);
        searchListView = (ListView) findViewById(R.id.search_list);         //歷史搜索記錄
        searchListView.setOnItemClickListener(this);
        searchClear = (TextView) findViewById(R.id.tv_clear);       //清除歷史搜索記錄
        searchClear.setOnClickListener(this);

        searchSpinner = (Spinner) findViewById(R.id.search_spinner);        //Spinner彈框，用於選擇搜索的類型
        //spinner添加“漫畫”,“動漫”,"音乐"
        spinnerList.add("动画");
        spinnerList.add("漫画");
        spinnerList.add("音乐");
        spinnerList.add("二次元");
        spinnerList.add("三次元");
        //設置spinner的adapter
        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(spinnerAdapter);
        //設置歷史搜索的adapter
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,historySearchList);
        searchListView.setAdapter(adapter);
        //初始化搜索類型，搜索url，spinner的顯示，輸入框的提示
        initActivity(type);
        SQLiteDatabase db = Connector.getDatabase();
        //更新曆史搜索記錄顯示
        upDateList();
        //設置spinner的每個item的操作
        searchSpinner.setOnItemSelectedListener(this);
    }

    private void initActivity(int code){
        SpannableString hint = null;
        switch (code){
            case ANIMATION:
                searchUrl = animationUrl;
                hint = new SpannableString("请输入动画名称");
                searchEditText.setHint(hint);
                searchSpinner.setSelection(0);
                break;
            case COMIC:
                searchUrl = comicUrl;
                hint = new SpannableString("请输入漫画名称");
                searchEditText.setHint(hint);
                searchSpinner.setSelection(1);
                break;
            case MUSIC:
                searchUrl = musicUrl;
                hint = new SpannableString("请输入音乐名称");
                searchEditText.setHint(hint);
                searchSpinner.setSelection(2);
                break;
            case PICTURE:
                searchUrl = pictureUrl;
                hint  = new SpannableString("请输入图片名称");
                searchEditText.setHint(hint);
                searchSpinner.setSelection(3);
                break;
            case COSPLAY:
                searchUrl = cosplayUrl;
                hint = new SpannableString("请输入cosplay名称");
                searchEditText.setHint(hint);
                searchSpinner.setSelection(4);
                break;
        }

    }

    private void upDateList(){
        historySearchList.clear();
        switch (type){
            case ANIMATION:
                historySearches = DataSupport.where("type = ?","animation").order("date desc").find(HistorySearch.class);
                break;
            case COMIC:
                historySearches = DataSupport.where("type = ?","comic").order("date desc").find(HistorySearch.class);
                break;
            case MUSIC:
                historySearches = DataSupport.where("type = ?","music").order("date desc").find(HistorySearch.class);
                break;
            case PICTURE:
                historySearches = DataSupport.where("type = ?","picture").order("date desc").find(HistorySearch.class);
                break;
            case COSPLAY:
                historySearches = DataSupport.where("type = ?","cosplay").order("date desc").find(HistorySearch.class);
                break;
        }
            for (HistorySearch searchResult : historySearches) {
                historySearchList.add(searchResult.getSearchName());
            }
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clear:
                switch (type){
                    case ANIMATION:
                        DataSupport.deleteAll(HistorySearch.class,"type = ?","animation");
                        upDateList();
                        break;
                    case COMIC:
                        DataSupport.deleteAll(HistorySearch.class,"type = ?","comic");
                        upDateList();
                        break;
                    case MUSIC:
                        DataSupport.deleteAll(HistorySearch.class,"type = ?","music");
                        upDateList();
                        break;
                    case PICTURE:
                        DataSupport.deleteAll(HistorySearch.class,"type = ?","picture");
                        upDateList();
                        break;
                    case COSPLAY:
                        DataSupport.deleteAll(HistorySearch.class,"type = ?","cosplay");
                        upDateList();
                        break;
                }
                break;
            case R.id.search_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(v.getId() == R.id.et_search){
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String searchName = searchEditText.getText().toString();
                switch (type){
                    case ANIMATION:
                        updateAnimationData(searchName);
                        startAnimationSearch(searchName);
                        return true;
                    case COMIC:
                        updateComicData(searchName);
                        startComicSearch(searchName);
                        return true;
                    case MUSIC:
                        updateMusicData(searchName);
                        startMusicSearch(searchName);
                        return true;
                    case PICTURE:
                        updatePictureData(searchName);
                        startPictureSearch(searchName);
                        return true;
                    case COSPLAY:
                        updateCosplayData(searchName);
                        startCosplaySearch(searchName);
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String searchName = historySearchList.get(position);
            switch (type) {
                case ANIMATION:
                    startAnimationSearch(searchName);
                    break;
                case COMIC:
                    startComicSearch(searchName);
                    break;
                case MUSIC:
                    startMusicSearch(searchName);
                    break;
                case PICTURE:
                    startPictureSearch(searchName);
                    break;
                case COSPLAY:
                    startCosplaySearch(searchName);
                    break;
            }
    }

    private void updateAnimationData(String searchName){
        historySearches = DataSupport.where("searchName = ? and type = ?", searchName,"animation").find(HistorySearch.class);
        if (historySearches.isEmpty()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setSearchName(searchName);
            historySearch.setType("animation");
            historySearch.setDate(new Date(System.currentTimeMillis()));
            historySearch.save();
            upDateList();
        }
    }

    private void updateComicData(String searchName){
        historySearches = DataSupport.where("searchName = ? and type = ?", searchName,"comic").find(HistorySearch.class);
        if (historySearches.isEmpty()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setSearchName(searchName);
            historySearch.setType("comic");
            historySearch.setDate(new Date(System.currentTimeMillis()));
            historySearch.save();
            upDateList();
        }
    }

    private void updateMusicData(String searchName){
        historySearches = DataSupport.where("searchName = ? and type = ?", searchName,"music").find(HistorySearch.class);
        if (historySearches.isEmpty()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setSearchName(searchName);
            historySearch.setType("music");
            historySearch.setDate(new Date(System.currentTimeMillis()));
            historySearch.save();
            upDateList();
        }
    }

    private void updatePictureData(String searchName){
        historySearches = DataSupport.where("searchName = ? and type = ?", searchName,"picture").find(HistorySearch.class);
        if (historySearches.isEmpty()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setSearchName(searchName);
            historySearch.setType("picture");
            historySearch.setDate(new Date(System.currentTimeMillis()));
            historySearch.save();
            upDateList();
        }
    }

    private void updateCosplayData(String searchName){
        historySearches = DataSupport.where("searchName = ? and type = ?", searchName,"cosplay").find(HistorySearch.class);
        if (historySearches.isEmpty()) {
            HistorySearch historySearch = new HistorySearch();
            historySearch.setSearchName(searchName);
            historySearch.setType("cosplay");
            historySearch.setDate(new Date(System.currentTimeMillis()));
            historySearch.save();
            upDateList();
        }
    }

    private void startAnimationSearch(String searchName){
        Intent intent = new Intent(SearchActivity.this,DownloadActivity.class);
        intent.putExtra(AnimationFragment.ANIMATION_NAME,searchName);
        intent.putExtra(AnimationFragment.ANIMATION_URL,searchUrl + searchName + ".html");
        SearchActivity.this.startActivity(intent);
        finish();
    }

    private void startComicSearch(String searchName){
        Intent intent = new Intent(SearchActivity.this, ComicSearchResult.class);
        intent.putExtra(AnimationFragment.ANIMATION_NAME, searchName);
        intent.putExtra(AnimationFragment.ANIMATION_URL, comicUrl + searchName + ".html");
        SearchActivity.this.startActivity(intent);
        finish();
    }

    private void startMusicSearch(String searchName){
        Intent intent = new Intent(SearchActivity.this, BasicWebActivity.class);
        intent.putExtra(AnimationFragment.ANIMATION_URL,musicUrl + searchName + ".html");
        SearchActivity.this.startActivity(intent);
        finish();
    }

    private void startPictureSearch(String searchName){
        Intent intent = new Intent(SearchActivity.this,SearchPictureResultActivity.class);
        intent.putExtra("pictureName",searchName);
        intent.putExtra("pictureType","picture");
        SearchActivity.this.startActivity(intent);
        finish();
    }

    private void startCosplaySearch(String searchName){
        Intent intent = new Intent(SearchActivity.this,SearchPictureResultActivity.class);
        intent.putExtra("pictureName",searchName);
        intent.putExtra("pictureType","cosplay");
        SearchActivity.this.startActivity(intent);
        finish();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) spinnerAdapter.getItem(position);
        SpannableString hint = null;
        switch (item){
            case "动画":
                MiStatInterface.recordCountEvent("animation","动画");
                searchUrl = animationUrl;
                type = ANIMATION;
                hint = new SpannableString("请输入动画名称");
                searchEditText.setHint(hint);
                upDateList();
                break;
            case "音乐":
                MiStatInterface.recordCountEvent("music","音乐");
                searchUrl = musicUrl;
                type = MUSIC;
                hint = new SpannableString("请输入音乐名称");
                searchEditText.setHint(hint);
                upDateList();
                break;
            case "漫画":
                MiStatInterface.recordCountEvent("comic","漫画");
                searchUrl = comicUrl;
                type = COMIC;
                hint = new SpannableString("请输入漫画名称");
                searchEditText.setHint(hint);
                upDateList();
                break;
            case "二次元":
                MiStatInterface.recordCountEvent("picture","二次元");
                searchUrl = pictureUrl;
                type = PICTURE;
                hint = new SpannableString("请输入图片名称");
                searchEditText.setHint(hint);
                upDateList();
                break;
            case "三次元":
                MiStatInterface.recordCountEvent("cosplay","三次元");
                searchUrl = cosplayUrl;
                type = COSPLAY;
                hint = new SpannableString("请输入cosplay名称");
                searchEditText.setHint(hint);
                upDateList();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
