package com.amx.jax.model.request;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;

public class CustomerEmploymentDetails {
	
	@ApiModelProperty(example="188")
	private BigDecimal employmentTypeId;
	
	@ApiModelProperty(example="262")
	private BigDecimal professionId;
	
	@ApiModelProperty(example="MUSSA  SALEH")
	private String employer;
	
	@ApiModelProperty(example="2")
	private BigDecimal articleId;
	
	@ApiModelProperty(example="27")
	private BigDecimal articleDetailsId;
	
	@ApiModelProperty(example="128")
	private BigDecimal incomeRangeId;
	
	@ApiModelProperty(example="Powai")
	private String area;
	
	@ApiModelProperty(example="1011")
	private String block;
	
	@ApiModelProperty(example="Gandhi Road")
	private String street;
	
	@ApiModelProperty(example="421 004")
	private String postal;
	
	@ApiModelProperty(example="22694512")
	private String officeTelephone;
	
	@ApiModelProperty(example="12760")
	private BigDecimal cityId;
	
	@ApiModelProperty(example="4165")
	private BigDecimal districtId;
	
	@ApiModelProperty(example="584")
	private BigDecimal stateId;
	
	@ApiModelProperty(example="91")
	private BigDecimal countryId;
	
	@ApiModelProperty(example="1")
	private BigDecimal companyId;
	
	public BigDecimal getEmploymentTypeId() {
		return employmentTypeId;
	}

	public void setEmploymentTypeId(BigDecimal employmentTypeId) {
		this.employmentTypeId = employmentTypeId;
	}

	public BigDecimal getProfessionId() {
		return professionId;
	}

	public void setProfessionId(BigDecimal professionId) {
		this.professionId = professionId;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public BigDecimal getArticleId() {
		return articleId;
	}

	public void setArticleId(BigDecimal articleId) {
		this.articleId = articleId;
	}

	public BigDecimal getArticleDetailsId() {
		return articleDetailsId;
	}

	public void setArticleDetailsId(BigDecimal articleDetailsId) {
		this.articleDetailsId = articleDetailsId;
	}

	public BigDecimal getIncomeRangeId() {
		return incomeRangeId;
	}

	public void setIncomeRangeId(BigDecimal incomeRangeId) {
		this.incomeRangeId = incomeRangeId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getOfficeTelephone() {
		return officeTelephone;
	}

	public void setOfficeTelephone(String officeTelephone) {
		this.officeTelephone = officeTelephone;
	}

	public BigDecimal getCityId() {
		return cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}

	public BigDecimal getDistrictId() {
		return districtId;
	}

	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}

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

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	
	

}
