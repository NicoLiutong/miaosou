package com.example.animation.customSystem.presenter;

import android.content.Context;

import com.example.animation.customSystem.model.RegisterModel;
import com.example.animation.customSystem.view.RegisterView;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/24.
 */

public class RegisterPresent extends BasePresenter<RegisterView> implements RegisterModel.RegisterCallBack{

    private RegisterModel registerModel = new RegisterModel(this);

    public void register(Context context, String email, String userName, String passowrd, String confirmPassword){
        if(passowrd.length()<4||passowrd.length()>16){
            mView.passwordNotMatch("请输入4-16个字符");
        }else if(!passowrd.equals(confirmPassword)){
            mView.passwordNotMatch("请确认两个密码一致");
        }else {
            mView.onRegisterStart();
            registerModel.register(context,email,userName,passowrd);
        }
    }
    @Override
    public void onSuccess() {
        mView.onRegisterEnd();
        mView.onSuccess();
    }

    @Override
    public void onFail(BmobException e) {
        mView.onRegisterEnd();
        mView.onFail(e);
    }
}
