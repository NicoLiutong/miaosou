package com.example.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.animation.Aaapter.ComicReadAdapter;
import com.example.animation.Fragment.ComicFragment;

import java.util.ArrayList;
import java.util.List;

public class ComicReadActivity extends AppCompatActivity {

    private String comicUrl;

    private String comicPage;

    private String comicAllPages;

    private Button comicReadBack;

    private TextView comicReadPage;

    private RecyclerView comicReadRecyclerview;

    private List<String> comicImageUrl = new ArrayList<>();

    private String comicReadFirst;

    private String comicReadSecond;

    private String comicReadPages;

    private int comicReadThird = 1;

    private ComicReadAdapter comicReadAdapter;

    private static String frontUrl = "http://tt.fuli.in/c86eo736r62";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_read);
        Intent intent = getIntent();
        comicUrl = intent.getStringExtra(ComicFragment.COMICREADURL);
        comicPage = intent.getStringExtra(ComicFragment.COMICREADPAGE);
        comicAllPages = intent.getStringExtra(ComicFragment.COMICREADEPISODES);
        comicReadBack = (Button) findViewById(R.id.comic_readBack);
        comicReadPage = (TextView) findViewById(R.id.comic_readPage);
        comicReadRecyclerview = (RecyclerView) findViewById(R.id.comic_readRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ComicReadActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        comicReadRecyclerview.setLayoutManager(layoutManager);
        comicReadAdapter = new ComicReadAdapter(comicImageUrl);
        comicReadRecyclerview.setAdapter(comicReadAdapter);

        comicReadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comicImageUrl.clear();
        comicReadPage.setText("第" + comicPage);
        comicReadFirst = comicUrl.split("/")[4].split("\\.")[0];
        comicReadSecond = comicPage.split("卷")[0].split("话")[0];
        comicReadPages = comicAllPages.split("P")[0];//.split("\\ ")[1];
        //Log.d("first", comicReadFirst);
        //Log.d("second", comicReadSecond);
        //Log.d("third", comicReadPages);
        //comicReadThird = Integer.getInteger(comicReadPages.);
        //Log.d("four",comicReadThird + " ");
        while (true) {
            if (String.valueOf(comicReadThird).equals(comicReadPages)) {
                if (comicReadThird < 10) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                } else if (comicReadThird < 100) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                } else {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                }
                break;
            } else {
                if (comicReadThird < 10) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                } else if (comicReadThird < 100) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                }else {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                }
                comicReadThird ++;
            }
        }
        comicReadAdapter.notifyDataSetChanged();
        //for (String a:comicImageUrl) {
          //  Log.d("a",a);
       //}
    }
}
