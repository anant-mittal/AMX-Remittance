package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "EX_DECLARATION_TEXT")
public class DeclarationModel implements IResourceEntity, Serializable {

	private static final long serialVersionUID = 7309549091432024935L;
	
	@Id
	@Column(name = "TEXT_ID")
	private BigDecimal textId;
	
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TEXT_TYPE")
	private String textType;

	@Column(name = "TEXT_REMARK")
	private String textRemark;

	@Column(name = "LANGUAGE_ID")
	private BigDecimal languageId;

	/*
	 * public BigDecimal getLanguage_id() { return languageid; }
	 * 
	 * public void setLanguage_id(BigDecimal language_id) { this.language_id =
	 * language_id; }
	 */

	public String getDescription() {
		return description;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public BigDecimal resourceId() {
		return this.textId;
	}

	@Override
	public String resourceName() {
		return this.description;
	}

	@Override
	public String resourceCode() {
		return this.textType;
	}

	public BigDecimal getTextId() {
		return textId;
	}

	public void setTextId(BigDecimal textId) {
		this.textId = textId;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	public String getTextRemark() {
		return textRemark;
	}

	public void setTextRemark(String textRemark) {
		this.textRemark = textRemark;
	}

}
