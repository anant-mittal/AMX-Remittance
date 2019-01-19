package com.amx.jax.model.auth;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class QuestModelDTO extends AbstractModel {

	private static final long serialVersionUID = -1250280441336967398L;

	private BigDecimal questId;
	private BigDecimal questNumber;
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private String status;
	private String description;

	private QuestAnswerModelDTO questAnswerModelDTO;

	public QuestModelDTO(BigDecimal questId, BigDecimal questNumber, String description) {
		super();
		this.questId = questId;
		this.questNumber = questNumber;
		this.description = description;
	}

	public QuestModelDTO() {
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getQuestId() {
		return questId;
	}

	public void setQuestId(BigDecimal questId) {
		this.questId = questId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getQuestNumber() {
		return questNumber;
	}

	public void setQuestNumber(BigDecimal questNumber) {
		this.questNumber = questNumber;
	}

	public QuestAnswerModelDTO getQuestAnswerModelDTO() {
		return questAnswerModelDTO;
	}

	public void setQuestAnswerModelDTO(QuestAnswerModelDTO questAnswerModelDTO) {
		this.questAnswerModelDTO = questAnswerModelDTO;
	}
}
