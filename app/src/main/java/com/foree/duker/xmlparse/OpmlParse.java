package com.foree.duker.xmlparse;

import android.content.Context;
import android.util.Log;

import com.foree.duker.db.RssDao;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by foree on 3/25/15.
 * 对opml文件进行解析，将解析出来的存入数据库feedlist中
 */
public class OpmlParse extends DefaultHandler {

    private static final String TAG = "OpmlParse";

    String mType;
    String mTitle;
    String mUrl;
    RssDao mRssDao;

    public OpmlParse(Context mContext) {
        mRssDao = new RssDao(mContext);
    }

    @Override
    public void startDocument() throws SAXException {
        Log.v(TAG, "开始解析opml");
    }

    //遇到某一个便签的时候，触发此方法
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //   Log.v(TAG, "标签解析开始");
        if (localName.equals("outline")) {
            if (attributes.getLength() < 3) {
                mType = attributes.getValue(attributes.getIndex("title"));
                Log.v(TAG, "type = " + attributes.getValue(attributes.getIndex("title")));

            }
            if (attributes.getLength() > 4) {
                mTitle = attributes.getValue(attributes.getIndex("title"));
                mUrl = attributes.getValue(attributes.getIndex("xmlUrl"));
                Log.v(TAG, "title = " + attributes.getValue(attributes.getIndex("title"))
                        + " url = " + attributes.getValue(attributes.getIndex("xmlUrl")));
                //添加到数据库中
                mRssDao.addToList(mType, mTitle, mUrl);
                // Log.v(TAG, attributes.getValue(attributes.getIndex("text")));
                // Log.v(TAG, attributes.getValue(attributes.getIndex("type")));
                // Log.v(TAG, attributes.getValue(attributes.getIndex("xmlUrl")));
                // Log.v(TAG, attributes.getValue(attributes.getIndex("htmlUrl")));
            }
        }
    }
}