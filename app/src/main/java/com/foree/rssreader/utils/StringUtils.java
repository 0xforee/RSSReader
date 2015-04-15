package com.foree.rssreader.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by foree on 4/15/15.
 * 一些特殊的String的匹配规则
 */
public class StringUtils {

    private static final String TAG = "StringUtils";

    public static final int FLAG_IMAGEURL = 0;
    public static final int FLAG_DATE = 1;

    /**
     * regex for imageUrl in Description
     */
    private static final String imagePattern = "http://mmbiz\\.qpic\\.cn/mmbiz/[^\"]+";

    /**
     * regex for date in Description
     */
    private static final String datePattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    /**
     * match ImageUrl(only weChat Image now) or Date in the Description String.
     */
    public static String matchString(String msg, int flag) {
        Pattern pattern;
        String result = "";

        if (flag == StringUtils.FLAG_IMAGEURL)
            pattern = Pattern.compile(imagePattern);
        else
            pattern = Pattern.compile(datePattern);

        Matcher matcher = pattern.matcher(msg);

        if (matcher.find()) {
            result = matcher.group();

            if (flag == StringUtils.FLAG_IMAGEURL) {
                if (LogUtils.isCompilerLog) LogUtils.v(TAG, "(匹配只适用于微信公众号)图片链接:" + result);
            } else {
                result = DateUtils.parseDate(result);
                if (LogUtils.isCompilerLog) LogUtils.v(TAG, "日期:" + result);
            }
        }
        return result;
    }

    /**
     * format title to remove space and \n
     *
     * @param title rss title
     * @return title after format
     */
    public static String formatTitle(String title) {

        //replace all space to null
        title = title.replaceAll(" +", "");
        //replace all \n to null
        title = title.replaceAll("\n", "");

        return title;
    }
}
