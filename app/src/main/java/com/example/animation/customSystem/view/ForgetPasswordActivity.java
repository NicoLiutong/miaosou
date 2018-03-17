package com.example.animation.customSystem.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.customSystem.presenter.ForgetPasswordPresent;
import com.example.animation.view.LineEditText;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import cn.bmob.v3.exception.BmobException;

public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordView,ForgetPasswordPresent> implements ForgetPasswordView,View.OnClickListener {

    private TextView tvTitle;

    private LineEditText txVerifiedEmail;

    private Button btVerified;

    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        tvTitle = (TextView) findViewById(R.id.basic_texttitle);
        tvTitle.setText("重置密码");
        txVerifiedEmail = (LineEditText) findViewById(R.id.forget_password_email);
        btVerified = (Button) findViewById(R.id.forget_password_button);
        btVerified.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this,"忘记密码界面");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    @Override
    public ForgetPasswordPresent initPresenter() {
        return new ForgetPasswordPresent();
    }

    @Override
    public void onResetStart() {
        //设置progress
        dialog = ProgressDialog.show(ForgetPasswordActivity.this, null, "请等待…", true, false);
    }

    @Override
    public void onResetSuccess(String email) {
        Toast.makeText(this,"请求验证邮件成功，请到" + email + "邮箱中进行激活。",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onResetFail(BmobException e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResetEnd() {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_password_button:
                presenter.resetPasswordByEmail(txVerifiedEmail.getText().toString());
        }
    }
}
