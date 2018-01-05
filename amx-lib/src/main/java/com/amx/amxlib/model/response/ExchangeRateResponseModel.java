package com.amx.amxlib.model.response;

import java.util.List;
import java.util.SortedSet;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.AbstractModel;

public class ExchangeRateResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ExchangeRateBreakup exRateBreakup;

	List<BankMasterDTO> bankWiseRates;

	@Override
	public String getModelType() {
		return "ex_rate";
	}

	public List<BankMasterDTO> getBankWiseRates() {
		return bankWiseRates;
	}

	public void setBankWiseRates(List<BankMasterDTO> bankWiseRates) {
		this.bankWiseRates = bankWiseRates;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

}
