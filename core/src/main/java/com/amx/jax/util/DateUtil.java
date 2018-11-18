package com.amx.jax.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


/*
 * Auth: Rabil
 * Date: 25/11/2017
 */
@Component
public class DateUtil {
	private static Logger logger = Logger.getLogger(DateUtil.class);

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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlDate;

	}

	public static void main(String[] args) {
		DateUtil u = new DateUtil();
		u.validateDate("01/01/2018", "dd/MM/yyyy");
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
	 * validates date string according to the format passed
	 * 
	 * @param strDate
	 * @param format
	 * @return if date string is valid Date object is returned otherwise null
	 * 
	 */
	public LocalDate validateDate(String strDate, String format) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			return LocalDate.parse(strDate, formatter);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * parses the date object
	 * 
	 * @param localDate
	 * @param format
	 * @return parsed date
	 * 
	 */
	public String format(LocalDate localDate, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		try {
			return localDate.format(formatter);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @Auth :Rabil
	 * @Date:  Time slot
	 */
	
	public static List<String> getTimeSlotRange(String date,int startTime,int endTime,int timeIntVal){
		logger.info("getTimeRange for Fx Order date :"+date+"\t startTime :"+startTime+"\t endTime:"+endTime+"\t timeIntVal :"+timeIntVal);
		List<String> timeSlotList = new ArrayList<>();
		Date d = new Date();
		SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat sdf = new SimpleDateFormat("h");
	    String todayDate = dateStr.format(d);
	    int hour = Integer.parseInt(sdf.format(d));
	    int j =0;
	    String meridienAm=" am";
	    String meridienPm=" pm";
	    String defaultZero =":00";
	   
	    
	    if(date !=null && !date.equalsIgnoreCase(todayDate)){
	    	todayDate = date;
	    	for (int i =startTime;i<endTime;  i = i+timeIntVal){
				 j = i+timeIntVal;
				 String str = "";
				 str = String.valueOf(i)+defaultZero+(i<12?meridienAm:meridienPm)+ "-"+String.valueOf(j)+defaultZero+(j<12?meridienAm:meridienPm);
				 timeSlotList.add(str);
	    	}
	    }else{
	    	 if (hour>startTime){
	    	    	startTime =hour; 
	    	    }
	    	for (int i =startTime;i<endTime;  i = i+timeIntVal){
				 j = i+timeIntVal;
				 String str = "";
				 str = String.valueOf(i)+defaultZero+(i<12?meridienAm:meridienPm)+ "-"+String.valueOf(j)+defaultZero+(j<12?meridienAm:meridienPm);
				 timeSlotList.add(str);
			}
	    }
	    timeSlotList.add("delivery date :"+todayDate);
		return timeSlotList;
		}
	
	
}
