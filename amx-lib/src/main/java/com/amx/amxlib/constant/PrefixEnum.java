package com.amx.amxlib.constant;

import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */

public enum PrefixEnum {

	MR_CODE("MR", "Mr.", new BigDecimal(181), "Male", "63"), MS("M/S", "M/s.", new BigDecimal(180), "Female",
			"64"), MRS_CODE("MS", "Ms.", new BigDecimal(182), "Female", "64");

	private String code;

	private String value;

	private BigDecimal componentDataId;

	private String gender;

	private String titleLocal;

	private PrefixEnum(String code, String value, BigDecimal componentDataId, String gender, String titleLocal) {
		this.code = code;
		this.value = value;
		this.componentDataId = componentDataId;
		this.gender = gender;
		this.titleLocal = titleLocal;
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

	public BigDecimal getComponentDataId() {
		return componentDataId;
	}

	public void setComponentDataId(BigDecimal componentDataId) {
		this.componentDataId = componentDataId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public static PrefixEnum getPrefixEnum(String componentDataId) {
		PrefixEnum[] allPrefix = PrefixEnum.values();
		for (PrefixEnum p : allPrefix) {
			if (p.getComponentDataId().toString().equals(componentDataId)) {
				return p;
			}
		}
		return null;
	}

	public String getTitleLocal() {
		return titleLocal;
	}

	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}
}
