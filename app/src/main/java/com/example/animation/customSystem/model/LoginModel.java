package com.example.animation.customSystem.model;

import com.example.animation.customSystem.bease.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 刘通 on 2018/1/23.
 */

public class LoginModel {
    private LoginCallBack callBack;

    public LoginModel(LoginCallBack callBack){
        this.callBack = callBack;
    }
    public void login(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    callBack.onSuccess();
                }else{
                    callBack.onFail(e);
                }
            }
        });
    }

    public interface LoginCallBack{
        void onSuccess();
        void onFail(BmobException e);
    }
}
