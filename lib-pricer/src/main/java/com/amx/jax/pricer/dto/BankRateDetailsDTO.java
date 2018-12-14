package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;

public class BankRateDetailsDTO implements Comparable<BankRateDetailsDTO>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5376785365468336325L;
	
	private BigDecimal bankId;
	private String bankCode;
	private String bankFullName;
	private String bankShortName;
	private BigDecimal bankCountryId;
	
	private BigDecimal serviceIndicatorId;

	private ExchangeRateBreakup exRateBreakup;

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

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getBankShortName() {
		return bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}

	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}

	public BigDecimal getServiceIndicatorId() {
		return serviceIndicatorId;
	}

	public void setServiceIndicatorId(BigDecimal serviceIndicatorId) {
		this.serviceIndicatorId = serviceIndicatorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
		result = prime * result + ((bankCountryId == null) ? 0 : bankCountryId.hashCode());
		result = prime * result + ((bankFullName == null) ? 0 : bankFullName.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((bankShortName == null) ? 0 : bankShortName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankRateDetailsDTO other = (BankRateDetailsDTO) obj;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (bankCountryId == null) {
			if (other.bankCountryId != null)
				return false;
		} else if (!bankCountryId.equals(other.bankCountryId))
			return false;
		if (bankFullName == null) {
			if (other.bankFullName != null)
				return false;
		} else if (!bankFullName.equals(other.bankFullName))
			return false;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
			return false;
		if (bankShortName == null) {
			if (other.bankShortName != null)
				return false;
		} else if (!bankShortName.equals(other.bankShortName))
			return false;
		return true;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

	@Override
	public int compareTo(BankRateDetailsDTO arg0) {
		BankRateDetailsDTO o1 = this;
		BankRateDetailsDTO o2 = arg0;
		BigDecimal o1Rate = (o1.getExRateBreakup() != null) ? o1.getExRateBreakup().getRate() : null;
		BigDecimal o2Rate = (o2.getExRateBreakup() != null) ? o2.getExRateBreakup().getRate() : null;
		if (o2Rate != null && o1Rate != null) {
			return o2Rate.compareTo(o1Rate);
		}
		return 0;
	}
	
	public static class BankMasterDTOComparator implements Comparator<BankRateDetailsDTO> {

		@Override
		public int compare(BankRateDetailsDTO o1, BankRateDetailsDTO o2) {

			BigDecimal o1Rate = (o1.getExRateBreakup() != null) ? o1.getExRateBreakup().getRate() : null;
			BigDecimal o2Rate = (o2.getExRateBreakup() != null) ? o2.getExRateBreakup().getRate() : null;
			if (o2Rate != null && o1Rate != null) {
				return o2Rate.compareTo(o1Rate);
			}
			return 0;
		}

	}
}
