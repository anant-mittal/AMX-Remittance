package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class ArticleDetailsDescDto {

	String articleDetailsDesc;
	BigDecimal articleDetailsDescId;
	BigDecimal articleDetailsId;
	BigDecimal languageId;
	public String getArticleDetailsDesc() {
		return articleDetailsDesc;
	}
	public void setArticleDetailsDesc(String articleDetailsDesc) {
		this.articleDetailsDesc = articleDetailsDesc;
	}
	public BigDecimal getArticleDetailsDescId() {
		return articleDetailsDescId;
	}
	public void setArticleDetailsDescId(BigDecimal articleDetailsDescId) {
		this.articleDetailsDescId = articleDetailsDescId;
	}
	public BigDecimal getArticleDetailsId() {
		return articleDetailsId;
	}
	public void setArticleDetailsId(BigDecimal articleDetailsId) {
		this.articleDetailsId = articleDetailsId;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	@Override
	public String toString() {
		return "ArticleDetailsDescDto [articleDetailsDesc=" + articleDetailsDesc + ", articleDetailsDescId="
				+ articleDetailsDescId + ", articleDetailsId=" + articleDetailsId + ", languageId=" + languageId + "]";
	}
	
	
}
