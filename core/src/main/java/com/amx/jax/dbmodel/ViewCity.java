package com.amx.jax.dbmodel;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "JAX_VW_CITY")
public class ViewCity implements Serializable, IResourceEntity {

	private static final long serialVersionUID = -41874977422090853L;

	@Id
	@Column(name = "IDNO")
	private BigDecimal idNo;

	@Column(name = "CITY_MASTER_DESC_ID")
	private BigDecimal cityMasterDescId;

	@Column(name = "LANGUAGE_ID")
	private BigDecimal languageId;

	@Column(name = "CITY_MASTER_ID")
	private BigDecimal cityMasterId;

	@Column(name = "CITY_NAME")
	private String cityName;

	@Column(name = "DISTRICT_ID")
	private BigDecimal districtId;

	public BigDecimal getCityMasterDescId() {
		return cityMasterDescId;
	}

	public void setCityMasterDescId(BigDecimal cityMasterDescId) {
		this.cityMasterDescId = cityMasterDescId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public BigDecimal getCityMasterId() {
		return cityMasterId;
	}

	public void setCityMasterId(BigDecimal cityMasterId) {
		this.cityMasterId = cityMasterId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public BigDecimal getDistrictId() {
		return districtId;
	}

	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}

	public BigDecimal getIdNo() {
		return idNo;
	}

	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}

	@Override
	public BigDecimal resourceId() {
		return this.cityMasterId;
	}

	@Override
	public String resourceName() {
		return this.cityName;
	}

	@Override
	public String resourceCode() {
		return null;
	}

}
