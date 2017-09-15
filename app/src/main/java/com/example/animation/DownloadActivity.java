package com.example.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.Aaapter.DividerItemDecoration;
import com.example.animation.Aaapter.DownloadAdapter;
import com.example.animation.Class.DownloadItem;
import com.example.animation.Fragment.AnimationFragment;
import com.example.animation.view.BounceBallView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView downloadRecyclerView;

    private DownloadAdapter downloadAdapter;

    private TextView animationTitle;

    private Button backButton;

    private Button returnPageButton;

    private Button nextPageButton;

    private TextView pageText;

    private AlertDialog.Builder alertDialogBuilder;

    private AlertDialog alertDialog;

    private LinearLayout linearLayout;

    private List<DownloadItem> downloadItemList = new ArrayList<>();

    private List<DownloadItem> charLists;

    private String animationName;

    private String animationUrl;

    private int page;

    private int maxPage;


    private boolean isNextPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        page = 1;
        maxPage = 0;
        Intent intent = getIntent();
        animationName = intent.getStringExtra(AnimationFragment.ANIMATION_NAME);        //獲取傳入的動漫名字
        animationUrl = intent.getStringExtra(AnimationFragment.ANIMATION_URL);      //獲取傳入的動漫url
        //初始化downloadRecyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView = (RecyclerView) findViewById(R.id.download_list);
        downloadRecyclerView.setLayoutManager(layoutManager);
        downloadAdapter = new DownloadAdapter(downloadItemList);
        downloadRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        downloadRecyclerView.setAdapter(downloadAdapter);
        
        animationTitle = (TextView) findViewById(R.id.download_title);      //動漫title
        backButton = (Button) findViewById(R.id.downliad_back);     //返回鍵
        linearLayout = (LinearLayout) findViewById(R.id.button_layout);     
        returnPageButton = (Button) findViewById(R.id.return_page);     //返回上一頁
        nextPageButton = (Button) findViewById(R.id.next_page);     //下一頁
        pageText = (TextView) findViewById(R.id.pages);     //頁碼顯示
        pageText.setText(String.valueOf(page));
        animationTitle.setText(animationName);
        backButton.setOnClickListener(this);
        returnPageButton.setOnClickListener(this);
        nextPageButton.setOnClickListener(this);
        DataSupport.deleteAll(DownloadItem.class);
        //Log.d("url",animationUrl);
        queryDownload(animationUrl);        //查詢動漫列表
    }

    //查詢數據，更新數據庫
    private void queryDownload(final String pageUrl){
        showProgressDialog();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    isNextPages = false;
                    Document document = Jsoup.connect(pageUrl).timeout(5000).post();
                        Elements allDownloads = document.getElementsByTag("tbody");
                        for (Element downloadlist : allDownloads) {
                            Elements downloads = downloadlist.select("tr");
                            for (Element download : downloads) {
                                Element downloadNumber = download.select("td").first();
                                Element downloadName = download.select("td").get(1).select("a").get(0);
                                Element downloadMessage = download.select("td").get(1).select("p").get(0);


                                /*Log.d("number",downloadNumber.text());
                                Log.d("name",downloadName.text());
                                Log.d("url","https://nyaso.com" + downloadName.attr("href"));
                                Log.d("message",downloadMessage.text());
                                Log.d("page",page + "");*/

                                DownloadItem downloadItem = new DownloadItem();
                                downloadItem.setDownloadPage(page);
                                downloadItem.setDownloadNumber(downloadNumber.text());
                                downloadItem.setDownloadItem(downloadName.text());
                                downloadItem.setDownloadMessage(downloadMessage.text());
                                downloadItem.setDownloadUrl("https://nyaso.com" + downloadName.attr("href"));
                                downloadItem.save();
                            }
                        }
                        //判斷是否到達最後一頁：如果是則將maxpage設置為這頁，isnextpages設置為false
                        Elements pages = document.select("a.button-primary");
                        for (Element pagess : pages) {
                            String attr = pagess.attr("href");
                            if (attr.equals("javascript:alert('已经是最后一页啦！')")) {
                                isNextPages = false;
                                maxPage = page;
                            } else {

                                isNextPages = true;
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        pageText.setText("第" + page + "页");
                        charLists = DataSupport.where("downloadPage = ?",String.valueOf(page)).find(DownloadItem.class);
                        updateData(charLists);
                    }
                });
            }

        }.start();

    }

    @Override
    public void onClick(View v) {
       int buttonId = v.getId();
        switch (buttonId){
            case R.id.downliad_back: finish();
                break;
            //上一頁按鍵，先判斷是否為第一頁；是，則顯示彈窗；否，則將page減1，isnextpages設置為true，pagetext更新，從數據庫中查詢數據，更新顯示
            case R.id.return_page:
                if(page == 1){
                    Toast.makeText(this,"已经是第一页啦",Toast.LENGTH_SHORT).show();
                }
                else {
                    page --;
                    isNextPages = true;
                    pageText.setText("第" + page + "页");
                    charLists = DataSupport.where("downloadPage = ?",String.valueOf(page)).find(DownloadItem.class);
                    updateData(charLists);
                }
                break;
                //下一頁按鍵，先判斷isnextpages；如果為false，彈出最後一頁通知；否則將page加1，然後與最大頁數比較，相等則把isnextpages設置為false；然後
                //更新顯示，先在數據庫中查詢，如果有則直接更新；沒有則從網絡查詢並更新
            case R.id.next_page:
               if(! isNextPages){
                   Toast.makeText(this,"已经是最后一页啦",Toast.LENGTH_SHORT).show();
               } else {
                   page ++;
                   if(maxPage == page){
                       isNextPages = false;
                   }
                   pageText.setText("第" + page + "页");
                   charLists = DataSupport.where("downloadPage = ?",String.valueOf(page)).find(DownloadItem.class);
                   if(charLists.isEmpty()){
                       String nextPageUrl = animationUrl.split("\\.html")[0];
                       queryDownload(nextPageUrl + "_" + page + ".html");
                   }else {
                       updateData(charLists);
                   }
               }
                break;
        }

    }
    //更新顯示
    public void updateData(List<DownloadItem> charLists){
        downloadItemList.clear();
        for(DownloadItem charList:charLists){
            downloadItemList.add(charList);
        }
        downloadAdapter.notifyDataSetChanged();
        this.charLists.clear();
    }

    private void showProgressDialog(){
        if(alertDialogBuilder == null){
            alertDialogBuilder = new AlertDialog.Builder(this);
            View v = View.inflate(this,R.layout.bounce_ball_view,null);
            BounceBallView ballView =(BounceBallView) v.findViewById(R.id.bounce_ball);
            ballView.start();
            alertDialogBuilder.setView(v);
            alertDialogBuilder.setCancelable(false);
        }
        alertDialog = alertDialogBuilder.show();
    }

    private void closeProgressDialog(){
        if(alertDialog != null){
            alertDialog.dismiss();
            alertDialogBuilder = null;
            alertDialog = null;
        }
    }
}
