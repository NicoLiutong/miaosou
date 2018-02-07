package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.Util.ACache;
import com.example.animation.Util.ImageLoader;
import com.example.animation.db.CosplayImageMessage;
import com.example.animation.view.MyScorllView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CosplayListActivity extends AppCompatActivity implements MyScorllView.OnClick{
    public static final String COSPLAYIMAGEURL = "cosplayImageUrl";
    public static final String COSPLAYIMAGETYPE = "cosplayImageType";

    private String cosplayImageUrl;
    private String type;
    private List<CosplayImageMessage> cosplayImageMessages = new ArrayList<>();
    private MyScorllView myScorllView;
    private TextView tvLoadingNumber;
    private ACache mAcache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosplay_list);
        Intent intent = getIntent();
        cosplayImageUrl = intent.getStringExtra(COSPLAYIMAGEURL);
        type = intent.getStringExtra(COSPLAYIMAGETYPE);
        myScorllView = (MyScorllView) findViewById(R.id.cosplay_scroll_view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialog);
        builder.setMessage("正在努力加载中请稍后...");
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        tvLoadingNumber = (TextView) findViewById(R.id.tv_loading_number);
        mAcache = ACache.get(new File(this.getCacheDir().getPath()+ File.separator+"cosplay"),1024*1024*500,100);
        myScorllView.setmAcache(mAcache);
        myScorllView.setView(dialog,tvLoadingNumber);
        myScorllView.setOnClickListener(this);
        queryImageLists(cosplayImageUrl);
    }

    private void queryImageLists(final String cosplayImageUrl){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    //Log.d("imageurl",cosplayImageUrl);
                    Document cosplayDocument = Jsoup.connect(cosplayImageUrl).post();
                    switch (type){
                        case "chinagirl":
                            creatChinaGirlImageList(cosplayDocument,cosplayImageMessages);
                            break;
                        case "jdlingyucos":
                            creatJDLingYuImageList(cosplayDocument,cosplayImageMessages);
                            break;
                        case "jdlingyumztu":
                            creatJDLingYuImageList(cosplayDocument,cosplayImageMessages);
                            break;
                        //case "bcy":
                            //creatBcyImageList(cosplayDocument,cosplayImageMessages);
                            //break;
                        /*case "cosplayla":
                            //Element elementcosplayla = cosplayDocument.select("div.talk_pic").get(0);
                            Elements cosplayLaElements = cosplayDocument.select("p.mbottom10");
                            for (Element cosplayLa : cosplayLaElements){
                                CosplayImageMessage message = new CosplayImageMessage();
                                String imageUrl = cosplayLa.select("img").get(0).attr("src").split("\\?")[0];
                                message.setImageUrl(imageUrl);
                                message.setImageId(imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.lastIndexOf(".")));
                                cosplayImageMessages.add(message);
                            }
                            break;*/
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                CosplayListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(cosplayImageMessages.isEmpty()){
                            Toast.makeText(CosplayListActivity.this,"请重试",Toast.LENGTH_SHORT).show();
                        }else {
                            ImageLoader.setCosplayImageMessages(cosplayImageMessages);
                            myScorllView.addImageUrl(cosplayImageMessages);
                        }

                    }
                });
            }
        }.start();
    }

    private void creatChinaGirlImageList(Document cosplayDocument,List<CosplayImageMessage> cosplayImageMessages){
        Elements cosplayElements = cosplayDocument.select("img.zoom");
        for (Element cosplayItem : cosplayElements){
            CosplayImageMessage message = new CosplayImageMessage();
            String imageUrl = "http://www.chinagirlol.cc/" + cosplayItem.select("img").get(0).attr("file");
            message.setImageUrl(imageUrl);
            message.setImageId(imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.lastIndexOf(".")));
            cosplayImageMessages.add(message);
        }
    }

    private void creatJDLingYuImageList(Document cosplayDocument,List<CosplayImageMessage> cosplayImageMessages) {
        Element element = cosplayDocument.select("div.main-body").get(0);
        Element elementcos = element.select("p").get(0);
        Elements cosplayJDElements = elementcos.select("a");
        for (Element cosplayJD : cosplayJDElements){
            CosplayImageMessage message = new CosplayImageMessage();
            String imageUrl = cosplayJD.select("a").get(0).attr("href");
            message.setImageUrl(imageUrl);
            message.setImageId(imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.lastIndexOf(".")));
            cosplayImageMessages.add(message);
            Log.d("imageUrl",message.getImageUrl());
        }
    }

    /*private void creatBcyImageList(Document cosplayDocument,List<CosplayImageMessage> cosplayImageMessages) {
        Element element1 = cosplayDocument.select("div.post__content").get(1);
        //Log.d("element1",element1.toString());
        Elements cosplayBcyElements = element1.select("img");
        for (Element cosplayBCY : cosplayBcyElements){
            String imageUrl = cosplayBCY.attr("src");
            CosplayImageMessage message = new CosplayImageMessage();
            message.setImageUrl(imageUrl.substring(0,imageUrl.lastIndexOf("/")));
            String imageId = imageUrl.substring(0,imageUrl.lastIndexOf("/"));
            message.setImageId(imageId.substring(imageId.lastIndexOf("/"),imageId.lastIndexOf(".")));
            Log.d("url",message.getImageUrl());
            Log.d("id",message.getImageId());
            cosplayImageMessages.add(message);
        }
    }*/
    @Override
    public void OnclickListener(String imageId) {
        Intent intent = new Intent(this,CosplayImageShowActivity.class);
        intent.putExtra("imageId",imageId);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.clearCosplayImageMessages();
    }
}
