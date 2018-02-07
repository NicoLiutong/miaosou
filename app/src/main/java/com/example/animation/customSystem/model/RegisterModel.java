package com.example.animation.customSystem.model;

import android.content.Context;

import com.example.animation.customSystem.bease.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 刘通 on 2018/1/24.
 */

public class RegisterModel {

    private RegisterCallBack callBack;

    public RegisterModel(RegisterCallBack callBack){
        this.callBack = callBack;
    }

    public void register(Context context,final String email,final String username,final String password){
                    User user = new User();
                    user.setName(username);
                    user.setUsername(email);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setBirthday("1970-01-01");
                    user.setSex("暂无");
                    user.setMyFavority("");
                    user.setUesrImage("http://bmob-cdn-16552.b0.upaiyun.com/2018/01/28/8252b4b740101b3980dae801ebbb08a9.png");
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                callBack.onSuccess();
                            }else {
                                callBack.onFail(e);
                            }
                        }
                    });
    }

    public interface RegisterCallBack{
        void onSuccess();
        void onFail(BmobException e);
    }
}
