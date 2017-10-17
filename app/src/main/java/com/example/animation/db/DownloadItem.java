package com.example.animation.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘通 on 2017/3/2.
 */

public class DownloadItem extends DataSupport{
    private String downloadItem;

    private String downloadMessage;

    private String downloadNumber;

    private int downloadPage;

    private String downloadUrl;

    public int getDownloadPage() {
        return downloadPage;
    }

    public void setDownloadPage(int downloadPage) {
        this.downloadPage = downloadPage;
    }

    public String getDownloadMessage() {
        return downloadMessage;
    }

    public void setDownloadMessage(String downloadMessage) {
        this.downloadMessage = downloadMessage;
    }

    public String getDownloadNumber() {
        return downloadNumber;
    }

    public void setDownloadNumber(String downloadNumber) {
        this.downloadNumber = downloadNumber;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadItem() {
        return downloadItem;
    }

    public void setDownloadItem(String downloadItem) {
        this.downloadItem = downloadItem;
    }
}
