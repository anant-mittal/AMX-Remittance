package com.amx.jax.ui.model;

import com.amx.amxlib.model.PersonInfo;

public class UserMetaData {

	public Boolean validSession = false;
	public Boolean active = false;
	public PersonInfo personinfo = null;

	public Boolean getValidSession() {
		return validSession;
	}

	public void setValidSession(Boolean valid) {
		this.validSession = valid;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setInfo(PersonInfo personinfo) {
		this.personinfo = personinfo;
	}

	public PersonInfo getInfo() {
		return this.personinfo;
	}
}
