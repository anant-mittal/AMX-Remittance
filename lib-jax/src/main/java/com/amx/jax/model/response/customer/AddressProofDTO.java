package com.amx.jax.model.response.customer;

import com.amx.jax.model.ResourceDTO;

public class AddressProofDTO extends ResourceDTO{
	public UploadType uploadType;
	
	public enum UploadType {
	    ADDR
	}

	public UploadType getUploadType() {
		return uploadType;
	}

	public void setUploadType(UploadType uploadType) {
		this.uploadType = uploadType;
	}
}
