package com.example.animation.customSystem.presenter;

import com.example.animation.customSystem.model.UserMessageModel;
import com.example.animation.customSystem.view.UserMessageView;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by 刘通 on 2018/1/29.
 */

public class UserMessagePresent extends BasePresenter<UserMessageView> implements UserMessageModel.UserMessageCallBack {
    private UserMessageModel userMessageModel = new UserMessageModel(this);

    public void changeUserName(String name){
        mView.onStartChangeMessage();
        if(name==null) {
            mView.onFailChangeMessage("昵称不能为空");
            mView.onEndChangeMessgae();
        }else {
            userMessageModel.changeUserName(name);
        }
    }

    public void changeUserSex(String sex){
        mView.onStartChangeMessage();
        userMessageModel.changeUserSex(sex);
    }

    public void changeUserBirthday(String birthday){
        mView.onStartChangeMessage();
        userMessageModel.changeUserBirthday(birthday);
    }

    public void changeUserPassword(String oldPassword,String newPassword){
        mView.onStartChangeMessage();
        if(newPassword==null) {
            mView.onFailChangeMessage("密码不能为空");
            mView.onEndChangeMessgae();
        }else if(newPassword.equals(oldPassword)){
            mView.onFailChangeMessage("新密码与旧密码相同");
            mView.onEndChangeMessgae();
        }else {
            userMessageModel.changeUserPassword(oldPassword,newPassword);
        }
    }

    @Override
    public void onNameSuccess(String name) {
        mView.onEndChangeMessgae();
        mView.onUserNameChangeSuccess(name);
    }

    @Override
    public void onSexSuccess(String sex) {
        mView.onEndChangeMessgae();
        mView.onUserSexChangeSuccess(sex);
    }

    @Override
    public void onBirthdaySuccess(String birthday) {
        mView.onEndChangeMessgae();
        mView.onUserBirthdayChangeSuccess(birthday);
    }

    @Override
    public void onPasswordSuccess() {
        mView.onEndChangeMessgae();
        mView.onUserChangePassword();
    }

    @Override
    public void onFail(BmobException e) {
        mView.onEndChangeMessgae();
        mView.onFailChangeMossage(e);
    }
}
