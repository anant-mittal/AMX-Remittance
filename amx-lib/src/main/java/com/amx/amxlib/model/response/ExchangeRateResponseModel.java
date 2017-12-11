package com.amx.amxlib.model.response;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.AbstractModel;

public class ExchangeRateResponseModel extends AbstractModel {

	BigDecimal rate;

	List<BankMasterDTO> bankWiseRates;

	@Override
	public String getModelType() {
		return "ex_rate";
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public List<BankMasterDTO> getBankWiseRates() {
		return bankWiseRates;
	}

	public void setBankWiseRates(List<BankMasterDTO> bankWiseRates) {
		this.bankWiseRates = bankWiseRates;
	}

}
