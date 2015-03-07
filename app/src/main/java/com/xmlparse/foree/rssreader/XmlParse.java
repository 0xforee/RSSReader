package com.xmlparse.foree.rssreader;

import android.util.Log;

import com.rssinfo.foree.rssreader.RssFeedInfo;
import com.rssinfo.foree.rssreader.RssItemInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by foree on 3/3/15.
 * 对xml文件进行解析，并将解析出来的文件放入RSSFeedInfo,RssItemInfo中，
 * 方便后边对内容的复用
 */
public class XmlParse extends DefaultHandler {
    private static final String TAG = "XmlParse";
    //标记当前标签
    private int currentTag = 0;
    private RssFeedInfo rssFeedInfo;
    private RssItemInfo rssItemInfo;

    //多次调用characters时的多次解析的数据总和
    private String sumString = "";

    //定义RSS的标签类型
    private final int RSS_NONE = 0;
    private final int RSS_CHANNEL = 1;
    private final int RSS_TITLE = 2;
    private final int RSS_ITEM = 3;
    private final int RSS_LINK = 4;
    private final int RSS_DESCRIPTION = 5;
    private final int RSS_IMAGE = 6;
    private final int RSS_PUBDATA = 7;
    private final int RSS_URL = 8;
    private final int RSS_LANGUAGE = 9;
    private final int RSS_GENERATOR = 10;
    private final int RSS_COPYRIGHT = 11;
    private final int RSS_AUTHOR = 12;
    private final int RSS_CATEGORY = 13;
    private final int RSS_COMMENT = 14;
    private final int RSS_GUID = 15;

    public XmlParse() {
        rssItemInfo = new RssItemInfo();
        rssFeedInfo = new RssFeedInfo();
    }

    public RssFeedInfo getFeedInfo() {
        return rssFeedInfo;
    }

    //开始解析文档
    public void startDocument() throws SAXException {
        Log.v(TAG, "XML文件解析开始");
    }


    //结束解析文档
    public void endDocument() throws SAXException {
        Log.v(TAG, "XML文件解析停止");

    }


    //遇到某一个便签的时候，触发此方法
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.v(TAG, "标签解析开始");
        if (localName.equals("channel")) {
            currentTag = RSS_CHANNEL;
            return;
        }
        if (localName.equals("item")) {
            rssItemInfo = new RssItemInfo();
            //currentTag = RSS_ITEM;
            return;
        }
        if (localName.equals("title")) {
            currentTag = RSS_TITLE;
            return;
        }
        if (localName.equals("link")) {
            currentTag = RSS_LINK;
            return;
        }
        if (localName.equals("description")) {
            currentTag = RSS_DESCRIPTION;
            return;
        }
        if (localName.equals("pubDate")) {
            currentTag = RSS_PUBDATA;
        }

    }


    //遇到结束某一个标签的时候，触发此方法
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.v(TAG, "标签解析停止 ");
        //等待地址全部解析完成，将其添加到地址链接中
        if (localName.equals("link")) {
            rssItemInfo.setLink(sumString);
        }
        if( localName.equals("item")){
            Log.v(TAG, "添加item");
            rssFeedInfo.addItem(rssItemInfo);
        }
        //标签解析结束，将tmp清理
        sumString = "";
        //并将标签标记清零
        currentTag = 0;
    }

    //每一个标签截取的内容都可以从这里获取到
    public void characters(char[] ch, int start, int length) throws SAXException {
        //存储解析出来的数据
        String mItemContent = new String(ch, start, length);

        switch (currentTag) {
            case RSS_CHANNEL:
                // rssItemInfo.s(mItemContent);
                Log.v(TAG, "channel = " + mItemContent);
                break;
            case RSS_TITLE:
                rssItemInfo.setTitle(mItemContent);
                Log.v(TAG, "title = " + mItemContent);
                break;
            case RSS_PUBDATA:
                rssItemInfo.setPubdata(mItemContent);
                Log.v(TAG, "pubDate = " + mItemContent);
                break;
            case RSS_LINK:
                sumString = sumString + mItemContent;
                //  rssItemInfo.setLink(sum_String);
                Log.v(TAG, "link = " + sumString + '\n');
                break;
            case RSS_DESCRIPTION:
                rssItemInfo.setDescription(mItemContent);
                Log.v(TAG, "description = " + mItemContent);
                break;
            default:
                //        Log.v(TAG, "有未解析的数据: " + sum_String);
                break;

        }

    }

}
