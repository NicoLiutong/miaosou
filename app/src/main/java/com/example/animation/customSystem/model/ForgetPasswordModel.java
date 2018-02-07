package com.example.animation.customSystem.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 刘通 on 2018/1/23.
 */

public class ForgetPasswordModel {
    private ForgetPasswordCallback callback;
    public ForgetPasswordModel(ForgetPasswordCallback callback){
        this.callback = callback;
    }

    public void resetPasswordByEmail(final String email){
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    callback.onSuccess(email);
                }else {
                    callback.onFail(e);
                }
            }
        });
    }

    public interface ForgetPasswordCallback{
        void onSuccess(String email);
        void onFail(BmobException e);
    }
}
