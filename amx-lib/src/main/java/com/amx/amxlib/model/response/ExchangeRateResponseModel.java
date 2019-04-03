package com.amx.amxlib.model.response;

import java.util.List;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ExchangeRateBreakup;

public class ExchangeRateResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ExchangeRateBreakup exRateBreakup;

	List<BankMasterDTO> bankWiseRates;

	public ExchangeRateResponseModel(ExchangeRateBreakup exRateBreakup) {
		super();
		this.exRateBreakup = exRateBreakup;
	}

	public ExchangeRateResponseModel() {
		super();
	}


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
