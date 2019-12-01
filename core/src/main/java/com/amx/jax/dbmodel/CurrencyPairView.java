package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "VW_EX_CURRENCY_PAIR")
public class CurrencyPairView implements IResourceEntity {

	private static final long serialVersionUID = 1L;

	private BigDecimal appCountryId;
	private BigDecimal pairId;
	private BigDecimal baseCurrencyId;
	private String baseCurrencyQuote;
	private BigDecimal targetCurrencyId;
	private String targetCurrencyQuote;
	private String curPairName;

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(BigDecimal appCountryId) {
		this.appCountryId = appCountryId;
	}

	@Id
	@Column(name = "PAIR_ID")
	public BigDecimal getPairId() {
		return pairId;
	}

	public void setPairId(BigDecimal pairId) {
		this.pairId = pairId;
	}

	@Column(name = "BASE_CURRENCY_ID")
	public BigDecimal getBaseCurrencyId() {
		return baseCurrencyId;
	}

	public void setBaseCurrencyId(BigDecimal baseCurrencyId) {
		this.baseCurrencyId = baseCurrencyId;
	}

	@Column(name = "BASE_CURRENCY_QUOTE")
	public String getBaseCurrencyQuote() {
		return baseCurrencyQuote;
	}

	public void setBaseCurrencyQuote(String baseCurrencyQuote) {
		this.baseCurrencyQuote = baseCurrencyQuote;
	}

	@Column(name = "TARGET_CURRENCY_ID")
	public BigDecimal getTargetCurrencyId() {
		return targetCurrencyId;
	}

	public void setTargetCurrencyId(BigDecimal targetCurrencyId) {
		this.targetCurrencyId = targetCurrencyId;
	}

	@Column(name = "TARGET_CURRENCY_QUOTE")
	public String getTargetCurrencyQuote() {
		return targetCurrencyQuote;
	}

	public void setTargetCurrencyQuote(String targetCurrencyQuote) {
		this.targetCurrencyQuote = targetCurrencyQuote;
	}

	@Column(name = "CURRENCY_PAIR_NAME")
	public String getCurPairName() {
		return curPairName;
	}

	public void setCurPairName(String curPairName) {
		this.curPairName = curPairName;
	}

	@Override
	public BigDecimal resourceId() {
		return this.pairId;
	}

	@Override
	public String resourceName() {
		return this.curPairName;
	}

	@Override
	public String resourceCode() {
		return null;
	}

	@Override
	public String resourceLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

}
