package org.foree.duker.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by foree on 3/31/15.
 * 解析两类时间格式:
 * 1.@link parseBuildDate: Tue, 31 Mar 2015 03:10:00 CST
 * 2.@link parseDate: 2015-03-31
 * 对解析出来的时间格式的处理以及界面的显示:
 * 1.超过三天的显示日期,
 * 2.超过1天的显示n天前
 * 3.超过1小时的显示小时
 */
public class DateUtils {
    private static final String TAG = "DateUtils";
    //天的millis数
    private static final long DAYMILLIS = 86400000;//(24x60x60x1000)
    //小时的millis数
    private static final long HOURMILLIS = 3600000;//(60x60x1000)


    //用来解析lastBuildDate中的字段到指定格式
    public static String parseBuildDate(String pubDateString) {
        Date pubDate = null;
        Date date = null;
        String result;
        //格式化传入的时间字符串
        SimpleDateFormat pubDateFormate = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        SimpleDateFormat myDateFormate = new SimpleDateFormat("yyyy-MMM-dd HH");
        SimpleDateFormat resultFormate = new SimpleDateFormat("MMM-dd");
        try {
            date = pubDateFormate.parse(pubDateString);
            String myDate = myDateFormate.format(date);
            pubDate = myDateFormate.parse(myDate);
            Log.v(TAG, myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //现在的时间
        Calendar nowDateCal = Calendar.getInstance();
        nowDateCal.setTime(new Date());
        long nowMillis = nowDateCal.getTimeInMillis();
        Log.v(TAG, nowDateCal.toString());
        //传入的时间
        Calendar pubDateCal = Calendar.getInstance();
        pubDateCal.setTime(pubDate);
        long pubMillis = pubDateCal.getTimeInMillis();
        /**
         * 计算差值,如果小时大于24,使用天,如果天数大于3,使用日期
         */
        long diff = pubMillis - nowMillis;
        if (diff < 24 * HOURMILLIS) {
            //小于24小时,使用小时计数
            result = (int) diff / HOURMILLIS + "h";
        } else if (diff < 3 * DAYMILLIS) {
            result = (int) diff / DAYMILLIS + "d";
        } else
            result = resultFormate.format(date);

        return result;
    }

    /**
     * 解析description中匹配到的时间
     */
    public static String parseDate(String pubDateString) {
        Date pubDate = null;
        String result;
        SimpleDateFormat pubDateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat resultFormate = new SimpleDateFormat("MM-dd");
        try {
            pubDate = pubDateFormate.parse(pubDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar pubCalendar = Calendar.getInstance();
        pubCalendar.setTime(pubDate);
        long pubMillis = pubCalendar.getTimeInMillis();

        //现在的时间
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        long nowMillis = nowCalendar.getTimeInMillis();

        long diff = nowMillis - pubMillis;
        if (diff < 24 * HOURMILLIS) {
            //小于24小时,使用小时格式
            result = "/" + (int) diff / HOURMILLIS + "h";
        } else if (diff < 3 * DAYMILLIS) {
            //大于24小时,而小于3天
            result = "/" + (int) diff / DAYMILLIS + "d";
        } else
            result = "/" + resultFormate.format(pubDate);

        return result;
    }

}
