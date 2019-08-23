package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentLinkRespDTO implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal id;
	private String verificationCode;
	private String applicationIds;
	
	private String curQutoe;
	private BigDecimal amount;
	private Date requestData;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(String applicationIds) {
		this.applicationIds = applicationIds;
	}

	public String getCurQutoe() {
		return curQutoe;
	}

	public void setCurQutoe(String curQutoe) {
		this.curQutoe = curQutoe;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getRequestData() {
		return requestData;
	}

	public void setRequestData(Date requestData) {
		this.requestData = requestData;
	}

	
}
