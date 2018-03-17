package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2018/2/17.
 */

public class DownloadComic extends DataSupport {
    private String comicName;
    private String comicUrl;
    private String comicPages;
    private int allPages;
    private int currentPages;
    private boolean downloadFinish;
    private int statue;
    private String comicImageUrl;
    private String comicPagesId;
    private String comicId;

    public DownloadComic(){

    }

    public DownloadComic(String comicName,String comicUrl,String comicPages,String comicImageUrl,String comicPagesId,String comicId){
        this(comicName,comicUrl,comicPages,0,0,false,0,comicImageUrl,comicPagesId,comicId,false);
    }

    public DownloadComic(String comicName,String comicUrl,String comicPages,int allPages,
                         int currentPages,boolean downloadFinish,int statue,String comicImageUrl,
                         String comicPagesId,String comicId,boolean isSelect){
        this.comicName = comicName;
        this.comicUrl = comicUrl;
        this.comicPages = comicPages;
        this.allPages = allPages;
        this.currentPages = currentPages;
        this.downloadFinish = downloadFinish;
        this.statue = statue;
        this.comicImageUrl = comicImageUrl;
        this.comicPagesId = comicPagesId;
        this.comicId = comicId;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private boolean isSelect;


    public String getComicId() {
        return comicId;
    }

    public void setComicId(String comicId) {
        this.comicId = comicId;
    }

    public String getComicPagesId() {
        return comicPagesId;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(String s, int statue) {
        this.statue = statue;
    }

    public void setComicPagesId(String comicPagesId) {
        this.comicPagesId = comicPagesId;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }

    public String getComicPages() {
        return comicPages;
    }

    public void setComicPages(String comicPages) {
        this.comicPages = comicPages;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getCurrentPages() {
        return currentPages;
    }

    public void setCurrentPages(int currentPages) {
        this.currentPages = currentPages;
    }

    public boolean isDownloadFinish() {
        return downloadFinish;
    }

    public void setDownloadFinish(boolean downloadFinish) {
        this.downloadFinish = downloadFinish;
    }

    public String getComicImageUrl() {
        return comicImageUrl;
    }

    public void setComicImageUrl(String comicImageUrl) {
        this.comicImageUrl = comicImageUrl;
    }

}
