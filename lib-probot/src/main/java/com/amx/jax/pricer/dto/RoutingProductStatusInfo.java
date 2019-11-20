package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class RoutingProductStatusInfo implements Serializable, Comparable<RoutingProductStatusInfo> {

	private static final long serialVersionUID = 4649960676412739176L;

	private BigDecimal correspondentId;
	private String correspondentName;

	private BigDecimal glcbalLocal;
	private BigDecimal glcbalForeign;

	private BigDecimal pendingTrnxAmountLocal;
	private BigDecimal pendingTrnxAmountForeign;

	private BigDecimal netBalanceLocal;
	private BigDecimal netBalanceForeign;

	private BigDecimal fundingCurrencyId;
	private BigDecimal fundingCurrencyQuote;
	private BigDecimal fundingCurrencyBal;

	private List<RemitModeStatusInfo> remitModesStatus;

	public BigDecimal getCorrespondentId() {
		return correspondentId;
	}

	public void setCorrespondentId(BigDecimal correspondentId) {
		this.correspondentId = correspondentId;
	}

	public String getCorrespondentName() {
		return correspondentName;
	}

	public void setCorrespondentName(String correspondentName) {
		this.correspondentName = correspondentName;
	}

	public BigDecimal getGlcbalLocal() {
		return glcbalLocal;
	}

	public void setGlcbalLocal(BigDecimal glcbalLocal) {
		this.glcbalLocal = glcbalLocal;
	}

	public BigDecimal getGlcbalForeign() {
		return glcbalForeign;
	}

	public void setGlcbalForeign(BigDecimal glcbalForeign) {
		this.glcbalForeign = glcbalForeign;
	}

	public BigDecimal getPendingTrnxAmountLocal() {
		return pendingTrnxAmountLocal;
	}

	public void setPendingTrnxAmountLocal(BigDecimal pendingTrnxAmountLocal) {
		this.pendingTrnxAmountLocal = pendingTrnxAmountLocal;
	}

	public BigDecimal getPendingTrnxAmountForeign() {
		return pendingTrnxAmountForeign;
	}

	public void setPendingTrnxAmountForeign(BigDecimal pendingTrnxAmountForeign) {
		this.pendingTrnxAmountForeign = pendingTrnxAmountForeign;
	}

	public BigDecimal getNetBalanceLocal() {
		return netBalanceLocal;
	}

	public void setNetBalanceLocal(BigDecimal netBalanceLocal) {
		this.netBalanceLocal = netBalanceLocal;
	}

	public BigDecimal getNetBalanceForeign() {
		return netBalanceForeign;
	}

	public void setNetBalanceForeign(BigDecimal netBalanceForeign) {
		this.netBalanceForeign = netBalanceForeign;
	}

	public BigDecimal getFundingCurrencyId() {
		return fundingCurrencyId;
	}

	public void setFundingCurrencyId(BigDecimal fundingCurrencyId) {
		this.fundingCurrencyId = fundingCurrencyId;
	}

	public BigDecimal getFundingCurrencyQuote() {
		return fundingCurrencyQuote;
	}

	public void setFundingCurrencyQuote(BigDecimal fundingCurrencyQuote) {
		this.fundingCurrencyQuote = fundingCurrencyQuote;
	}

	public BigDecimal getFundingCurrencyBal() {
		return fundingCurrencyBal;
	}

	public void setFundingCurrencyBal(BigDecimal fundingCurrencyBal) {
		this.fundingCurrencyBal = fundingCurrencyBal;
	}

	public List<RemitModeStatusInfo> getRemitModesStatus() {
		return remitModesStatus;
	}

	public void setRemitModesStatus(List<RemitModeStatusInfo> remitModesStatus) {
		this.remitModesStatus = remitModesStatus;
	}

	@Override
	public int compareTo(RoutingProductStatusInfo o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
