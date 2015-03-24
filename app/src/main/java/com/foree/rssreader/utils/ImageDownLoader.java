package com.foree.rssreader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
 */
public class ImageDownLoader {
    public static final String TAG = "ImageDownLoader";

    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 下载Image的线程池
     */
    private ExecutorService mImageThreadPool = null;

    public ImageDownLoader(Context context) {
        //获取系统分配的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //规定缓存的尺寸为系统最大内润的1/8
        int mCacheSize = maxMemory / 8;
        //使用lrucache来实现缓存机制
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 获取线程池，因为涉及到并发的问题，我们加上同步锁
     * @return
     */
    public ExecutorService getThreadPool() {
        if (mImageThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (mImageThreadPool == null) {
                    //为了下载图片更加的流畅，我们用了2个线程来下载图片
                    mImageThreadPool = Executors.newFixedThreadPool(2);
                }
            }
        }
        return mImageThreadPool;
    }

    /**
     * 添加Bitmap到内存缓存,即所谓的lrucache
     * @param key 查询的key
     * @param bitmap 需要缓存的bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从内存缓存中获取一个Bitmap
     * @param key 需要查询的key
     * @return 返回取得的bitmap
     */
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取
     * SD卡或者手机缓存没有就去下载
     * @param url 原始的image的url,从description中截取
     * @param listener image加载时刻的监听对象
     * @return 返回下载的bitmap
     */
    public Bitmap downloadImage(final String url, final onImageLoaderListener listener) {
        //替换Url中非字母和非数字的字符,作为文件名使用
        final String subUrl = url.replaceAll("[^\\w]", "");
        //从cache中寻找bitmap,找到就返回,找不到就下载
        Bitmap bitmap = showCacheBitmap(subUrl);
        if (bitmap != null) {
            return bitmap;
        } else {
            //如果内存中没有,本地磁盘也没有,那么就去下载
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onImageLoader((Bitmap) msg.obj, url);
                }
            };

            //获取线程池来分配线程运行此任务
            getThreadPool().execute(new Runnable() {

                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFormUrl(url);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                        FileUtils.saveBitmap(subUrl, bitmap);
                    addBitmapToMemoryCache(subUrl, bitmap);
                }
            });
        }

        return null;
    }

    /**
     * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
     *
     * @param urlName 已经处理过的文件名称
     * @return 返回找到的bitmap
     */
    public Bitmap showCacheBitmap(String urlName) {
        if (getBitmapFromMemCache(urlName) != null) {
            return getBitmapFromMemCache(urlName);
        } else if (CacheUtils.isListCached(urlName) && FileUtils.getFileSize(urlName) != 0) {
            Bitmap bitmap = FileUtils.getBitmap(urlName);
            addBitmapToMemoryCache(urlName, bitmap);
            return bitmap;
        }
        return null;
    }

    /**
     * @param url 图片的链接
     * @return 返回一个bitmap对象, 用与设置listView的图片位
     */
    private Bitmap getBitmapFormUrl(final String url) {
        Bitmap bitmap = null;
        try {
            synchronized (this) {
                URL imageurl = new URL(url);
                URLConnection urlConnection = imageurl.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                Log.v(TAG, "从" + url + "下载image");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     */
    public synchronized void cancelTask() {
        if (mImageThreadPool != null) {
            mImageThreadPool.shutdownNow();
            mImageThreadPool = null;
        }
    }

    /**
     * @author len
     */
    public interface onImageLoaderListener {
        void onImageLoader(Bitmap bitmap, String url);
    }

}
