package com.amx.amxlib.model.request;

import java.math.BigDecimal;

public class EmploymentDetailsRequest {
	BigDecimal articleId;
	BigDecimal articleDetailsId;
	
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
	@Override
	public String toString() {
		return "EmploymentDetailsRequest [articleId=" + articleId + ", articleDetailsId=" + articleDetailsId + "]";
	}
	
}
