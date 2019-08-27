package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionDetailsDTO implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal applicationId;
	private String accountNumber;
	private String beneName;
	private String bankName;
	private BigDecimal localCurId;
	private String localCur;
	private BigDecimal foreignCurId;
	private String foreignCurrency;
	private BigDecimal localTrnxAmt;
	private BigDecimal localComAmt;
	private BigDecimal localNetTrnxAmt;
	private BigDecimal exRateAppl;
	private BigDecimal exRateRev;
	
	public BigDecimal getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(BigDecimal applicationId) {
		this.applicationId = applicationId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBeneName() {
		return beneName;
	}

	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getLocalCurId() {
		return localCurId;
	}

	public void setLocalCurId(BigDecimal localCurId) {
		this.localCurId = localCurId;
	}

	public String getLocalCur() {
		return localCur;
	}

	public void setLocalCur(String localCur) {
		this.localCur = localCur;
	}

	public BigDecimal getForeignCurId() {
		return foreignCurId;
	}

	public void setForeignCurId(BigDecimal foreignCurId) {
		this.foreignCurId = foreignCurId;
	}

	public String getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public BigDecimal getLocalTrnxAmt() {
		return localTrnxAmt;
	}

	public void setLocalTrnxAmt(BigDecimal localTrnxAmt) {
		this.localTrnxAmt = localTrnxAmt;
	}

	public BigDecimal getLocalComAmt() {
		return localComAmt;
	}

	public void setLocalComAmt(BigDecimal localComAmt) {
		this.localComAmt = localComAmt;
	}

	public BigDecimal getLocalNetTrnxAmt() {
		return localNetTrnxAmt;
	}

	public void setLocalNetTrnxAmt(BigDecimal localNetTrnxAmt) {
		this.localNetTrnxAmt = localNetTrnxAmt;
	}

	public BigDecimal getExRateAppl() {
		return exRateAppl;
	}

	public void setExRateAppl(BigDecimal exRateAppl) {
		this.exRateAppl = exRateAppl;
	}

	public BigDecimal getExRateRev() {
		return exRateRev;
	}

	public void setExRateRev(BigDecimal exRateRev) {
		this.exRateRev = exRateRev;
	}

}
