package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * Author : Rabil
 * Date   :22 Nov 2017
 */

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "JAX_VW_DISTRICT")
public class ViewDistrict implements Serializable, IResourceEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "STATE_ID")
	public BigDecimal stateId;

	@Id
	@Column(name = "DISTRICT_ID")
	public BigDecimal districtId;

	@Column(name = "DISTRICT")
	public String districtDesc;
	@Column(name = "LANGUAGE_ID")
	public BigDecimal languageId;

	public BigDecimal getStateId() {
		return stateId;
	}

	public void setStateId(BigDecimal stateId) {
		this.stateId = stateId;
	}

	public BigDecimal getDistrictId() {
		return districtId;
	}

	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}

	public String getDistrictDesc() {
		return districtDesc;
	}

	public void setDistrictDesc(String districtDesc) {
		this.districtDesc = districtDesc;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Override
	public BigDecimal getResourceId() {
		return this.districtId;
	}

	@Override
	public String getResourceName() {
		return this.districtDesc;
	}

	@Override
	public String getResourceCode() {
		return null;
	}

}
