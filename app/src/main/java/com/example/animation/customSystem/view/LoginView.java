package com.example.animation.customSystem.view;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/23.
 */

public interface LoginView {
    void startLogin();
    void loginSuccess();
    void loginFail(BmobException e);
    void endLogin();
}
