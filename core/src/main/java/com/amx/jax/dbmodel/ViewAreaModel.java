package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "V_AREA_GROUP")
public class ViewAreaModel implements IResourceEntity {

	@Id
	@Column(name = "AREA_LOCCOD")
	private BigDecimal areaCode;
	@Column(name = "FUDESC")
	private String areaDesc;
	@Column(name = "SHDESC")
	private String shortDesc;

	public BigDecimal getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(BigDecimal areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	@Override
	public BigDecimal getResourceId() {
		return this.areaCode;
	}

	@Override
	public String getResourceName() {
		return this.areaDesc;
	}

	@Override
	public String getResourceCode() {
		return this.shortDesc;
	}
}
