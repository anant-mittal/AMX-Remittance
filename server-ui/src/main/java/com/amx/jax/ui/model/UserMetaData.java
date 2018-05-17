package com.amx.jax.ui.model;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.user.UserDevice;

public class UserMetaData extends AbstractModel {

	private static final long serialVersionUID = 1243745569228714127L;
	public Boolean validSession = false;
	public Boolean active = false;
	PersonInfo info = null;
	CurrencyMasterDTO domCurrency = null;
	UserDevice device = null;
	AuthState state = null;
	Tenant tenant = null;
	Language lang = null;
	JaxMetaParameter config = null;
	String cdnUrl = null;
	// List<String> features = new ArrayList<String>();
	String[] features = null;
	
	public JaxMetaParameter getConfig() {
		return config;
	}

	public void setConfig(JaxMetaParameter config) {
		this.config = config;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

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
		this.info = personinfo;
	}

	public PersonInfo getInfo() {
		return this.info;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public String getCdnUrl() {
		return cdnUrl;
	}

	public void setCdnUrl(String cdnUrl) {
		this.cdnUrl = cdnUrl;
	}

	public String[] getFeatures() {
		return features;
	}

	public void setFeatures(String[] features) {
		this.features = features;
	}
}
