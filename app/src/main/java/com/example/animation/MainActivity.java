package com.example.animation;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animation.Fragment.AnimationFragment;
import com.example.animation.Fragment.ComicFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.waps.AppConnect;

public class MainActivity extends AppCompatActivity {

    private static final int ANIMATION = 0;  

    private static final int COMIC = 1;

    private FloatingActionButton floatingActionButton;

    private CollapsingToolbarLayout collapsingToolbar;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CardView animationPageCardview;

    private CardView comicPageCardview;

    private ImageView mainactivityToolbarImageview;

    private int pagesName;

    private SharedPreferences pref;

    private String animationTitle;

    private List<Integer> imageUrl;

    //private String imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DemoApplication.setMainActivity(this);
        //XiaomiUpdateAgent.update(this);

        //广告开启
        AppConnect.getInstance("4ccc2526c7afd772c96d61e301721275","“default",this);

        imageUrl = setImageUrl();  //獲取圖片url
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapsingToolbar);
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));  //設置toolbar背景色
        setSupportActionBar(toolbar);  //設置ActionBar

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        animationPageCardview = (CardView) findViewById(R.id.animation_pageCard);
        comicPageCardview = (CardView) findViewById(R.id.comic_pageCard);
        mainactivityToolbarImageview = (ImageView) findViewById(R.id.animation_toobarImage);
        setToolbarImage();  //設置ToolBarImage
        setAnimationTitle();  //設置Title

        final AnimationFragment animationFragment = new AnimationFragment();  //設置AnimationFragment
        final ComicFragment comicFragment = new ComicFragment();  //設置comicFragment
        animationPageSet(animationFragment);  //初始化為animationFragment

        isInternetOk();  //判斷網絡是否打開

        animationPageCardview.setOnClickListener(new View.OnClickListener() {  //點擊animationPageCardview，調用animationPageSet

            //1、設置button顏色和title 2、設置顯示的Fragment

            @Override
            public void onClick(View v) {
                animationPageSet(animationFragment);
            }
        });

        comicPageCardview.setOnClickListener(new View.OnClickListener() {      //點擊animationPageCardview，調用comicPageSet

            @Override
            public void onClick(View v) {
                comicPageSet(comicFragment);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {  //下拉刷新，下拉先關掉顯示，判斷是ANIMATION還是COMIC刷新
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if(pagesName == ANIMATION){
                    animationFragment.queryAnimation();
                    setAnimationTitle();

                }else {
                    comicFragment.refreshComic();
                }
                //setToolbarImage();
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.animation_search);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {    //搜索button，點擊進入搜索activity
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppConnect.getInstance(this).close();
        //DemoApplication.setMainActivity(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //設置菜單欄
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //菜單欄的點擊事件，點擊進入收藏activity
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.my_favourity:
                intent = new Intent(MainActivity.this,MyFavourityActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.my_signin:
                intent = new Intent(this,SignInActivity.class);
                MainActivity.this.startActivity(intent);
                /*NiceDialog.init()
                        .setLayoutId(R.layout.sign_in)
                        .setDimAmount(0.7f)
                        .setOutCancel(true)
                        .setMargin(40)
                        .setShowBottom(false)
                        .show(getSupportFragmentManager());*/

                break;
        }
        return true;
    }

    private void setAnimationTitle(){   //設置title，獲取datename，如果有則將其設置為title；否則設置默認的
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        animationTitle = pref.getString("datename","");
        if(animationTitle == null){
            animationTitle = "日本新番动画放送列表";
        }else {
            animationTitle = animationTitle + "动画列表";
        }

    }

    private void animationPageSet(Fragment fragment){   //動漫頁面設置，先設置button顏色，設置顯示的Fragment，設置title，設置標籤
        animationPageCardview.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
        //animationPageView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));
        //animationPageView.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.blue));
        comicPageCardview.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
        //comicPageView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.gray));
        //comicPageView.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.white));
        replaceFragenment(fragment,"animationFragmentTag");
        collapsingToolbar.setTitle(animationTitle);
        pagesName = ANIMATION;
    }

    private void comicPageSet(Fragment fragment){   //漫畫頁面設置，先設置button顏色，設置顯示的Fragment，設置title，設置標籤
        animationPageCardview.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorPrimary));
        //animationPageView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.gray));
        //animationPageView.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.white));
        comicPageCardview.setCardBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));
        //comicPageView.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));
        //comicPageView.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.blue));
        replaceFragenment(fragment,"animationFragmentTag");
        collapsingToolbar.setTitle("漫画列表");
        pagesName = COMIC;
    }

    private void replaceFragenment(Fragment fragment,String tag){   //Fragment的更換，傳入Fragment和tag
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment,tag);
        transaction.commit();
    }
    /*判斷網絡是否ok
    1、獲取ConnectivityManager
    2、獲取NetworkInfo
    3、判斷是否有連接，沒有彈出對話框
    4、對話框的確認button，點擊進入設置頁面
    */

    public void isInternetOk(){
        ConnectivityManager manger = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manger.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
        } else {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("点击确认设置网路");
            dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    } else {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            });
            dialog.show();
        }

    }

    /* 
>>>>>>> 78d69cd103828115de3c40dcfad215950b4eacaa
    添加圖片
    */
    private List<Integer> setImageUrl(){
        List<Integer> imageUrls = new ArrayList<>();
        imageUrls.add(R.drawable.img001);
        imageUrls.add(R.drawable.img002);
        imageUrls.add(R.drawable.img003);
        imageUrls.add(R.drawable.img004);
        imageUrls.add(R.drawable.img005);
        imageUrls.add(R.drawable.img006);
        imageUrls.add(R.drawable.img007);
        imageUrls.add(R.drawable.img008);
        imageUrls.add(R.drawable.img009);
        imageUrls.add(R.drawable.img010);
        imageUrls.add(R.drawable.img011);
        imageUrls.add(R.drawable.img012);
        imageUrls.add(R.drawable.img013);
        imageUrls.add(R.drawable.img014);
        imageUrls.add(R.drawable.img015);
        imageUrls.add(R.drawable.img016);
        imageUrls.add(R.drawable.img017);
        imageUrls.add(R.drawable.img018);
        imageUrls.add(R.drawable.img019);
        imageUrls.add(R.drawable.img020);
        imageUrls.add(R.drawable.img021);
        imageUrls.add(R.drawable.img022);
        imageUrls.add(R.drawable.img023);
        imageUrls.add(R.drawable.img024);
        imageUrls.add(R.drawable.img025);
        imageUrls.add(R.drawable.img026);
        imageUrls.add(R.drawable.img027);
        imageUrls.add(R.drawable.img028);
        imageUrls.add(R.drawable.img029);
        imageUrls.add(R.drawable.img030);
        imageUrls.add(R.drawable.img031);
        imageUrls.add(R.drawable.img032);
        imageUrls.add(R.drawable.img033);
        imageUrls.add(R.drawable.img034);
        imageUrls.add(R.drawable.img035);
        imageUrls.add(R.drawable.img036);
        imageUrls.add(R.drawable.img037);
        imageUrls.add(R.drawable.img038);
        imageUrls.add(R.drawable.img039);
        imageUrls.add(R.drawable.img040);
        imageUrls.add(R.drawable.img041);
        imageUrls.add(R.drawable.img042);
        imageUrls.add(R.drawable.img043);
        imageUrls.add(R.drawable.img044);
        return imageUrls;
    }

    /*
     設置toolbar顯示的image*/
    private void setToolbarImage(){
        Random random = new Random();
        int number = random.nextInt(imageUrl.size() - 1);
        Glide.with(MainActivity.this).load(imageUrl.get(number)).into(mainactivityToolbarImageview);
        /*new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document document1 = Jsoup.connect("http://www.mengtu.me/").timeout(3000).post();
                    String url = document1.select("div.flow-thumb").get(0).select("a").get(0).attr("href");
                    Log.d("url",url);
                    Document document2 = Jsoup.connect(url).timeout(3000).post();
                    imgurl = "http://www.mengtu.me" + document2.select("div.show_pic").get(0).select("img").get(0).attr("src");
                    Log.d("imgurl",imgurl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(imgurl).into(mainactivityToolbarImageview);
                    }
                });
            }
        }.start();*/

    }
}
