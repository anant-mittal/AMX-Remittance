package com.amx.jax.ui.model;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.user.UserDevice;

public class UserMetaData extends AbstractModel {

	private static final long serialVersionUID = 1243745569228714127L;
	public Boolean validSession = false;
	public Boolean active = false;
	PersonInfo personinfo = null;
	CurrencyMasterDTO domCurrency = null;
	UserDevice device = null;
	AuthState state = null;

	public AuthState getState() {
		return state;
	}

	public void setState(AuthState state) {
		this.state = state;
	}

	public UserDevice getDevice() {
		return device;
	}

	public void setDevice(UserDevice device) {
		this.device = device;
	}

	public CurrencyMasterDTO getDomCurrency() {
		return domCurrency;
	}

	public void setDomCurrency(CurrencyMasterDTO domCurrency) {
		this.domCurrency = domCurrency;
	}

	public Boolean getValidSession() {
		return validSession;
	}

	public void setValidSession(Boolean validSession) {
		this.validSession = validSession;
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
