package com.example.animation.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.animation.adapter.DepthPageTransformer;
import com.example.animation.adapter.MyPageAdapter;
import com.example.animation.fragments.ComicFragment;
import com.example.animation.R;

import java.util.ArrayList;
import java.util.List;

public class ComicReadActivity extends AppCompatActivity {

    private String comicUrl;

    private String comicPage;

    private String comicAllPages;

    private Button comicReadBack;

    private TextView comicReadPage;

    private List<String> comicImageUrl = new ArrayList<>();

    private List<ImageView> imageViews = new ArrayList<>();

    private ViewPager viewPager;

    private TextView comicPagesText;

    private MyPageAdapter pageAdapter;

    private String comicReadFirst;

    private String comicReadSecond;

    private String comicReadPages;

    private int currentMaxPagePosition = 0;

    private int comicReadThird = 1;

    private static String frontUrl = "http://tt.fuli.in/c86ns736r62";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_read);
        Intent intent = getIntent();
        comicUrl = intent.getStringExtra(ComicFragment.COMICREADURL);
        comicPage = intent.getStringExtra(ComicFragment.COMICREADPAGE);
        comicAllPages = intent.getStringExtra(ComicFragment.COMICREADEPISODES);
        comicReadBack = (Button) findViewById(R.id.comic_readBack);
        comicReadPage = (TextView) findViewById(R.id.comic_readPage);
        comicReadPage.setText("第" + comicPage);
        viewPager = (ViewPager) findViewById(R.id.comic_read_pager);
        comicPagesText = (TextView) findViewById(R.id.read_comic_pages);

        comicReadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        pageAdapter = new MyPageAdapter(imageViews);
        viewPager.setAdapter(pageAdapter);
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        comicPagesText.setText((viewPager.getCurrentItem() + 1) + "/" + comicImageUrl.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                comicPagesText.setText((position + 1) + "/" + comicImageUrl.size());

                if(currentMaxPagePosition + 1 == comicImageUrl.size()){
                    currentMaxPagePosition = comicImageUrl.size() - 1;
                }else if(position == currentMaxPagePosition){
                    currentMaxPagePosition = currentMaxPagePosition + 1;
                    PhotoView imageView = new PhotoView(getApplicationContext());
                    //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //Glide.with(getApplicationContext()).load(comicImageUrl.get(position + 1)).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(imageView);
                    //imageViews.add(imageView);
                    loadImage(comicImageUrl.get(position + 1),imageView);

                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData(){
        comicImageUrl.clear();
        comicReadFirst = comicUrl.split("/")[4].split("\\.")[0];
        comicReadSecond = comicPage.split("卷")[0].split("话")[0];
        comicReadPages = comicAllPages.split("P")[0];//.split("\\ ")[1];
        //Log.d("first", comicReadFirst);
        //Log.d("second", comicReadSecond);
        //Log.d("third", comicReadPages);
        //comicReadThird = Integer.getInteger(comicReadPages.);
        //Log.d("four",comicReadThird + " ");
        while (true) {
            if (String.valueOf(comicReadThird).equals(comicReadPages)) {
                if (comicReadThird < 10) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                } else if (comicReadThird < 100) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                } else {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                }
                break;
            } else {
                if (comicReadThird < 10) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "00" + comicReadThird + ".jpg");
                } else if (comicReadThird < 100) {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + "0" + comicReadThird + ".jpg");
                }else {
                    comicImageUrl.add(frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                    //Log.d("url", frontUrl + "/" + comicReadFirst + "/" + comicReadSecond + "/" + comicReadThird + ".jpg");
                }
                comicReadThird ++;
            }
        }

        /*for (int i = 0; i < comicImageUrl.size(); i ++){
            Log.d("pagesurl",comicImageUrl.get(i));
        }*/

        /*for (int i = 0; i < comicImageUrl.size(); i ++){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getApplicationContext()).load(comicImageUrl.get(i)).into(imageView);
            imageViews.add(imageView);
        }*/
        if(comicImageUrl.size() >= 2){
            currentMaxPagePosition = 1;
            PhotoView imageView1 = new PhotoView(getApplicationContext());
            //imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //Glide.with(getApplicationContext()).load(comicImageUrl.get(0)).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(imageView1);
            //imageViews.add(imageView1);
            loadImage(comicImageUrl.get(0),imageView1);

            PhotoView imageView2 = new PhotoView(getApplicationContext());
            //imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //Glide.with(getApplicationContext()).load(comicImageUrl.get(1)).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(imageView2);
            //imageViews.add(imageView2);
            loadImage(comicImageUrl.get(1),imageView2);


        }else if(comicImageUrl.size() == 1){
            currentMaxPagePosition = 0;
            PhotoView imageView1 = new PhotoView(getApplicationContext());
            //imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //Glide.with(getApplicationContext()).load(comicImageUrl.get(0)).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(imageView1);
            //imageViews.add(imageView1);
            loadImage(comicImageUrl.get(0),imageView1);
        }
    }

    private void loadImage(final String comicImageUrl, final PhotoView imageView){
        //Glide.with(getApplicationContext()).load(comicImageUrl).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(imageView);
        Glide.with(getApplicationContext()).load(comicImageUrl).placeholder(R.drawable.loading).error(R.drawable.picture_loaderror).crossFade().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.disenable();
                imageView.setImageDrawable(placeholder);
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.enable();
                imageView.setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.disenable();
                imageView.setImageDrawable(errorDrawable);
            }
        });
        imageViews.add(imageView);

    }
}
