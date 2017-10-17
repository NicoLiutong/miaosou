package com.example.animation.db;

/**
 * Created by 刘通 on 2017/4/9.
 */

public class ComicNumberList {

    private String comicEpisodes;

    private String comicPages;

    private boolean comicLastRead;

    private String comicUrl;

    private String comicPagesUrl;

    public String getComicPagesUrl() {
        return comicPagesUrl;
    }

    public void setComicPagesUrl(String comicPagesUrl) {
        this.comicPagesUrl = comicPagesUrl;
    }

    public String getComicUrl() {
        return comicUrl;
    }

    public void setComicUrl(String comicUrl) {
        this.comicUrl = comicUrl;
    }

    public String getComicEpisodes() {
        return comicEpisodes;
    }

    public void setComicEpisodes(String comicEpisodes) {
        this.comicEpisodes = comicEpisodes;
    }

    public String getComicPages() {
        return comicPages;
    }

    public void setComicPages(String comicPages) {
        this.comicPages = comicPages;
    }
    public boolean isComicLastRead() {
        return comicLastRead;
    }

    public void setComicLastRead(boolean comicLastRead) {
        this.comicLastRead = comicLastRead;
    }
}