package com.foree.rssreader.utils;

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
}

