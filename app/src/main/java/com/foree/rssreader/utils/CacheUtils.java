package com.foree.rssreader.utils;

import android.util.Log;
import android.util.Xml;

import com.foree.rssreader.base.MyApplication;
import com.foree.rssreader.rssinfo.RssFeedInfo;
import com.foree.rssreader.rssinfo.RssItemInfo;
import com.foree.rssreader.xmlparse.XmlParseHandler;

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

    //生成缓存文件
    public static void writeCacheList(RssFeedInfo rssFeedInfo) {
        XmlSerializer xmlSerializer = Xml.newSerializer();
        //获取所有的数据对象
        List<RssItemInfo> list = rssFeedInfo.getItemlist();

        //获取缓存文件名称
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

    //读取缓存文件
    public static RssFeedInfo readCacheList(String urlName) {
        XmlParseHandler xmlParseHandler;

        //标准化缓存文件名称
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

            return null;
            // return xmlParseHandler.getFeedInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //将URL中不合适的字符去掉(用URL来标识对应的缓存文件)
    public static String urlToName(String url) {
        return url.replaceAll("[^\\w]", "");
    }

    //判断缓存文件是否存在
    public static boolean isListCached(String urlName) {
        //  String cacheName = CacheUtils.urlToName(urlName);
        try {
            File file = new File(MyApplication.mySdcardCacheDir + "/" + urlName);
            if (file.exists()) {
                Log.v(TAG, MyApplication.mySdcardCacheDir + File.separator + urlName + " CacheFileExit");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //清除缓存文件
    public static boolean cleanAllCache() {
        File file = new File(MyApplication.mySdcardCacheDir + "/");
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (File file1 : files) {
                if (file1.delete())
                    Log.v(TAG, file1.getName() + " cleaned");
            }
            return true;
        }
        return false;
    }
}
