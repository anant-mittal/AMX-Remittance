package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="VW_EX_RATE_ALERT_FREQUENCY")
public class ViewRateAlertFrequency implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="IDNO")
	private BigDecimal idNo;
	
	@Column(name="FREQ_CODE")
	private String frequencyCode;
	
	@Column(name="FREQ_FULL_NAME")
	private String frequencyFullName;
	
	@Column(name="FREQ_SHORT_NAME")
	private String frequencyShortName;
	
	public String getFrequencyShortName() {
		return frequencyShortName;
	}
	public void setFrequencyShortName(String frequencyShortName) {
		this.frequencyShortName = frequencyShortName;
	}
	public String getFrequencyFullName() {
		return frequencyFullName;
	}
	public void setFrequencyFullName(String frequencyFullName) {
		this.frequencyFullName = frequencyFullName;
	}
	public String getFrequencyCode() {
		return frequencyCode;
	}
	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
 
}
