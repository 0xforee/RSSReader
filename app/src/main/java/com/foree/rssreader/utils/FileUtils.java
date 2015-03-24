package com.foree.rssreader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.foree.rssreader.base.MyApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    //取得缓存目录下缓存文件大小
    public static long getFileSize(String fileName) {
        return new File(MyApplication.mySdcardCacheDir + File.separator + fileName).length();
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public static void saveBitmap(String fileName, Bitmap bitmap) {
        File file = new File(MyApplication.mySdcardCacheDir + File.separator + fileName);
        try {
            if (file.createNewFile()) {
                FileOutputStream output = new FileOutputStream(file);
                //压缩bitmap
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从内存卡中获取bitmap文件
     */
    public static Bitmap getBitmap(String fileName) {
        return decodeFile(new File(MyApplication.mySdcardCacheDir + File.separator + fileName));
    }

    //decodes image and scales it to reduce memory consumption
    private static Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 198;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
