package com.amx.amxlib.model.response;

import java.util.List;
import java.util.Set;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.AbstractModel;

public class ExchangeRateResponseModel extends AbstractModel {

	ExchangeRateBreakup exRateBreakup;

	Set<BankMasterDTO> bankWiseRates;

	@Override
	public String getModelType() {
		return "ex_rate";
	}

	public Set<BankMasterDTO> getBankWiseRates() {
		return bankWiseRates;
	}

	public void setBankWiseRates(Set<BankMasterDTO> bankWiseRates) {
		this.bankWiseRates = bankWiseRates;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

}
