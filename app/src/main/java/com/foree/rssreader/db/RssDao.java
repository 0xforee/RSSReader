package com.foree.rssreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.foree.rssreader.rssinfo.RssFeedInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foree on 3/16/15.
 * 数据库增删改查的实现类
 */
public class RssDao {
    private static final String TAG = "RssDao";
    private RssSQLiteOpenHelper rssSQLiteOpenHelper;

    public RssDao(Context context) {
        rssSQLiteOpenHelper = new RssSQLiteOpenHelper(context);
    }

    /**
     * 数据库增加操作
     */
    public long add(String FeedName, String url, String link) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getWritableDatabase();
        //不能添加重复的内容
        Cursor cursor = db.query("rss", null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("name", FeedName);
            values.put("url", url);
            values.put("link", link);
            Long result = db.insert("rss", null, values);
            db.close();
            return result;
        } else {
            return -1;
        }
    }

    /**
     * 数据库删除操作
     */
    public int delete(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        int result = db.delete("rss", "name=?", new String[]{FeedName});
        db.close();
        return result;
    }

    /**
     * 数据库修改更新操作
     */
    public int update(String FeedName, String newUrl) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("url", newUrl);
        int result = db.update("rss", values, "name=?", new String[]{newUrl});
        db.close();
        return result;
    }

    /**
     * 数据库查找操作
     */
    public boolean find(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("rss", null, "name=?", new String[]{FeedName}, null, null, null);
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 通过名称来查找url(数据解析的链接)
     */
    public String findUrl(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("rss", null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            cursor.close();
            db.close();
            Log.v(TAG, url);
            return url;
        }
        return null;
    }

    /**
     * 通过名称来查找link(从xml里解析出来的链接,用来作为缓存文件的名称)
     */
    public String findLink(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("rss", null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.moveToNext()) {
            String link = cursor.getString(cursor.getColumnIndex("link"));
            cursor.close();
            db.close();
            Log.v(TAG, link);
            return link;
        }
        return null;
    }

    /**
     * 数据库查找操作(返回所有数据库数据),填充到rssFeedInfo中
     */
    public List<RssFeedInfo> findAll() {
        List<RssFeedInfo> feedInfos = new ArrayList<>();
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("rss", new String[]{"id", "name", "url", "link"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String link = cursor.getString(cursor.getColumnIndex("link"));
            RssFeedInfo rssFeedInfo = new RssFeedInfo(id, name, url, link);
            feedInfos.add(rssFeedInfo);
        }
        cursor.close();
        db.close();

        return feedInfos;

    }
}
