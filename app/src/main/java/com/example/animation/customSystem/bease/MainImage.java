package com.example.animation.customSystem.bease;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 刘通 on 2018/2/18.
 */

public class MainImage extends BmobObject {
    private BmobFile file;

    private BmobFile verticalPicture;

    private String animationUrl;

    public String getAnimationUrl() {
        return animationUrl;
    }

    public void setAnimationUrl(String animationUrl) {
        this.animationUrl = animationUrl;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public BmobFile getVerticalPicture() {
        return verticalPicture;
    }

    public void setVerticalPicture(BmobFile verticalPicture) {
        this.verticalPicture = verticalPicture;
    }
}
