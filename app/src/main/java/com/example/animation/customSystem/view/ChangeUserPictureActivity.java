package com.example.animation.customSystem.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.Util.SavePicture;
import com.example.animation.customSystem.bease.User;
import com.oginotihiro.cropview.CropView;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ChangeUserPictureActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txTitle;
    private Button btCropFinish;
    private CropView cropView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_picture);
        txTitle = (TextView) findViewById(R.id.basic_texttitle);
        txTitle.setText("修改头像");
        btCropFinish = (Button) findViewById(R.id.crop_picture_finish);
        btCropFinish.setOnClickListener(this);
        cropView = (CropView) findViewById(R.id.crop_view);
        user = BmobUser.getCurrentUser(User.class);
        getPermission();
    }

    private void getPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(ContextCompat.checkSelfPermission(this,permissions[0])!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,2);
            }else {
                cropPicture();
            }
        }else {
            cropPicture();
        }
    }

    private void cropPicture(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    Uri imageUri = data.getData();
                    cropView.of(imageUri).asSquare().initialize(this);
                }else finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 2:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    cropPicture();

                }else {
                    Toast.makeText(this,"请开启读写SD卡权限",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crop_picture_finish:
                //设置progress
                final ProgressDialog dialog = ProgressDialog.show(this, null, "请等待…", true, false);
                final BmobFile deletFail = new BmobFile();
                deletFail.setUrl(user.getUesrImage());
                final String filepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "user" + File.separator;
                final String filename = user.getObjectId();
                Bitmap bitmap = cropView.getOutput();
                if(SavePicture.savePicture(filepath,bitmap,filename,ChangeUserPictureActivity.this)) {
                    final BmobFile bmobFile = new BmobFile(new File(filepath, filename + ".jpg"));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                User updateUser = new User();
                                updateUser.setUesrImage(bmobFile.getUrl());
                                updateUser.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(ChangeUserPictureActivity.this, "头像更新", Toast.LENGTH_SHORT).show();
                                            if (!user.getUesrImage().equals("http://bmob-cdn-16552.b0.upaiyun.com/2018/01/28/8252b4b740101b3980dae801ebbb08a9.png"))
                                                deletFail.delete(new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });
                                            dialog.dismiss();
                                            finish();
                                        } else {
                                            bmobFile.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    dialog.dismiss();
                                                    finish();
                                                }
                                            });
                                            Toast.makeText(ChangeUserPictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(ChangeUserPictureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
                break;
        }
    }
}
