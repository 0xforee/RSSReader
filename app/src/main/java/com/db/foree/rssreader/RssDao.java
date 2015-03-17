package com.db.foree.rssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rssinfo.foree.rssreader.RssFeedInfo;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by foree on 3/16/15.
 * 数据库增删改查的实现类
 */
public class RssDao {
    private RssSQLiteOpenHelper rssSQLiteOpenHelper;

    public RssDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        rssSQLiteOpenHelper = new RssSQLiteOpenHelper(context, name, null, version);
    }

    /**
     * 数据库增加操作
     */
    public long add(String FeedName, String url) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", FeedName);
        values.put("url", url);
        Long result = db.insert("rss", null, values);
        db.close();
        return result;
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
     * 数据库查找操作(返回所有数据库数据),填充到rssFeedInfo中
     */
    public List<RssFeedInfo> findAll() {
        List<RssFeedInfo> feedInfos = new ArrayList<>();
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("rss", new String[]{"id", "name", "url"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            RssFeedInfo rssFeedInfo = new RssFeedInfo(id, name, url);
            feedInfos.add(rssFeedInfo);
        }
        cursor.close();
        db.close();

        return feedInfos;

    }
}
