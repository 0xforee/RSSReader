package com.foree.rssreader.xmlparse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.rssinfo.RssFeedInfo;
import com.foree.rssreader.rssinfo.RssItemInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by foree on 3/3/15.
 * 对xml文件进行解析，并将解析出来的文件放入RSSFeedInfo,RssItemInfo中，
 * 方便后边对内容的复用
 */
public class XmlParseHandler extends DefaultHandler {
    private static final String TAG = "XmlParseHandler";
    private RssFeedInfo rssFeedInfo;
    BaseActivity mActivity;
    private RssItemInfo mRssItemInfo;
    Bitmap bitmap;
    //多次调用characters时的多次解析的数据总和
    StringBuilder mBuff;

    private String feedTitle;
    private String feedDescription;
    private String feedLink;
    private String feedPubdate;
    String mTitle;
    String mPubDate;
    String mLink;
    String mDescription;
    Bitmap mImage;

    public XmlParseHandler(BaseActivity activity) {
        this.mActivity = activity;
        rssFeedInfo = new RssFeedInfo();
        mBuff = new StringBuilder();
        mInItem = false;
    }

    //当前是否位于Item内,可用于判断解析头数据
    boolean mInItem;

    public XmlParseHandler() {

    }

    public RssFeedInfo getFeedInfo() {
        return rssFeedInfo;
    }

    //遇到某一个便签的时候，触发此方法
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Log.v(TAG, "标签解析开始");
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
        //Log.v(TAG, "标签解析停止 ");
        //等待地址全部解析完成，将内容添加
        if (mInItem) {
            if (localName.equals("title")) mTitle = mBuff.toString();
            if (localName.equals("link")) mLink = mBuff.toString();
            if (localName.equals("pubDate")) mPubDate = mBuff.toString();
            if (localName.equals("description")) {
                //打印description信息,cdata中数据是否包含在其中
                mDescription = mBuff.toString();
                // Log.v(TAG, mDescription);
                Pattern pattern = Pattern.compile("http://mmbiz\\.qpic\\.cn/mmbiz/[^\"]+");
                final Matcher matcher = pattern.matcher(mDescription);
                if (matcher.find()) {
                    Log.v(TAG, "(匹配只适用于微信公众号)图片链接:" + matcher.group());
                    mImage = getBitMap(matcher.group());
                }
            }
            if (localName.equals("item")) {
                Log.v(TAG, "添加item");
                //要退出当前item时,设置为false
                mInItem = false;

                //  rssFeedInfo.addItem();
                //handler发送消息到主队列
                mRssItemInfo = new RssItemInfo(mTitle, mLink, mPubDate, mDescription, mImage, feedTitle, feedLink, feedPubdate, feedDescription);
                mActivity.getmHandler().post(new RssAdder());
            }
        }
        //解析RSS的头部的信息
        else {

            if (localName.equals("title")) feedTitle = mBuff.toString();
            if (localName.equals("link")) feedLink = mBuff.toString();
            if (localName.equals("pubDate")) feedPubdate = mBuff.toString();
            if (localName.equals("description")) feedDescription = mBuff.toString();
        }
    }

    //每一个标签截取的内容都可以从这里获取到
    public void characters(char[] ch, int start, int length) throws SAXException {
        //存储解析出来的数据
        mBuff.append(ch, start, length);
    }

    /**
     * @param url 图片的链接
     * @return 返回一个bitmap对象, 用与设置listview的图片位
     */
    public Bitmap getBitMap(final String url) {
        try {
            synchronized (this) {
                URL imageurl = new URL(url);
                URLConnection urlConnection = imageurl.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                Log.v(TAG, "run 内部");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * RssAdder用于将解析出来的RssItemInfo置于listview中,
     * Runnable接口,用于post传递给主线程使用
     */
    public class RssAdder implements Runnable {
        private BaseActivity.RssAdapter mRssAdapter;

        //获取与类相关的Adapter
        public RssAdder() {
            mRssAdapter = mActivity.getmRssAdapter();
        }

        @Override
        public void run() {
            mRssAdapter.add(mRssItemInfo);
        }
    }
}