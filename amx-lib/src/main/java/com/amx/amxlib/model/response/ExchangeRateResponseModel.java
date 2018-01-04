package com.amx.amxlib.model.response;

import java.util.SortedSet;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.AbstractModel;

public class ExchangeRateResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ExchangeRateBreakup exRateBreakup;

	SortedSet<BankMasterDTO> bankWiseRates;

	@Override
	public String getModelType() {
		return "ex_rate";
	}

	public SortedSet<BankMasterDTO> getBankWiseRates() {
		return bankWiseRates;
	}

	public void setBankWiseRates(SortedSet<BankMasterDTO> bankWiseRates) {
		this.bankWiseRates = bankWiseRates;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

}
