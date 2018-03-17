package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/4/9.
 */

public class ComicMessageItem extends DataSupport {

    private String comicUrl;

    private boolean myFavourity;

    private String readNow;

    private String comicImageUrl;

    private String comicName;

    public ComicMessageItem(){

    }

    public ComicMessageItem(String comicUrl,Boolean myFavourity,String readNow,String comicImageUrl,String comicName){
        this.comicUrl = comicUrl;
        this.myFavourity = myFavourity;
        this.readNow = readNow;
        this.comicImageUrl = comicImageUrl;
        this.comicName = comicName;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicImageUrl() {
        return comicImageUrl;
    }

    public void setComicImageUrl(String comicImageUrl) {
        this.comicImageUrl = comicImageUrl;
    }

    public String getReadNow() {
        return readNow;
    }

    public void setReadNow(String readNow) {
        this.readNow = readNow;
    }

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }

    public boolean isMyFavourity() {
        return myFavourity;
    }

    public void setMyFavourity(boolean myFavourity) {
        this.myFavourity = myFavourity;
    }

}
