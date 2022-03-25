package com.top.okya.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

    private DateFormatUtil(){};

    public static String getYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getYearWithTwo() {
        return new SimpleDateFormat("yyyy").format(new Date()).substring(2);
    }

    public static String getYearMonthWithCharacter() {
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }

    public static String getYearMonthDay() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getYearMonthDayBefore() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }

    public static String getYearMonthDayNext(String dt) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("yyyyMMdd").parse(dt));
        c.add(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }

    public static String getTime() {
        return new SimpleDateFormat("HHmmss").format(new Date());
    }

    public static String getTimeWithCharacter() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static String getYearMonthDayWithCharacter() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getTimes() {
        return String.valueOf(new Date().getTime());
    }

    public static String getYearTimeWithCharacter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getFormatDateTime(String dt) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new SimpleDateFormat("yyyyMMddHHmmss").parse(dt.substring(0, 14)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String getFormatDate(String dt) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd")
                .format(new SimpleDateFormat("yyyyMMdd").parse(dt));
    }

    public static String getFormatTime(String dt) throws ParseException {
        return new SimpleDateFormat("HH:mm:ss")
                .format(new SimpleDateFormat("HHmmss").parse(dt));
    }
}