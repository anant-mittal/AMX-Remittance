package com.amx.jax.cache;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.amx.utils.DateUtil;

public class WorkingHoursData {

	// Work Week - Starting 0: Monday to 6: Sunday
	private boolean[] workWeek = new boolean[7];

	// Working Time From in HrMin - 0: Monday to 6: Sunday, Values: 800,
	// 1030, 2350
	private int[] workTimeFromInHrsMins = new int[7];

	// Working Time To in HrMin - 0: Monday to 6: Sunday, Values: 800,
	// 1030, 2350
	private int[] workTimeToInHrsMins = new int[7];

	// Processing Time in Hours or Fraction of it.
	private long totalProcessingTimeInMins;

	private boolean isHoldaySnooze = false;

	private int snoozeWakeUpInHrMin = 0;

	public WorkingHoursData() {
		Arrays.fill(workWeek, false);
		Arrays.fill(workTimeFromInHrsMins, 0);
		Arrays.fill(workTimeToInHrsMins, 0);
		totalProcessingTimeInMins = 0;
	}

	public boolean[] getWorkWeek() {
		return workWeek;
	}

	public int[] getWorkTimeFromInHrsMins() {
		return workTimeFromInHrsMins;
	}

	public int[] getWorkTimeToInHrsMins() {
		return workTimeToInHrsMins;
	}

	public long getTotalProcessingTimeInMins() {
		return totalProcessingTimeInMins;
	}

	public long getTotalProcessingTimeInSec() {
		return totalProcessingTimeInMins * 60;
	}

	public void setTotalProcessingTimeInMins(long totalProcessingTimeInMins) {
		this.totalProcessingTimeInMins = totalProcessingTimeInMins;
	}

	public boolean isHoldaySnooze() {
		return isHoldaySnooze;
	}

	public void setHoldaySnooze(boolean isHoldaySnooze) {
		this.isHoldaySnooze = isHoldaySnooze;
	}

	public int getSnoozeWakeUpInHrMin() {
		return snoozeWakeUpInHrMin;
	}

	public void setSnoozeWakeUpInHrMin(double snoozeWakeUpInHrMin) {

		int parsedSnoozeWakeUpInHrMin = DateUtil.getHrMinIntVal(String.valueOf(snoozeWakeUpInHrMin));

		this.snoozeWakeUpInHrMin = parsedSnoozeWakeUpInHrMin;
	}

	public boolean setWorkDayOnArabicDoW(int arabicDayOfWeek) {

		int ISODayOfWeek = DateUtil.arabicToISODayOfWeek(arabicDayOfWeek);

		// Mon : 1 through Sun : 7
		if (DateUtil.isValidDayOfWeek(ISODayOfWeek)) {
			workWeek[arabicDayOfWeek - 1] = true;
			return true;
		}

		return false;
	}

	/**
	 * Sets WorkDay Week. Week Starts From Sun
	 * 
	 * @param startDay
	 * @param endDay
	 */
	public boolean setWorkDaysThroughArabicDoW(int arabicStartDay, int arabicEndDay) {

		// range Out of Bound
		if (!DateUtil.isValidDayOfWeek(arabicStartDay) || !DateUtil.isValidDayOfWeek(arabicEndDay))
			return false;

		int startDay = DateUtil.arabicToISODayOfWeek(arabicStartDay);
		int endDay = DateUtil.arabicToISODayOfWeek(arabicEndDay);

		if (startDay <= endDay) {
			// Case Where Range is Straight

			for (int i = startDay - 1; i < endDay; i++) {
				workWeek[i] = true;
			}

		} else {
			// Case where Range is Circular-Reverse
			for (int i = startDay - 1; i < 7; i++) {
				workWeek[i] = true;
			}

			for (int i = 0; i < endDay; i++) {
				workWeek[i] = true;
			}

		}

		return true;

	}

	public boolean setWorkHrsThroughArabicDoW(int arabicStartDay, int arabicEndDay, double workTimeFrom,
			double workTimeTo) {

		int parsedWorkTimeFrom = DateUtil.getHrMinIntVal(String.valueOf(workTimeFrom));
		int parsedWorkTimeTo = DateUtil.getHrMinIntVal(String.valueOf(workTimeTo));

		if (!isValidHrMin(parsedWorkTimeFrom) || !isValidHrMin(parsedWorkTimeTo)) {
			return false;
		}

		// range Out of Bound
		if (!DateUtil.isValidDayOfWeek(arabicStartDay) || !DateUtil.isValidDayOfWeek(arabicEndDay))
			return false;

		int startDay = DateUtil.arabicToISODayOfWeek(arabicStartDay);
		int endDay = DateUtil.arabicToISODayOfWeek(arabicEndDay);

		if (startDay <= endDay) {
			// Case Where Range is Straight

			for (int i = startDay - 1; i < endDay; i++) {
				workWeek[i] = true;
				workTimeFromInHrsMins[i] = parsedWorkTimeFrom;
				workTimeToInHrsMins[i] = parsedWorkTimeTo;
			}

		} else {
			// Case where Range is Circular-Reverse
			for (int i = startDay - 1; i < 7; i++) {
				workWeek[i] = true;
				workTimeFromInHrsMins[i] = parsedWorkTimeFrom;
				workTimeToInHrsMins[i] = parsedWorkTimeTo;
			}

			for (int i = 0; i < endDay; i++) {
				workWeek[i] = true;
				workTimeFromInHrsMins[i] = parsedWorkTimeFrom;
				workTimeToInHrsMins[i] = parsedWorkTimeTo;
			}

		}

		return true;
	}

	public boolean isWorkingDay(int dayOfWeekIndex) {
		return DateUtil.isValidDayOfWeek(dayOfWeekIndex) ? this.workWeek[dayOfWeekIndex - 1] : false;
	}

	public boolean isWorkingDayTime(int dayOfWeekIndex, int compareToTime) {
		if (isWorkingDay(dayOfWeekIndex)) {
			return (workTimeFromInHrsMins[dayOfWeekIndex - 1] <= compareToTime
					&& workTimeToInHrsMins[dayOfWeekIndex - 1] > compareToTime) ? true : false;
		}

		return false;
	}

	public boolean isBeforeWorkingHours(int dayOfWeekIndex, int compareToTime) {
		if (isWorkingDay(dayOfWeekIndex)) {
			return workTimeFromInHrsMins[dayOfWeekIndex - 1] > compareToTime ? true : false;
		}
		return false;
	}

	public boolean isAfterWorkingHours(int dayOfWeekIndex, int compareToTime) {
		if (isWorkingDay(dayOfWeekIndex)) {
			return workTimeToInHrsMins[dayOfWeekIndex - 1] < compareToTime ? true : false;
		}
		return true;
	}

	public long getSnoozeTimeOffsetInSeconds(int dayOfWeekIndex, int hourMinNow) {

		long workWindowOffsetSecs = this.getWorkWindowOffsetInSeconds(dayOfWeekIndex, hourMinNow);

		if (workWindowOffsetSecs < 0) {
			return workWindowOffsetSecs;
		} else {
			// Case where current Hr Min time is either equals or before the work Start
			// Time.

			int curHr = extractHour(hourMinNow);
			int curMin = extractMinute(hourMinNow);

			int snoozeWakeUpHr = extractHour(snoozeWakeUpInHrMin);
			int snoozeWakeUpMin = extractMinute(snoozeWakeUpInHrMin);

			Date nowDate = DateUtil.getCurrentDateAtTime(curHr, curMin, 0, 0);

			Date snoozeWakeUpDate = DateUtil.getCurrentDateAtTime(snoozeWakeUpHr, snoozeWakeUpMin, 0, 0);

			long snoozeWindowOffsetSecs = TimeUnit.SECONDS.convert(snoozeWakeUpDate.getTime() - nowDate.getTime(),
					TimeUnit.SECONDS);

			return snoozeWindowOffsetSecs >= workWindowOffsetSecs ? snoozeWindowOffsetSecs : workWindowOffsetSecs;

		}

	}

	public long getWorkWindowOffsetInSeconds(int dayOfWeekIndex, int hourMinNow) {

		if (isWorkingDay(dayOfWeekIndex) && (hourMinNow >= 0 && hourMinNow <= 2400)
				&& !isAfterWorkingHours(dayOfWeekIndex, hourMinNow)) {

			// Case if time is within the working window.
			if (isWorkingDayTime(dayOfWeekIndex, hourMinNow)) {
				return 0;
			}

			// Case Where time is before the work window

			int curHr = extractHour(hourMinNow);
			int curMin = extractMinute(hourMinNow);

			int workStartHr = extractHour(workTimeFromInHrsMins[dayOfWeekIndex - 1]);
			int workStartMin = extractMinute(workTimeFromInHrsMins[dayOfWeekIndex - 1]);

			Date nowDate = DateUtil.getCurrentDateAtTime(curHr, curMin, 0, 0);

			Date workStartDate = DateUtil.getCurrentDateAtTime(workStartHr, workStartMin, 0, 0);

			long diffInMilliSec = workStartDate.getTime() - nowDate.getTime();

			// long diffHr = TimeUnit.HOURS.convert(diffInMilliSec, TimeUnit.MILLISECONDS);
			// long diffMin = TimeUnit.MINUTES.convert(diffInMilliSec,
			// TimeUnit.MILLISECONDS) - (diffHr * 60);

			// return (int) (diffHr * 100 + diffMin);
			return TimeUnit.SECONDS.convert(diffInMilliSec, TimeUnit.MILLISECONDS);

		}

		return -1;
	}

	public boolean isValidHrMin(int hourMinVal) {

		int hr = extractHour(hourMinVal);
		int min = extractMinute(hourMinVal);

		if (hr >= 0 && min >= 0) {
			return true;
		}

		return false;
	}

	public int extractHour(int hourMinVal) {
		if (hourMinVal >= 0 && hourMinVal <= 2400) {
			int hr = hourMinVal / 100;
			if (hr >= 0 && hr <= 24) {
				return hr;
			}
		}

		return -1;
	}

	public int extractMinute(int hourMinVal) {
		if (hourMinVal >= 0 && hourMinVal <= 2400) {
			int min = hourMinVal % 100;
			if (min >= 0 && min <= 60) {
				return min;
			}
		}

		return -1;
	}

}
