package com.amx.jax.ui.model;

import java.util.HashMap;
import java.util.Map;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.PersonInfo;

public class UserMetaData extends AbstractModel {

	private static final long serialVersionUID = 1243745569228714127L;
	public Boolean validSession = false;
	public Boolean active = false;
	PersonInfo personinfo = null;
	CurrencyMasterDTO domCurrency = null;
	Map<String, Object> device = new HashMap<String, Object>();

	public Map<String, Object> getDevice() {
		return device;
	}

	public void setDevice(Map<String, Object> device) {
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
