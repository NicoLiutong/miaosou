package com.example.animation.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.example.animation.R;
import com.example.animation.Util.ACache;
import com.example.animation.Util.LoadImageAsync;
import com.example.animation.view.RoundProgressBarWithNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 刘通 on 2017/11/1.
 */

public class ComicReadFragment extends Fragment implements LoadImageAsync.OnLoadImageListener,
        View.OnClickListener,View.OnLongClickListener,DialogInterface.OnClickListener{

    private static final String IMAGEURL = "imageurl";
    private static final String CURRENTPAGE = "currentpage";
    private static final String PICTUREID = "pictureid";
    private static final String TYPE = "type";
    public static final String COMIC = "comic";
    public static final String PICTURE = "picture";
    private LoadImageAsync loadImageAsync;
    private String imageUrl;
    private int currentPage;
    private String pictureId;
    private Bitmap pictureBitmap;
    private ACache mCache;
    private String type;  //0代表漫画；1代表图片
    private PhotoView comicPhotoView;
    private LinearLayout llLoadingProgress;
    private LinearLayout llLoadingFailed;
    private TextView tvLoadingCurrentPage;
    private Button btLoadingFailed;
    private RoundProgressBarWithNumber pbLoadingProgress;

    public static final ComicReadFragment newInstance(String imageUrl,Integer currentPage,String pictureId,String type){
        ComicReadFragment comicReadFragment = new ComicReadFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(IMAGEURL,imageUrl);
        bundle.putInt(CURRENTPAGE,currentPage);
        bundle.putString(TYPE,type);
        bundle.putString(PICTUREID,pictureId);
        comicReadFragment.setArguments(bundle);
        return comicReadFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString(IMAGEURL);
        currentPage = getArguments().getInt(CURRENTPAGE);
        pictureId = getArguments().getString(PICTUREID);
        //Log.d("pictureid",pictureId);
        type = getArguments().getString(TYPE);
        mCache = ACache.get(getContext());
        pictureBitmap = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_photo_view,container,false);
        comicPhotoView = (PhotoView) view.findViewById(R.id.pv_comic_read);
        llLoadingProgress = (LinearLayout) view.findViewById(R.id.ll_loading_progress);
        llLoadingFailed = (LinearLayout) view.findViewById(R.id.ll_loading_failed);
        btLoadingFailed = (Button) view.findViewById(R.id.bt_loading_fail);
        tvLoadingCurrentPage = (TextView) view.findViewById(R.id.tv_loading_currentpage);
        pbLoadingProgress = (RoundProgressBarWithNumber) view.findViewById(R.id.pb_loading_progress);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        comicPhotoView.enable();
        btLoadingFailed.setOnClickListener(this);
        if(type.equals(PICTURE)){
            comicPhotoView.setOnLongClickListener(this);
        }
        loadingImage();
    }

    private void loadingImage(){
        pictureBitmap = mCache.getAsBitmap(pictureId);
        if(pictureBitmap == null){
        loadImageAsync = new LoadImageAsync(this);
        loadImageAsync.execute(imageUrl);
        }else {
            llLoadingProgress.setVisibility(View.INVISIBLE);
            comicPhotoView.setVisibility(View.VISIBLE);
            comicPhotoView.setImageBitmap(pictureBitmap);
        }
    }

    @Override
    public void onStartLoading() {
        llLoadingProgress.setVisibility(View.VISIBLE);
        if(type.equals(PICTURE)){
            tvLoadingCurrentPage.setVisibility(View.INVISIBLE);
        }else {
        tvLoadingCurrentPage.setVisibility(View.VISIBLE);
        }
        comicPhotoView.setVisibility(View.INVISIBLE);
        llLoadingFailed.setVisibility(View.INVISIBLE);
        tvLoadingCurrentPage.setText(currentPage + "");
        pbLoadingProgress.setProgress(0);
    }

    @Override
    public void onProgressUpdate(Integer progress) {
        pbLoadingProgress.setProgress(progress);
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        mCache.put(pictureId,bitmap,7*ACache.TIME_DAY);
        pictureBitmap = bitmap;
        llLoadingProgress.setVisibility(View.INVISIBLE);
        comicPhotoView.setVisibility(View.VISIBLE);
        comicPhotoView.setImageBitmap(bitmap);
    }

    @Override
    public void onFailed() {
        llLoadingFailed.setVisibility(View.VISIBLE);
        llLoadingProgress.setVisibility(View.INVISIBLE);
        comicPhotoView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_loading_fail:
                loadingImage();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.pv_comic_read:
                if(type == PICTURE){
                    final AlertDialog.Builder savePicutreBuilder = new AlertDialog.Builder(getContext(),R.style.MyDialog);
                    savePicutreBuilder.setCancelable(true);
                    final String[] string = {"保存图片到本地"};
                    savePicutreBuilder.setItems(string, this);
                    savePicutreBuilder.show();
                }
        }
        return false;
    }

    private boolean savePicture(Bitmap bitmap){
        boolean flag = false;
        String filepath = null;
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            filepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "picture" + File.separator;
            File dirFile = new File(filepath);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            File file = new File(dirFile,pictureId + ".jpg");
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                flag = true;
                outputStream.flush();
                outputStream.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }else {
            Toast.makeText(getActivity(),"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
        }

    return flag;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        boolean saveSuccess = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(ContextCompat.checkSelfPermission(getActivity(), permissions[0]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),permissions,0);
            }else {
                saveSuccess = savePicture(pictureBitmap);
            }
        }else {
            saveSuccess = savePicture(pictureBitmap);
        }
        if(saveSuccess){
        Toast.makeText(getActivity(),"图片已存储在 /miaosou/picture 文件夹下",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"图片保存失败，请检查SD卡读写权限是否开启",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 0:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"SD卡读写权限已开启",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(),"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
