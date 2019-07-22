package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_PR_HOLIDAY_MASTER")
public class HolidayListMasterModel implements Serializable, Comparable<HolidayListMasterModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String eventName;
	private BigDecimal countryId;
	private BigDecimal eventYear;
	private Date eventDate;
	private BigDecimal eventDayOfTheWeek;
	private String startTime;
	private String endTime;
	private char allDayEvent;
	private String eventCategory;
	private char isActive;
	private String info;
	private BigDecimal flags;
	private String createdBY;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;

	public HolidayListMasterModel() {
	}

	public HolidayListMasterModel(BigDecimal id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(generator = "jax_pr_holiday_mas_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "jax_pr_holiday_mas_seq", sequenceName = "JAX_PR_HOLIDAY_MAS_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "EVENT_NAME")
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "EVENT_YEAR")
	public BigDecimal getEventYear() {
		return eventYear;
	}

	public void setEventYear(BigDecimal eventYear) {
		this.eventYear = eventYear;
	}

	@Column(name = "EVENT_DATE")
	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	@Column(name = "EVENT_DAY_OF_WEEK")
	public BigDecimal getEventDayOfTheWeek() {
		return eventDayOfTheWeek;
	}

	public void setEventDayOfTheWeek(BigDecimal eventDayOfTheWeek) {
		this.eventDayOfTheWeek = eventDayOfTheWeek;
	}

	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "ALL_DAY_EVENT")
	public char getAllDayEvent() {
		return allDayEvent;
	}

	public void setAllDayEvent(char allDayEvent) {
		this.allDayEvent = allDayEvent;
	}

	@Column(name = "EVENT_CATEGORY")
	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String ecentCategory) {
		this.eventCategory = ecentCategory;
	}

	@Column(name = "ISACTIVE")
	public char getIsActive() {
		return isActive;
	}

	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}

	@Column(name = "INFO")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Column(name = "FLAGS")
	public BigDecimal getFlags() {
		return flags;
	}

	public void setFlags(BigDecimal flags) {
		this.flags = flags;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBY() {
		return createdBY;
	}

	public void setCreatedBY(String createdBY) {
		this.createdBY = createdBY;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public int compareTo(HolidayListMasterModel that) {

		if (this.eventDate == null)
			if (that == null || that.eventDate == null)
				return 0; // equal
			else
				return -1; // null is before other strings
		else if (that.eventDate == null)
			return 1; // all other strings are after null
		else
			return this.eventDate.compareTo(that.eventDate);

	}

}
