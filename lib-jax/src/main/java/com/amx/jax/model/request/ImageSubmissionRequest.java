package com.amx.jax.model.request;

import java.math.BigDecimal;
import java.util.Date;

public class ImageSubmissionRequest {
	
	private BigDecimal customerId;
	private BigDecimal idenetityIntId;
	private BigDecimal idenetityInt;
	private Date identityExpiryDate;	
	private byte[] image;
	
	
	
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getIdenetityIntId() {
		return idenetityIntId;
	}
	public void setIdenetityIntId(BigDecimal idenetityIntId) {
		this.idenetityIntId = idenetityIntId;
	}
	public BigDecimal getIdenetityInt() {
		return idenetityInt;
	}
	public void setIdenetityInt(BigDecimal idenetityInt) {
		this.idenetityInt = idenetityInt;
	}
	public Date getIdentityExpiryDate() {
		return identityExpiryDate;
	}
	public void setIdentityExpiryDate(Date identityExpiryDate) {
		this.identityExpiryDate = identityExpiryDate;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	
	
	

}
