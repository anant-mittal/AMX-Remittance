/**
 * @author Prashant
 */
package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;

public class RemittanceTransactionRequestModel extends RemittanceAdditionalBeneFieldModel implements IRemitTransReqPurpose {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mOtp;
	private String eOtp;
	@NotNull
	private ExchangeRateBreakup exRateBreakup;
	private BigDecimal placeOrderId;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}


	@Override
	public String toString() {
		return "RemittanceTransactionRequestModel [beneId=" + beneId + ", sourceOfFund=" + sourceOfFund
				+ ", localAmount=" + localAmount + ", foreignAmount=" + foreignAmount + ", availLoyalityPoints="
				+ availLoyalityPoints + "]";
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

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}

	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}

	@Override
	public ExchangeRateBreakup getExchangeRateBreakup() {
		return this.exRateBreakup;
	}

}

