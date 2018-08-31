package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_EMPLOYMENT_TYPE")
public class EmploymentTypeMasterView implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private BigDecimal componentDataId;
	private BigDecimal componentId;
	private String componentCode;
	private BigDecimal languageId;
	private String dataDesc;
	
	@Id
	@Column(name = "COMPONENT_DATA_ID")
	public BigDecimal getComponentDataId() {
		return componentDataId;
	}
	public void setComponentDataId(BigDecimal componentDataId) {
		this.componentDataId = componentDataId;
	}
	
	@Column(name="COMPONENT_ID")
	public BigDecimal getComponentId() {
		return componentId;
	}
	public void setComponentId(BigDecimal componentId) {
		this.componentId = componentId;
	}
	
	@Column(name = "COMPONENT_CODE")
	public String getComponentCode() {
		return componentCode;
	}
	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}
	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	@Column(name = "DATA_DESC")
	public String getDataDesc() {
		return dataDesc;
	}
	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
	
	

}
