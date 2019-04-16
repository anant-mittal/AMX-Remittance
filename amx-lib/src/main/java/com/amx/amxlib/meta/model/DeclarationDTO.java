package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class DeclarationDTO {
	public String description;
	public BigDecimal text_id;
	public String text_remark;
	public String text_type;
	
	public BigDecimal languageId;
	
	
	
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
	public BigDecimal getText_id() {
		return text_id;
	}
	public void setText_id(BigDecimal text_id) {
		this.text_id = text_id;
	}
	public String getText_remark() {
		return text_remark;
	}
	public void setText_remark(String text_remark) {
		this.text_remark = text_remark;
	}
	public String getText_type() {
		return text_type;
	}
	public void setText_type(String text_type) {
		this.text_type = text_type;
	}
	
}
