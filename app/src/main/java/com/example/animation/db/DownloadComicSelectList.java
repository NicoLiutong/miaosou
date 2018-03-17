package com.example.animation.db;

/**
 * Created by 刘通 on 2018/3/8.
 */

public class DownloadComicSelectList {
    private String comicPages;
    private String comicUrl;
    private String comicPageId;
    private boolean isSelect;
    private boolean isDownload;

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }

    public String getComicPageId() {
        return comicPageId;
    }

    public void setComicPageId(String comicPageId) {
        this.comicPageId = comicPageId;
    }

    public String getComicPages() {
        return comicPages;
    }

    public void setComicPages(String comicPages) {
        this.comicPages = comicPages;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

}
