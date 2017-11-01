package com.example.animation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bm.library.PhotoView;
import com.example.animation.R;
import com.example.animation.view.RoundProgressBarWithNumber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;

public class PictureShowActivity extends AppCompatActivity {

    public static final String PICTUREURL = "pictureurl";
    private PhotoView pvPictureShow;
    private RoundProgressBarWithNumber pictureProgressBar;
    private String pictureUrl;
    private String pictureWebUrl;
    private String pictureId;

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

        pictureProgressBar = (RoundProgressBarWithNumber) findViewById(R.id.pb_picture_progress);
        pvPictureShow = (PhotoView) findViewById(R.id.pv_picture_show);

        OkHttpClient okHttpClient = ProgressManager.getInstance().with(new OkHttpClient.Builder()).build();
        Intent intent = getIntent();
        pictureWebUrl = intent.getStringExtra(PICTUREURL);
        pictureId = pictureWebUrl.substring(pictureWebUrl.lastIndexOf("/") + 1);
        getPictureUrl();
    }

    private void getPictureUrl(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document pictureDocument = Jsoup.connect(pictureWebUrl).timeout(3000).get();
                    pictureUrl = pictureDocument.select("img.image").get(0).attr("src");
                    Log.d("id",pictureId);
                    Log.d("pictureurl",pictureUrl);
                }catch (IOException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }.start();
    }
}
