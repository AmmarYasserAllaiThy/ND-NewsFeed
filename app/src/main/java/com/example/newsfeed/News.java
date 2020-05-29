package com.example.newsfeed;

public class News {

    private boolean live;
    private String sectionName;
    private String authorName;
    private String webPubDate;
    private String webTitle;
    private String webUrl;
    // TODO: Add property of author bio

    public News(boolean live, String sectionName, String authorName, String webPubDate, String webTitle, String webUrl) {
        this.live = live;
        this.sectionName = sectionName;
        this.authorName = authorName;
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

    public String getAuthorName() {
        return authorName;
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