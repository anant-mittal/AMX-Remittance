package com.amx.jax.ui.response;

public class RegistrationdData implements ResponseData {

	private Boolean valid = false;
	private Boolean otpsent = false;

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getOtpsent() {
		return otpsent;
	}

	public void setOtpsent(Boolean otpsent) {
		this.otpsent = otpsent;
	}
	
	
	

}
