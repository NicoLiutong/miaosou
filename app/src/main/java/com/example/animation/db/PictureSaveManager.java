package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2018/3/13.
 */

public class PictureSaveManager extends DataSupport {

    private String filePath;
    private int type;  //type=1为animation图片，type=2为cosplay图片


    public PictureSaveManager(){}

    public PictureSaveManager(String filePath,int type){
        this.filePath = filePath;
        this.type = type;
    }
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
