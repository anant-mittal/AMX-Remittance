package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "VW_GOVERNORATES_AREA")
public class VwGovernateAreaModel implements IResourceEntity, Serializable{

	private static final long serialVersionUID = 2523252921630492806L;
	@Id
	@Column(name="GOVERNORATES_AREA_ID")
	private BigDecimal goverAreaId;
	
	@Column(name="GOVERNORATES_ID")
	private BigDecimal governateId;
	
	@Column(name="FULL_NAME") 
	private String fullName;
	
	@Column(name="FULL_NAME_ARABIC") 
	private String arFullName;
	
	
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
	public BigDecimal getGoverAreaId() {
		return goverAreaId;
	}
	public void setGoverAreaId(BigDecimal goverAreaId) {
		this.goverAreaId = goverAreaId;
	}
	
	
	
	@Override
	public BigDecimal resourceId() {
		return this.goverAreaId;
	}

	@Override
	public String resourceName() {
		return this.fullName;
	}

	@Override
	public String resourceCode() {
		return this.arFullName;
	}
	@Override
	public String resourceLocalName() {
		// TODO Auto-generated method stub
		return null;
	}
}

