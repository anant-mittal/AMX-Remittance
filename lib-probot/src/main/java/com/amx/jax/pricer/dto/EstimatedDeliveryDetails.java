package com.amx.jax.pricer.dto;

import java.time.ZonedDateTime;

public class EstimatedDeliveryDetails {

	private long processTimeTotalInHrs;

	private long processTimeOperationalInHrs;

	private long operatinalDelayInDays;

	private long holidayDelayInDays;

	private long completionTT;

	private ZonedDateTime completionDateLocal;

	private ZonedDateTime completionDateForeign;

	private String timezone;

	public long getProcessTimeTotalInHrs() {
		return processTimeTotalInHrs;
	}

	public void setProcessTimeTotalInHrs(long processTimeTotalInHrs) {
		this.processTimeTotalInHrs = processTimeTotalInHrs;
	}

	public long getProcessTimeOperationalInHrs() {
		return processTimeOperationalInHrs;
	}

	public void setProcessTimeOperationalInHrs(long processTimeOperationalInHrs) {
		this.processTimeOperationalInHrs = processTimeOperationalInHrs;
	}

	public long getOperatinalDelayInDays() {
		return operatinalDelayInDays;
	}

	public void setOperatinalDelayInDays(long operatinalDelayInDays) {
		this.operatinalDelayInDays = operatinalDelayInDays;
	}

	public long getHolidayDelayInDays() {
		return holidayDelayInDays;
	}

	public void setHolidayDelayInDays(long holidayDelayInDays) {
		this.holidayDelayInDays = holidayDelayInDays;
	}

	public long getCompletionTT() {
		return completionTT;
	}

	public void setCompletionTT(long completionTT) {
		this.completionTT = completionTT;
	}

	public ZonedDateTime getCompletionDateLocal() {
		return completionDateLocal;
	}

	public void setCompletionDateLocal(ZonedDateTime completionDateLocal) {
		this.completionDateLocal = completionDateLocal;
	}

	public ZonedDateTime getCompletionDateForeign() {
		return completionDateForeign;
	}

	public void setCompletionDateForeign(ZonedDateTime completionDateForeign) {
		this.completionDateForeign = completionDateForeign;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
