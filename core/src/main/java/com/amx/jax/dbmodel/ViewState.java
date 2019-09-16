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
@Table(name = "JAX_VW_STATE")
public class ViewState implements Serializable, IResourceEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7323468455635991354L;

	@Id
	@Column(name = "STATE_ID")
	private BigDecimal stateId;

	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;

	@Column(name = "LANGUAGE_ID")
	private BigDecimal languageId;
	@Column(name = "STATE_CODE")
	private String stateCode;
	@Column(name = "STATE_NAME")
	private String stateName;

	public BigDecimal getStateId() {
		return stateId;
	}

	public void setStateId(BigDecimal stateId) {
		this.stateId = stateId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public BigDecimal resourceId() {
		return this.stateId;
	}

	@Override
	public String resourceName() {
		return this.stateName;
	}

	@Override
	public String resourceCode() {
		return this.stateCode;
	}

	@Override
	public String resourceLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

}
