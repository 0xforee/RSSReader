package com.foree.rssreader.xmlparse;

import android.util.Log;
import com.foree.rssreader.base.BaseActivity;
import com.foree.rssreader.rssinfo.RssItemInfo;
import com.foree.rssreader.utils.DateUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by foree on 3/3/15.
 * 对xml文件进行解析，并将解析出来的文件放入RSSFeedInfo,RssItemInfo中，
 * 方便后边对内容的复用
 */
public class XmlParseHandler extends DefaultHandler {
    private static final String TAG = "XmlParseHandler";
    // private RssFeedInfo rssFeedInfo;
    private ParseHandlerCallbacks mCallbacks;
    BaseActivity mActivity;
    //private RssItemInfo mRssItemInfo;
    //Bitmap bitmap;
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
    // Bitmap mImage;
    String mImageUrl;
    boolean isNotify = false;

    public XmlParseHandler(BaseActivity activity) {
        this.mActivity = activity;
        // rssFeedInfo = new RssFeedInfo();
        mBuff = new StringBuilder();
        mInItem = false;
        mCallbacks = (ParseHandlerCallbacks) mActivity;

    }

    //当前是否位于Item内,可用于判断解析头数据
    boolean mInItem;

    public XmlParseHandler() {

    }

    /*public RssFeedInfo getFeedInfo() {
        return rssFeedInfo;
    }*/

    //遇到某一个便签的时候，触发此方法
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // Log.v(TAG, "标签解析开始");
        //标签解析开始，将缓存清理
        mBuff.delete(0, mBuff.length());

        //如果一个item开始的时候，将子元素内容清除
        if (localName.equals("item")) {
            mInItem = true;
            mTitle = mPubDate = mLink = mDescription = mImageUrl = "";
        }
    }

    //遇到结束某一个标签的时候，触发此方法
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //Log.v(TAG, "标签解析停止 ");
        //等待地址全部解析完成，将内容添加
        if (mInItem) {
            if (localName.equals("title")) mTitle = mBuff.toString();
            if (localName.equals("link")) mLink = mBuff.toString();
            if (localName.equals("pubDate")) {
                mPubDate = mBuff.toString();
                //如果pubDate为空,则使用FeedPubDate
                if (mPubDate.isEmpty())
                    mPubDate = feedPubdate;
            }
            if (localName.equals("description")) {
                //打印description信息,cdata中数据是否包含在其中
                mDescription = mBuff.toString();

                //匹配description中的image链接的正则表达式
                Pattern imagePattern = Pattern.compile("http://mmbiz\\.qpic\\.cn/mmbiz/[^\"]+");

                //匹配description中的时间的正则表达式
                Pattern datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

                final Matcher imageMatcher = imagePattern.matcher(mDescription);
                final Matcher dateMatcher = datePattern.matcher(mDescription);

                //如果匹配成功,获取url链接
                if (imageMatcher.find()) {
                    mImageUrl = imageMatcher.group();
                    Log.v(TAG, "(匹配只适用于微信公众号)图片链接:" + mImageUrl);
                }

                //如果匹配成功,获取日期
                if (dateMatcher.find()) {
                    mPubDate = DateUtils.parseDate(dateMatcher.group());
                    Log.v(TAG, "日期:" + mPubDate);
                }
            }
            if (localName.equals("item")) {
                Log.v(TAG, "添加item");
                //要退出当前item时,设置为false
                mInItem = false;

                //  rssFeedInfo.addItem();
                //handler发送消息到主队列
                //此处对象必须重新引用和新建,而不是使用全局的引用来接收对象,防止出现相同对象被传递进而出现的列表重复问题
                RssItemInfo mRssItemInfo = new RssItemInfo(mTitle, mLink, mPubDate, mDescription, mImageUrl, feedTitle, feedLink, feedPubdate, feedDescription);
                mActivity.getmHandler().post(new RssAdder(mRssItemInfo));
                if (!isNotify && mCallbacks != null) {
                    //更新progressBar等ui
                    mCallbacks.notifyUiUpdate();
                    isNotify = true;
                }
            }
        }
        //解析RSS的头部的信息
        else {

            if (localName.equals("title")) feedTitle = mBuff.toString();
            if (localName.equals("link")) feedLink = mBuff.toString();
            if (localName.equals("lastBuildDate")) feedPubdate = mBuff.toString();
            if (localName.equals("description")) feedDescription = mBuff.toString();
        }
    }

    //每一个标签截取的内容都可以从这里获取到
    public void characters(char[] ch, int start, int length) throws SAXException {
        //存储解析出来的数据
        mBuff.append(ch, start, length);
    }


    /**
     * RssAdder用于将解析出来的RssItemInfo置于listview中,
     * Runnable接口,用于post传递给主线程使用
     */
    public class RssAdder implements Runnable {
        private BaseActivity.RssAdapter mRssAdapter;
        private RssItemInfo mRssItemInfo;

        //获取与类相关的Adapter
        public RssAdder(RssItemInfo rssItemInfo) {
            this.mRssItemInfo = rssItemInfo;
            mRssAdapter = mActivity.getmRssAdapter();
        }

        @Override
        public void run() {
            mRssAdapter.add(mRssItemInfo);
        }
    }

    public static interface ParseHandlerCallbacks {
        //提示更新ui,主要是progressBar
        void notifyUiUpdate();
    }
}