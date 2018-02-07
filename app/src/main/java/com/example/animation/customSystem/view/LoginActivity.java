package com.example.animation.customSystem.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.activity.MainActivity;
import com.example.animation.customSystem.bease.User;
import com.example.animation.customSystem.presenter.LoginPresent;
import com.example.animation.view.LineEditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class LoginActivity extends BaseActivity<LoginView,LoginPresent> implements LoginView,View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private LineEditText txUserName;
    private LineEditText txPassword;
    private CheckBox cbSeePassword;
    private TextView tvForgetPassowrd;
    private Button btLogin;
    private TextView tvRegister;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,"df1a802c660365ec34c26730bc5b641d");
        if(BmobUser.getCurrentUser(User.class)!=null){
            loginSuccess();
        }
        txUserName =(LineEditText) findViewById(R.id.login_username);
        txPassword = (LineEditText) findViewById(R.id.login_passowrd);
        txPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cbSeePassword = (CheckBox) findViewById(R.id.login_isseepassword);
        cbSeePassword.setOnCheckedChangeListener(this);
        tvForgetPassowrd = (TextView) findViewById(R.id.login_forgetpassword);
        tvForgetPassowrd.setOnClickListener(this);
        btLogin = (Button) findViewById(R.id.login_button);
        btLogin.setOnClickListener(this);
        tvRegister = (TextView) findViewById(R.id.login_register);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public LoginPresent initPresenter() {
        return new LoginPresent();
    }

    @Override
    public void startLogin() {
        dialog = ProgressDialog.show(LoginActivity.this, null, "登陆中…", true, false);
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void loginFail(BmobException e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void endLogin() {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_forgetpassword:
                Intent intent1 = new Intent(this,ForgetPasswordActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.login_register:
                Intent intent2 = new Intent(this,RegisterActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.login_button:
                presenter.login(txUserName.getText().toString(),txPassword.getText().toString());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            txPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else {
            txPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
}
