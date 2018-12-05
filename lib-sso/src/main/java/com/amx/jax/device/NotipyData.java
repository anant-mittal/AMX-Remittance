package com.amx.jax.device;

import java.io.Serializable;

public class NotipyData implements Serializable {

	private static final long serialVersionUID = -7419447038177652732L;
	private long updatestamp;
	private long checkstamp;

	private String sac;

	public String getSac() {
		return sac;
	}

	public void setSac(String sac) {
		this.sac = sac;
	}

	public long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(long updatestamp) {
		this.updatestamp = updatestamp;
	}

	public long getCheckstamp() {
		return checkstamp;
	}

	public void setCheckstamp(long checkstamp) {
		this.checkstamp = checkstamp;
	}
}