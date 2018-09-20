package com.amx.jax.model.request;

import java.math.BigDecimal;

public class CustomerEmploymentDetails {
	private BigDecimal employmentTypeId;
	private BigDecimal professionId;
	private String employer;
	private BigDecimal articleId;
	private BigDecimal articleDetailsId;
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

}
