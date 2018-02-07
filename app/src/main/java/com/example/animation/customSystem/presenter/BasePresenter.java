package com.example.animation.customSystem.presenter;

/**
 * Created by 刘通 on 2018/1/23.
 */

public abstract class BasePresenter<T> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void dettach() {
        mView = null;
    }

}
