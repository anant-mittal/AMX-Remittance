package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name="JAX_VW_SOURCE_OF_INCOME")
public class SourceOfIncomeView implements IResourceEntity,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894611190222529134L;
	@Id
	@Column(name="SOURCE_OF_INCOME_ID")
	private BigDecimal sourceofIncomeId;
	@Column(name="LANGUAGE_ID")
	private BigDecimal languageId;
	@Column(name="FULL_DESC")
	private String description;
	@Column(name="SHORT_DESC")
	private String shortDesc;
	@Column(name="LOCAL_FULL_DESC")
	private String localName;
	@Column(name = "DEFAULT_INDIC")
	private String defaultIndic;
	
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
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	@Override
	public BigDecimal resourceId() {
		return this.sourceofIncomeId;
	}
	@Override
	public String resourceName() {
		return this.description;
	}
	@Override
	public String resourceCode() {
		return this.shortDesc;
	}
	@Override
	public String resourceLocalName() {
		return this.localName;
	}
	public String getDefaultIndic() {
		return defaultIndic;
	}
	public void setDefaultIndic(String defaultIndic) {
		this.defaultIndic = defaultIndic;
	}
	
	
	
}
