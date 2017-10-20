package com.example.animation.db;

/**
 * Created by 刘通 on 2017/10/19.
 */

public class News {
    private String newsTitle;
    private String newsContent;
    private String newsPictureUrl;
    private String newsAuthor;
    private String newsTime;
    private String newsHot;
    private String newsUrl;

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsPictureUrl() {
        return newsPictureUrl;
    }

    public void setNewsPictureUrl(String newsPictureUrl) {
        this.newsPictureUrl = newsPictureUrl;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public void setNewsAuthor(String newsAuthor) {
        this.newsAuthor = newsAuthor;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsHot() {
        return newsHot;
    }

    public void setNewsHot(String newsHot) {
        this.newsHot = newsHot;
    }

}
