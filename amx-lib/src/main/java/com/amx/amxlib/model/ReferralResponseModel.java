package com.amx.amxlib.model;

import com.amx.jax.model.AbstractModel;

public class ReferralResponseModel extends AbstractModel implements Cloneable{
	private static final long serialVersionUID = 1L;
	
	private String customerReferralCode;
	private String link;
//	private Boolean isWinner;
	
	public ReferralResponseModel() {
		
	}
	public String getCustomerReferralCode() {
		return this.customerReferralCode;
	}

	public void setCustomerRefferalCode(String customerReferralCode) {
		this.customerReferralCode = customerReferralCode;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	

	
}