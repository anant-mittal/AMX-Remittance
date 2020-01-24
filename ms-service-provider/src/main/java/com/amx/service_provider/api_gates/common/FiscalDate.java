package com.amx.service_provider.api_gates.common;

import java.util.Calendar;
import java.util.Date;

public class FiscalDate
   {
      private static final int FIRST_FISCAL_MONTH = Calendar.APRIL;

      private Calendar calendarDate;

      public FiscalDate()
	 {
	    calendarDate = Calendar.getInstance();
	 }

      public FiscalDate(
			int year,
			int month,
			int day)
	 {
	    calendarDate = Calendar.getInstance();
	    calendarDate.set(Calendar.YEAR,
			     year);
	    calendarDate.set(Calendar.MONTH,
			     month);
	    calendarDate.set(Calendar.DAY_OF_MONTH,
			     day);
	    calendarDate.set(Calendar.HOUR_OF_DAY,
			     0);
	    calendarDate.set(Calendar.MINUTE,
			     0);
	    calendarDate.set(Calendar.SECOND,
			     0);
	 }

      public FiscalDate(
			Calendar calendarDate)
	 {
	    this.calendarDate = calendarDate;
	 }

      public FiscalDate(
			Date date)
	 {
	    this.calendarDate = Calendar.getInstance();
	    this.calendarDate.setTime(date);
	 }

      public int getFiscalMonth()
	 {
	    int month = calendarDate.get(Calendar.MONTH);
	    int result = ((month - FIRST_FISCAL_MONTH - 1) % 12) + 1;
	    if (result < 0)
	       {
		  result += 12;
	       }
	    return result;
	 }

      public int getFiscalYear()
	 {
	    int month = calendarDate.get(Calendar.MONTH);
	    int year = calendarDate.get(Calendar.YEAR);
	    return (month >= FIRST_FISCAL_MONTH) ? year : year - 1;
	 }

      public int getCalendarMonth()
	 {
	    return calendarDate.get(Calendar.MONTH);
	 }

      public int getCalendarYear()
	 {
	    return calendarDate.get(Calendar.YEAR);
	 }

      // public static void main(String[] args)
      // {
      // //displayFinancialDate(Calendar.getInstance());
      // //displayFinancialDate(setDate(2017, 11, 13));
      // //displayFinancialDate(setDate(2018, 3, 25));
      //
      // System.out.println(FiscalDate.FIRST_FISCAL_MONTH);
      //
      // Calendar calendarDate = Calendar.getInstance();
      //
      // System.out.println(calendarDate.getTime());
      //
      // FiscalDate obj = new FiscalDate(2018,calendarDate.MARCH,13);
      // FiscalDate obj2 = new FiscalDate();
      // System.out.println(obj2.getFiscalYear());
      // }
      //
      // private static Calendar setDate(int year, int month, int day)
      // {
      // Calendar calendar = Calendar.getInstance();
      // calendar.set(Calendar.YEAR, year);
      // calendar.set(Calendar.MONTH, month);
      // calendar.set(Calendar.DAY_OF_MONTH, day);
      // calendar.set(Calendar.HOUR_OF_DAY, 0);
      // calendar.set(Calendar.MINUTE, 0);
      // calendar.set(Calendar.SECOND, 0);
      //
      // return calendar;
      // }
      //
      // private static void displayFinancialDate(Calendar calendar)
      // {
      // FiscalDate fiscalDate = new FiscalDate(calendar);
      // int year = fiscalDate.getFiscalYear();
      // System.out.println("Current Date : " + calendar.getTime().toString());
      // System.out.println("Fiscal Years : " + year + "-" + (year + 1));
      // System.out.println("Fiscal Month : " + fiscalDate.getFiscalMonth());
      // System.out.println(" ");
      // }
   }
