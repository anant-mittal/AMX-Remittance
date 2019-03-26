package com.amx.jax.model.response.customer;

import java.io.Serializable;
import java.util.Date;

public class CustomerIdProofDto  implements Serializable {

	String identityType;
	String identityInt;
	Date identityExpiryDate;

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public Date getIdentityExpiryDate() {
		return identityExpiryDate;
	}

	public void setIdentityExpiryDate(Date identityExpiryDate) {
		this.identityExpiryDate = identityExpiryDate;
	}

}
