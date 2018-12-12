package com.amx.jax.dbmodel.fx;

/**
 * @author 	:Rabil
 * @Date	:14/11/2018
 * @Purposer:To get time slot for Fx delivery.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "EX_DELIVERY_TIME_SLOTS")
public class FxDeliveryTimeSlotMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7068335655273778035L;
	@Id
	@GeneratedValue(generator = "ex_delivery_time_slots_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_delivery_time_slots_seq", sequenceName = "ex_delivery_time_slots_seq", allocationSize = 1)
	@Column(name = "EX_DELIVERY_TIME_SLOT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal deliveryTimeSlotId;
	@Column(name="COUNTRY_ID")
	private BigDecimal countryId;
	
	@Column(name="COMPANY_ID")
	private BigDecimal companyId;
	
	@Column(name="START_TIME")
	private BigDecimal startTime;
	@Column(name="END_TIME")
	private BigDecimal endTime;
	@Column(name="TIME_INTERVAL")
	private BigDecimal timeInterval;
	@Column(name="ISACTIVE")
	private String isActive;
	@Column(name="REMARKS")
	private String remarks;
	@Column(name="NO_Of_DAYS")
	private BigDecimal noOfDays;
	
	@Column(name="OFFICE_END_TIME")
	private BigDecimal officeEndTime;
	
	
	
	public BigDecimal getDeliveryTimeSlotId() {
		return deliveryTimeSlotId;
	}
	public void setDeliveryTimeSlotId(BigDecimal deliveryTimeSlotId) {
		this.deliveryTimeSlotId = deliveryTimeSlotId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getStartTime() {
		return startTime;
	}
	public void setStartTime(BigDecimal startTime) {
		this.startTime = startTime;
	}
	public BigDecimal getEndTime() {
		return endTime;
	}
	public void setEndTime(BigDecimal endTime) {
		this.endTime = endTime;
	}
	public BigDecimal getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(BigDecimal timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public BigDecimal getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(BigDecimal noOfDays) {
		this.noOfDays = noOfDays;
	}
	public BigDecimal getOfficeEndTime() {
		return officeEndTime;
	}
	public void setOfficeEndTime(BigDecimal officeEndTime) {
		this.officeEndTime = officeEndTime;
	}
	
	
}

