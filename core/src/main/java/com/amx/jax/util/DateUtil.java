package com.amx.jax.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

/*
 * Auth: Rabil
 * Date: 25/11/2017
 */
@Component
public class DateUtil {

	public static String todaysDateWithDDMMYY(Date date, String action) {
		// action ->1 today Date else user defind date
		SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
		String dt = null;
		if (action.equalsIgnoreCase("1")) {
			Date now = new Date();
			dt = sm.format(now);
		} else {
			dt = sm.format(date);
		}
		return dt;
	}

	public static Date convertStringToDate(String dateString) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		try {
			date = formatter.parse(dateString);

		} catch (ParseException e) {

		}
		return date;
	}

	public static java.sql.Date convretStringToSqlDate(String strdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date utilDate;
		java.sql.Date sqlDate = null;
		try {
			utilDate = sdf.parse(strdate);
			sqlDate = new java.sql.Date(utilDate.getTime());
			System.out.println("String converted to java.sql.Date :" + sqlDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlDate;

	}

	public static void main(String[] args) {
		String date = "10/10/2017";
		System.out
				.println("Date ---->:" + DateUtil.convertStringToDate(DateUtil.todaysDateWithDDMMYY(new Date(), "10")));
		System.out.println("Date to String :" + DateUtil.todaysDateWithDDMMYY(new Date(), "10"));

		System.out.println("Convert String to SQL Date :" + DateUtil.convretStringToSqlDate("17/06/2018"));
	}

	/** Added by Rabil */
	public static String getCurrentAccMMYear() {
		Map<Integer, String> data = new HashMap<Integer, String>();
		for (int i = 0; i < 12; i++) {
			if (i < 9) {
				data.put(i, "0" + String.valueOf(i + 1));
			} else {
				data.put(i, String.valueOf(i + 1));
			}
		}

		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));

		System.out.println(Calendar.getInstance().get(Calendar.MONTH));
		return "01/" + data.get(Calendar.getInstance().get(Calendar.MONTH)) + "/" + year;
	}
	
	public static boolean isToday(Date date) {
		if (date == null) {
			return false;
		}
		Date today = Calendar.getInstance().getTime();
		return DateUtils.isSameDay(today, date);
	}

	public Date getMidnightToday() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		return date.getTime();
	}
	
	/**
	 * validates date string
	 * 
	 * @param strDate
	 * @param format
	 * @return if date string is valid Date object is returned otherwise null
	 * 
	 */
	public LocalDateTime validateDate(String strDate, String format) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			return LocalDateTime.parse(strDate, formatter);
		} catch (Exception e) {
			return null;
		}
	}
}
