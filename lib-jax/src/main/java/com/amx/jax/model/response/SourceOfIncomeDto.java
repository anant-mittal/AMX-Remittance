package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;



public class SourceOfIncomeDto extends ResourceDTO{
	
	private BigDecimal sourceofIncomeId;
	private BigDecimal languageId;
	private String description;
	private String shortDesc;
	private String localName;
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

	@Override
	public String getResourceCode() {
		return this.shortDesc;
	}

	@Override
	public BigDecimal getResourceId() {
		return this.sourceofIncomeId;
	}
	
	@Override
	public String getResourceLocalName() {
		return this.localName;

	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;

	}
	
	
	

}
