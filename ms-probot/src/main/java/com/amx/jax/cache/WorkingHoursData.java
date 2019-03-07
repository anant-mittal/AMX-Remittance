package com.amx.jax.cache;

import java.util.Arrays;

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
	private double processTimeInHrs;

	public WorkingHoursData() {
		Arrays.fill(workWeek, false);
		Arrays.fill(workTimeFromInHrsMins, 0);
		Arrays.fill(workTimeToInHrsMins, 0);
		processTimeInHrs = 0;
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

	public double getProcessTimeInHrs() {
		return processTimeInHrs;
	}

	public void setProcessTimeInHrs(double processTimeInHrs) {
		this.processTimeInHrs = processTimeInHrs;
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

	public boolean setWorkHrsThroughArabicDoW(int arabicStartDay, int arabicEndDay, int workTimeFrom, int workTimeTo) {

		Boolean isWorkWeek = this.setWorkDaysThroughArabicDoW(arabicStartDay, arabicEndDay);

		if (!isWorkWeek)
			return false;

		for (int i = 0; i < workWeek.length; i++) {
			if (true == workWeek[i]) {
				workTimeFromInHrsMins[i] = workTimeFrom;
				workTimeToInHrsMins[i] = workTimeTo;
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

}
