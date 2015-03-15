package com.utils.foree.rssreader;

import android.util.Log;
import android.util.Xml;

import com.baseapplication.foree.rssreader.MyApplication;
import com.rssinfo.foree.rssreader.RssFeedInfo;
import com.rssinfo.foree.rssreader.RssItemInfo;
import com.xmlparse.foree.rssreader.XmlParseHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by foree on 15-3-10.
 * 存储解析完毕的数据
 */
public class CacheUtils {
    private static final String TAG = "CacheUtils";

    public static void writeCacheList(RssFeedInfo rssFeedInfo) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        //获取所有的数据对象
        List<RssItemInfo> list = rssFeedInfo.getItemlist();

        //获取缓存文件
        String cacheName = urlToName(rssFeedInfo.getLink());


        try {
            //建立一个cache文件
            File file = new File(MyApplication.mySdcardCacheDir + "/" + cacheName);
            FileOutputStream os = new FileOutputStream(file);
            //设置输出流和编码类型
            xmlSerializer.setOutput(os, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "cachelist");
            //开始存放解析出来的数据
            for (int i = 0; i < list.size(); i++) {
                xmlSerializer.startTag(null, "item");

                //将title存放到数据库中
                xmlSerializer.startTag(null, "title");
                xmlSerializer.text(list.get(i).getTitle());
                xmlSerializer.endTag(null, "title");
                //将pubdate存放到数据库中
                xmlSerializer.startTag(null, "pubDate");
                if (list.get(i).getpubDate() == null)
                    xmlSerializer.text(" ");
                xmlSerializer.endTag(null, "pubDate");
                //将link放入到数据库中
                xmlSerializer.startTag(null, "link");
                xmlSerializer.text(list.get(i).getLink());
                xmlSerializer.endTag(null, "link");

                xmlSerializer.endTag(null, "item");
            }

            xmlSerializer.endTag(null, "cachelist");
            xmlSerializer.endDocument();
            os.close();
            Log.v(TAG, "cache保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RssFeedInfo readCacheList(String urlName) {
        XmlParseHandler xmlParseHandler;

//获取缓存文件
        String cacheName = urlToName(urlName);
        try {
            FileInputStream input = new FileInputStream(new File(MyApplication.mySdcardCacheDir + "/" + cacheName));
            InputSource inputSource = new InputSource(input);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //使用SAX解析工厂构造SAX解析器
            SAXParser parser = factory.newSAXParser();
            //使用SAX解析器构建xml读取工具
            XMLReader xmlReader = parser.getXMLReader();

            //获取一个代理，并设置xml解析的代理为xmlparse解析类
            xmlParseHandler = new XmlParseHandler();
            xmlReader.setContentHandler(xmlParseHandler);

            xmlReader.parse(inputSource);

            return xmlParseHandler.getFeedInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //将URL中不合适的字符去掉(用URL来标识对应的缓存文件)
    public static String urlToName(String url) {
        String name = url.replaceAll(":", "");
        name = name.replaceAll("\\.", "");
        name = name.replaceAll("//", "");
        name = name.replaceAll("/", "");
        name = name.replaceAll("=", "");
        name = name.replaceAll("-", "");
        name = name.replaceAll(",", "");
        name = name.replaceAll("&", "");
        return name;
    }
}
