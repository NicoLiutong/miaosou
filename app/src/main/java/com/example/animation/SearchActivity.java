package com.example.animation;

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

import com.example.animation.Class.HistorySearch;
import com.example.animation.Fragment.AnimationFragment;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.where;

public class SearchActivity extends AppCompatActivity {

    private static final int ANIMATION = 0;

    private static final int COMIC = 1;

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

    private String searchUrl;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        searchBackButton = (Button) findViewById(R.id.search_back);
        searchEditText = (EditText) findViewById(R.id.et_search);
        searchListView = (ListView) findViewById(R.id.search_list);
        searchClear = (TextView) findViewById(R.id.tv_clear);
        searchSpinner = (Spinner) findViewById(R.id.search_spinner);

        spinnerList.add("动漫");
        spinnerList.add("漫画");

        spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(spinnerAdapter);

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,historySearchList);
        searchListView.setAdapter(adapter);

        searchUrl = animationUrl;
        type = ANIMATION;
        SpannableString hint = new SpannableString("请输入动漫名称");
        searchEditText.setHint(hint);

        SQLiteDatabase db = Connector.getDatabase();

        upDateList();

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
                }else {
                    DataSupport.deleteAll(HistorySearch.class,"type = ?","comic");
                    upDateList();
                }
            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (type == ANIMATION) {
                        historySearches = where("searchName = ? and type = ?", searchEditText.getText().toString(),"animation").find(HistorySearch.class);
                        if (historySearches.isEmpty()) {
                            HistorySearch historySearch = new HistorySearch();
                            historySearch.setSearchName(searchEditText.getText().toString());
                            historySearch.setType("animation");
                            historySearch.save();
                            upDateList();
                        }
                        Intent intent = new Intent(SearchActivity.this, DownloadActivity.class);
                        intent.putExtra(AnimationFragment.ANIMATION_NAME, searchEditText.getText().toString());
                        intent.putExtra(AnimationFragment.ANIMATION_URL, searchUrl + searchEditText.getText().toString() + ".html");
                        SearchActivity.this.startActivity(intent);
                    }
                    else {
                        historySearches = where("searchName = ? and type = ?", searchEditText.getText().toString(),"comic").find(HistorySearch.class);
                        if (historySearches.isEmpty()) {
                            HistorySearch historySearch = new HistorySearch();
                            historySearch.setSearchName(searchEditText.getText().toString());
                            historySearch.setType("comic");
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


        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchName = historySearchList.get(position);
                if(type == ANIMATION){
                Intent intent = new Intent(SearchActivity.this,DownloadActivity.class);
                intent.putExtra(AnimationFragment.ANIMATION_NAME,searchName);
                intent.putExtra(AnimationFragment.ANIMATION_URL,searchUrl + searchName + ".html");
                SearchActivity.this.startActivity(intent);
                }else{
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
        historySearches = where("type = ?","animation").find(HistorySearch.class);
        }else {
            historySearches = where("type = ?","comic").find(HistorySearch.class);
        }
        for(HistorySearch searchResult:historySearches){
            historySearchList.add(searchResult.getSearchName());
        }
        adapter.notifyDataSetChanged();
    }
}
