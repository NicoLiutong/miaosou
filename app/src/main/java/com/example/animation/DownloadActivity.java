package com.example.animation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.Aaapter.DividerItemDecoration;
import com.example.animation.Aaapter.DownloadAdapter;
import com.example.animation.Class.DownloadItem;
import com.example.animation.Fragment.AnimationFragment;

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

    private ProgressDialog progressDialog;

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
        animationName = intent.getStringExtra(AnimationFragment.ANIMATION_NAME);
        animationUrl = intent.getStringExtra(AnimationFragment.ANIMATION_URL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        downloadRecyclerView = (RecyclerView) findViewById(R.id.download_list);
        downloadRecyclerView.setLayoutManager(layoutManager);
        downloadAdapter = new DownloadAdapter(downloadItemList);
        downloadRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        downloadRecyclerView.setAdapter(downloadAdapter);

        animationTitle = (TextView) findViewById(R.id.download_title);
        backButton = (Button) findViewById(R.id.downliad_back);
        linearLayout = (LinearLayout) findViewById(R.id.button_layout);
        returnPageButton = (Button) findViewById(R.id.return_page);
        nextPageButton = (Button) findViewById(R.id.next_page);
        pageText = (TextView) findViewById(R.id.pages);
        pageText.setText(String.valueOf(page));
        animationTitle.setText(animationName);
        backButton.setOnClickListener(this);
        returnPageButton.setOnClickListener(this);
        nextPageButton.setOnClickListener(this);
        DataSupport.deleteAll(DownloadItem.class);
        //Log.d("url",animationUrl);
        queryDownload(animationUrl);
    }


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


                                Log.d("number",downloadNumber.text());
                                Log.d("name",downloadName.text());
                                Log.d("url","https://nyaso.com" + downloadName.attr("href"));
                                Log.d("message",downloadMessage.text());
                                Log.d("page",page + "");

                                DownloadItem downloadItem = new DownloadItem();
                                downloadItem.setDownloadPage(page);
                                downloadItem.setDownloadNumber(downloadNumber.text());
                                downloadItem.setDownloadItem(downloadName.text());
                                downloadItem.setDownloadMessage(downloadMessage.text());
                                downloadItem.setDownloadUrl("https://nyaso.com" + downloadName.attr("href"));
                                downloadItem.save();
                            }
                        }

                    Elements pages = document.select("a.button-primary");
                    for(Element pagess:pages){
                        String attr = pagess.attr("href");
                        if(attr.equals("javascript:alert('已经是最后一页啦！')")){
                            isNextPages = false;
                            maxPage = page;
                        }else {

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


    private void showProgressDialog(){
        linearLayout.setVisibility(View.INVISIBLE);
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        linearLayout.setVisibility(View.VISIBLE);
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
       int buttonId = v.getId();
        switch (buttonId){
            case R.id.downliad_back: finish();
                break;
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

    public void updateData(List<DownloadItem> charLists){
        downloadItemList.clear();
        for(DownloadItem charList:charLists){
            downloadItemList.add(charList);
        }
        downloadAdapter.notifyDataSetChanged();
        this.charLists.clear();
    }
}
