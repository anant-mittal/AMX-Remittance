package com.amx.jax.model.response.customer;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerIncomeRangeDto  implements Serializable {

	@JsonProperty("article")
	String articleeDescription;
	@JsonProperty("level")
	String articleDetailDesc;
	@JsonProperty("incomeRange")
	String monthlyIncome;

	public String getArticleeDescription() {
		return articleeDescription;
	}

	public void setArticleeDescription(String articleeDescription) {
		this.articleeDescription = articleeDescription;
	}

	public String getArticleDetailDesc() {
		return articleDetailDesc;
	}

	public void setArticleDetailDesc(String articleDetailDesc) {
		this.articleDetailDesc = articleDetailDesc;
	}

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
}
