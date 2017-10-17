package com.example.animation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.animation.adapter.ComicSearchResultAdapter;
import com.example.animation.R;
import com.example.animation.db.ComicMessageItem;
import com.example.animation.db.ComicSearchResultList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyFavourityActivity extends AppCompatActivity {

    private List<ComicMessageItem> comicMessageItems;

    private List<ComicSearchResultList> myFavourityLists = new ArrayList<>();

    private Button backButton;

    private TextView tvHaveMyFavourity;

    private RecyclerView myFavourityRecyclerview;

    private ComicSearchResultAdapter myFavourityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourity);
        backButton = (Button) findViewById(R.id.my_favourityBack);
        tvHaveMyFavourity = (TextView) findViewById(R.id.tv_myfavourity);
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
        myFavourityAdapter = new ComicSearchResultAdapter(myFavourityLists);
        myFavourityRecyclerview.setAdapter(myFavourityAdapter);
        //查詢並顯示favourity的數據
        myFavourityLists.clear();
        comicMessageItems = DataSupport.findAll(ComicMessageItem.class);
        for(ComicMessageItem comicMessageItem:comicMessageItems){
            if(comicMessageItem.isMyFavourity()) {
                ComicSearchResultList comicSearchResultList = new ComicSearchResultList();
                comicSearchResultList.setComicImageUrl(comicMessageItem.getComicImageUrl());
                comicSearchResultList.setComicName(comicMessageItem.getComicName());
                comicSearchResultList.setComicUrl(comicMessageItem.getComicUrl());

                /*Log.d("ComicImageUrl", comicMessageItem.getComicImageUrl());
                Log.d("ComicName", comicMessageItem.getComicName());
                Log.d("ComicUrl", comicMessageItem.getComicImageUrl());*/

                myFavourityLists.add(comicSearchResultList);
            }
        }
        if(myFavourityLists.isEmpty()){
            tvHaveMyFavourity.setVisibility(View.VISIBLE);
        }else {
            tvHaveMyFavourity.setVisibility(View.INVISIBLE);
        }
    }
}
