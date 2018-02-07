package com.example.animation.customSystem.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.animation.customSystem.presenter.BasePresenter;

/**
 * Created by 刘通 on 2018/1/23.
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {

    public T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        presenter.attach((V)this);
    }

    @Override
    protected void onDestroy() {
        presenter.dettach();
        super.onDestroy();
    }

    // 实例化presenter
    public abstract T initPresenter();
}
