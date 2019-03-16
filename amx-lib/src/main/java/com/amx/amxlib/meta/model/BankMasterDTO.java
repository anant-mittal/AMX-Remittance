package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Comparator;

import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.jax.model.AbstractModel;

public class BankMasterDTO extends AbstractModel implements Comparable<BankMasterDTO> {

	private BigDecimal bankId;
	private String bankCode;
	private String bankFullName;
	private String bankShortName;
	private BigDecimal bankCountryId;
	private boolean ibanRequired;

	private ExchangeRateBreakup exRateBreakup;

	@Override
	public String getModelType() {
		return "bankmaster";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((exRateBreakup == null) ? 0 : exRateBreakup.hashCode());
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
		BankMasterDTO other = (BankMasterDTO) obj;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
			return false;
		if (exRateBreakup == null) {
			if (other.exRateBreakup != null)
				return false;
		} else if (!exRateBreakup.equals(other.exRateBreakup))
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
	public int compareTo(BankMasterDTO arg0) {
		BankMasterDTO o1 = this;
		BankMasterDTO o2 = arg0;
		BigDecimal o1Rate = (o1.getExRateBreakup() != null) ? o1.getExRateBreakup().getRate() : null;
		BigDecimal o2Rate = (o2.getExRateBreakup() != null) ? o2.getExRateBreakup().getRate() : null;
		if (o2Rate != null && o1Rate != null) {
			return o2Rate.compareTo(o1Rate);
		}
		return 0;
	}
	
	public static class BankMasterDTOComparator implements Comparator<BankMasterDTO> {

		@Override
		public int compare(BankMasterDTO o1, BankMasterDTO o2) {

			BigDecimal o1Rate = (o1.getExRateBreakup() != null) ? o1.getExRateBreakup().getRate() : null;
			BigDecimal o2Rate = (o2.getExRateBreakup() != null) ? o2.getExRateBreakup().getRate() : null;
			if (o2Rate != null && o1Rate != null) {
				return o2Rate.compareTo(o1Rate);
			}
			return 0;
		}

	}

	public boolean getIbanRequired() {
		return ibanRequired;
	}

	public void setIbanRequired(boolean ibanRequired) {
		this.ibanRequired = ibanRequired;
	}
}
