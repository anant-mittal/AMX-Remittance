package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.ArticleDetails;

public interface IArticleDetailsRepository extends JpaRepository<ArticleDetails, Serializable>{
	public ArticleDetails getArticleDetailsByArticleDetailId(BigDecimal id);
}
