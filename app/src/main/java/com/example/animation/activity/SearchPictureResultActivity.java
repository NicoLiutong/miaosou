package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.fragments.CosplayListFragment;
import com.example.animation.fragments.PictureListFragment;

public class SearchPictureResultActivity extends AppCompatActivity {

    private TextView tvsSearchName;

    private String searchName;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_picture_result);
        tvsSearchName = (TextView) findViewById(R.id.picture_search_tv);
        Intent intent = getIntent();
        searchName = intent.getStringExtra("pictureName");
        type = intent.getStringExtra("pictureType");
        tvsSearchName.setText(searchName);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment searchPictureFragment = null;
        switch (type){
            case "picture":
                searchPictureFragment = PictureListFragment.pictureInstance(PictureListFragment.SEARCH,searchName);
                break;
            case "cosplay":
                searchPictureFragment = CosplayListFragment.cosplayInstance(CosplayListFragment.SEARCHRESULT,searchName);
                break;
        }

        transaction.replace(R.id.picture_search_fl,searchPictureFragment);
        transaction.commit();
    }
}
