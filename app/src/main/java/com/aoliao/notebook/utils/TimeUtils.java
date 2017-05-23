package com.aoliao.notebook.utils;

import android.content.Context;

import com.aoliao.notebook.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 你的奥利奥 on 2017/2/11.
 */

public class TimeUtils {
    public static final long HOUR_MILLS = 60 * 60 * 1000;
    public static final long HALF_HOUR_MILLS = HOUR_MILLS / 2;
    public static final long DAY_MILLS = 24 * HOUR_MILLS;
    public static final long HALF_DAY_MILLS = DAY_MILLS / 2;
    public static final long WEEK_MILLS = 7 * DAY_MILLS;
    public static final long MONTH_MILLS = 30 * DAY_MILLS;
    public static final long YEAR_MILLS = 365 * DAY_MILLS;

    //默认日期格式(格式写错会报异常)
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd     HH:mm", Locale.getDefault());
    //简单日期格式
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public TimeUtils() {
        throw new AssertionError();
    }

    public static String getTime(long editINMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(editINMillis));
    }

    public static String getTime(long editINMillis) {
        return getTime(editINMillis, DEFAULT_DATE_FORMAT);
    }

    public  static String getConciseTime(long editInMillis, long nowInMillis, Context context) {
        if (context == null) {
            return "";
        }
        long diff = nowInMillis - editInMillis;
        if (diff > YEAR_MILLS) {
            int year = (int) (diff / YEAR_MILLS);
            return context.getString(R.string.before_year, year);
        }
        if (diff > MONTH_MILLS) {
            int month = (int) (diff / MONTH_MILLS);
            return context.getString(R.string.before_month, month);
        }
        if (diff > WEEK_MILLS) {
            int week = (int) (diff / WEEK_MILLS);
            return context.getString(R.string.before_week, week);
        }
        if (diff > 2 * DAY_MILLS) {
            int day = (int) (diff / DAY_MILLS);
            return context.getString(R.string.before_day, day);
        }
        if (diff > DAY_MILLS) {
            return context.getString(R.string.yesterday);
        }
//        if (diff > HALF_DAY_MILLS) {
//            return context.getString(R.string.today);
//        }
        if (diff > HOUR_MILLS) {
            int hour = (int) (diff / HOUR_MILLS);
            return context.getString(R.string.before_hour, hour);
        }
        if (diff > HALF_HOUR_MILLS) {
            return context.getString(R.string.half_hour);
        }
        return context.getString(R.string.just_now);
    }

    public static String getConciseTime(long editInMillis, Context context) {
        return getConciseTime(editInMillis, getCurrentMillisLong(), context);
    }

    public static long getCurrentMillisLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentMillisString() {
        return getTime(getCurrentMillisLong());
    }

    public static String getCurrentMillisString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentMillisLong(), dateFormat);
    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        StringBuilder dateTime = new StringBuilder();
        dateTime.append(year);
        dateTime.append("-");
        dateTime.append(month);
        dateTime.append("-");
        dateTime.append(day);
        dateTime.append(" ");
        dateTime.append(hour);
        dateTime.append(":");
        dateTime.append(minute);
        dateTime.append(":");
        dateTime.append(second);
        return dateTime.toString();
    }
}
