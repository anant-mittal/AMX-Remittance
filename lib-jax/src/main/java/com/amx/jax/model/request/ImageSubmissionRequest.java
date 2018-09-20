package com.amx.jax.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class ImageSubmissionRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private BigDecimal idenetityIntId;
	private String idenetityInt;
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
	public String getIdenetityInt() {
		return idenetityInt;
	}
	public void setIdenetityInt(String idenetityInt) {
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
	@Override
	public String toString() {
		return "ImageSubmissionRequest [customerId=" + customerId + ", idenetityIntId=" + idenetityIntId
				+ ", idenetityInt=" + idenetityInt + ", identityExpiryDate=" + identityExpiryDate + ", image="
				+ Arrays.toString(image) + "]";
	}
	
	
	
	

}
