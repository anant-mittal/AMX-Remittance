package com.amx.jax.model.response;

import java.math.BigDecimal;



public class SourceOfIncomeDto {
	
	private BigDecimal sourceofIncomeId;
	private BigDecimal languageId;
	private String description;
	private String shortDesc;
	private String localFulldesc;
	public BigDecimal getSourceofIncomeId() {
		return sourceofIncomeId;
	}
	public void setSourceofIncomeId(BigDecimal sourceofIncomeId) {
		this.sourceofIncomeId = sourceofIncomeId;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getLocalFulldesc() {
		return localFulldesc;
	}
	public void setLocalFulldesc(String localFulldesc) {
		this.localFulldesc = localFulldesc;
	}
	

}
