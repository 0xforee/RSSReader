package com.utils.foree.rssreader;

import android.util.Log;

import com.baseapplication.foree.rssreader.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by foree on 3/7/15.
 * 文件工具类：给定一个用地址初始化的file对象，就可以写入或读取文件
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    //读取文件
    public static String readFile(File file) throws IOException {
        FileReader in = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        try {
            in = new FileReader(file);
            bufferedReader = new BufferedReader(in);
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
            if (in != null)
                in.close();
        }
        return stringBuffer.toString();
    }

    //写入文件
    public static void writeFile(File file, String string) throws IOException {
        FileWriter out = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(out);
        bufferedWriter.write(string);
        bufferedWriter.close();
        out.close();
    }

    //判断缓存文件是否存在
    public static boolean getCacheList(String urlName) {
        String cacheName = CacheUtils.urlToName(urlName);

        try {
            File file = new File(MyApplication.mySdcardCacheDir + "/" + cacheName);
            if (file.exists()) {
                Log.v(TAG, "CacheFileExit");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}
