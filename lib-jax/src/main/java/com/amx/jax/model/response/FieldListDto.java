package com.amx.jax.model.response;

import java.math.BigDecimal;

public class FieldListDto {
	private BigDecimal fieldId;
	private String tenant;
	private String nationality;
	private String remitCountry;
	private String key;
	private String value;
	private String details;
	private String component;
	public BigDecimal getFieldId() {
		return fieldId;
	}
	public void setFieldId(BigDecimal fieldId) {
		this.fieldId = fieldId;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getRemitCountry() {
		return remitCountry;
	}
	public void setRemitCountry(String remitCountry) {
		this.remitCountry = remitCountry;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	@Override
	public String toString() {
		return "FieldListDto [fieldId=" + fieldId + ", tenant=" + tenant + ", nationality=" + nationality
				+ ", remitCountry=" + remitCountry + ", key=" + key + ", value=" + value + ", details=" + details
				+ ", component=" + component + "]";
	}
	
	
}
