package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class AnimationItem extends DataSupport{

    private String animationItem;

    private String animationType;

    private String animationInformationUrl;

    private boolean isFavortiy;

    private String downloadUrl;

    private String seeOnlineUrl;

    private String week;

    public AnimationItem (){

    }

    public AnimationItem(String animationItem,String animationType,String animationInformationUrl,Boolean isFavortiy,String downloadUrl,
                         String seeOnlineUrl,String week){
        this.animationItem = animationItem;
        this.animationType = animationType;
        this.animationInformationUrl = animationInformationUrl;
        this.isFavortiy = isFavortiy;
        this.downloadUrl = downloadUrl;
        this.seeOnlineUrl = seeOnlineUrl;
        this.week = week;
    }

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

    public boolean isFavortiy() {
        return isFavortiy;
    }

    public void setFavortiy(boolean favortiy) {
        isFavortiy = favortiy;
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
