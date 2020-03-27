package com.example.newsfeed;

public class News {

    private boolean live;
    private String sectionName;
    private String webPubDate;
    private String webTitle;
    private String webUrl;

    public News(boolean live, String sectionName, String webPubDate, String webTitle, String webUrl) {
        this.live = live;
        this.sectionName = sectionName;
        this.webPubDate = webPubDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
    }

    public boolean isLive() {
        return live;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPubDate() {
        return webPubDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }
}