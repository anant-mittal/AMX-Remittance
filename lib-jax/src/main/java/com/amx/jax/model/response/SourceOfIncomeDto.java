package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.model.AResourceDTO;

public class SourceOfIncomeDto extends AResourceDTO<SourceOfIncomeDto> {

	private static final long serialVersionUID = -2049933165750792143L;
	private BigDecimal languageId;
	private String description;
	private String shortDesc;
	private String localName;

	public BigDecimal getSourceofIncomeId() {
		return this.resourceId;
	}

	public void setSourceofIncomeId(BigDecimal sourceofIncomeId) {
		this.resourceId = sourceofIncomeId;
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
	public SourceOfIncomeDto newInstance() {
		return new SourceOfIncomeDto();
	}

}
