package org.foree.duker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.foree.duker.base.MyApplication;
import org.foree.duker.rssinfo.RssFeedInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 用户个人数据库增加操作
     */
    public long add(String FeedName, String url, String link) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getWritableDatabase();
        //不能添加重复的内容
        Cursor cursor = db.query(MyApplication.userFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("name", FeedName);
            values.put("url", url);
            values.put("link", link);
            Long result = db.insert(MyApplication.userFeedTable, null, values);
            cursor.close();
            db.close();
            return result;
        } else {
            cursor.close();
            db.close();
            return -1;
        }
    }

    /**
     * feedlist增加函数
     */
    public long addToList(String Type, String FeedName, String url) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getWritableDatabase();
        //不能添加重复的内容
        Cursor cursor = db.query(MyApplication.allFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("name", FeedName);
            values.put("url", url);
            values.put("type", Type);
            Long result = db.insert(MyApplication.allFeedTable, null, values);
            cursor.close();
            db.close();
            return result;
        } else {
            cursor.close();
            db.close();
            return -1;
        }
    }

    /**
     * 数据库删除操作
     */
    public int delete(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        int result = db.delete(MyApplication.userFeedTable, "name=?", new String[]{FeedName});
        db.close();
        return result;
    }

    /**
     * 数据库全部删除
     */
    public void deleteAll(String tableName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    /**
     * 数据库修改更新操作
     */
    public int update(String FeedName, String newUrl) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("url", newUrl);
        int result = db.update(MyApplication.userFeedTable, values, "name=?", new String[]{newUrl});
        db.close();
        return result;
    }

    /**
     * 数据库查找操作
     */
    public boolean find(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.userFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
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
        Cursor cursor = db.query(MyApplication.userFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            cursor.close();
            db.close();
            Log.v(TAG, url);
            return url;
        }
        cursor.close();
        db.close();
        return null;
    }

    /**
     * 通过名称来查找List中的url(数据解析的链接)
     */
    public String findUrlFromList(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.allFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.moveToNext()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            cursor.close();
            db.close();
            Log.v(TAG, url);
            return url;
        }
        cursor.close();
        db.close();
        return null;
    }

    /**
     * 通过名称来查找link(从xml里解析出来的链接,用来作为缓存文件的名称)
     */
    public String findLink(String FeedName) {
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.userFeedTable, null, "name=?", new String[]{FeedName}, null, null, null);
        if (cursor.moveToNext()) {
            String link = cursor.getString(cursor.getColumnIndex("link"));
            cursor.close();
            db.close();
            Log.v(TAG, link);
            return link;
        }
        cursor.close();
        db.close();
        return null;
    }

    /**
     * 数据库查找操作(返回所有数据库数据),填充到rssFeedInfo中
     */
    public List<RssFeedInfo> findAll() {
        List<RssFeedInfo> feedInfos = new ArrayList<>();
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.userFeedTable, new String[]{"id", "name", "url", "link"}, null, null, null, null, null);
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

    /**
     * 数据库查找操作,查找feedlist的type,填充到groupList<Map<String, String>>
     */
    public List<Map<String, String>> findGroup() {
        String First;
        String Second = null;
        List<Map<String, String>> groupList = new ArrayList<>();
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.allFeedTable, new String[]{"type"}, null, null, "type", null, "type");
        while (cursor.moveToNext()) {
            First = cursor.getString(cursor.getColumnIndex("type"));
            if (!First.equals(Second)) {
                Map<String, String> map = new HashMap<>();
                map.put("type", First);
                Log.v(TAG, First);
                groupList.add(map);
            }
            Second = First;
        }
        cursor.close();
        db.close();
        return groupList;
    }

    /**
     * 数据库查找操作,查找feedlist的type,填充到childList<List<Map<String, String>>>
     */
    public List<List<Map<String, String>>> findChild() {
        String First;
        String Second = null;
        List<List<Map<String, String>>> childList = new ArrayList<>();
        SQLiteDatabase db = rssSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(MyApplication.allFeedTable, new String[]{"type", "name", "url"}, null, null, null, null, "type");
        cursor.moveToFirst();
        First = cursor.getString(cursor.getColumnIndex("type"));
        //Log.v(TAG, First);
        while (!First.equals(Second)) {
            List<Map<String, String>> children = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("url", cursor.getString(cursor.getColumnIndex("url")));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex("name")));
            Log.v(TAG, cursor.getString(cursor.getColumnIndex("url")));
            children.add(map);
            Second = First;
            while (cursor.moveToNext()) {
                First = cursor.getString(cursor.getColumnIndex("type"));
                // Log.v(TAG, First);
                if (First.equals(Second)) {
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    map1.put("url", cursor.getString(cursor.getColumnIndex("url")));
                    Log.v(TAG, cursor.getString(cursor.getColumnIndex("name")));
                    Log.v(TAG, cursor.getString(cursor.getColumnIndex("url")));
                    children.add(map1);
                    Second = First;
                } else {
                    break;
                }
            }
            childList.add(children);
        }
        cursor.close();
        db.close();
        return childList;
    }
}
