package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.utils.ArgUtil;

//public class RemittanceTransactionDrRequestModel extends RemittanceTransactionRequestModel {

public class RemittanceTransactionDrRequestModel extends RemittanceAdditionalBeneFieldModel { 
//implements IRemitTransReqPurpose {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4453130484416309371L;
	@NotNull
	DynamicRoutingPricingDto dynamicRroutingPricingBreakup;
	private String mOtp;
	private String eOtp;
	private BigDecimal placeOrderId;
	private String paymentType;
	
	

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public DynamicRoutingPricingDto getDynamicRroutingPricingBreakup() {
		return dynamicRroutingPricingBreakup;
	}

	public void setDynamicRroutingPricingBreakup(DynamicRoutingPricingDto dynamicRroutingPricingBreakup) {
		this.dynamicRroutingPricingBreakup = dynamicRroutingPricingBreakup;
	}

	

	@Override
	public ExchangeRateBreakup getExchangeRateBreakup() {
		if (!ArgUtil.isEmpty(this.dynamicRroutingPricingBreakup)) {
			return this.dynamicRroutingPricingBreakup.getExRateBreakup();
		}
		return getExchangeRateBreakup();
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}

	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}
}
