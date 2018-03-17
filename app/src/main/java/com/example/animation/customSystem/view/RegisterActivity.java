package com.example.animation.customSystem.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.customSystem.presenter.RegisterPresent;
import com.example.animation.view.LineEditText;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import cn.bmob.v3.exception.BmobException;

public class RegisterActivity extends BaseActivity<RegisterView,RegisterPresent> implements RegisterView,View.OnClickListener{

    private TextView txTitle;
    private LineEditText txUserName;
    private LineEditText txEmail;
    private LineEditText txPassword;
    private LineEditText txConfirmPassword;
    private Button btRegister;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txTitle = (TextView) findViewById(R.id.basic_texttitle);
        txTitle.setText("注册");
        txUserName = (LineEditText) findViewById(R.id.register_uesrname);
        txEmail = (LineEditText) findViewById(R.id.register_email);
        txPassword = (LineEditText) findViewById(R.id.register_password);
        txConfirmPassword = (LineEditText) findViewById(R.id.register_confirm_password);
        btRegister = (Button) findViewById(R.id.register_button);
        btRegister.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this,"注册界面");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    @Override
    public RegisterPresent initPresenter() {
        return new RegisterPresent();
    }

    @Override
    public void onRegisterStart() {
       dialog = ProgressDialog.show(RegisterActivity.this, null, "正在注册，请稍后…", true, false);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this,"注册成功,请去邮箱验证",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFail(BmobException e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterEnd() {
        dialog.dismiss();
    }

    @Override
    public void passwordNotMatch(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_button:
                presenter.register(this,txEmail.getText().toString(),txUserName.getText().toString(),txPassword.getText().toString(),txConfirmPassword.getText().toString());
                break;
        }
    }
}
