package com.amx.amxlib.model.response;

import java.util.List;
import java.util.Map;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

public class ExchangeRateResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ExchangeRateBreakup exRateBreakup;
	List<BankMasterDTO> bankWiseRates;
	Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails;
	Boolean discountAvailed;
	Boolean costRateLimitReached;

	
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

	public Map<DISCOUNT_TYPE, ExchangeDiscountInfo> getCustomerDiscountDetails() {
		return customerDiscountDetails;
	}

	public void setCustomerDiscountDetails(Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails) {
		this.customerDiscountDetails = customerDiscountDetails;
	}

	public Boolean getDiscountAvailed() {
		return discountAvailed;
	}

	public void setDiscountAvailed(Boolean discountAvailed) {
		this.discountAvailed = discountAvailed;
	}

	public Boolean getCostRateLimitReached() {
		return costRateLimitReached;
	}

	public void setCostRateLimitReached(Boolean costRateLimitReached) {
		this.costRateLimitReached = costRateLimitReached;
	}

	

}
