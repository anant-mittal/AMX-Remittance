package com.amx.amxlib.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.AbstractModel;



public class ReferralDTO extends AbstractModel implements Cloneable{
	private static final long serialVersionUID = 1L;
	@NotNull(message="customerID may not be null")
	private BigDecimal customerId;
	private String customerReferralCode;
	private BigDecimal referredByCustomerId;
	private String isConsumed;
//	private Boolean isWinner;
	
	public ReferralDTO() {
		
	}
	
	
	public BigDecimal getCustomerId() {
		return this.customerId;
	}

	public void setCustomerID(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	

	public String getCustomerReferralCode() {
		return this.customerReferralCode;
	}

	public void setCustomerRefferalCode(String customerReferralCode) {
		this.customerReferralCode = customerReferralCode;
	}

	public BigDecimal getRefferedByCustomerId() {
		return this.referredByCustomerId;
	}

	public void setRefferedByCustomerID(BigDecimal referedByCustomerId) {
		this.referredByCustomerId = referedByCustomerId;
	}
	

	public String isConsumed() {
		return this.isConsumed;
	}

	public void setIsConsumed(String isConsumed) {
		this.isConsumed = isConsumed;
	}
}
