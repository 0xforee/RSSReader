package com.rssinfo.foree.rssreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by foree on 3/3/15.
 * //一个Rssfeed需要的元素有title,link,pubdata,image(后加）
 */
public class RssFeedInfo {

    //订阅号的名称
    private String title = null;
    //订阅号的链接
    private String link = null;
    private String pubdate = "";
    //list中存放所有的保存新闻的对象
    private List<RssItemInfo> itemlist = new ArrayList<>();

    //将解析出来的元素item添加进列表
    public void addItem(RssItemInfo rssItemInfo) {
        itemlist.add(rssItemInfo);
    }

    //从列表中获取指定的项目item
    public RssItemInfo getItem(int location) {
        return itemlist.get(location);
    }

    //获取所有对象的列表
    public List<RssItemInfo> getItemlist() {
        return itemlist;
    }

    //返回符合listview的格式的list
    public List<Map<String, Object>> getAllItemForListView() {
        //循环将所有的item加入itemlist中，并返回一个list，供listAdapter使用
        int size = itemlist.size();
        List<Map<String, Object>> datalist = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", itemlist.get(i).getTitle());
            map.put("pubdate", itemlist.get(i).getpubDate());
            // map.put("image",itemlist.get(i).getimage());
            datalist.add(map);
        }
        return datalist;
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

    public String pubDate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }


}

