package com.amx.jax.model.response;

import java.math.BigDecimal;

public class IncomeRangeDto {
	
	BigDecimal incomeRangeId;
	BigDecimal articleDetailsId;
	BigDecimal incomeFrom;
	BigDecimal incomeTo;
	public BigDecimal getIncomeRangeId() {
		return incomeRangeId;
	}
	public void setIncomeRangeId(BigDecimal incomeRangeId) {
		this.incomeRangeId = incomeRangeId;
	}
	public BigDecimal getArticleDetailsId() {
		return articleDetailsId;
	}
	public void setArticleDetailsId(BigDecimal articleDetailsId) {
		this.articleDetailsId = articleDetailsId;
	}
	public BigDecimal getIncomeFrom() {
		return incomeFrom;
	}
	public void setIncomeFrom(BigDecimal incomeFrom) {
		this.incomeFrom = incomeFrom;
	}
	public BigDecimal getIncomeTo() {
		return incomeTo;
	}
	public void setIncomeTo(BigDecimal incomeTo) {
		this.incomeTo = incomeTo;
	}
	@Override
	public String toString() {
		return "IncomeRangeDto [incomeRangeId=" + incomeRangeId + ", articleDetailsId=" + articleDetailsId
				+ ", incomeFrom=" + incomeFrom + ", incomeTo=" + incomeTo + "]";
	}
	
	

}
