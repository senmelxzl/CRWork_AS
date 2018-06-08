package com.crwork.app.util;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * tools class
 * 
 * @author xiezhenlin
 *
 */
public class LitterUtil {
	public final static int LITTER_TYPE_NO_R = 0;
	public final static int LITTER_TYPE_YES_R = 1;
	public final static double LITTER_PRICE_NO_R = 0.3;
	public final static double LITTER_PRICE_YES_R = 0.7;

	/**
	 * 
	 * @return dateNowStr
	 */
	public static Date getLitterDate() {

		java.util.Date utilDate = new java.util.Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			utilDate = sdf.parse(sdf.format(new java.util.Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new java.sql.Date(utilDate.getTime());
	}
}
