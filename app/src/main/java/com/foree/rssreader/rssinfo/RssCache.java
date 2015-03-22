package com.foree.rssreader.rssinfo;

import com.foree.rssreader.base.MyApplication;
import com.foree.rssreader.utils.FileUtils;

import java.io.File;

/**
 * Created by foree on 3/7/15.
 * 存储RSS的cache，加快加载速度
 */
public class RssCache {
    private static String urlData;

    //通过Url来获取本地的cache文件
    public static String getUrlCache(String urlString) {
        if (urlString == null)
            return null;

        String result = null;
        try {
            //指定文件路径
            File file = new File(MyApplication.mySdcardCacheDir + "/" + urlString);
            //读取文件
            if (file.exists() && file.isFile()) {
                result = FileUtils.readFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //将给定的url写入文件缓存中
    public static void setUrlCache(String urlString) {
        if (urlString == null) {
            return;
        }

        try {
            File file = new File(MyApplication.mySdcardCacheDir + "/" + urlString);
            FileUtils.writeFile(file, urlData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
