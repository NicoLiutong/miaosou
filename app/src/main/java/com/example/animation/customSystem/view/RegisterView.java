package com.example.animation.customSystem.view;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/24.
 */

public interface RegisterView {
    void onRegisterStart();
    void onSuccess();
    void onFail(BmobException e);
    void onRegisterEnd();
    void passwordNotMatch(String s);
}
