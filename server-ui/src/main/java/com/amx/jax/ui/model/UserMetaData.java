package com.amx.jax.ui.model;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.PersonInfo;

public class UserMetaData {

	public Boolean validSession = false;
	public Boolean active = false;
	PersonInfo personinfo = null;
	CurrencyMasterDTO domCurrency = null;

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
