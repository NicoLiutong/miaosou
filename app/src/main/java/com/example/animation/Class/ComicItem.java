package com.example.animation.Class;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/4/5.
 */

public class ComicItem extends DataSupport {

    private String backgroundUrl;

    private String updateClass;

    private String comicName;

    private String comicIntroduction;

    private String comicAuthor;

    private String comicUrl;

    private String comicType;

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }


    public String getComicType() {
        return comicType;
    }

    public void setComicType(String comicType) {
        this.comicType = comicType;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getComicAuthor() {
        return comicAuthor;
    }

    public void setComicAuthor(String comicAuthor) {
        this.comicAuthor = comicAuthor;
    }

    public String getComicIntroduction() {
        return comicIntroduction;
    }

    public void setComicIntroduction(String comicIntroduction) {
        this.comicIntroduction = comicIntroduction;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getUpdateClass() {
        return updateClass;
    }

    public void setUpdateClass(String updateClass) {
        this.updateClass = updateClass;
    }

}
