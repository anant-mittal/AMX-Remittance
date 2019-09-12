package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

public class PurposeTrnxAmicDescDto {

	
	 private BigDecimal Id;
	 private String amicCode;
	 private String shortDesc;
	 private String fullDesc;
	 private String localFulldesc;
	 private BigDecimal languageId;
	
	 public BigDecimal getId() {
		return Id;
	}
	public void setId(BigDecimal id) {
		Id = id;
	}
	public String getAmicCode() {
		return amicCode;
	}
	public void setAmicCode(String amicCode) {
		this.amicCode = amicCode;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getFullDesc() {
		return fullDesc;
	}
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}
	public String getLocalFulldesc() {
		return localFulldesc;
	}
	public void setLocalFulldesc(String localFulldesc) {
		this.localFulldesc = localFulldesc;
	}
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	 
}
