package com.example.animation.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.Util.ACache;
import com.example.animation.Util.ImageLoader;
import com.example.animation.Util.SavePicture;
import com.example.animation.adapter.CosplayImageShowViewPagerAdapter;
import com.example.animation.db.CosplayImageMessage;
import com.example.animation.db.PictureSaveManager;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

public class CosplayImageShowActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener,
        CosplayImageShowViewPagerAdapter.OnLongClick,DialogInterface.OnClickListener{
    private List<CosplayImageMessage> messages;
    private ACache mAcache;

    private ViewPager viewPager;
    private TextView tvShowPager;
    private CosplayImageShowViewPagerAdapter adapter;
    private String imageId;
    private int currentPosition = 0;

    private String savePictureId;
    private Bitmap savePictureBitmap;
    private String savePicturePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosplay_image_show);
        viewPager =(ViewPager) findViewById(R.id.cosplay_image_show_vp);
        tvShowPager = (TextView) findViewById(R.id.cosplay_image_show_tv);
        messages = ImageLoader.getCosplayImageMessages();
        mAcache = ACache.get(new File(this.getCacheDir().getPath()+ File.separator+"cosplay"),1024*1024*100,100);
        Intent intent = getIntent();
        imageId = intent.getStringExtra("imageId");
        currentPosition = calculatePage(imageId);
        setImageShow();
    }

    public int calculatePage(String imageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getImageId().equals(imageId)) {
                return i;
            }
        }
        return -1;
    }

    private void setImageShow(){
        adapter = new CosplayImageShowViewPagerAdapter(messages,mAcache,this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition);
        viewPager.addOnPageChangeListener(this);
        tvShowPager.setText(currentPosition + 1 + "/" + messages.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvShowPager.setText(position + 1 + "/" + messages.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messages = null;
        savePictureBitmap = null;
    }

    @Override
    public void OnLongClickLinstener(String imageId,Bitmap bitmap) {
        savePictureId = imageId;
        AlertDialog.Builder savePicutreBuilder = new AlertDialog.Builder(this,R.style.MyDialog);
        savePicutreBuilder.setCancelable(true);
        String[] string = {"保存图片到本地","设为壁纸"};
        savePicutreBuilder.setItems(string, this);
        savePicutreBuilder.show();
        savePictureBitmap = bitmap;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
                boolean saveSuccess = false;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if(ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,permissions,0);
                    }else {
                        saveSuccess = savePicture(savePictureBitmap);
                    }
                }else {
                    saveSuccess = savePicture(savePictureBitmap);
                }
                if(saveSuccess){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    if(DataSupport.where("filePath = ?",savePicturePath).find(PictureSaveManager.class).isEmpty()) {
                        PictureSaveManager saveManager = new PictureSaveManager(savePicturePath, 2);
                        saveManager.save();
                    }
                    Toast.makeText(this,"图片已存储在"+preferences.getString("cosplayPictureFilepath","sdcard/miaosou/cosplay"),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"图片保存失败，请检查SD卡读写权限是否开启",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                try {
                    wallpaperManager.setBitmap(savePictureBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private boolean savePicture(Bitmap bitmap){
        boolean flag = false;
        String filepath = null;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            filepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "cosplay" + File.separator;
            savePicturePath = filepath + savePictureId + ".jpg";
            flag = SavePicture.savePicture(preferences.getString("cosplayPictureFilepath",filepath),bitmap,savePictureId,this);
        }else {
            Toast.makeText(this,"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"SD卡读写权限已开启",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this,"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}

