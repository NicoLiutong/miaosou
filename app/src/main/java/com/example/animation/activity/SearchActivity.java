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

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final int ANIMATION = 0;

    private static final int COMIC = 1;

    private static final int MUSIC = 3;

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

        searchBackButton = (Button) findViewById(R.id.search_back);
        searchEditText = (EditText) findViewById(R.id.et_search);           //搜素輸入框
        searchListView = (ListView) findViewById(R.id.search_list);         //歷史搜索記錄
        searchClear = (TextView) findViewById(R.id.tv_clear);       //清除歷史搜索記錄
        searchSpinner = (Spinner) findViewById(R.id.search_spinner);        //Spinner彈框，用於選擇搜索的類型
        //spinner添加“漫畫”,“動漫”,"音乐"
        spinnerList.add("动漫");
        spinnerList.add("漫画");
        spinnerList.add("音乐");
        //設置spinner的adapter
        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(spinnerAdapter);
        //設置歷史搜索的adapter
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,historySearchList);
        searchListView.setAdapter(adapter);
        //初始化搜索類型，搜索url，spinner的顯示，輸入框的提示
        searchUrl = animationUrl;
        type = ANIMATION;
        final SpannableString hint = new SpannableString("请输入动漫名称");
        searchEditText.setHint(hint);

        SQLiteDatabase db = Connector.getDatabase();
        //更新曆史搜索記錄顯示
        upDateList();
        //設置spinner的每個item的操作
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String item = (String) spinnerAdapter.getItem(position);
                if(item.equals("动漫")){
                    searchUrl = animationUrl;
                    type = ANIMATION;
                    SpannableString hint = new SpannableString("请输入动漫名称");
                    searchEditText.setHint(hint);
                    upDateList();
               }else if (item.equals("音乐")){
                    searchUrl = musicUrl;
                    type = MUSIC;
                    SpannableString hint = new SpannableString("请输入音乐名称");
                    searchEditText.setHint(hint);
                    upDateList();

                }
               else {
                    searchUrl = comicUrl;
                    type = COMIC;
                    SpannableString hint = new SpannableString("请输入漫画名称");
                    searchEditText.setHint(hint);
                    upDateList();
               }
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        //清空搜索
        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type == ANIMATION){
                DataSupport.deleteAll(HistorySearch.class,"type = ?","animation");
                upDateList();
                }else if(type == MUSIC){
                    DataSupport.deleteAll(HistorySearch.class,"type = ?","music");
                    upDateList();
                }
                else {
                    DataSupport.deleteAll(HistorySearch.class,"type = ?","comic");
                    upDateList();
                }
            }
        });
        //搜索框的keylistener，判斷是否是回車功能，是則隱藏鍵盤，搜索動漫或漫畫，並進入相對應的的介面
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (type == ANIMATION) {
                        historySearches = DataSupport.where("searchName = ? and type = ?", searchEditText.getText().toString(),"animation").find(HistorySearch.class);
                        if (historySearches.isEmpty()) {
                            HistorySearch historySearch = new HistorySearch();
                            historySearch.setSearchName(searchEditText.getText().toString());
                            historySearch.setType("animation");
                            historySearch.setDate(new Date(System.currentTimeMillis()));
                            historySearch.save();
                            upDateList();
                        }
                        Intent intent = new Intent(SearchActivity.this, DownloadActivity.class);
                        intent.putExtra(AnimationFragment.ANIMATION_NAME, searchEditText.getText().toString());
                        intent.putExtra(AnimationFragment.ANIMATION_URL, searchUrl + searchEditText.getText().toString() + ".html");
                        SearchActivity.this.startActivity(intent);
                    }else if(type == MUSIC){
                        historySearches = DataSupport.where("searchName = ? and type = ?", searchEditText.getText().toString(),"music").find(HistorySearch.class);
                        if (historySearches.isEmpty()) {
                            HistorySearch historySearch = new HistorySearch();
                            historySearch.setSearchName(searchEditText.getText().toString());
                            historySearch.setType("music");
                            historySearch.setDate(new Date(System.currentTimeMillis()));
                            historySearch.save();
                            upDateList();
                        }
                        Intent intent = new Intent(SearchActivity.this, BasicWebActivity.class);
                        intent.putExtra(AnimationFragment.ANIMATION_URL,musicUrl + searchEditText.getText().toString() + ".html");
                        SearchActivity.this.startActivity(intent);

                    }
                    else {
                        historySearches = DataSupport.where("searchName = ? and type = ?", searchEditText.getText().toString(),"comic").find(HistorySearch.class);
                        if (historySearches.isEmpty()) {
                            HistorySearch historySearch = new HistorySearch();
                            historySearch.setSearchName(searchEditText.getText().toString());
                            historySearch.setType("comic");
                            historySearch.setDate(new Date(System.currentTimeMillis()));
                            historySearch.save();
                            upDateList();
                        }
                        Intent intent = new Intent(SearchActivity.this, ComicSearchResult.class);
                        intent.putExtra(AnimationFragment.ANIMATION_NAME, searchEditText.getText().toString());
                        intent.putExtra(AnimationFragment.ANIMATION_URL, comicUrl + searchEditText.getText().toString() + ".html");
                        SearchActivity.this.startActivity(intent);
                    }
                }
                return false;
            }
        });

        //處理歷史搜索的每個item的點擊，先判斷搜索的類型，然後傳入相對應的url，進入搜索介面
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchName = historySearchList.get(position);
                if(type == ANIMATION){
                Intent intent = new Intent(SearchActivity.this,DownloadActivity.class);
                intent.putExtra(AnimationFragment.ANIMATION_NAME,searchName);
                intent.putExtra(AnimationFragment.ANIMATION_URL,searchUrl + searchName + ".html");
                SearchActivity.this.startActivity(intent);
                }else if(type == MUSIC){
                    Intent intent = new Intent(SearchActivity.this, BasicWebActivity.class);
                    intent.putExtra(AnimationFragment.ANIMATION_URL,musicUrl + searchName + ".html");
                    SearchActivity.this.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(SearchActivity.this, ComicSearchResult.class);
                    intent.putExtra(AnimationFragment.ANIMATION_NAME, searchName);
                    intent.putExtra(AnimationFragment.ANIMATION_URL, comicUrl + searchName + ".html");
                    SearchActivity.this.startActivity(intent);
                }

            }
        });

        searchBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void upDateList(){
        historySearchList.clear();
        if(type == ANIMATION){
        historySearches = DataSupport.where("type = ?","animation").order("date desc").find(HistorySearch.class);
        }else if(type == MUSIC){
            historySearches = DataSupport.where("type = ?","music").order("date desc").find(HistorySearch.class);
    }
        else {
            historySearches = DataSupport.where("type = ?","comic").order("date desc").find(HistorySearch.class);
        }

            for (HistorySearch searchResult : historySearches) {
                historySearchList.add(searchResult.getSearchName());
            }
            adapter.notifyDataSetChanged();
    }
}
