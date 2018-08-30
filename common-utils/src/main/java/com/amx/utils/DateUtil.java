package com.amx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * The Class DateUtil.
 */
// Needs further Customizations 
public final class DateUtil {

	/** The Constant GMT. */
	private static final String GMT = "GMT";

	/** The Constant INT_1000. */
	private static final int INT_1000 = 1000;

	/** The Constant ONEDAY. */
	public static final long ONEDAY = 86400L * 1000L;

	/** The Constant DEFAULT_DATE_FORMAT. */
	private static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

	/** The Constant DATE_FORMAT. */
	private static final String DATE_FORMAT = "dd MMM yyyy";

	/** The Constant DATE_FORMAT_STYLE. */
	private static final String DATE_FORMAT_STYLE = "us";

	/** The Constant TIME_FORMAT_STYLE. */
	private static final String TIME_FORMAT_STYLE = "24_hr";

	/** The Constant US_FORMAT. */
	private static final String US_FORMAT = " MMM dd yyyy";

	/** The Constant NON_US_FORMAT. */
	private static final String NON_US_FORMAT = " dd MMM yyyy";

	/** The Constant AMPM_FORMAT. */
	private static final String AMPM_FORMAT = " hh:mm:ss aaa ";

	/** The Constant NON_AMPM_FORMAT. */
	private static final String NON_AMPM_FORMAT = " HH:mm:ss ";

	/** The Constant WEEK_DAY. */
	private static final String WEEK_DAY = "EEE";

	/** The Constant COMMA. */
	private static final String COMMA = ",";


	/**
	 * Instantiates a new date util.
	 */
	private DateUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/**
	 * Parses the date.
	 *
	 * @param dateStr
	 *            the date str
	 * @return the date
	 */
	public static Date parseDate(String dateStr) {
		dateStr = dateStr.trim();
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		format.setTimeZone(TimeZone.getTimeZone(GMT));
		Date newDate;
		try {
			newDate = setDefaultGMTTime(format.parse(dateStr));
		} catch (ParseException e) {
			// try parsing as long
			try {
				newDate = setDefaultGMTTime(new Date(Long.parseLong(dateStr)));
			} catch (NumberFormatException ex) {
				/* return null if date string cannot be parsed */
				newDate = null;
			}
		}
		return newDate;
	}

	/**
	 * Format date.
	 *
	 * @param dateStr
	 *            the date str
	 * @return the string
	 */
	public static String formatDate(String dateStr) {
		Date date = parseDate(dateStr);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		if (date != null) {
			return format.format(date);
		}
		return null;
	}

	/**
	 * Format date.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @param dateFormat
	 *            the date format
	 * @param timeFormat
	 *            the time format
	 * @return the string
	 */
	public static String formatDate(long timestamp, String dateFormat, String timeFormat) {
		Date date = new Date(timestamp);
		String dateStyle = (dateFormat.equalsIgnoreCase(DATE_FORMAT_STYLE)) ? US_FORMAT : NON_US_FORMAT;
		String timeStyle = (timeFormat.equalsIgnoreCase(TIME_FORMAT_STYLE)) ? NON_AMPM_FORMAT : AMPM_FORMAT;
		String style = WEEK_DAY + COMMA + dateStyle + COMMA + timeStyle;
		SimpleDateFormat format = new SimpleDateFormat(style);
		return format.format(date);
	}

	/**
	 * Format date.
	 *
	 * @param date
	 *            the date
	 * @return the string
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return format.format(date);
	}

	/**
	 * Forward date.
	 *
	 * @param date
	 *            the date
	 * @param time
	 *            the time
	 * @return the date
	 */
	public static Date forwardDate(Date date, long time) {
		return new Date(date.getTime() + time);
	}

	/**
	 * Backward date.
	 *
	 * @param date
	 *            the date
	 * @param time
	 *            the time
	 * @return the date
	 */
	public static Date backwardDate(Date date, long time) {
		return new Date(date.getTime() - time);
	}

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	public static Long getCurrentTime() {
		return Long.valueOf(new Date().getTime());
	}

	/**
	 * This function returns date in required timezone. This doesn't return
	 * correct time.
	 *
	 * @param id
	 *            the id
	 * @return the current time
	 */
	public static long getCurrentTime(String id) {
		TimeZone timeZone = TimeZone.getTimeZone(id);
		return getCurrentTime(timeZone);
	}

	/**
	 * This function returns date in required timezone. This doesn't return
	 * correct time.
	 *
	 * @param timeZone
	 *            the time zone
	 * @return the current time
	 */
	public static long getCurrentTime(TimeZone timeZone) {
		Calendar userTimeZoneCal = Calendar.getInstance(timeZone);
		userTimeZoneCal.setTime(new Date());

		Calendar gmtTimeZoneCal = Calendar.getInstance(TimeZone.getTimeZone(GMT));
		final DateFormat gmtFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		gmtFormatter.setTimeZone(TimeZone.getTimeZone(GMT));

		final DateFormat userTimeZoneFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		userTimeZoneFormatter.setTimeZone(timeZone);

		String dateString = userTimeZoneFormatter.format(userTimeZoneCal.getTime());

		Date gmtDate;
		try {
			gmtDate = gmtFormatter.parse(dateString);
			gmtTimeZoneCal.setTime(gmtDate);
			gmtTimeZoneCal.set(Calendar.HOUR, 12);
			gmtTimeZoneCal.set(Calendar.MINUTE, 0);
			gmtTimeZoneCal.set(Calendar.SECOND, 0);
			gmtTimeZoneCal.set(Calendar.MILLISECOND, 0);

		} catch (ParseException e) {
			// IGNORE THIS ERROR as this will never happen
		}
		return gmtTimeZoneCal.getTimeInMillis();

	}

	/**
	 * Gets the current date with time.
	 *
	 * @param ID
	 *            the id
	 * @return : unix timestamp for the current time
	 */
	public static long getCurrentDateWithTime(String ID) {
		TimeZone timeZone = TimeZone.getTimeZone(ID);
		return getCurrentDateWithTime(timeZone);
	}

	/**
	 * Getting timezone offset in milliseconds from GMT.
	 *
	 * @param id
	 *            is timezone. e.g. "GMT +8:30", or for Day Light Saving
	 *            "Europe/London"
	 * @return : current offset in milliseconds from GMT
	 */
	public static long getOffSet(String id) {
		TimeZone timeZone = TimeZone.getTimeZone(id);
		long utc = getCurrentDateWithTime(TimeZone.getTimeZone(GMT));
		return (timeZone.getOffset(utc));
	}

	/**
	 * Getting timezone offset in milliseconds from GMT.
	 *
	 * @param id
	 *            is timezone. e.g. "GMT +8:30", or for Day Light Saving
	 *            "Europe/London"
	 * @return : current offset in milliseconds from GMT
	 */
	public static long getOffSetWithDst(String id) {
		TimeZone timeZone = TimeZone.getTimeZone(id);
		long utc = getCurrentDateWithTimeAndOffset(TimeZone.getTimeZone(GMT));
		/*return (timeZone.getOffset(utc) + timeZone.getDSTSavings());*/
		return timeZone.getOffset(utc);
	}

	/**
	 * This funcion returns date & time in required timezone.
	 *
	 * @param timeZone
	 *            the time zone
	 * @return : unix timestamp for the current time
	 */
	public static long getCurrentDateWithTime(TimeZone timeZone) {
		Calendar userTimeZoneCal = Calendar.getInstance(timeZone);
		userTimeZoneCal.setTimeInMillis(new Date().getTime());
		Calendar serverCal = Calendar.getInstance();
		serverCal.set(Calendar.YEAR, userTimeZoneCal.get(Calendar.YEAR));
		serverCal.set(Calendar.MONTH, userTimeZoneCal.get(Calendar.MONTH));
		serverCal.set(Calendar.DAY_OF_MONTH, userTimeZoneCal.get(Calendar.DAY_OF_MONTH));
		serverCal.set(Calendar.HOUR_OF_DAY, userTimeZoneCal.get(Calendar.HOUR_OF_DAY));
		serverCal.set(Calendar.MINUTE, userTimeZoneCal.get(Calendar.MINUTE));
		serverCal.set(Calendar.SECOND, userTimeZoneCal.get(Calendar.SECOND));
		serverCal.set(Calendar.MILLISECOND, userTimeZoneCal.get(Calendar.MILLISECOND));
		return serverCal.getTimeInMillis();
	}

	/**
	 * Gets the current date with time and offset.
	 *
	 * @param id
	 *            the id
	 * @return the current date with time and offset
	 */
	public static long getCurrentDateWithTimeAndOffset(String id) {
		TimeZone timeZone = TimeZone.getTimeZone(id);
		return getCurrentDateWithTimeAndOffset(timeZone);
	}

	/**
	 * This funcion returns date & time in required timezone with dst offset.
	 *
	 * @param timeZone
	 *            the time zone
	 * @return : unix timestamp for the current time
	 */
	public static long getCurrentDateWithTimeAndOffset(TimeZone timeZone) {
		Calendar userTimeZoneCal = Calendar.getInstance(timeZone);
		return getTimeAndOffset(userTimeZoneCal, new Date().getTime());
	}

	/**
	 * Gets the date with time and offset.
	 *
	 * @param id
	 *            the id
	 * @param timestamp
	 *            the timestamp
	 * @return the date with time and offset
	 */
	public static long getDateWithTimeAndOffset(String id, long timestamp) {
		TimeZone timeZone = TimeZone.getTimeZone(id);
		Calendar userTimeZoneCal = Calendar.getInstance(timeZone);
		return getTimeAndOffset(userTimeZoneCal, timestamp + userTimeZoneCal.getTimeZone().getDSTSavings());
	}

	/**
	 * Gets the time and offset.
	 *
	 * @param userTimeZoneCal
	 *            the user time zone cal
	 * @param timestamp
	 *            the timestamp
	 * @return the time and offset
	 */
	public static long getTimeAndOffset(Calendar userTimeZoneCal, long timestamp) {
		userTimeZoneCal.setTimeInMillis(timestamp);
		Calendar serverCal = Calendar.getInstance();
		serverCal.set(Calendar.YEAR, userTimeZoneCal.get(Calendar.YEAR));
		serverCal.set(Calendar.MONTH, userTimeZoneCal.get(Calendar.MONTH));
		serverCal.set(Calendar.DAY_OF_MONTH, userTimeZoneCal.get(Calendar.DAY_OF_MONTH));
		serverCal.set(Calendar.HOUR_OF_DAY, userTimeZoneCal.get(Calendar.HOUR_OF_DAY));
		serverCal.set(Calendar.MINUTE, userTimeZoneCal.get(Calendar.MINUTE));
		serverCal.set(Calendar.SECOND, userTimeZoneCal.get(Calendar.SECOND));
		serverCal.set(Calendar.MILLISECOND, userTimeZoneCal.get(Calendar.MILLISECOND));
		return serverCal.getTimeInMillis();
	}

	/**
	 * Removes the time.
	 *
	 * @param date
	 *            the date
	 * @return : Date with hours, minutes, second, milliseconds as 0
	 */
	public static Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		// Set time fields to zero
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Put it back in the Date object
		date = cal.getTime();
		return date;
	}

	/**
	 * Removes the time.
	 *
	 * @param date
	 *            the date
	 * @param timezone
	 *            the timezone
	 * @return : Date with hours, minutes, second, milliseconds as 0
	 */
	public static Date removeTime(Date date, TimeZone timezone) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(timezone);
		cal.setTime(date);

		// Set time fields to zero
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Put it back in the Date object
		date = cal.getTime();
		return date;
	}

	/**
	 * Parse the date. Set the time as GMT 12 noon.
	 *
	 * @param date
	 *            the date
	 * @return : Date with the time as GMT 12 noon.
	 */
	public static Date setDefaultGMTTime(Date date) {
		TimeZone timezone = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance(timezone);
		cal.setTime(date);
		// Set time fields to zero
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		// Put it back in the Date object
		date = cal.getTime();
		return date;
	}

	/**
	 * Convenience method to convert Unix timestamp to human readable date.
	 *
	 * @param ts
	 *            the ts
	 * @return : Human Readable Date
	 */
	public static String getReadableDateFromUnixTimestamp(long ts) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a");
		return sdf.format(Long.valueOf(ts * INT_1000));
	}

	/**
	 * Gets the diff in days.
	 *
	 * @param ts1
	 *            From Timestamp in milliseconds
	 * @param ts2
	 *            To Timestamp in milliseconds
	 * @return Difference in Days between ts1 and ts2.
	 */
	public static long getDiffInDays(long ts1, long ts2) {
		return (ts2 - ts1) / ONEDAY;
	}

	/**
	 * Forward ts by days.
	 *
	 * @param ts
	 *            From Timestamp in milliseconds
	 * @param days
	 *            No of days
	 * @return Timestamp forwarded by 'days'
	 */
	public static long forwardTsByDays(long ts, int days) {
		return ts + days * ONEDAY;
	}

	/**
	 * Gets the date start.
	 *
	 * @param ts
	 *            unix time stamp
	 * @return unix time stamp (milliseconds)
	 */
	public static long getDateStart(long ts) {

		TimeZone tzGMT = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tzGMT);
		cal.setTimeInMillis(ts);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTimeInMillis();

	}

	/**
	 * Gets the date end.
	 *
	 * @param ts
	 *            unix time stamp
	 * @return unix time stamp (milliseconds)
	 */
	public static long getDateEnd(long ts) {

		TimeZone tzGMT = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tzGMT);
		cal.setTimeInMillis(ts);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTimeInMillis();

	}

	/**
	 * Today or after.
	 *
	 * @param ts
	 *            : timestamp
	 * @return true (if ts >= start timestamp of the current day), false
	 *         (otherwise).
	 */
	public static boolean todayOrAfter(long ts) {
		boolean after = false;
		long startMs = DateUtil.getDateStart(System.currentTimeMillis());
		if (ts >= startMs) {
			after = true;
		}

		return after;
	}

	/**
	 * Today or before.
	 *
	 * @param ts
	 *            : timestamp
	 * @return true (if ts < start timestamp of the next day), false
	 *         (otherwise).
	 */
	public static boolean todayOrBefore(long ts) {
		boolean before = false;
		long endMs = DateUtil.forwardTsByDays(DateUtil.getDateStart(System.currentTimeMillis()), 1);
		if (ts < endMs) {
			before = true;
		}

		return before;
	}

	/**
	 * Today or after.
	 *
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param date
	 *            the date
	 * @return true, if successful
	 */
	public static boolean todayOrAfter(int year, int month, int date) {
		boolean after = false;

		TimeZone tzGMT = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tzGMT);

		cal.set(year, month, date, 23, 59, 59);
		cal.set(Calendar.MILLISECOND, 999);

		long startMs = DateUtil.getDateStart(System.currentTimeMillis());
		if (cal.getTimeInMillis() >= startMs) {
			after = true;
		}

		return after;
	}

	/**
	 * Today or before.
	 *
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param date
	 *            the date
	 * @return true, if successful
	 */
	public static boolean todayOrBefore(int year, int month, int date) {
		boolean before = false;

		TimeZone tzGMT = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(tzGMT);

		cal.set(year, month, date, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		long endMs = DateUtil.forwardTsByDays(DateUtil.getDateStart(System.currentTimeMillis()), 1);
		if (cal.getTimeInMillis() < endMs) {
			before = true;
		}

		return before;
	}
	

	/**
	 * As date.
	 *
	 * @param localDate the local date
	 * @return the date
	 */
	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.of("UTC"))
				.toInstant());
	}

	/**
	 * As date.
	 *
	 * @param localDateTime the local date time
	 * @return the date
	 */
	public static Date asDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
	}

	/**
	 * As local date.
	 *
	 * @param date the date
	 * @return the local date
	 */
	public static LocalDate asLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC"))
				.toLocalDate();
	}

	/**
	 * As local date time.
	 *
	 * @param date the date
	 * @return the local date time
	 */
	public static LocalDateTime asLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of("UTC"))
				.toLocalDateTime();
	}

	/**
	 * Returns timestamp according to timezone and DST. 
	 *
	 * @param id the id
	 * @param timestamp the timestamp
	 * @return the date with time and offset DST
	 */
	public static long getDateWithTimeAndOffsetDST(String id, long timestamp) { 
		TimeZone timeZone = TimeZone.getTimeZone(id); 
		Calendar userTimeZoneCal = Calendar.getInstance(timeZone); 
		return getTimeAndOffset(userTimeZoneCal, timestamp); 
	}
}
