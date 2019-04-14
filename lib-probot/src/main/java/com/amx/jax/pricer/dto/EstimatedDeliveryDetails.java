package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EstimatedDeliveryDetails implements Serializable, Comparable<EstimatedDeliveryDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1512835971575740752L;

	private long processTimeTotalInSeconds = 0;

	private long processTimeOperationalInSeconds = 0;

	private long processTimeAbsoluteInSeconds = 0;

	private long nonWorkingDelayInDays = 0;

	private long holidayDelayInDays = 0;

	private long completionTT = 0;

	private long startTT = 0;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	@JsonIgnore
	private ZonedDateTime startDateForeign;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
	// "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	@JsonIgnore
	private ZonedDateTime completionDateForeign;

	private boolean crossedMaxDeliveryDays = false;

	public long getProcessTimeTotalInSeconds() {
		return processTimeTotalInSeconds;
	}

	public long getProcessTimeOperationalInSeconds() {
		return processTimeOperationalInSeconds;
	}

	public long getProcessTimeAbsoluteInSeconds() {
		return processTimeAbsoluteInSeconds;
	}

	public long getNonWorkingDelayInDays() {
		return nonWorkingDelayInDays;
	}

	public void addToNonWorkingDelayInDays(long operatinalDelayInDays) {
		this.nonWorkingDelayInDays += operatinalDelayInDays;
	}

	public long getHolidayDelayInDays() {
		return holidayDelayInDays;
	}

	public void addToHolidayDelayInDays(long holidayDelayInDays) {
		this.holidayDelayInDays += holidayDelayInDays;
	}

	public long getCompletionTT() {
		return completionTT;
	}

	public void setCompletionTT(long completionTT) {
		this.completionTT = completionTT;
	}

	public ZonedDateTime getStartDateForeign() {
		return startDateForeign;
	}

	public void setStartDateForeign(ZonedDateTime startDateforeign) {
		this.startDateForeign = startDateforeign;
	}

	public ZonedDateTime getCompletionDateForeign() {
		return completionDateForeign;
	}

	public void setCompletionDateForeign(ZonedDateTime completionDateForeign) {
		this.completionDateForeign = completionDateForeign;
	}

	public long getStartTT() {
		return startTT;
	}

	public void setStartTT(long startTT) {
		this.startTT = startTT;
	}


	public boolean isCrossedMaxDeliveryDays() {
		return crossedMaxDeliveryDays;
	}

	public void setCrossedMaxDeliveryDays(boolean crossedMaxDeliveryDays) {
		this.crossedMaxDeliveryDays = crossedMaxDeliveryDays;
	}

	public void addToNonWorkingDelay(long days) {
		this.nonWorkingDelayInDays += days;
	}

	public void addToHolidayDelay(long days) {
		this.holidayDelayInDays += days;
	}

	public void addToProcessTimeAbsolute(long seconds) {
		this.processTimeAbsoluteInSeconds += seconds;
		addToProcessTimeOperational(seconds);
	}

	public void addToProcessTimeOperational(long seconds) {
		this.processTimeOperationalInSeconds += seconds;
		addToProcessTimeTotal(seconds);
	}

	public void addToProcessTimeTotal(long seconds) {
		this.processTimeTotalInSeconds += seconds;
	}

	public void setProcessTimeTotalInSeconds(long processTimeTotalInSeconds) {
		this.processTimeTotalInSeconds = processTimeTotalInSeconds;
	}

	public void setProcessTimeOperationalInSeconds(long processTimeOperationalInSeconds) {
		this.processTimeOperationalInSeconds = processTimeOperationalInSeconds;
	}

	public void setProcessTimeAbsoluteInSeconds(long processTimeAbsoluteInSeconds) {
		this.processTimeAbsoluteInSeconds = processTimeAbsoluteInSeconds;
	}

	public void setNonWorkingDelayInDays(long nonWorkingDelayInDays) {
		this.nonWorkingDelayInDays = nonWorkingDelayInDays;
	}

	public void setHolidayDelayInDays(long holidayDelayInDays) {
		this.holidayDelayInDays = holidayDelayInDays;
	}
	
	
	

	@Override
	public int compareTo(EstimatedDeliveryDetails that) {

		return (this.completionTT < that.completionTT ? -1 : (this.completionTT == that.completionTT ? 0 : 1));

	}

}
