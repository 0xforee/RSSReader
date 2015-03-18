package com.rssreader.foree.rssreader;

import android.test.AndroidTestCase;

import com.db.foree.rssreader.RssDao;

public class MainActivityTest extends AndroidTestCase {
    public void testAdd() throws Exception {
        RssDao rssDao = new RssDao(getContext(), "rss", null, 1);
        // rssDao.add("好奇心日报","http://feed.dogear.cn/rss/10244.xml");
        //rssDao.add("香港《凤凰周刊》官方博客", "http://blog.sina.com.cn/rss/1267454277.xml");
        // rssDao.add("凤凰读书", "http://www.dogear.cn/feed/9768.xml");
        // rssDao.add("VICE中国", "https://diy-devz.rhcloud.com/weixin?openid=oIWsFt2GmdYbEi0kCF0K4yO5qAXo");
        // rssDao.add("魅族论坛", "http://home.meizu.cn/forum.php?mod=rss&fid=47&auth=75adh9VXN4w2LUlUU0Geo7K8IZfQN%2BCrljYieNxcSsqOKNcBDaFStvzrGsBM");
        // rssDao.add("信托圈", "http://feed.dogear.cn/rss/9374.xml");

    }

    public void testDelete() throws Exception {
        RssDao rssDao = new RssDao(getContext(), "rss.db", null, 1);
        rssDao.delete("测试1");
        // rssDao.delete("新浪");
        // rssDao.delete("凤凰读书");
    }

}