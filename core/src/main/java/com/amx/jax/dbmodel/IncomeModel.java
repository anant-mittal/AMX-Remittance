package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "FS_INCOME_RANGE_VALUE_MASTER")
public class IncomeModel implements IResourceEntity, Serializable {
	
	private static final long serialVersionUID = 7309549091432024935L;
	
	@Id
	@Column(name="INCOME_RANGE_VALUE_MASTER_ID")
	private BigDecimal incomeRangeMasterId;
	
	public BigDecimal getIncomeRangeMasterId() {
		return incomeRangeMasterId;
	}

	public void setIncomeRangeMasterId(BigDecimal incomeRangeMasterId) {
		this.incomeRangeMasterId = incomeRangeMasterId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	
	@Column(name = "INCOME_RANGE_FROM")
	private BigDecimal incomeRangeFrom;
	
	@Column(name = "INCOME_RANGE_TO")
	private BigDecimal incomeRangeTo;
	
	@Column(name = "ISACTIVE")
	private String activeStatus;
	
	
	@Column(name = "RANGE_TYPE")
	private String rangeType;

	

	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getIncomeRangeFrom() {
		return incomeRangeFrom;
	}

	public void setIncomeRangeFrom(BigDecimal incomeRangeFrom) {
		this.incomeRangeFrom = incomeRangeFrom;
	}

	public BigDecimal getIncomeRangeTo() {
		return incomeRangeTo;
	}

	public void setIncomeRangeTo(BigDecimal incomeRangeTo) {
		this.incomeRangeTo = incomeRangeTo;
	}
	
	@Override
	public BigDecimal resourceId() {
		
		return null;
	}
	@Override
	public String resourceName() {
		
		return null;
	}

	@Override
	public String resourceCode() {
		
		return null;
	}
	
}
