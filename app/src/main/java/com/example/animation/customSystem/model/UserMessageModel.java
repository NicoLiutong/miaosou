package com.example.animation.customSystem.model;

import com.example.animation.customSystem.bease.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 刘通 on 2018/1/29.
 */

public class UserMessageModel {
    private UserMessageCallBack callBack;
    public UserMessageModel(UserMessageCallBack callBack){
        this.callBack = callBack;
    }

    public void changeUserName(final String name){
        User user = new User();
        user.setName(name);
        User currentUser = BmobUser.getCurrentUser(User.class);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callBack.onNameSuccess(name);
                }else {
                    callBack.onFail(e);
                }
            }
        });
    }

    public void changeUserSex(final String sex){
        User user = new User();
        user.setSex(sex);
        User currentUser = BmobUser.getCurrentUser(User.class);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callBack.onSexSuccess(sex);
                }else {
                    callBack.onFail(e);
                }
            }
        });
    }

    public void changeUserBirthday(final String birthday){
        User user = new User();
        user.setBirthday(birthday);
        User currentUser = BmobUser.getCurrentUser(User.class);
        user.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callBack.onBirthdaySuccess(birthday);
                }else {
                    callBack.onFail(e);
                }
            }
        });
    }

    public void changeUserPassword(String oldPassword,String newPassword){
        BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callBack.onPasswordSuccess();
                }else {
                    callBack.onFail(e);
                }
            }
        });
    }


    public interface UserMessageCallBack{
        void onNameSuccess(String name);
        void onSexSuccess(String sex);
        void onBirthdaySuccess(String birthday);
        void onPasswordSuccess();
        void onFail(BmobException e);
    }
}
