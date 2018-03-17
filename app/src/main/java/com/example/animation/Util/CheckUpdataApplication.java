package com.example.animation.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.animation.BuildConfig;
import com.example.animation.customSystem.bease.ApplicationVersion;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by 刘通 on 2018/2/23.
 */

public class CheckUpdataApplication {

    public static void checkUpdata(final Context context, final int type){
        BmobQuery<ApplicationVersion> query = new BmobQuery<>();
        query.getObject("0d99ba836c", new QueryListener<ApplicationVersion>() {
            @Override
            public void done(final ApplicationVersion applicationVersion, BmobException e) {
                if(e==null){
                    if(BuildConfig.VERSION_CODE <applicationVersion.getVersionCode()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(applicationVersion.getVersionName());
                    builder.setMessage(applicationVersion.getUpdateMessage());
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(applicationVersion.getApk().getUrl()));
                            context.startActivity(intent);
                        }
                    });
                    builder.show();
                }else {
                    if(type==1){
                        Toast.makeText(context,"暂无更新",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
