package com.amx.jax.dbmodel;

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

/**
 * @author 
 */    

@Entity
@Table(name = "EX_ONLINE_RATE_ALERTS")
public class RateAlert implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal onlineRateAlertId;
	//private Customer customerId;
	private BigDecimal customerId;
	
	private BigDecimal baseCurrencyId;
	private String baseCurrencyCode;
	private BigDecimal foreignCurrencyId;
	private String foreignCurrencyCode;
	private BigDecimal exchangeRateId;
	private BigDecimal alertRate;
	private String alertEmail;
	private String alertSms;
	private Date lastDate;
	private Date alertLastDate;
	private BigDecimal civilId;
	private String frequency;
	private String isActive;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;
	private BigDecimal exchangeRate;
	private Date fromDate;
	private Date toDate;
	private String rule;
	private BigDecimal payAmount;
	private BigDecimal receiveAmount;
	
	
	public RateAlert() {
	}

	public RateAlert(BigDecimal onlineRateAlertId, BigDecimal customerId,
			BigDecimal baseCurrencyId,String baseCurrencyCode, 
			BigDecimal foreignCurrencyId,String foreignCurrencyCode, BigDecimal exchangeRateId,
			BigDecimal alertRate, String alertEmail, String alertSms,
			Date lastDate, Date alertLastDate, BigDecimal civilId,
			String frequency, String isActive, Date createdDate,
			String createdBy, Date updatedDate, String updatedBy,
			BigDecimal exchangeRate) {
		
		
		this.onlineRateAlertId = onlineRateAlertId;
		this.customerId = customerId;
	
		this.baseCurrencyId = baseCurrencyId;
		this.baseCurrencyCode = baseCurrencyCode;
		this.foreignCurrencyId = foreignCurrencyId;
		this.foreignCurrencyCode = foreignCurrencyCode;
		//this.setExchangeRateId(exchangeRateId);
		this.alertRate = alertRate;
		this.alertEmail = alertEmail;
		this.alertSms = alertSms;
		this.lastDate = lastDate;
		this.alertLastDate = alertLastDate;
		this.civilId = civilId;
		this.frequency = frequency;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.exchangeRate = exchangeRate;
	}

	
	
	@Id
	@GeneratedValue(generator="ex_online_rate_alert_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_online_rate_alert_seq",sequenceName="EX_ONLINE_RATE_ALERT_SEQ",allocationSize=1)
	@Column(name ="ONLINE_RATE_ALERT_ID" , unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getOnlineRateAlertId() {
		return onlineRateAlertId;
	}

	public void setOnlineRateAlertId(BigDecimal onlineRateAlertId) {
		this.onlineRateAlertId = onlineRateAlertId;
	}

	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}



	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "BASE_CURRENCY_ID")
	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	@Column(name = "BASE_CURRENCY_CODE")
	public String getBaseCurrencyCode() {
		return baseCurrencyCode;
	}

	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}

	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "FOREIGN_CURRENCY_ID")
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}

	@Column(name = "FOREIGN_CURRENCY_CODE")
	public String getForeignCurrencyCode() {
		return foreignCurrencyCode;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXCHANGE_RATE_ID")
	public ExchangeRate getExchangeRateId() {
		return exchangeRateId;
	}

	public void setExchangeRateId(ExchangeRate exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}
*/
	@Column(name = "ALERT_RATE")
	public BigDecimal getAlertRate() {
		return alertRate;
	}

	public void setAlertRate(BigDecimal alertRate) {
		this.alertRate = alertRate;
	}

	@Column(name = "ALERT_EMAIL")
	public String getAlertEmail() {
		return alertEmail;
	}

	public void setAlertEmail(String alertEmail) {
		this.alertEmail = alertEmail;
	}

	@Column(name = "ALERT_SMS")
	public String getAlertSms() {
		return alertSms;
	}

	public void setAlertSms(String alertSms) {
		this.alertSms = alertSms;
	}

	@Column(name = "LAST_DATE")
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	@Column(name = "ALERT_LAST_DATE")
	public Date getAlertLastDate() {
		return alertLastDate;
	}

	public void setAlertLastDate(Date alertLastDate) {
		this.alertLastDate = alertLastDate;
	}

	@Column(name = "CIVIL_ID")
	public BigDecimal getCivilId() {
		return civilId;
	}

	public void setCivilId(BigDecimal civilId) {
		this.civilId = civilId;
	}

	@Column(name = "FREQUENCY")
	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_DATE")
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "EXCHANGE_RATE")
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	@Column(name = "EXCHANGE_RATE_ID")
	public  BigDecimal getExchangeRateId() {
		return exchangeRateId;
	}

	public void setExchangeRateId(BigDecimal exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	/**
	 * @return the fromDate
	 */
	@Column(name = "FROM_DATE")
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	@Column(name = "TO_DATE")
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the rule
	 */
	@Column(name = "RULE")
	public String getRule() {
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	public void setRule(String rule) {
		this.rule = rule;
	}

	/**
	 * @return the payAmount
	 */
	@Column(name = "PAY_AMOUNT")
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @return the receiveAmount
	 */
	@Column(name = "RECEIVE_AMOUNT")
	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	/**
	 * @param receiveAmount the receiveAmount to set
	 */
	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}
}
