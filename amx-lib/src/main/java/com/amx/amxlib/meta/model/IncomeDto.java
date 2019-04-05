package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class IncomeDto {
	
	
	public BigDecimal incomeRangeFrom;
	public BigDecimal incomeRangeTo;
	
	public BigDecimal articleDetailId;
	public String companyName;
	public String image;
	public String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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
