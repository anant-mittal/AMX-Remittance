package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class IncomeDto {
	/*
	 * public BigDecimal customerId; public String employeerName; public BigDecimal
	 * designationId; public BigDecimal incomeRangeId; public IncomeRangeDto
	 * incomeRangeDto; public ArticleDetailsDescDto articleDetailsDescDto; public
	 * BigDecimal getCustomerId() { return customerId; } public void
	 * setCustomerId(BigDecimal customerId) { this.customerId = customerId; } public
	 * String getEmployeerName() { return employeerName; } public void
	 * setEmployeerName(String employeerName) { this.employeerName = employeerName;
	 * } public BigDecimal getDesignationId() { return designationId; } public void
	 * setDesignationId(BigDecimal designationId) { this.designationId =
	 * designationId; } public BigDecimal getIncomeRangeId() { return incomeRangeId;
	 * } public void setIncomeRangeId(BigDecimal incomeRangeId) { this.incomeRangeId
	 * = incomeRangeId; } public IncomeRangeDto getIncomeRangeDto() { return
	 * incomeRangeDto; } public void setIncomeRangeDto(IncomeRangeDto
	 * incomeRangeDto) { this.incomeRangeDto = incomeRangeDto; } public
	 * ArticleDetailsDescDto getArticleDetailsDescDto() { return
	 * articleDetailsDescDto; } public void
	 * setArticleDetailsDescDto(ArticleDetailsDescDto articleDetailsDescDto) {
	 * this.articleDetailsDescDto = articleDetailsDescDto; }
	 */

	public BigDecimal applicationCountryId;
	public BigDecimal incomeRangeFrom;
	public BigDecimal incomeRangeTo;
	public String fullName;
	public BigDecimal articleDetailId;
	public String companyName;
	public String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getArticleDetailId() {
		return articleDetailId;
	}

	public void setArticleDetailId(BigDecimal articleDetailId) {
		this.articleDetailId = articleDetailId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

}
