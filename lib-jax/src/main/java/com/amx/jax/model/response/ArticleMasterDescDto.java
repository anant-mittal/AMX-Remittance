package com.amx.jax.model.response;

import java.math.BigDecimal;

public class ArticleMasterDescDto {
	
	private BigDecimal articleDescId;
	private BigDecimal articleId;
	private BigDecimal languageType;
	private String articleDescription;
	
	public BigDecimal getArticleDescId() {
		return articleDescId;
	}
	public void setArticleDescId(BigDecimal articleDescId) {
		this.articleDescId = articleDescId;
	}	
	public BigDecimal getArticleId() {
		return articleId;
	}
	public void setArticleId(BigDecimal articleId) {
		this.articleId = articleId;
	}
	public BigDecimal getLanguageType() {
		return languageType;
	}
	public void setLanguageType(BigDecimal languageType) {
		this.languageType = languageType;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	@Override
	public String toString() {
		return "ArticleMasterDescDto [articleDescId=" + articleDescId + ", articleId=" + articleId
				+ ", languageType=" + languageType + ", articleDescription=" + articleDescription + "]";
	}
	
	
	
	
}
