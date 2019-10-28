package com.amx.jax.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.model.response.fx.TimeSlotDto;

import jodd.typeconverter.Convert;




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
	

/*	
	public static List<TimeSlotDto> getTimeSlotRange(BigDecimal startTime, BigDecimal endTime, BigDecimal timeIntVal,
			int noofDay) {
		logger.info("getTimeRange for Fx Order date :\t startTime :" + startTime + "\t endTime:" + endTime
				+ "\t timeIntVal :" + timeIntVal + "\t noofDay :" + noofDay);
		List<String> timeSlotList = new ArrayList<>();
		List<TimeSlotDto> timeSlotDto = new ArrayList<>();

		BigDecimal j = BigDecimal.ZERO;
		String startHour = null;
		String startMinutes = null;
		BigDecimal startTimeHour = BigDecimal.ZERO;
		BigDecimal startTimeMinutes = BigDecimal.ZERO;
		GregorianCalendar calendar = new GregorianCalendar();

		Date d = new Date();
		SimpleDateFormat sdfhours = new SimpleDateFormat("H");
		BigDecimal hour = new BigDecimal(sdfhours.format(d));

		SimpleDateFormat sdfmin = new SimpleDateFormat("m");
		BigDecimal minutes = new BigDecimal(sdfmin.format(d));

		startTime = RoundUtil.roundBigDecimal(startTime, 2);
		String[] splitStartTime = startTime.toString().split("\\.");
		
				
		if (splitStartTime != null) {
			if (splitStartTime.length >= 1 && splitStartTime[0] != null) {
				startHour = splitStartTime[0];
			}
			if (splitStartTime.length >= 2 && splitStartTime[1] != null) {
				startMinutes = splitStartTime[1];
			}
			if (startHour != null) {
				startTimeHour = new BigDecimal(startHour);
			}
			if (startMinutes != null) {
				startTimeMinutes = new BigDecimal(startMinutes);
				if(startTimeMinutes != null && startTimeMinutes.compareTo(new BigDecimal(50)) == 0) {
					startTimeMinutes = new BigDecimal(30);
				}
			}
		}

		BigDecimal startTimeNToday = startTimeHour;

		for (int n = 0; n <= noofDay; n++) {
			if (n == 0) {
				if (hour.compareTo(startTimeHour) >= 0) {
					if (startTimeMinutes.compareTo(BigDecimal.ZERO) != 0 && minutes.compareTo(BigDecimal.ZERO) != 0) {
						String estStartTime = hour.toString().concat(".").concat(startTimeMinutes.toString());
						startTime = new BigDecimal(estStartTime).add(timeIntVal);
					} else {
						String estStartTime = hour.toString().concat(".").concat("00");
						startTime = new BigDecimal(estStartTime).add(timeIntVal);
					}
				}
			} else {
				if (startTimeMinutes.compareTo(BigDecimal.ZERO) != 0 && minutes.compareTo(BigDecimal.ZERO) != 0) {
					String estStartTime = startTimeNToday.toString().concat(".").concat(startTimeMinutes.toString());
					startTime = new BigDecimal(estStartTime);
				} else {
					String estStartTime = startTimeNToday.toString().concat(".").concat("00");
					startTime = new BigDecimal(estStartTime);
				}

			}

			TimeSlotDto dto = new TimeSlotDto();
			timeSlotList = new ArrayList<>();
			for (BigDecimal i = startTime; i.compareTo(endTime) < 0; i = i.add(timeIntVal)) {
				j = i.add(timeIntVal);
				String str = "";
				if (j.compareTo(endTime) <= 0) {
					str = String.valueOf(i).replace(".", ":") + "-" + String.valueOf(j).replace(".", ":");
					timeSlotList.add(str);
				}

				dto.setTimeSlot(timeSlotList);
			}

			SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy");
			calendar.add(calendar.DAY_OF_MONTH, n);
			Date dateD = calendar.getTime();
			dto.setDate(dateStr.format(dateD));
			timeSlotDto.add(dto);
		}

		return timeSlotDto;
	}*/
	
	public static List<TimeSlotDto> getTimeSlotRange(BigDecimal startTime, BigDecimal endTime, BigDecimal timeIntVal,
			int noofDay) {
		logger.info("getTimeRange for Fx Order date :\t startTime :" + startTime + "\t endTime:" + endTime
				+ "\t timeIntVal :" + timeIntVal + "\t noofDay :" + noofDay);
		List<String> timeSlotList = new ArrayList<>();
		List<TimeSlotDto> timeSlotDto = new ArrayList<>();

		BigDecimal j = BigDecimal.ZERO;
		String startHour = null;
		String endHour =null;
		String startMinutes = null;
		String endMinutes = null;
		String timeIntHour = null;
		String timeIntMin = null;
		String estTimeInterval = null;
		BigDecimal startTimeHour = BigDecimal.ZERO;
		BigDecimal endTimeHour = BigDecimal.ZERO;
		BigDecimal startTimeMinutes = BigDecimal.ZERO;
		BigDecimal endTimeMinutes = BigDecimal.ZERO;
		BigDecimal timeIntervalHour = BigDecimal.ZERO;
		BigDecimal timeIntervalMin = BigDecimal.ZERO;
		
		GregorianCalendar calendar = new GregorianCalendar();

		Date d = new Date();
		SimpleDateFormat sdfhours = new SimpleDateFormat("H");
		BigDecimal hour = new BigDecimal(sdfhours.format(d));

		SimpleDateFormat sdfmin = new SimpleDateFormat("m");
		BigDecimal minutes = new BigDecimal(sdfmin.format(d));
		
		timeIntVal = RoundUtil.roundBigDecimal(timeIntVal, 2);
		String[] splitTimeInterval = timeIntVal.toString().split("\\.");
		
		if(splitTimeInterval!=null) {
			if (splitTimeInterval.length >= 1 && splitTimeInterval[0] != null) {
				timeIntHour = splitTimeInterval[0];
			}
			if (splitTimeInterval.length >= 2 && splitTimeInterval[1] != null) {
				timeIntMin = splitTimeInterval[1];
			}
			if (timeIntHour != null) {
				timeIntervalHour = new BigDecimal(timeIntHour);
			}
			if (timeIntMin != null) {
				timeIntervalMin = new BigDecimal(timeIntMin);
				if(timeIntervalMin != null && timeIntervalMin.compareTo(new BigDecimal(50)) == 0) {
					timeIntervalMin = new BigDecimal(30);
				}
			}
			estTimeInterval = timeIntervalHour.toString().concat(".").concat(timeIntervalMin.toString());
		}

		startTime = RoundUtil.roundBigDecimal(startTime, 2);
		String[] splitStartTime = startTime.toString().split("\\.");
		
				
		if (splitStartTime != null) {
			if (splitStartTime.length >= 1 && splitStartTime[0] != null) {
				startHour = splitStartTime[0];
			}
			if (splitStartTime.length >= 2 && splitStartTime[1] != null) {
				startMinutes = splitStartTime[1];
			}
			if (startHour != null) {
				startTimeHour = new BigDecimal(startHour);
			}
			if (startMinutes != null) {
				startTimeMinutes = new BigDecimal(startMinutes);
				if(startTimeMinutes != null && startTimeMinutes.compareTo(new BigDecimal(50)) == 0) {
					startTimeMinutes = new BigDecimal(30);
				}
			}
		}
		
		endTime = RoundUtil.roundBigDecimal(endTime, 2);
		String[] splitEndTime = endTime.toString().split("\\.");
		
				
		if (splitEndTime != null) {
			if (splitEndTime.length >= 1 && splitEndTime[0] != null) {
				endHour = splitEndTime[0];
			}
			if (splitEndTime.length >= 2 && splitEndTime[1] != null) {
				endMinutes = splitEndTime[1];
			}
			if (endHour != null) {
				endTimeHour = new BigDecimal(endHour);
			}
			if (endMinutes != null) {
				endTimeMinutes = new BigDecimal(endMinutes);
				if(endTimeMinutes != null && endTimeMinutes.compareTo(new BigDecimal(50)) == 0) {
					endTimeMinutes = new BigDecimal(30);
				}
			}
		}

		
		if (endTimeMinutes.compareTo(BigDecimal.ZERO) != 0 && minutes.compareTo(BigDecimal.ZERO) != 0) {
			String estEndTime = endTimeHour.toString().concat(".").concat(endTimeMinutes.toString());
			endTime = new BigDecimal(estEndTime);
		
		}

		BigDecimal startTimeNToday = startTimeHour;

		for (int n = 0; n <= noofDay; n++) {
			if (n == 0) {
				if (hour.compareTo(startTimeHour) >= 0) {
					if (startTimeMinutes.compareTo(BigDecimal.ZERO) != 0 && minutes.compareTo(BigDecimal.ZERO) != 0) {
						String estStartTime = hour.toString().concat(".").concat(startTimeMinutes.toString());
						startTime = new BigDecimal(estStartTime).add(new BigDecimal(estTimeInterval));
					} else {
						String estStartTime = hour.toString().concat(".").concat("00");
						startTime = new BigDecimal(estStartTime).add(new BigDecimal(estTimeInterval));
					}
				}
			} else {
				if (startTimeMinutes.compareTo(BigDecimal.ZERO) != 0 && minutes.compareTo(BigDecimal.ZERO) != 0) {
					String estStartTime = startTimeNToday.toString().concat(".").concat(startTimeMinutes.toString());
					startTime = new BigDecimal(estStartTime);
				} else {
					String estStartTime = startTimeNToday.toString().concat(".").concat("00");
					startTime = new BigDecimal(estStartTime);
				}

			}

			TimeSlotDto dto = new TimeSlotDto();
			timeSlotList = new ArrayList<>();
			for (BigDecimal i = startTime; i.compareTo(endTime) < 0; i = i) {
				BigDecimal convertI = convertMinuteTohours(i,BigDecimal.ZERO,endTime);
				BigDecimal convertJ = convertMinuteTohours(convertI,new BigDecimal(estTimeInterval),endTime);
				i = convertJ;
				String str = "";
				if (j.compareTo(endTime) <= 0) {
					str = String.valueOf(convertI).replace(".", ":") + "-" + String.valueOf(convertJ).replace(".", ":");
					timeSlotList.add(str);
				}

				dto.setTimeSlot(timeSlotList);
			}

			SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy");
			calendar.add(calendar.DAY_OF_MONTH, n);
			Date dateD = calendar.getTime();
			dto.setDate(dateStr.format(dateD));
			timeSlotDto.add(dto);
		}

		return timeSlotDto;
	}
	
	public static BigDecimal convertMinuteTohours(BigDecimal value,BigDecimal estTimeInterval,BigDecimal endTime) {
		BigDecimal convertValue = value.add(estTimeInterval);
		BigDecimal hoursCal = RoundUtil.roundBigDecimal(convertValue, 2);
		String[] splithoursCal = hoursCal.toString().split("\\.");
		String startHourCal = null;
		String startMinutesCal = null;
		BigDecimal startTimeHourCal = BigDecimal.ZERO;
		BigDecimal startTimeMinutesCal = BigDecimal.ZERO;
				
		if (splithoursCal != null) {
			if (splithoursCal.length >= 1 && splithoursCal[0] != null) {
				startHourCal = splithoursCal[0];
			}
			if (splithoursCal.length >= 2 && splithoursCal[1] != null) {
				startMinutesCal = splithoursCal[1];
			}
			if (startHourCal != null) {
				startTimeHourCal = new BigDecimal(startHourCal);
			}
			if (startMinutesCal != null) {
				startTimeMinutesCal = new BigDecimal(startMinutesCal);
				startTimeHourCal = (startTimeHourCal.multiply(new BigDecimal(60))).multiply(new BigDecimal(60));
				startTimeHourCal = startTimeHourCal.add(startTimeMinutesCal.multiply(new BigDecimal(60)));
				startTimeHourCal = (startTimeHourCal.divide(new BigDecimal(60), 2, RoundingMode.FLOOR)).divide(new BigDecimal(60), 2, RoundingMode.FLOOR);
				convertValue = startTimeHourCal;
				double doubleNumber = Convert.toDouble(convertValue);
				int hoursval = (int) doubleNumber;
				String doubleAsString = String.valueOf(doubleNumber);
				int indexOfDecimal = doubleAsString.indexOf(".");
				Double minutes = Double.parseDouble(doubleAsString.substring(indexOfDecimal));
				if (minutes != null && minutes != 0) {
					BigDecimal minuteVal = new BigDecimal(minutes * 60);
					if (minuteVal.compareTo(new BigDecimal(30)) == 0) {
						// continue
						convertValue = new BigDecimal(Integer.toString(hoursval).concat(".").concat(minuteVal.toString()));
					}
				}else {
					String estEndTime = Integer.toString(hoursval).concat(".").concat("00");
					convertValue = new BigDecimal(estEndTime);
											
						if (convertValue.compareTo(endTime) == 0)  { 
							convertValue =endTime;
				        } 
				        else if (convertValue.compareTo(endTime) == 1) { 
				        	convertValue =endTime;
				        } 
				        else { 
				        	convertValue = new BigDecimal(estEndTime);
				        } 
						
				}
			}
		}
		return convertValue;
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
	 public static String convertDatetostringWithddMmYyyywithHMinute(Date date) {
		 String dateString = null;
		 try {
			 SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
				 //Date date = new Date();
				 dateString = format.format(date);
		 }catch (Exception e) {
			 e.printStackTrace();
			}
		 return dateString;
	 }
	 
	 public static Date convertStringToDatewithddMmYyyywithHMinute(String dateString) {
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
			Date date = new Date();
			try {
				date = formatter.parse(dateString);
			} catch (ParseException e) {

			}
			return date;
		}
	 
}
