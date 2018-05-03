package com.amx.jax.dbmodel;

import com.amx.amxlib.constant.PrefixEnum;

/**
 * @author Subodh Bhoir
 *
 */

public class PrefixModel implements java.io.Serializable {
	private String code;
	private String value;

	public PrefixModel() {
		super();
	}

	public PrefixModel(PrefixEnum prefix) {
		this.code = prefix.getCode();
		this.value = prefix.getValue();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}