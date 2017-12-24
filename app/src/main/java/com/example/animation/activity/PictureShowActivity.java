package com.example.animation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.animation.R;
import com.example.animation.fragments.ComicReadFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PictureShowActivity extends AppCompatActivity {

    public static final String PICTUREURL = "pictureurl";
    private ProgressBar progressBar;
    private String pictureUrl;
    private String pictureWebUrl;
    private String pictureId;
    private ComicReadFragment pictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_picture_show);
        Intent intent = getIntent();
        pictureWebUrl = intent.getStringExtra(PICTUREURL);
        pictureId = pictureWebUrl.substring(pictureWebUrl.lastIndexOf("/") + 1);
        progressBar = (ProgressBar) findViewById(R.id.pb_progress);
        getPictureUrl();
    }

    private void getPictureUrl(){
        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document pictureDocument = Jsoup.connect(pictureWebUrl).timeout(3000).get();
                    pictureUrl = pictureDocument.select("img.image").get(0).attr("src");
                    //Log.d("id",pictureId);
                    //Log.d("pictureurl",pictureUrl);
                }catch (IOException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(pictureUrl != null){
                            pictureFragment = ComicReadFragment.newInstance(pictureUrl,1,pictureId,ComicReadFragment.PICTURE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_picture,pictureFragment).commit();
                        }

                    }
                });
            }
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
