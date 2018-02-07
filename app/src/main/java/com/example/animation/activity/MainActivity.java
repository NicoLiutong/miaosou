package com.example.animation.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.Util.SHA;
import com.example.animation.customSystem.bease.User;
import com.example.animation.customSystem.view.UserMessageActivity;
import com.example.animation.fragments.AnimationFragment;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.pay.AliZhi;
import com.example.animation.pay.Config;
import com.example.animation.pay.MiniPayUtils;
import com.example.animation.server.UpdateMyFavority;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,
        SwipeRefreshLayout.OnRefreshListener,View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    private static final int ANIMATION = 0;  

    private static final int COMIC = 1;

    private DrawerLayout mDrawerLayout;
    private NavigationView nvMenu;
    private ImageView nvHeardImage;
    private CircleImageView nvHeadUserPicture;
    private TextView nvHeadUserName;
    private Handler mHandler;

    private boolean isCloseApp = false;

    private FloatingActionButton floatingActionButton;

    private CollapsingToolbarLayout collapsingToolbar;

    private AppBarLayout appBarLayout;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private CardView animationPageCardview;

    private CardView comicPageCardview;

    private ImageView mainactivityToolbarImageview;

    private int pagesName;

    private SharedPreferences pref;

    private String animationTitle;

    private AnimationFragment animationFragment;
    private ComicFragment comicFragment;

    private List<String> imageUrl;
    private float scrollY = 0;

    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DemoApplication.setMainActivity(this);

        service = new Intent(this, UpdateMyFavority.class);
        this.startService(service);

        try {
            imageUrl = setImageUrl();  //獲取圖片url
        } catch (IOException e) {
            e.printStackTrace();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.main_collapsingToolbar);
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(MainActivity.this,R.color.colorAccent));  //設置toolbar背景色
        setSupportActionBar(toolbar);  //設置ActionBar

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_menu);
        nvMenu = (NavigationView) findViewById(R.id.nv_menu);
        nvHeardImage = (ImageView) nvMenu.getHeaderView(0).findViewById(R.id.nv_heard_image);
        nvHeadUserPicture = (CircleImageView) nvMenu.getHeaderView(0).findViewById(R.id.nv_head_user_picture);
        nvHeadUserName = (TextView) nvMenu.getHeaderView(0).findViewById(R.id.nv_head_user_name);
        nvMenu.setNavigationItemSelectedListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(this);
        animationPageCardview = (CardView) findViewById(R.id.animation_pageCard);
        comicPageCardview = (CardView) findViewById(R.id.comic_pageCard);
        mainactivityToolbarImageview = (ImageView) findViewById(R.id.animation_toobarImage);
        setToolbarImage();  //設置ToolBarImage
        setAnimationTitle();  //設置Title
        setToolBarNavigation();
        setPayAuthor();
        animationFragment = new AnimationFragment();  //設置AnimationFragment
        comicFragment = new ComicFragment();  //設置comicFragment
        animationPageSet(animationFragment);  //初始化為animationFragment

        isInternetOk();  //判斷網絡是否打開
        nvHeadUserPicture.setOnClickListener(this);
        animationPageCardview.setOnClickListener(this);

        comicPageCardview.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(this);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.animation_search);

        floatingActionButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setNvHeardImage();   //设置NavigationView的heard图片
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        isCloseApp = false;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopService(service);
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
    添加圖片
    */
    private List<String> setImageUrl() throws IOException {
        List<String> imageUrls = new ArrayList<>();
        String[] imageUrl =  getAssets().list("picture");
        for (int i = 0;i < imageUrl.length;i ++){
            imageUrls.add("file:///android_asset/picture/" + imageUrl[i]);
        }
        return imageUrls;
    }

    /**
     * 設置toolbar顯示的image
     */

    private void setToolbarImage(){
        Random random = new Random();
        int number = random.nextInt(imageUrl.size() - 1);
        Glide.with(MainActivity.this).load(imageUrl.get(number)).into(mainactivityToolbarImageview);

    }

    private void setToolBarNavigation(){
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setNvHeardImage(){
        Random random = new Random();
        int number = random.nextInt(imageUrl.size() - 1);
        User currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser!=null){
            Glide.with(MainActivity.this).load(currentUser.getUesrImage()).into(nvHeadUserPicture);
            nvHeadUserName.setText(currentUser.getName());
        }else {
            Glide.with(MainActivity.this).load(R.drawable.user_picture).into(nvHeadUserPicture);
            nvHeadUserName.setText("未登录");
        }
        Glide.with(MainActivity.this).load(imageUrl.get(number)).into(nvHeardImage);
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset < scrollY ){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }else if(verticalOffset > scrollY){
            floatingActionButton.setVisibility(View.VISIBLE);
        }else if(verticalOffset == scrollY && verticalOffset == 0){
            floatingActionButton.setVisibility(View.VISIBLE);
        }else {
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        scrollY = verticalOffset;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        if(pagesName == ANIMATION){
            animationFragment.queryAnimation();
            setAnimationTitle();

        }else {
            comicFragment.refreshComic();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.animation_pageCard:
                animationPageSet(animationFragment);
                break;
            case R.id.comic_pageCard:
                comicPageSet(comicFragment);
                break;
            case R.id.animation_search:
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                intent.putExtra("type",0);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.nv_head_user_picture:
                Intent userMessage = new Intent(MainActivity.this, UserMessageActivity.class);
                this.startActivity(userMessage);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()){
            case R.id.animation_news:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this,AnimationNewActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.my_favourity:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(MainActivity.this,MyFavourityActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.my_signin:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this,SignInActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.pay_author:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                MiniPayUtils.setupPay(this,new Config.Builder("FKX01294KSKKFN2F9ESS47",R.drawable.ic_zhufubao_pay,R.drawable.ic_weixin_pay).build());
                break;
            case R.id.about_author:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this,AboutActivity.class);
                this.startActivity(intent);
                break;
            case R.id.about_exit:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.animation_picture:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this,AnimationPicture.class);
                this.startActivity(intent);
                break;
            case R.id.cosplay_picture:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this,CosplayPicture.class);
                this.startActivity(intent);
                break;
            case R.id.chatting:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                //intent = new Intent(this,BugReportActivity.class);
                //this.startActivity(intent);
                openChatting();
                break;
        }
        return true;
    }

    private void closeApp(){
        if(isCloseApp){
            finish();
        }else {
            isCloseApp = true;
            Toast.makeText(this,"再次点击关闭喵搜",Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(1,3000);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }else {
                closeApp();
                return true;
            }
        }
        return false;
    }

    private void setPayAuthor(){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        String keyCode = preferences.getString("payKey","1.0.0");
        if(!keyCode.equals("1.27.0")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.main_dialog,null,false);
            Button paybt = (Button) view.findViewById(R.id.dialog_pay);
            final Button weixinbt = (Button) view.findViewById(R.id.dialog_weixin);
            Button qqbt = (Button) view.findViewById(R.id.dialog_qq);
            Button sharebt = (Button) view.findViewById(R.id.dialog_share);
            Button closebt = (Button) view.findViewById(R.id.dialog_close);
            builder.setView(view);
            builder.setCancelable(false);
            final Dialog dialog = builder.show();
            paybt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("payKey","1.27.0");
                    editor.commit();
                    AliZhi.startAlipayClient(MainActivity.this,"FKX01294KSKKFN2F9ESS47");
                    dialog.dismiss();
                }
            });
            weixinbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("payKey","1.27.0");
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,WeiXinHao.class);
                    intent.putExtra(WeiXinHao.TYPE,WeiXinHao.WEIXIN);
                    MainActivity.this.startActivity(intent);
                    dialog.dismiss();
                }
            });
            qqbt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("payKey","1.27.0");
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,WeiXinHao.class);
                    intent.putExtra(WeiXinHao.TYPE,WeiXinHao.QQQUN);
                    MainActivity.this.startActivity(intent);
                    dialog.dismiss();
                }
            });
            sharebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("payKey","1.27.0");
                    editor.commit();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app,"喵搜"));
                    startActivity(Intent.createChooser(intent,getString(R.string.share)));
                    dialog.dismiss();
                }
            });
            closebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("payKey","1.27.0");
                    editor.commit();
                    dialog.dismiss();
                }
            });
        }
    }

    private void openChatting() {
        User user = BmobUser.getCurrentUser(User.class);
        long timestamp = System.currentTimeMillis()/1000;
        String userName = "";
        String imageUrl = user.getUesrImage();
        try {
            userName = URLEncoder.encode(user.getName(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String xlm = "12483_"+user.getObjectId()+"_"+timestamp+"_jWr4HgC3hIFhgWTudvoclclZYwjBvwK4";
        String xlmHash = SHA.shaEncrypt(xlm);
        String url = "https://xianliao.me/website/12483?mobile=1&uid="+user.getObjectId()+"&username="+userName+"&avatar="+imageUrl+"&ts="+timestamp+"&token="+xlmHash;
        Intent intent = new Intent(this,BasicWebActivity.class);
        intent.putExtra(AnimationFragment.ANIMATION_URL,url);
        this.startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
