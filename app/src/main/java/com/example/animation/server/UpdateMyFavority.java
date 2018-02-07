package com.example.animation.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.animation.customSystem.bease.User;
import com.example.animation.db.ComicMessageItem;

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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<ComicMessageItem> favority = new ArrayList<>();
        searchMyFavority(favority);
        if(favority.isEmpty()){
            downloadMyFavority();
        }else {
            uploadMyFavority(favority);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void searchMyFavority(List<ComicMessageItem> favority){
        for (ComicMessageItem item: DataSupport.findAll(ComicMessageItem.class)){
            if(item.isMyFavourity()){
                favority.add(item);
            }
        }
    }

    private void uploadMyFavority(List<ComicMessageItem> favority){
        String s = "";
        for (ComicMessageItem item:favority){
            s = s + item.getReadNow()+"!"+item.getComicImageUrl()+"!"+item.getComicName()+"!"+item.getComicUrl()+"+";
        }
        //Log.d("upload",s);
        User updateUser = new User();
        updateUser.setMyFavority(s);
        updateUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                //if(e==null) Log.d("update","updateSuccess");
                //else Log.d("update","updateFail");
            }
        });
    }

    private void downloadMyFavority(){
        if(user.getMyFavority()==null)return;
        String[] list = user.getMyFavority().split("\\+");
        if(list==null)return;
        for(int i = 0;i<list.length;i++){
            if(list[i]!=null){
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
