package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2018/2/18.
 */

public class ReadNowComic extends DataSupport {
    private String comicUrl;
    private int pages;
    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }
}
