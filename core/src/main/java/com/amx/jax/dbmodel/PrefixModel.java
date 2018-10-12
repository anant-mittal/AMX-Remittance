package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import com.amx.amxlib.constant.PrefixEnum;

/**
 * @author Subodh Bhoir
 *
 */
public class PrefixModel implements java.io.Serializable {
	private static final long serialVersionUID = 2381126548661383520L;
	private String code;
	private String value;
	private BigDecimal componentDataId;

	public PrefixModel() {
		super();
	}

	public PrefixModel(PrefixEnum prefix) {
		this.code = prefix.getCode();
		this.value = prefix.getValue();
		this.componentDataId=prefix.getComponentDataId();
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
}