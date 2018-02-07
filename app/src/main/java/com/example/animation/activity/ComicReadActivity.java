package com.example.animation.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.adapter.DepthPageTransformer;
import com.example.animation.adapter.MyPageAdapter;
import com.example.animation.db.ComicViewPager;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.fragments.ComicReadFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComicReadActivity extends AppCompatActivity implements View.OnClickListener,ComicReadFragment.InvisibleBar {

    private String comicUrl;
    private String comicUrlGet;

    private String comicReadPage;

    private String pictureId;

    private SharedPreferences preferences;
    private int currentReadPage = 1;
    private Button comicSelectPage;

    private TextView tvComicReadPage;
    private TextView tvComicUrlLines;

    private ProgressBar progressBar;
    private LinearLayout llComicReadBar;

    private List<ComicViewPager> comicViewPagerList = new ArrayList<>();
    private List<String> comicImageUrl = new ArrayList<>();

    private ViewPager viewPager;

    private TextView comicPagesText;

    private MyPageAdapter pageAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_read);
        Intent intent = getIntent();
        comicUrlGet = intent.getStringExtra(ComicFragment.COMICREADURL);
        comicUrl = getComicUrl(comicUrlGet);
        //pictureId = comicUrl.substring(comicUrl.lastIndexOf("/") + 1,comicUrl.lastIndexOf("."));
        //.replace(".html","_2.html");
        //Log.d("url",comicUrl);
        comicSelectPage = (Button) findViewById(R.id.comic_selectPage);
        comicSelectPage.setOnClickListener(this);
        tvComicReadPage = (TextView) findViewById(R.id.comic_readPage);
        tvComicUrlLines = (TextView) findViewById(R.id.comic_change_lines);
        tvComicUrlLines.setOnClickListener(this);
        setTVComicUrlLines(tvComicUrlLines);
        progressBar = (ProgressBar) findViewById(R.id.pb_comic_progress);
        llComicReadBar = (LinearLayout) findViewById(R.id.comic_read_llbar);
        comicPagesText = (TextView) findViewById(R.id.read_comic_pages);
        viewPager = (ViewPager) findViewById(R.id.comic_read_pager);
        //viewPager.setOnLongClickListener(this);
        viewPager.setOffscreenPageLimit(1);
        pageAdapter = new MyPageAdapter(this.getSupportFragmentManager(), comicViewPagerList);
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    llComicReadBar.setVisibility(View.GONE);
                }
            }
        };
        handler.sendEmptyMessageDelayed(1, 3000);
        queryComicUrl();
    }

    private void queryComicUrl() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document comicDocument = Jsoup.connect(comicUrl).timeout(3000).post();
                    Elements comicUrls = comicDocument.select("a.thumb");
                    for (int i = 0; i < comicUrls.size(); i++) {
                        String comicUrl;
                        if (i != comicUrls.size() - 1) {
                            comicUrl = comicUrls.get(i).attr("href");
                        } else {
                            comicUrl = "http:" + comicUrls.get(i).attr("href");
                        }
                        comicImageUrl.add(comicUrl);
                        //Log.d("comurl",comicUrl);
                    }
                    comicReadPage = comicUrls.get(0).text().split(" ")[0];
                    //Log.d("compage",comicReadPage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        setView();
                    }
                });
            }
        }.start();
    }

    private void setView() {
        tvComicReadPage.setText("第" + comicReadPage);
        comicPagesText.setText(currentReadPage + "/" + comicImageUrl.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentReadPage = position + 1;
                comicPagesText.setText(currentReadPage + "/" + comicImageUrl.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pictureId = comicUrl.substring(comicUrl.lastIndexOf("/") + 1, comicUrl.lastIndexOf("."));
        for (int i = 0; i < comicImageUrl.size(); i++) {
            ComicViewPager comicViewPager = new ComicViewPager();
            comicViewPager.setComicUrl(comicImageUrl.get(i));
            comicViewPager.setCurrentPage(i+1);
            comicViewPager.setPictureId(pictureId + "-" + comicReadPage + "-" + (i + 1));
            comicViewPager.setType(ComicReadFragment.COMIC);
            comicViewPagerList.add(comicViewPager);
        }
        pageAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(currentReadPage - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comic_selectPage:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择页数");
                builder.setCancelable(true);
                final String[] strings = new String[comicImageUrl.size()];
                for (int i = 0; i < comicImageUrl.size(); i++) {
                    strings[i] = String.valueOf(i + 1);
                }
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewPager.setCurrentItem(which);
                    }
                });
                builder.show();
                break;
            case R.id.comic_change_lines:
                AlertDialog.Builder comicLines = new AlertDialog.Builder(this);
                comicLines.setTitle("选择线路");
                final String[] lines = new String[]{"线路1（全球）","线路2（亚洲）","线路3（压缩）","线路4（省流）"};
                comicLines.setItems(lines, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setComicLines(which+1);
                    }
                });
                AlertDialog dialog = comicLines.show();
                dialog.setCancelable(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        comicViewPagerList.clear();
        pageAdapter.notifyDataSetChanged();
    }

    @Override
    public void setInvisibleBar() {
        //Log.d("2", "2");
        llComicReadBar.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(1, 3000);
    }

    private String getComicUrl(String comicUrl){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (preferences.getInt("comicUrlLine",1)){
            case 1:return comicUrl;
            case 2:comicUrl = comicUrl.replace(".html","_2.html");
                return comicUrl;
            case 3:comicUrl = comicUrl.replace(".html","_3.html");
                return comicUrl;
            case 4:comicUrl = comicUrl.replace(".html","_4.html");
                return comicUrl;
        }
        return null;
    }

    private void setTVComicUrlLines(TextView textView){
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (preferences.getInt("comicUrlLine",1)){
            case 1:
                textView.setText("线路1");
                break;
            case 2:
                textView.setText("线路2");
                break;
            case 3:
                textView.setText("线路3");
                break;
            case 4:
                textView.setText("线路4");
                break;
        }
    }

    private void setComicLines(int i){
        comicViewPagerList.clear();
        comicImageUrl.clear();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        switch (i){
            case 1:
                editor.putInt("comicUrlLine",1);
                editor.commit();
                break;
            case 2:
                editor.putInt("comicUrlLine",2);
                editor.commit();
                break;
            case 3:
                editor.putInt("comicUrlLine",3);
                editor.commit();
                break;
            case 4:
                editor.putInt("comicUrlLine",4);
                editor.commit();
                break;
        }
        comicUrl = getComicUrl(comicUrlGet);
        Log.d("comicurl",comicUrl);
        setTVComicUrlLines(tvComicUrlLines);
        queryComicUrl();
    }
}
