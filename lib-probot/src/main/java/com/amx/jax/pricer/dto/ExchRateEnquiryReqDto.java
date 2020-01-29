package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class ExchRateEnquiryReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7219110301205798923L;

	@NotNull(message = "Currency Id Can not be Null or Empty")
	private BigDecimal currencyId;

	@NotNull(message = "Country Id Can not be Null or Empty")
	private BigDecimal countryId;

	private BigDecimal bankId;

	private BigDecimal serviceIndId;

	private BigDecimal countryBranchId;

	private BigDecimal pageNo;

	private BigDecimal pageSize;

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getServiceIndId() {
		return serviceIndId;
	}

	public void setServiceIndId(BigDecimal serviceIndId) {
		this.serviceIndId = serviceIndId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public BigDecimal getPageNo() {
		return pageNo;
	}

	public void setPageNo(BigDecimal pageNo) {
		this.pageNo = pageNo;
	}

	public BigDecimal getPageSize() {
		return pageSize;
	}

	public void setPageSize(BigDecimal pageSize) {
		this.pageSize = pageSize;
	}

}
