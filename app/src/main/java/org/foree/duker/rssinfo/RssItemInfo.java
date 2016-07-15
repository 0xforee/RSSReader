package org.foree.duker.rssinfo;

import android.graphics.Bitmap;

/**
 * Created by foree on 3/3/15.
 * 每个Item自己的信息集合
 */
public class RssItemInfo {
    // Item 标题
    private String title;
    // Item 概览
    private String summary;
    // Item 发布时间
    private String pubDate;
    // Item ID
    private String entryId;
    // Itme 链接
    private String url;

    private String feedTitle;
    private String feedDescription;
    private String feedLink;
    private String feedPubdate;
    //private String title;
    private String link;
    private String pubdate;
    private String description;
    private Bitmap image;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    //初始化变量
    public RssItemInfo() {
        title = "";
        link = "";
        pubdate = "";
        description = "";
    }

    public RssItemInfo(String feedTitle, String title) {
        this.feedTitle = feedTitle;
        this.title = title;
    }

    public RssItemInfo(String title, String link, String pubdate, String description, String imageUrl, String feedTitle, String feedLink, String feedPubdate, String feedDescription) {
        this.title = title;
        this.link = link;
        this.pubdate = pubdate;
        this.description = description;
        //   this.image = image;
        this.imageUrl = imageUrl;
        this.feedTitle = feedTitle;
        this.feedLink = feedLink;
        this.feedPubdate = feedPubdate;
        this.feedDescription = feedDescription;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    public String getFeedPubdate() {
        return feedPubdate;
    }

    public void setFeedPubdate(String feedPubdate) {
        this.feedPubdate = feedPubdate;
    }
}
