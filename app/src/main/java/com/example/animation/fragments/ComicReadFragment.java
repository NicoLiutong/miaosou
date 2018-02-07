package com.example.animation.fragments;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
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
import com.example.animation.Util.SavePicture;
import com.example.animation.view.RoundProgressBarWithNumber;

import java.io.File;
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
    //public static  final String ANIMATIONPIC = "animationPicture";
    private InvisibleBar invisibleBar;
    private LoadImageAsync loadImageAsync = null;
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("onCeate",currentPage+"-onCeate");
        imageUrl = getArguments().getString(IMAGEURL);
        currentPage = getArguments().getInt(CURRENTPAGE);
        pictureId = getArguments().getString(PICTUREID);
        //Log.d("pictureid",pictureId);
        type = getArguments().getString(TYPE);
        //mCache = ACache.get(getContext());
        mCache = ACache.get(new File(getActivity().getCacheDir().getPath()+ File.separator+"picture"),1024*1024*100,100);
        pictureBitmap = null;
        if(type == COMIC){
            invisibleBar = (InvisibleBar) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Log.d("onCeatedView",currentPage+"-onCeatedView");

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
        //Log.d("onActivityCreated",currentPage+"-onActivityCreated");

        comicPhotoView.enable();
        btLoadingFailed.setOnClickListener(this);
        comicPhotoView.setOnLongClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d("onStart",currentPage+"-onStart");

        loadingImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d("onResume",currentPage+"-onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d("onPause",currentPage+"-onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.d("onStop",currentPage+"-onStop");
        if(loadImageAsync!=null){
            loadImageAsync.cancel(true);
        }
        if(pictureBitmap != null)
        pictureBitmap.recycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.d("onDestroyView",currentPage+"-onDestroyView");
        if(loadImageAsync!=null){
            loadImageAsync.cancel(true);
        }
        if(pictureBitmap != null)
            pictureBitmap.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("onDestroy",currentPage+"-onDestroy");
        if(loadImageAsync!=null){
            loadImageAsync.cancel(true);
        }
        if(pictureBitmap != null)
            pictureBitmap.recycle();

        if(invisibleBar != null)
            invisibleBar = null;
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
                //Log.d("1","1");
                if(type == PICTURE){
                    final AlertDialog.Builder savePicutreBuilder = new AlertDialog.Builder(getContext(),R.style.MyDialog);
                    savePicutreBuilder.setCancelable(true);
                    final String[] string = {"保存图片到本地","设为壁纸"};
                    savePicutreBuilder.setItems(string, this);
                    savePicutreBuilder.show();
                    return true;
                }else {
                    invisibleBar.setInvisibleBar();
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
            flag = SavePicture.savePicture(filepath,bitmap,pictureId,getActivity());
        }else {
            Toast.makeText(getActivity(),"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
        }

    return flag;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
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
                break;
            case 1:
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                try {
                        wallpaperManager.setBitmap(pictureBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
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

    public interface InvisibleBar{
        void setInvisibleBar();
    }
}
