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
public class XmlParseHandler extends DefaultHandler {
    private static final String TAG = "XmlParse";
    private RssFeedInfo rssFeedInfo;
    //多次调用characters时的多次解析的数据总和
    StringBuilder mBuff;

    String mTitle;
    String mPubDate;
    String mLink;
    String mDescription;

    //当前是否位于Item内,可用于判断解析头数据
    boolean mInItem;

    public XmlParseHandler() {
        rssFeedInfo = new RssFeedInfo();
        mBuff = new StringBuilder();
        mInItem = false;
    }

    public RssFeedInfo getFeedInfo() {
        return rssFeedInfo;
    }

    //遇到某一个便签的时候，触发此方法
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.v(TAG, "标签解析开始");
        //标签解析开始，将缓存清理
        mBuff.delete(0, mBuff.length());

        //如果一个item开始的时候，将子元素内容清除
        if (localName.equals("item")) {
            mInItem = true;
            mTitle = mPubDate = mLink = mDescription = "";
        }

    }

    //遇到结束某一个标签的时候，触发此方法
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.v(TAG, "标签解析停止 ");
        //等待地址全部解析完成，将内容添加
        if (mInItem) {
            if (localName.equals("title")) mTitle = mBuff.toString();
            if (localName.equals("link")) mLink = mBuff.toString();
            if (localName.equals("pubDate")) mPubDate = mBuff.toString();
            if (localName.equals("description")) mDescription = mBuff.toString();
            if (localName.equals("item")) {
                Log.v(TAG, "添加item");
                mInItem = false;
                rssFeedInfo.addItem(new RssItemInfo(mTitle, mLink, mPubDate, mDescription));
            }
        }
        //解析RSS的头部的信息
        else {
            if (localName.equals("title")) rssFeedInfo.setTitle(mBuff.toString());
            if (localName.equals("link")) rssFeedInfo.setLink(mBuff.toString());
            if (localName.equals("pubDate")) rssFeedInfo.setPubdate(mBuff.toString());
        }
    }

    //每一个标签截取的内容都可以从这里获取到
    public void characters(char[] ch, int start, int length) throws SAXException {
        //存储解析出来的数据
        mBuff.append(ch, start, length);
    }

}
