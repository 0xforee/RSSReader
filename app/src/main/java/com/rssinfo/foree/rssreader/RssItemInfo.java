package com.rssinfo.foree.rssreader;

/**
 * Created by foree on 3/3/15.
 * 每个Item自己的信息集合
 */
public class RssItemInfo {
    private String title;
    private String link;
    private String pubdate;
    private String description;

    //初始化变量
    public RssItemInfo() {
        title = "";
        link = "";
        pubdate = "";
        description = "";
    }

    public RssItemInfo(String title, String link, String pubdate, String description) {
        this.title = title;
        this.link = link;
        this.pubdate = pubdate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getpubDate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
