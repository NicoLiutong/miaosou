package com.example.animation.customSystem.presenter;

import com.example.animation.customSystem.model.ForgetPasswordModel;
import com.example.animation.customSystem.view.ForgetPasswordView;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/23.
 */

public class ForgetPasswordPresent extends BasePresenter<ForgetPasswordView> implements ForgetPasswordModel.ForgetPasswordCallback{
    private ForgetPasswordModel forgetPasswordModel = new ForgetPasswordModel(this);

    public void resetPasswordByEmail(String email){
        mView.onResetStart();
        forgetPasswordModel.resetPasswordByEmail(email);
    }

    @Override
    public void onSuccess(String email) {
        mView.onResetEnd();
        mView.onResetSuccess(email);
    }

    @Override
    public void onFail(BmobException e) {
        mView.onResetEnd();
        mView.onResetFail(e);
    }
}
