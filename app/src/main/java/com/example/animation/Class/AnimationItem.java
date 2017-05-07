package com.example.animation.Class;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class AnimationItem extends DataSupport{

    private String animationItem;

    private String animationType;

    private String animationInformationUrl;

    private String downloadUrl;

    private String seeOnlineUrl;

    private String week;

    public String getAnimationInformationUrl() {
        return animationInformationUrl;
    }

    public void setAnimationInformationUrl(String animationInformationUrl) {
        this.animationInformationUrl = animationInformationUrl;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSeeOnlineUrl() {
        return seeOnlineUrl;
    }

    public void setSeeOnlineUrl(String seeOnlineUrl) {
        this.seeOnlineUrl = seeOnlineUrl;
    }

    public String getAnimationItem() {
        return animationItem;
    }

    public void setAnimationItem(String animationItem) {
        this.animationItem = animationItem;
    }

    public String getAnimationType() {
        return animationType;
    }

    public void setAnimationType(String animationType) {
        this.animationType = animationType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}
