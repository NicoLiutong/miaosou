package com.example.animation.customSystem.presenter;

import com.example.animation.customSystem.model.LoginModel;
import com.example.animation.customSystem.view.LoginView;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/23.
 */

public class LoginPresent extends BasePresenter<LoginView> implements LoginModel.LoginCallBack {
    private LoginModel loginModel = new LoginModel(this);

    public void login(String username,String password){
        mView.startLogin();
        loginModel.login(username,password);

    }

    @Override
    public void onSuccess() {
        mView.endLogin();
        mView.loginSuccess();
    }

    @Override
    public void onFail(BmobException e) {
        mView.endLogin();
        mView.loginFail(e);
    }
}
