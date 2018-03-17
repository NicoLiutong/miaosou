package com.example.animation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.fragments.ComicReadFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PictureShowActivity extends AppCompatActivity {

    public static final String PICTUREURL = "pictureurl";
    public static final String TYPE = "type";
    private String type;
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
        type = intent.getStringExtra(TYPE);
        switch (type){
            case "yandere":
                pictureId ="yandere_" + pictureWebUrl.substring(pictureWebUrl.lastIndexOf("/") + 1);
                break;
            //case "lolibooru":
                //pictureId ="lolibooru_" + pictureWebUrl.substring(pictureWebUrl.indexOf("show") + 5,pictureWebUrl.lastIndexOf("/"));
                //break;
            case "konachan":
                pictureId ="konachan_" + pictureWebUrl.substring(pictureWebUrl.indexOf("show") + 5,pictureWebUrl.lastIndexOf("/"));
                break;
            case "animation":
                pictureId = "animation_" + pictureWebUrl.substring(pictureWebUrl.lastIndexOf("/") + 1,pictureWebUrl.lastIndexOf("?"));
                //Log.d("pictureId",pictureId);
                break;
        }
        //Log.d("pictureid",pictureId);
        //pictureId = pictureWebUrl.substring(pictureWebUrl.lastIndexOf("/") + 1);
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
                    //Log.d("url11",pictureWebUrl);
                    Document pictureDocument = Jsoup.connect(pictureWebUrl).timeout(3000).get();
                    switch (type){
                        case "yandere":
                            pictureUrl = pictureDocument.select("img.image").get(0).attr("src");
                            break;
                        //case "lolibooru":
                            //pictureUrl = pictureDocument.select("img.image").get(0).attr("src");
                            //break;
                        case "konachan":
                            pictureUrl = "http:" + pictureDocument.select("img.image").get(0).attr("src");
                            break;
                        case "animation":
                            //pictureUrl ="https://anime-pictures.net" + pictureDocument.select("div").get(11).select("a").get(0).attr("href");
                            pictureUrl = "https:" + pictureDocument.select("picture").get(0).select("img").get(0).attr("src");
                            //pictureUrl = pictureDocument.select("source").get(0).attr("srcset").split(".webp")[0];
                            //pictureUrl ="https://anime-pictures.net" + pictureDocument.select("a.download_icon").get(0).attr("href").toString();
                            //pictureUrl = pictureUrl.replace("download_image","get_image");
                            //Log.d("pictureUrl",pictureUrl);
                            break;
                    }
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
                        }else {
                            Toast.makeText(PictureShowActivity.this,"请求失败,请重新加载",Toast.LENGTH_SHORT).show();
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
