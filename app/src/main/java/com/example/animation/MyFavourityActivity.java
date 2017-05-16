package com.example.animation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.animation.Aaapter.ComicSearchResultAdapter;
import com.example.animation.Class.ComicMessageItem;
import com.example.animation.Class.ComicSearchResultList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyFavourityActivity extends AppCompatActivity {

    private List<ComicMessageItem> comicMessageItems;

    private List<ComicSearchResultList> comicSearchResults = new ArrayList<>();

    private Button backButton;

    private RecyclerView myFavourityRecyclerview;

    private ComicSearchResultAdapter myFavourityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourity);
        backButton = (Button) findViewById(R.id.my_favourityBack);
        myFavourityRecyclerview = (RecyclerView) findViewById(R.id.my_favourityRecyclerview);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //設置recyclerview的屬性
        GridLayoutManager layoutManager = new GridLayoutManager(MyFavourityActivity.this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myFavourityRecyclerview.setLayoutManager(layoutManager);
        myFavourityAdapter = new ComicSearchResultAdapter(comicSearchResults);
        myFavourityRecyclerview.setAdapter(myFavourityAdapter);
        //查詢並顯示favourity的數據
        comicSearchResults.clear();
        comicMessageItems = DataSupport.findAll(ComicMessageItem.class);
        for(ComicMessageItem comicMessageItem:comicMessageItems){
            if(comicMessageItem.isMyFavourity()) {
                ComicSearchResultList comicSearchResultList = new ComicSearchResultList();
                comicSearchResultList.setComicImageUrl(comicMessageItem.getComicImageUrl());
                comicSearchResultList.setComicName(comicMessageItem.getComicName());
                comicSearchResultList.setComicUrl(comicMessageItem.getComicUrl());

                Log.d("ComicImageUrl", comicMessageItem.getComicImageUrl());
                Log.d("ComicName", comicMessageItem.getComicName());
                Log.d("ComicUrl", comicMessageItem.getComicImageUrl());

                comicSearchResults.add(comicSearchResultList);
            }
        }
    }
}
