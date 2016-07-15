package com.rssreader.foree.rssreader;

import android.test.AndroidTestCase;

import com.foree.duker.db.RssDao;
import com.foree.duker.xmlparse.OpmlParse;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivityTest extends AndroidTestCase {
    public void testAdd() throws Exception {
        RssDao rssDao = new RssDao(getContext());
        // rssDao.add("好奇心日报","http://feed.dogear.cn/rss/10244.xml");
        //rssDao.add("香港《凤凰周刊》官方博客", "http://blog.sina.com.cn/rss/1267454277.xml");
        // rssDao.add("凤凰读书", "http://www.dogear.cn/feed/9768.xml");
        // rssDao.add("VICE中国", "https://diy-devz.rhcloud.com/weixin?openid=oIWsFt2GmdYbEi0kCF0K4yO5qAXo");
        // rssDao.add("魅族论坛", "http://home.meizu.cn/forum.php?mod=rss&fid=47&auth=75adh9VXN4w2LUlUU0Geo7K8IZfQN%2BCrljYieNxcSsqOKNcBDaFStvzrGsBM");
        // rssDao.add("信托圈", "http://feed.dogear.cn/rss/9374.xml");

    }

    public void testDelete() throws Exception {
        RssDao rssDao = new RssDao(getContext());
        rssDao.delete("新浪");
        rssDao.delete("凤凰读书");
        rssDao.delete("好奇心日报");
        rssDao.delete("雅虎 20 年起伏，互联网成长的 20 年缩影");
    }

    public void testOpmlParse() throws Exception {
        // String fileName = "那些好看的微信公众号合集.xml";
        String fileName = "111111";
        // String fileName = "历史-读书合集.xml";
        String FilePath = "/sdcard/Duker";
        File filepath = new File(FilePath + File.separator + fileName);
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        OpmlParse opmlParse = new OpmlParse(getContext());
        saxParser.parse(filepath, opmlParse);
    }
}