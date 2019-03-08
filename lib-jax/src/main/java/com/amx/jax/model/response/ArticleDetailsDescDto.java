package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class ArticleDetailsDescDto extends ResourceDTO {

	String articleDetailsDesc;
	BigDecimal articleDetailsDescId;
	BigDecimal articleDetailsId;
	BigDecimal languageId;

	public String getArticleDetailsDesc() {
		return articleDetailsDesc;
	}

	public void setArticleDetailsDesc(String articleDetailsDesc) {
		this.articleDetailsDesc = articleDetailsDesc;
		this.setResourceName(articleDetailsDesc);
	}

	public BigDecimal getArticleDetailsDescId() {
		return articleDetailsDescId;
	}

	public void setArticleDetailsDescId(BigDecimal articleDetailsDescId) {
		this.articleDetailsDescId = articleDetailsDescId;
		this.setResourceId(articleDetailsId);
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
