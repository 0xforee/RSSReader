package com.foree.rssreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.foree.rssreader.base.MyApplication;

/**
 * Created by foree on 3/16/15.
 * 为RSS创建一个数据库用来保存RSS的类别
 */
public class RssSQLiteOpenHelper extends SQLiteOpenHelper {

    //默认的构造方法,用来传入数据库的名字,游标和版本
    public RssSQLiteOpenHelper(Context context) {
        super(context, "rss.db", null, MyApplication.myDataBaseVersion);
    }

    //在数据库第一次被创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库的表结构,表rss,字段id,name,url
        db.execSQL("create table rss (id integer primary key autoincrement, name varchar(20), url varchar(20), link varchar(20)) ");
        //创建新表,用于存放数据源
        db.execSQL("create table feedlist (id integer primary key autoincrement, type varchar(20), name varchar(20), url varchar(40))");
    }

    //在数据库版本号增加的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            db.execSQL("create table feedlist (id integer primary key autoincrement, type varchar(20), name varchar(20), url varchar(40))");
        }
    }
}
