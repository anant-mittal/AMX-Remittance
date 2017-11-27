package com.amx.jax.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Auth: Rabil
 * Date: 25/11/2017
 */
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

	
	public static Date convertStringToDate(String dateString){
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		try{
			date = formatter.parse(dateString);
			
		}catch(ParseException e){
			
		}
		return date;
	}
	
	public static void main(String[] args) {
		String date ="10/10/2017"; 
		System.out.println("Date ---->:"+DateUtil.convertStringToDate(DateUtil.todaysDateWithDDMMYY(new Date(), "10")));
		System.out.println("Date to String :"+DateUtil.todaysDateWithDDMMYY(new Date(), "10"));
	}

}
