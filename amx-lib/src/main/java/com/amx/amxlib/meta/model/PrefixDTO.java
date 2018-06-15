package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */
public class PrefixDTO implements Serializable {
	private static final long serialVersionUID = -6590935535931771084L;
	private String code;
	private String value;
	private BigDecimal componentDataId;

	public PrefixDTO() {
		super();
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
