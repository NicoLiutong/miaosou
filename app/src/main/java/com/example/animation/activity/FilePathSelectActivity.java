package com.example.animation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animation.R;
import com.example.animation.adapter.SelectFileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePathSelectActivity extends AppCompatActivity implements SelectFileAdapter.SelectFileOnClick,View.OnClickListener{

    private String firstFilePath;
    private String frountFilePath;
    private String currentFilePath;
    private List<File> fileList = new ArrayList<>();
    private SelectFileAdapter adapter;
    private ImageView ivBackButton;
    private TextView tvCurrentTextView;
    private RecyclerView rvFileList;
    private TextView tvPositive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_path_select);
        ivBackButton = (ImageView) findViewById(R.id.iv_file_select_return);
        ivBackButton.setOnClickListener(this);
        tvCurrentTextView = (TextView) findViewById(R.id.tv_file_select_current_path);
        rvFileList = (RecyclerView) findViewById(R.id.rv_file_select);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFileList.setLayoutManager(manager);
        adapter = new SelectFileAdapter(fileList,this);
        rvFileList.setAdapter(adapter);
        tvPositive = (TextView) findViewById(R.id.tv_file_select_positive);
        tvPositive.setOnClickListener(this);
        currentFilePath = getIntent().getStringExtra("currentFilePath");
        Log.d("currentFilePath",currentFilePath);
        checkWriteAndReadPermission();
    }

    private void checkWriteAndReadPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] s = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, s, 1);
            } else {
                getCurrentFiles(fileList, currentFilePath);
            }
        }else {
            getCurrentFiles(fileList,currentFilePath);
        }

    }

    private void getCurrentFiles(List<File> fileLists,String currentFilePath){
        firstFilePath = Environment.getExternalStorageDirectory().toString();
        fileLists.clear();
        File file = new File(currentFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
        frountFilePath = file.getParent();
        //if(frountFilePath!=null)
        //Log.d("frountFilePath",frountFilePath);
        File[] files = file.listFiles();
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    fileLists.add(files[i]);
                }
            }
            updateShowList();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_file_select_return:
                if(currentFilePath.equals(firstFilePath)){
                    Toast.makeText(this,"暂无更多的文件夹",Toast.LENGTH_SHORT).show();
                }else {
                    currentFilePath = frountFilePath;
                    getCurrentFiles(fileList,currentFilePath);
                }
                break;
            case R.id.tv_file_select_positive:
                Intent intent = new Intent();
                intent.putExtra("filePath",currentFilePath);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public void onClickListener(int position, String absolutePath) {
        currentFilePath = absolutePath;
        getCurrentFiles(fileList,currentFilePath);
    }

    private  void updateShowList(){
        tvCurrentTextView.setText(currentFilePath);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentFiles(fileList,currentFilePath);
            }else {
                Toast.makeText(this,"请开启SD卡读写权限",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
