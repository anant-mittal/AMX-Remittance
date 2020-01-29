package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateEnquiryRespDto implements Serializable {

	public static class BuySellRateDetails implements Serializable {

		private static final long serialVersionUID = -2093574339814519683L;

		private BigDecimal currencyId;
		private String currencyQuote;
		private String currencyCode;

		private BigDecimal countryId;
		private String countryName;
		private String countryCode;

		private BigDecimal bankId;
		private String bankCode;

		private BigDecimal serviceIndId;
		private String serviceIndName;

		private BigDecimal countryBranchId;
		private String branchName;

		private BigDecimal sellRate;
		private BigDecimal buyRate;

		private BigDecimal exchRateMin;
		private BigDecimal exchRateMax;

		private String modifedDate;
		private String approvedDate;

		public BigDecimal getCurrencyId() {
			return currencyId;
		}

		public void setCurrencyId(BigDecimal currencyId) {
			this.currencyId = currencyId;
		}

		public String getCurrencyQuote() {
			return currencyQuote;
		}

		public void setCurrencyQuote(String currencyQuote) {
			this.currencyQuote = currencyQuote;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public BigDecimal getCountryId() {
			return countryId;
		}

		public void setCountryId(BigDecimal countryId) {
			this.countryId = countryId;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public BigDecimal getBankId() {
			return bankId;
		}

		public void setBankId(BigDecimal bankId) {
			this.bankId = bankId;
		}

		public String getBankCode() {
			return bankCode;
		}

		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}

		public BigDecimal getServiceIndId() {
			return serviceIndId;
		}

		public void setServiceIndId(BigDecimal serviceIndId) {
			this.serviceIndId = serviceIndId;
		}

		public String getServiceIndName() {
			return serviceIndName;
		}

		public void setServiceIndName(String serviceIndName) {
			this.serviceIndName = serviceIndName;
		}

		public BigDecimal getCountryBranchId() {
			return countryBranchId;
		}

		public void setCountryBranchId(BigDecimal countryBranchId) {
			this.countryBranchId = countryBranchId;
		}

		public String getBranchName() {
			return branchName;
		}

		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}

		public BigDecimal getSellRate() {
			return sellRate;
		}

		public void setSellRate(BigDecimal sellRate) {
			this.sellRate = sellRate;
		}

		public BigDecimal getBuyRate() {
			return buyRate;
		}

		public void setBuyRate(BigDecimal buyRate) {
			this.buyRate = buyRate;
		}

		public BigDecimal getExchRateMin() {
			return exchRateMin;
		}

		public void setExchRateMin(BigDecimal exchRateMin) {
			this.exchRateMin = exchRateMin;
		}

		public BigDecimal getExchRateMax() {
			return exchRateMax;
		}

		public void setExchRateMax(BigDecimal exchRateMax) {
			this.exchRateMax = exchRateMax;
		}

		public String getModifedDate() {
			return modifedDate;
		}

		public void setModifedDate(String modifedDate) {
			this.modifedDate = modifedDate;
		}

		public String getApprovedDate() {
			return approvedDate;
		}

		public void setApprovedDate(String approvedDate) {
			this.approvedDate = approvedDate;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8634119186978804711L;

	private BigDecimal pageNo;

	private BigDecimal pageSize;

	private List<BuySellRateDetails> rateDetails;

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

	public List<BuySellRateDetails> getRateDetails() {
		return rateDetails;
	}

	public void setRateDetails(List<BuySellRateDetails> rateDetails) {
		this.rateDetails = rateDetails;
	}

}
