package com.amx.jax.dbmodel.auth;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.event.JaxAuthLogEvent;

@Entity
@Table(name = "JAX_AUTH_FAILURE_LOG")
public class AuthFailureLog {

	@Id
	@GeneratedValue(generator = "JAX_AUTH_FAILURE_LOG_SEQ", strategy = GenerationType.SEQUENCE)
	@Column(name = "AUTH_FAILURE_LOG_ID")
	@SequenceGenerator(sequenceName = "JAX_AUTH_FAILURE_LOG_SEQ", name = "JAX_AUTH_FAILURE_LOG_SEQ", allocationSize = 1)
	BigDecimal authFailureLogId;

	@Column(name = "EVENT_DATE")
	Date eventDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "EVENT_NAME")
	JaxAuthLogEvent eventName;

	@Column(name = "IP_ADDRESS")
	String ipAddress;

	@Column(name = "IDENTITY_INT")
	String identityInt;

	public BigDecimal getAuthFailureLogId() {
		return authFailureLogId;
	}

	public void setAuthFailureLogId(BigDecimal authFailureLogId) {
		this.authFailureLogId = authFailureLogId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public JaxAuthLogEvent getEventName() {
		return eventName;
	}

	public void setEventName(JaxAuthLogEvent eventName) {
		this.eventName = eventName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

}
