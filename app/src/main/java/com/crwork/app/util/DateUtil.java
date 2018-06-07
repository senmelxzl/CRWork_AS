package com.crwork.app.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * date util class
 * 
 * @author xiezhenlin
 *
 */
public class DateUtil {

	public DateUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
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
	 * 
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
}
