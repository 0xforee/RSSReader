package com.rssinfo.foree.rssreader;

/**
 * Created by foree on 3/3/15.
 * 每个Item自己的信息集合
 */
public class RssItemInfo {
    private String title = null;
    private String link = null;
    private String pubdata = null;
    private String description = null;


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

    public String getpubData() {
        return pubdata;
    }

    public void setPubdata(String pubdata) {
        this.pubdata = pubdata;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) {this.description = description;}
}
