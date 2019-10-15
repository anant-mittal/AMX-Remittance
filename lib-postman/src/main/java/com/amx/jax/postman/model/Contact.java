package com.amx.jax.postman.model;

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;

public class Contact {
	Tenant tenant;
	String country;
	String userid;
	String prefix;
	String mobile;
	String email;
	Language lang;

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Contact prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public Contact mobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public Contact email(String email) {
		this.email = email;
		return this;
	}
}
