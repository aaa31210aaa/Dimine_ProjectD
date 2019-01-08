package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDate {
    // 字符串类型日期转化成date类型
    public static Date strToDate(String style, String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateToStr(String style, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(date);
    }
    public static String clanderTodatetime(Calendar calendar, String style) {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());
    }
}
