package com.example.animation.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.animation.R;
import com.example.animation.adapter.DownloadPageAdapter;
import com.example.animation.db.PictureSaveManager;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2018/3/15.
 */

public class DownloadManagerDialogFragment extends DialogFragment {
    private String showPath;
    private int type;
    private int id;
    private List<String> urls = new ArrayList<>();
    private ViewPager viewPager;
    private DownloadPageAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        showPath = bundle.getString("showPath");
        type = bundle.getInt("type");
        setList();
    }

    private void setList() {
        int i = 0;
        if (type == 1) {
            List<PictureSaveManager> list = DataSupport.where("type=?", "1").find(PictureSaveManager.class);
            for (PictureSaveManager item : list) {
                File file = new File(item.getFilePath());
                if (file.exists()) {
                    urls.add(item.getFilePath());
                    if (showPath.equals(item.getFilePath()))
                        id = i;
                    i++;
                }
            }
        } else if (type == 2) {
            List<PictureSaveManager> list = DataSupport.where("type=?", "2").find(PictureSaveManager.class);
            for (PictureSaveManager item : list) {
                File file = new File(item.getFilePath());
                if (file.exists()) {
                    urls.add(item.getFilePath());
                    if (showPath.equals(item.getFilePath()))
                        id = i;
                    i++;
                }
            }
        }else if(type == 3){
            File file = new File(showPath);
            if(file.exists()){
               File[] files = file.listFiles();
                if(files!=null){
                    for(int j = 0;j<files.length;j++){
                        urls.add(files[j].getAbsolutePath());
                    }
                }
            }
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.download_manager_show_picture,null,false);
        viewPager =(ViewPager) view.findViewById(R.id.download_manager_viewpager);
        adapter = new DownloadPageAdapter(urls,getActivity());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(id);
        builder.setView(view);
        Dialog dialog = builder.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;

    }
}
