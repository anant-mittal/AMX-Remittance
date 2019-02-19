package com.amx.jax.pricer.util;

import java.math.BigDecimal;
import java.util.Date;

public class HolidayListDto {
	
	private String eventName;
	private BigDecimal countryId;
	private BigDecimal eventYear;
	private Date eventDate;
	private BigDecimal eventDayOfTheWeek;
	private String startTime;
	private String endTime;
	private char allDayEvent;
	private String eventCategory;
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getEventYear() {
		return eventYear;
	}
	public void setEventYear(BigDecimal eventYear) {
		this.eventYear = eventYear;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public BigDecimal getEventDayOfTheWeek() {
		return eventDayOfTheWeek;
	}
	public void setEventDayOfTheWeek(BigDecimal eventDayOfTheWeek) {
		this.eventDayOfTheWeek = eventDayOfTheWeek;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public char getAllDayEvent() {
		return allDayEvent;
	}
	public void setAllDayEvent(char allDayEvent) {
		this.allDayEvent = allDayEvent;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	
	
	

}
