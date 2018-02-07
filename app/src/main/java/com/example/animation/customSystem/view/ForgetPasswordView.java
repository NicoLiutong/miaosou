package com.example.animation.customSystem.view;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/23.
 */

public interface ForgetPasswordView {
    void onResetStart();
    void onResetSuccess(String email);
    void onResetFail(BmobException e);
    void onResetEnd();
}
