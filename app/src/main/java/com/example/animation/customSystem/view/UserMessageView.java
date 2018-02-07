package com.example.animation.customSystem.view;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/29.
 */

public interface UserMessageView {
    void onStartChangeMessage();
    void onUserNameChangeSuccess(String name);
    void onUserSexChangeSuccess(String sex);
    void onUserBirthdayChangeSuccess(String birthday);
    void onUserChangePassword();
    void onFailChangeMossage(BmobException e);
    void onFailChangeMessage(String s);
    void onEndChangeMessgae();
}
