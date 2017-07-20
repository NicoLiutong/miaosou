package com.example.animation;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animation.Aaapter.ComicNumberAdapter;
import com.example.animation.Class.ComicMessageItem;
import com.example.animation.Class.ComicNumberList;
import com.example.animation.Fragment.ComicFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicNumberActivity extends AppCompatActivity {

    private String comicUrl;

    private String comicName;

    private String comicImageUrl;

    private String lastUpdateTime;

    private String comicGenre;

    private String comicAuthor;

    private String comicNumber;

    private String comicPesronLove;

    private String comicUpdateType;

    private String comicIntroduction;

    private List<ComicNumberList> comicNumberLists = new ArrayList<>();

    private ComicMessageItem comicMessageItem;

    private Button backButton;

    private TextView numberNameView;

    private ImageView numberImageview;

    private TextView numberUpdateTypeView;

    private TextView numberLastUpdateTimeView;

    private TextView numberGenreView;

    private TextView numberAuthorView;

    private TextView numberNumberView;

    private TextView numberPersonalLoveView;

    private TextView numberIntroductionView;

    private CardView numberFavourityCard;

//    private CardView numberSeeNextCard;

    private Button numberFavourityButton;

 //   private Button numberSeeNextButton;

    private RecyclerView numberRecyclerView;

    private ComicNumberAdapter numberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_number);
        Intent intent = getIntent();
        comicUrl = intent.getStringExtra(ComicFragment.COMICURL);
        initialize();
        comicMessageItem = getComicMessage(comicUrl);
        setButtonClick();
        queryComicNumber();
    }

    private void initialize(){
        backButton = (Button) findViewById(R.id.comic_numberBack);
        numberNameView = (TextView) findViewById(R.id.comic_numberName);
        numberImageview = (ImageView) findViewById(R.id.comic_numberImage);
        numberUpdateTypeView = (TextView) findViewById(R.id.comic_numberUpdateType);
        numberLastUpdateTimeView = (TextView) findViewById(R.id.comic_numberLastUpdateTime);
        numberGenreView = (TextView) findViewById(R.id.comic_numberGenre);
        numberAuthorView = (TextView) findViewById(R.id.comic_numberAuthor);
        numberNumberView = (TextView) findViewById(R.id.comic_numberNumber);
        numberPersonalLoveView = (TextView) findViewById(R.id.comic_numberPersonLove);
        numberIntroductionView = (TextView) findViewById(R.id.comic_numberIntroduction);
        numberFavourityCard = (CardView) findViewById(R.id.comic_numberFavourityCard);
    //    numberSeeNextCard = (CardView) findViewById(R.id.comic_numberSeeNextCard);
        numberFavourityButton = (Button) findViewById(R.id.comic_numberFavourity);
    //    numberSeeNextButton = (Button) findViewById(R.id.comic_numberSeeNext);
        numberRecyclerView = (RecyclerView) findViewById(R.id.comic_numberRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(ComicNumberActivity.this,3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        numberRecyclerView.setLayoutManager(layoutManager);
        numberAdapter = new ComicNumberAdapter(comicNumberLists);
        numberRecyclerView.setNestedScrollingEnabled(false);
        numberRecyclerView.setAdapter(numberAdapter);

    }

    private void updateDisplay(){
        numberNameView.setText(comicName);
        Glide.with(this).load(comicImageUrl).into(numberImageview);
        numberUpdateTypeView.setText(comicUpdateType);
        numberLastUpdateTimeView.setText(lastUpdateTime);
        numberGenreView.setText(comicGenre);
        numberAuthorView.setText(comicAuthor);
        numberNumberView.setText(comicNumber);
        numberPersonalLoveView.setText(comicPesronLove);
        numberIntroductionView.setText(comicIntroduction);
        if(comicMessageItem.isMyFavourity()){
            numberFavourityButton.setText("已收藏");
            numberFavourityCard.setCardBackgroundColor(ContextCompat.getColor(ComicNumberActivity.this,R.color.colorPrimary));
        }else {
            numberFavourityButton.setText("未收藏");
            numberFavourityCard.setCardBackgroundColor(ContextCompat.getColor(ComicNumberActivity.this,R.color.colorAccent));
        }
        numberAdapter.notifyDataSetChanged();
    }

    private void setButtonClick(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        numberFavourityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comicMessageItem.isMyFavourity()){
                    numberFavourityCard.setCardBackgroundColor(ContextCompat.getColor(ComicNumberActivity.this,R.color.colorAccent));
                    /*ComicMessageItem myFavourity = new ComicMessageItem();
                    myFavourity.setMyFavourity(false);
                    myFavourity.updateAll("comicUrl = ?",comicUrl);*/
                    ContentValues values = new ContentValues();
                    values.put("myFavourity",false);
                    values.put("comicImageUrl","");
                    values.put("comicName","");
                    numberFavourityButton.setText("未收藏");
                    DataSupport.updateAll(ComicMessageItem.class,values,"comicUrl = ?",comicUrl);
                    comicMessageItem = getComicMessage(comicUrl);

                }else {
                numberFavourityCard.setCardBackgroundColor(ContextCompat.getColor(ComicNumberActivity.this,R.color.colorPrimary));
                    /*ComicMessageItem myFavourity = new ComicMessageItem();
                    myFavourity.setMyFavourity(true);
                    myFavourity.updateAll("comicUrl = ?",comicUrl);*/
                    ContentValues values = new ContentValues();
                    values.put("myFavourity",true);
                    values.put("comicImageUrl",comicImageUrl);
                    values.put("comicName",comicName);
                    numberFavourityButton.setText("已收藏");
                    DataSupport.updateAll(ComicMessageItem.class,values,"comicUrl = ?",comicUrl);

                    comicMessageItem = getComicMessage(comicUrl);
                }

            }
        });

     /*   numberSeeNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comicMessageItem.getReadNow().isEmpty()){
                    //设置list的最后一个选项把lastread更true，并更新数据库
                    comicNumberLists.get(comicNumberLists.size()-1).setComicLastRead(true);
                    ComicMessageItem readNow = new ComicMessageItem();
                    readNow.setReadNow(comicNumberLists.get(comicNumberLists.size()-1).getComicPages());
                    //adapter更新
                    numberAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(ComicNumberActivity.this,ComicReadActivity.class);
                    intent.putExtra(ComicFragment.COMICREADURL,comicNumberLists.get(comicNumberLists.size()-1).getComicPagesUrl());
                    startActivity(intent);

                }else {
                    //从list找出lastread为true的项目
                    String comicPageUrl = null;
                    for (int i = 0;i < comicNumberLists.size();i++){
                        if(comicNumberLists.get(i).isComicLastRead()){
                            comicPageUrl = comicNumberLists.get(i).getComicPagesUrl();
                        }
                    }
                    Intent intent = new Intent(ComicNumberActivity.this,ComicReadActivity.class);
                    intent.putExtra(ComicFragment.COMICREADURL,comicPageUrl);
                    startActivity(intent);
                }
            }
        });*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        //遍历list，并从数据库中提取最后更新的章节，更新显示
        comicMessageItem = getComicMessage(comicUrl);
        //Log.d("readnowsss",comicMessageItem.getReadNow());
        for(int i = 0;i < comicNumberLists.size();i++){

            //Log.d("pages",comicNumberLists.get(i).getComicPages());

            if(comicNumberLists.get(i).getComicPages().equals(comicMessageItem.getReadNow())){
                comicNumberLists.get(i).setComicLastRead(true);
            }else {
                comicNumberLists.get(i).setComicLastRead(false);
            }
        }

        //adapter改变
        numberAdapter.notifyDataSetChanged();
    }

    private ComicMessageItem getComicMessage(String comicUrl){
        List<ComicMessageItem> comicMessageItem = DataSupport.where("comicUrl = ?",comicUrl).find(ComicMessageItem.class);
        if(comicMessageItem.isEmpty()){
            ComicMessageItem message = new ComicMessageItem();
            message.setComicUrl(comicUrl);
            message.setMyFavourity(false);
            message.setReadNow("");
            message.setComicImageUrl("");
            message.setComicName("");
            message.save();
        }

        /*if(comicMessageItem.get(0).isMyFavourity()){
        Log.d("favourit","ok");
        }else {
            Log.d("favourit","no");
        }*/

      return DataSupport.where("comicUrl = ?",comicUrl).find(ComicMessageItem.class).get(0);
    }

    private void queryComicNumber(){
            comicNumberLists.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Document document = Jsoup.connect(comicUrl).timeout(3000).post();

                    comicName = document.select("h1").get(0).text();
                    comicImageUrl = "https:" + document.select("img").get(1).attr("src");
                    comicUpdateType = document.select("div").get(4).text().split("：")[1].split(" ")[0];
                    Element aboutMeaasge = document.select("ul").get(3);
                    Elements message = aboutMeaasge.select("li");
                    lastUpdateTime = message.get(0).text();
                    comicGenre = message.get(1).text();
                    comicAuthor = message.get(2).text();
                    comicNumber = message.get(3).text();
                    comicPesronLove = message.get(4).text();
                    comicIntroduction = "简介：" + document.select("code").get(0).text();
                    //Log.d("intro",comicIntroduction);
                    Element comic = document.select("div").get(7);
                    Elements chapters = comic.select("a");
                    for(Element chapter:chapters){
                        ComicNumberList numberList = new ComicNumberList();
                        numberList.setComicUrl(comicUrl);
                        String comicPages = chapter.text().split(" ")[0];
                        numberList.setComicPages(comicPages);
                        numberList.setComicEpisodes(chapter.select("span").text());
                        numberList.setComicPagesUrl("https://nyaso.com/comic/" + chapter.attr("href"));
                        if(comicPages.equals(comicMessageItem.getReadNow())){
                            numberList.setComicLastRead(true);
                        }else {
                            numberList.setComicLastRead(false);
                        }
                        comicNumberLists.add(numberList);
                    }


                }
                catch (IOException e){
                    e.printStackTrace();
                }

                ComicNumberActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplay();
                    }
                });
            }
        }.start();
    }
}
