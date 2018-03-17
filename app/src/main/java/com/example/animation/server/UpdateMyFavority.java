package com.example.animation.server;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;

import com.example.animation.customSystem.bease.User;
import com.example.animation.db.AnimationItem;
import com.example.animation.db.ComicMessageItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateMyFavority extends Service {
    private User user;
    public UpdateMyFavority() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        user = BmobUser.getCurrentUser(User.class);
        MiStatInterface.recordPageStart(this,"收藏同步service");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<ComicMessageItem> favority = new ArrayList<>();
        searchMyFavority(favority);
        List<AnimationItem> animationItemFavority = new ArrayList<>();
        searchAnimationFavority(animationItemFavority);
        try {
            if(!favority.isEmpty()){
                uploadMyFavority(favority,animationItemFavority);
            }else if(!animationItemFavority.isEmpty()){
                uploadAnimation(animationItemFavority);
            }else {
                downloadMyFavority();
            }
        }catch (Exception e){

        }
        finally {
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MiStatInterface.recordPageEnd();
    }

    private void searchAnimationFavority(List<AnimationItem> favority){
        for(AnimationItem item:DataSupport.findAll(AnimationItem.class)){
            if(item.isFavortiy()){
                favority.add(item);
            }
        }
    }

    private void searchMyFavority(List<ComicMessageItem> favority){
        for (ComicMessageItem item: DataSupport.findAll(ComicMessageItem.class)){
            if(item.isMyFavourity()){
                favority.add(item);
            }
        }
    }

    private void uploadMyFavority(List<ComicMessageItem> favority,List<AnimationItem> animationFavority){
        String comic = "";
        String animation = "";
        Gson gson = new Gson();
        comic = gson.toJson(favority);
        animation = gson.toJson(animationFavority);
        //Log.d("uploadcomic",comic);
        //Log.d("uploadAnimation",animation);
        User updateUser = new User();
        updateUser.setMyFavority(comic);
        updateUser.setAnimationFavority(animation);
        updateUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                //if(e==null) Log.d("update","updateSuccess");
                //else Log.d("update","updateFail");
            }
        });
    }

    private void uploadAnimation(List<AnimationItem> animationFavority){
        String animation = "";
        Gson gson = new Gson();
        animation = gson.toJson(animationFavority);
        //Log.d("uploadAnimation",animation);
        User updateUser = new User();
        updateUser.setAnimationFavority(animation);
        updateUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                //if(e==null) Log.d("update","updateSuccess");
                //else Log.d("update","updateFail");
            }
        });
    }

    private void downloadMyFavority() {
        if (user.getMyFavority() != null && !user.getMyFavority().equals("")) {
            if (user.getMyFavority().startsWith("[")) {
                Gson gson = new Gson();
                List<ComicMessageItem> items = gson.fromJson(user.getMyFavority(),new TypeToken<List<ComicMessageItem>>(){}.getType());
                for(ComicMessageItem item:items){
                    ComicMessageItem save = new ComicMessageItem(item.getComicUrl(),item.isMyFavourity(),item.getReadNow(),item.getComicImageUrl(),item.getComicName());
                    save.save();
                }
            } else {
                //Log.d("1","1");
                String[] list = user.getMyFavority().split("\\+");
                for (int i = 0; i < list.length; i++) {
                    if (list[i] != null) {
                        //Log.d("downloadlist",list[i]);
                        ComicMessageItem item = new ComicMessageItem();
                        item.setReadNow(list[i].split("!")[0]);
                        //Log.d("downloadreadnow",list[i].split("!")[0]);

                        item.setComicImageUrl(list[i].split("!")[1]);
                        //Log.d("downloadimageurl",list[i].split("!")[1]);

                        item.setComicName(list[i].split("!")[2]);
                        //Log.d("downloadcomicname",list[i].split("!")[2]);

                        item.setComicUrl(list[i].split("!")[3]);
                        //Log.d("downloadcomicurl",list[i].split("!")[3]);

                        item.setMyFavourity(true);
                        item.save();
                    }
                }
            }
        }

        if(user.getAnimationFavority()!=null&&user.getAnimationFavority()!=""){
            Gson gson = new Gson();
            List<AnimationItem> items = gson.fromJson(user.getAnimationFavority(),new TypeToken<List<AnimationItem>>(){}.getType());
            List<AnimationItem> animationItemList = DataSupport.findAll(AnimationItem.class);
            for(AnimationItem item:items){
                for(AnimationItem listItem:animationItemList){
                    if(item.getAnimationItem().equals(listItem.getAnimationItem())){
                        ContentValues values = new ContentValues();
                        values.put("isFavortiy",true);
                        DataSupport.updateAll(AnimationItem.class,values,"animationItem=?",item.getAnimationItem());
                        break;
                    }
                }
            }
        }

    }
}
