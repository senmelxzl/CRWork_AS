package com.crwork.app.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * date util class
 *
 * @author xiezhenlin
 */
public class DateUtil {

    public DateUtil() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return dateNowStr
     */
    public static Date getCurrentDate() {
        java.util.Date utilDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            utilDate = sdf.parse(sdf.format(new java.util.Date()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Date(utilDate.getTime());
    }

    /**
     * @return dateNowStr
     */
    public static Date getPreviousDate() {
        java.util.Date utilDate = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            utilDate = sdf.parse(sdf.format(new java.util.Date()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new Date(utilDate.getTime());
    }

    /**
     * 比较日期大小
     *
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date dt1 = df.parse(DATE1);
            java.util.Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
