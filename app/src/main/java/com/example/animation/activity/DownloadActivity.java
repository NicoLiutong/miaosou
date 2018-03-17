package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.adapter.DividerItemDecoration;
import com.example.animation.adapter.DownloadAdapter;
import com.example.animation.db.DownloadItem;
import com.example.animation.fragments.AnimationFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener,OnRefreshLoadmoreListener {

    private RecyclerView downloadRecyclerView;

    private DownloadAdapter downloadAdapter;

    private TextView animationTitle;

    private Button backButton;

    private TextView downloadFailText;

    private SmartRefreshLayout smartRefreshLayout;
    private ImageView head;

    private AlertDialog.Builder alertDialogBuilder;

    private AlertDialog alertDialog;

    private List<DownloadItem> downloadItemList = new ArrayList<>();

    private String animationName;

    private String animationUrl;

    private int page;

    private boolean isNextPages = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        page = 1;
        Intent intent = getIntent();
        animationName = intent.getStringExtra(AnimationFragment.ANIMATION_NAME);        //獲取傳入的動漫名字
        animationUrl = intent.getStringExtra(AnimationFragment.ANIMATION_URL);      //獲取傳入的動漫url
        //初始化downloadRecyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView = (RecyclerView) findViewById(R.id.download_list);
        downloadRecyclerView.setLayoutManager(layoutManager);
        downloadRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        downloadAdapter = new DownloadAdapter(downloadItemList);
        downloadRecyclerView.setAdapter(downloadAdapter);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.animation_download_srl);
        head = (ImageView) findViewById(R.id.animation_download_head);
        Glide.with(this).load(R.drawable.loading_image).asGif().into(head);
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        smartRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        smartRefreshLayout.setDisableContentWhenRefresh(true);
        animationTitle = (TextView) findViewById(R.id.download_title);      //動漫title
        backButton = (Button) findViewById(R.id.downliad_back);     //返回鍵
        downloadFailText = (TextView) findViewById(R.id.download_fali);
        animationTitle.setText(animationName);
        backButton.setOnClickListener(this);
        if(downloadItemList.isEmpty()){
            smartRefreshLayout.autoRefresh();
        }else {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadmore();
            downloadAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "动画下载列表");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    //查詢數據，更新數據庫
    private void queryDownload(final String pageUrl){
        //showProgressDialog();
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
                            if (downloads.get(0).select("td").get(1).text().split(" ")[0].equals("唔，喵搜娘检索不到相关动画资源")) {
                            } else{
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
                                    downloadItemList.add(downloadItem);
                                }
                            }
                        }
                        //判斷是否到達最後一頁：如果是則將maxpage設置為這頁，isnextpages設置為false
                        Elements pages = document.select("a.button-primary");
                        for (Element pagess : pages) {
                            String attr = pagess.attr("href");
                            if (attr.equals("javascript:alert('已经是最后一页啦！')")) {
                                isNextPages = false;
                            } else {
                                isNextPages = true;
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                    page = page--;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //closeProgressDialog();
                        //for (DownloadItem downloadItem:downloadItemList){
                            //Log.d("name",downloadItem.getDownloadItem());
                            //Log.d("page",downloadItem.getDownloadPage()+"");
                        //}
                        if(downloadItemList.isEmpty()){
                            downloadFailText.setText("唔，喵搜娘检索不到相关动画资源 ,,Ծ‸Ծ,,");
                            downloadFailText.setVisibility(View.VISIBLE);
                            smartRefreshLayout.finishLoadmore();
                            smartRefreshLayout.finishRefresh();
                            downloadAdapter.notifyDataSetChanged();
                        }else {
                            downloadFailText.setVisibility(View.GONE);
                            smartRefreshLayout.finishLoadmore();
                            smartRefreshLayout.finishRefresh();
                            downloadAdapter.notifyDataSetChanged();
                        }
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
        }

    }

    private void showProgressDialog(){
        if(alertDialogBuilder == null){
            alertDialogBuilder = new AlertDialog.Builder(this);
            View v = View.inflate(getContext(),R.layout.loading_layout,null);
            ImageView imageView = (ImageView) v.findViewById(R.id.loading_image);
            Glide.with(this).load(R.drawable.loading_image).asGif().into(imageView);
            alertDialogBuilder.setView(v);
            alertDialogBuilder.create();
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

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        if(!isNextPages){
            Toast.makeText(this,"没有更多的数据了",Toast.LENGTH_LONG).show();
            smartRefreshLayout.finishLoadmore();
        }else {
            page=page+1;
            String nextPageUrl = animationUrl.split("\\.html")[0];
            queryDownload(nextPageUrl + "_" + page + ".html");
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page=1;
        downloadItemList.clear();
        queryDownload(animationUrl);        //查詢動漫列表
    }
}
