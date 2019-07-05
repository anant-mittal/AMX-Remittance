package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class LinkDTO extends AbstractModel implements Cloneable{
	private String linkId;
	private BigDecimal customerId;
	private String customerDetail;
	
    
	
	public LinkDTO() {
		
	}
	
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public String getCustomerDetail() {
		return customerDetail;
	}
	public void setCustomerDetail(String customerDetail) {
		this.customerDetail = customerDetail;
	}	
	
	
}
