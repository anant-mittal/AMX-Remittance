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
	
	
	public static java.sql.Date convretStringToSqlDate(String strdate){
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
		String date ="10/10/2017"; 
		System.out.println("Date ---->:"+DateUtil.convertStringToDate(DateUtil.todaysDateWithDDMMYY(new Date(), "10")));
		System.out.println("Date to String :"+DateUtil.todaysDateWithDDMMYY(new Date(), "10"));
		
		System.out.println("Convert String to SQL Date :"+DateUtil.convretStringToSqlDate("10/05/2017"));
	}

}
