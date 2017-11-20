package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EX_ONLINE_QUES_MAST")
public class OnlineQuestModel {
	

	
	private BigDecimal questId;
	private BigDecimal questNumber;
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private String status;
	private String description;
	
	@Id
	@Column(name="QUEST_ID")
	public BigDecimal getQuestId() {
		return questId;
	}
	public void setQuestId(BigDecimal questId) {
		this.questId = questId;
	}
	
	@Column(name="COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	@Column(name="LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}
	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}
	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	@Column(name="ISACTIVE")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name="QUEST_DESC")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description=description;
	}
	
	
	
	@Column(name="QUEST_NO")
	public BigDecimal getQuestNumber() {
		return questNumber;
	}
	public void setQuestNumber(BigDecimal questNumber) {
		this.questNumber = questNumber;
	}
}
