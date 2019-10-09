package com.amx.jax.model.request;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerEmploymentDetails {
	
	@ApiMockModelProperty(example="188")
	private BigDecimal employmentTypeId;
	
	@ApiMockModelProperty(example="262")
	private BigDecimal professionId;
	
	@ApiMockModelProperty(example="MUSSA  SALEH")
	private String employer;
	
	@ApiMockModelProperty(example="2")
	private BigDecimal articleId;
	
	@ApiMockModelProperty(example="27")
	private BigDecimal articleDetailsId;
	
	@ApiMockModelProperty(example="128")
	private BigDecimal incomeRangeId;
	
	@ApiMockModelProperty(example="Powai")
	private String area;
	
	@ApiMockModelProperty(example="1011")
	private String block;
	
	@ApiMockModelProperty(example="Gandhi Road")
	private String street;
	
	@ApiMockModelProperty(example="421 004")
	private String postal;
	
	@ApiMockModelProperty(example="22694512")
	private String officeTelephone;
	
	@ApiMockModelProperty(example="12760")
	private BigDecimal cityId;
	
	@ApiMockModelProperty(example="4165")
	private BigDecimal districtId;
	
	@ApiMockModelProperty(example="584")
	private BigDecimal stateId;
	
	@ApiMockModelProperty(example="91")
	private BigDecimal countryId;
	
	@ApiMockModelProperty(example="1")
	private BigDecimal companyId;
	
	@ApiMockModelProperty(example="Software Developer")
	private String designation;
	
	@ApiMockModelProperty(example="ARTICLE-18")
	private String articleDesc;
	
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
	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public String getArticleDesc() {
		return articleDesc;
	}

	public void setArticleDesc(String articleDesc) {
		this.articleDesc = articleDesc;
	}

	@Override
	public String toString() {
		return "CustomerEmploymentDetails [employmentTypeId=" + employmentTypeId + ", professionId=" + professionId
				+ ", employer=" + employer + ", articleId=" + articleId + ", articleDetailsId=" + articleDetailsId
				+ ", incomeRangeId=" + incomeRangeId + ", area=" + area + ", block=" + block + ", street=" + street
				+ ", postal=" + postal + ", officeTelephone=" + officeTelephone + ", cityId=" + cityId + ", districtId="
				+ districtId + ", stateId=" + stateId + ", countryId=" + countryId + ", companyId=" + companyId + "]";
	}
	
	

}
