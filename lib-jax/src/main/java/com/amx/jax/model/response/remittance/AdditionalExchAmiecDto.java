package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class AdditionalExchAmiecDto extends ResourceDTO {

	

	
	//private BigDecimal additionalBankRuleDetailId;
	private BigDecimal countryId;
	private String flexField;
	//private String amiecCode;
	//private String amiecDescription;
	//private String isActive;
	private BigDecimal additionalBankFieldId;

	/*public BigDecimal getAdditionalBankRuleDetailId() {
		return additionalBankRuleDetailId;
	}

	public void setAdditionalBankRuleDetailId(BigDecimal additionalBankRuleDetailId) {
		this.additionalBankRuleDetailId = additionalBankRuleDetailId;
	}*/

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getFlexField() {
		return flexField;
	}

	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}

	/*public String getAmiecCode() {
		return amiecCode;
	}

	public void setAmiecCode(String amiecCode) {
		this.amiecCode = amiecCode;
	}

	public String getAmiecDescription() {
		return amiecDescription;
	}

	public void setAmiecDescription(String amiecDescription) {
		this.amiecDescription = amiecDescription;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}*/

	public BigDecimal getAdditionalBankFieldId() {
		return additionalBankFieldId;
	}

	public void setAdditionalBankFieldId(BigDecimal additionalBankFieldId) {
		this.additionalBankFieldId = additionalBankFieldId;
	}

	

}
