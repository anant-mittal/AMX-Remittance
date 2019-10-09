package com.amx.jax.model.request;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class UpdateCustomerEmploymentDetailsReq {

	@ApiMockModelProperty(example = "188")
	private BigDecimal employmentTypeId;

	@ApiMockModelProperty(example = "262")
	private BigDecimal professionId;

	@ApiMockModelProperty(example = "MUSSA  SALEH")
	private String employer;

	@ApiMockModelProperty(example = "27")
	private BigDecimal articleDetailsId;

	@ApiMockModelProperty(example = "128")
	private BigDecimal incomeRangeId;

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

}
