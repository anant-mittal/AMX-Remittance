package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.model.ResourceDTO;


public class UserFinancialYearDTO extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal financialYearID;
	private BigDecimal financialYear;
	private Date financialYearBegin;
	private Date financialYearEnd;
	private String fullDesc;
	public String shortDesc;
	private Date NewBeginYear;
	private String createdBy;
	private Date createdDate;
	public String modifiedBy;
	private Date modifiedDate;
	
	private ResourceDTO resourceDto;
	
	
	

	
	
	public UserFinancialYearDTO(){
		
	}
	
	public BigDecimal getFinancialYearID() {
		return financialYearID;
	}
	public void setFinancialYearID(BigDecimal financialYearID) {
		this.financialYearID = financialYearID;
	}
	

	public BigDecimal getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(BigDecimal financialYear) {
		this.financialYear = financialYear;
	}
	

	public Date getFinancialYearBegin() {
		return financialYearBegin;
	}
	public void setFinancialYearBegin(Date financialYearBegin) {
		this.financialYearBegin = financialYearBegin;
	}
	

	public Date getFinancialYearEnd() {
		return financialYearEnd;
	}
	public void setFinancialYearEnd(Date financialYearEnd) {
		this.financialYearEnd = financialYearEnd;
	}
	

	public String getFullDesc() {
		return fullDesc;
	}
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	

	public Date getNewBeginYear() {
		return NewBeginYear;
	}
	public void setNewBeginYear(Date newBeginYear) {
		NewBeginYear = newBeginYear;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}
	
	@Override
	public BigDecimal getResourceId() {
		return this.financialYearID;
	};
	

}
