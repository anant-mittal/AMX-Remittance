package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "VW_GOVERNORATES")
public class VwGovernateModel implements IResourceEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2523252921630492806L;
	@Id
	@Column(name="GOVERNORATES_ID")
	private BigDecimal governateId;
	@Column(name="APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	@Column(name="FULL_NAME") 
	private String fullName;
	@Column(name="FULL_NAME_ARABIC") 
	private String arFullName;
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getArFullName() {
		return arFullName;
	}
	public void setArFullName(String arFullName) {
		this.arFullName = arFullName;
	}
	public BigDecimal getGovernateId() {
		return governateId;
	}
	public void setGovernateId(BigDecimal governateId) {
		this.governateId = governateId;
	}
	@Override
	public BigDecimal resourceId() {
		return this.governateId;
	}
	@Override
	public String resourceName() {
		// TODO Auto-generated method stub
		return this.fullName;
	}
	@Override
	public String resourceCode() {
		// TODO Auto-generated method stub
		return this.arFullName;
	}
	@Override
	public String resourceLocalName() {
		// TODO Auto-generated method stub
		return null;
	}
}

