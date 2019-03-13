package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class ArticleDetailsDescDto extends ResourceDTO {

	String articleDetailsDesc;
	BigDecimal articleDetailsId;
	
	public String getArticleDetailsDesc() {
		return articleDetailsDesc;
	}

	public void setArticleDetailsDesc(String articleDetailsDesc) {
		this.articleDetailsDesc = articleDetailsDesc;
		this.setResourceName(articleDetailsDesc);
	}

	

	public BigDecimal getArticleDetailsId() {
		return articleDetailsId;
	}

	public void setArticleDetailsId(BigDecimal articleDetailsId) {
		this.articleDetailsId = articleDetailsId;
		this.setResourceId(articleDetailsId);
	}

	

	

}
