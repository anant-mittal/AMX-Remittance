package com.amx.amxlib.model.response;
/**
 * Author :Rabil
 */

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;


public class FcSaleOrderApplicationResponseModel extends AbstractModel {
	
	private static final long serialVersionUID = 9184782859804476157L;
	private BigDecimal txnFee;
	private ExchangeRateBreakup exRateBreakup;
	private String denominationType;
	private BigDecimal purposeOfTrnxId;
	private BigDecimal sourceOffundId;
	
	@Override
	public String getModelType() {
		return "fcsale_application";
	}
	
	public BigDecimal getTxnFee() {
		return txnFee;
	}
	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
	}
	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}
	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}
	public String getDenominationType() {
		return denominationType;
	}
	public void setDenominationType(String denominationType) {
		this.denominationType = denominationType;
	}
	public BigDecimal getPurposeOfTrnxId() {
		return purposeOfTrnxId;
	}
	public void setPurposeOfTrnxId(BigDecimal purposeOfTrnxId) {
		this.purposeOfTrnxId = purposeOfTrnxId;
	}
	public BigDecimal getSourceOffundId() {
		return sourceOffundId;
	}
	public void setSourceOffundId(BigDecimal sourceOffundId) {
		this.sourceOffundId = sourceOffundId;
	}
	

}
