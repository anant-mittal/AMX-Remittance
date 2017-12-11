package com.amx.jax.ui.model;

import com.amx.jax.ui.response.ResponseDataInterface;

public class UserMetaData implements ResponseDataInterface {

	public Boolean valid = false;
	public Boolean active = false;

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
