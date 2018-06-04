package com.amx.amxlib.constant;

/**
 * @author Subodh Bhoir
 *
 */

public enum PrefixEnum {

	MR_CODE("MR", "Mr."), MRS_CODE("MRS", "Mrs."), MS("MS","Ms.");

	private String code;

	private String value;

	private PrefixEnum(String code, String value) {
		this.code = code;
		this.value = value;
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
