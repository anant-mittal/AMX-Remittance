package com.amx.amxlib.constant;

import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */

public enum PrefixEnum {

	MR_CODE("MR", "Mr.",new BigDecimal(181)), MRS_CODE("M/S", "M/s.",new BigDecimal(180)), MS("MS","Ms.",new BigDecimal(182));

	private String code;

	private String value;
	
	private BigDecimal componentDataId;
	
	private PrefixEnum(String code, String value,BigDecimal componentDataId) {
		this.code = code;
		this.value = value;
		this.componentDataId=componentDataId;
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
	
	public static PrefixEnum  getPrefixEnum(String componentDataId) {
		PrefixEnum[] allPrefix = PrefixEnum.values();
		for (PrefixEnum p : allPrefix) {
			if (p.getComponentDataId().toString().equals(componentDataId)) {
				return p;
			}
		}
		return null;
	}
}
