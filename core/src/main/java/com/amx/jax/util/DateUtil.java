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

import com.amx.jax.model.response.fx.TimeSlotDto;




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
	

	
	public static List<TimeSlotDto> getTimeSlotRange(int startTime,int endTime,int timeIntVal,int noofDay){
	logger.info("getTimeRange for Fx Order date :\t startTime :"+startTime+"\t endTime:"+endTime+"\t timeIntVal :"+timeIntVal+"\t noofDay :"+noofDay);
	List<String> timeSlotList = new ArrayList<>();
	List<TimeSlotDto> timeSlotDto = new ArrayList<>();
	Date d = new Date();
	
	SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("H");
    String todayDate = dateStr.format(d);
    int hour = Integer.parseInt(sdf.format(d));
    int j =0;
    String defaultZero =":00";
    GregorianCalendar calendar = new GregorianCalendar();
    Date now = calendar.getTime();
    int startTimeNToday = startTime;
   
    	for(int n=0;n<=noofDay;n++){
    		if(n==0){
    			if (hour>=startTime){
    		    	startTime =hour+timeIntVal; 
    		    }
    		}else{
    			startTime=startTimeNToday;
    		}
    		
	    	TimeSlotDto dto = new TimeSlotDto();
	    	 timeSlotList = new ArrayList<>();
	    	for (int i =startTime;i<endTime;  i = i+timeIntVal){
	   		 j = i+timeIntVal;
	   		 String str = "";
	   		 if(j<=endTime){
	   		 	str = String.valueOf(i)+defaultZero+ "-"+String.valueOf(j)+defaultZero;
	   		  timeSlotList.add(str);
	   		 }
	   		
	   		 dto.setTimeSlot(timeSlotList);
	    	}
	    	 calendar.add(calendar.DAY_OF_MONTH, n);
	    	 Date dateD = calendar.getTime();
	    	 dto.setDate(dateStr.format(dateD));
	    	 timeSlotDto.add(dto);

    }
    
   
    return timeSlotDto;
}
	
	public static  Date daysAddInCurrentDate(int noOfDays) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
	    calendar.add(Calendar.DATE, noOfDays);
	    return calendar.getTime();
	}
	
	
	 public static String getAccountingMonthYearNew(String transactionDate) {
		   String accountingMonthYear=null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			LocalDate date =LocalDate.parse(transactionDate, formatter);
			   accountingMonthYear = null;
			   int dd =0;
			   int mm = 0;
			   int yyyy = 0;
			   String mmS ="0";
			   if(date != null) {
			    dd = date.getDayOfMonth();
			    mm = date.getMonthValue();
			    yyyy = date.getYear();
			    
			    if(mm<9) {
			    	mmS +=mm;
			    }else {
			    	mmS =String.valueOf(mm);
			    }
			    accountingMonthYear ="01"+"/"+mmS+"/"+yyyy;
			   }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return accountingMonthYear;
	   }
	 
	 /** added by Rabil 30 07 2019 **/
	 public static String convertDatetostringWithddMmYyyywithHMinute() {
		 String dateString = null;
		 try {
			 SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a");
				Date date = new Date();
				 dateString = format.format(date);
		 }catch (Exception e) {
			 e.printStackTrace();
			}
		 return dateString;
	 }
	 
	 public static Date convertStringToDatewithddMmYyyywithHMinute(String dateString) {
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss a");
			Date date = new Date();
			try {
				date = formatter.parse(dateString);
			} catch (ParseException e) {

			}
			return date;
		}
	 
}
