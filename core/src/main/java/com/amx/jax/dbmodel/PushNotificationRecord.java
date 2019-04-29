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

@Entity
@Table(name = "JAX_PUSH_NOTIFICATION")
public class PushNotificationRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal notificationId;
	private BigDecimal customerId;
	private BigDecimal nationalityId;
	private BigDecimal currencyId;
	private String title;
	private String message;
	private Date notificationDate;
	private BigDecimal countryId;

	@Id
	@GeneratedValue(generator = "jax_notification_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "jax_notification_seq", sequenceName = "JAX_NOTIFICATION_SEQ", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(BigDecimal notificationId) {
		this.notificationId = notificationId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "NATINALITY_ID")
	public BigDecimal getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}

	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "NOTIFICATION_DATE")
	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

}
