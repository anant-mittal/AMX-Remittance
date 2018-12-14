package com.amx.jax.model.response.fx;

import com.amx.jax.model.ResourceDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AddressTypeDto extends ResourceDTO {

	@JsonIgnore
	public String getAddressTypeCode() {
		return this.getResourceCode();
	}

	@JsonIgnore
	public void setAddressTypeCode(String addressTypeCode) {
		this.setResourceCode(addressTypeCode);
	}

	@JsonIgnore
	public String getAddressTypeDesc() {
		return this.getResourceName();
	}

	@JsonIgnore
	public void setAddressTypeDesc(String addressTypeDesc) {
		this.setResourceName(addressTypeDesc);
	}

}
