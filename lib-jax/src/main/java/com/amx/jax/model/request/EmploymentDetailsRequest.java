package com.amx.jax.model.request;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class EmploymentDetailsRequest {
	@ApiMockModelProperty(example="2")
	BigDecimal articleId;
	
	@ApiMockModelProperty(example="27")
	BigDecimal articleDetailsId;
	
	@ApiMockModelProperty(example="91")
	BigDecimal countryId;

	public EmploymentDetailsRequest(BigDecimal articleId, BigDecimal articleDetailsId, BigDecimal countryId) {
		super();
		this.articleId = articleId;
		this.articleDetailsId = articleDetailsId;
		this.countryId = countryId;
	}

	public EmploymentDetailsRequest() {
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

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "EmploymentDetailsRequest [articleId=" + articleId + ", articleDetailsId=" + articleDetailsId + "]";
	}

}
