package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.Comparator;

import com.amx.jax.model.ResourceDTO;

public class AdditionalBankDetailsViewDto extends ResourceDTO{
	  private BigDecimal srlId;
	  private String flexField;
	  private BigDecimal countryId;
	  private String amiecCode;
	  private String amieceDescription;
	  private BigDecimal bankId;
	  private String bankCode;
	  private String bankDescription;
	  private BigDecimal serviceApplicabilityRuleId;
	  private BigDecimal applicationCountryId;
	  private BigDecimal currencyId;
	  private BigDecimal remittanceId;
	  private BigDecimal deliveryId;
	  private String fieldType;
	  private String localName;

	public BigDecimal getSrlId() {
		return srlId;
	}
	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}
	public String getFlexField() {
		return flexField;
	}
	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public String getAmiecCode() {
		return amiecCode;
	}
	public void setAmiecCode(String amiecCode) {
		this.amiecCode = amiecCode;
	}
	public String getAmieceDescription() {
		return amieceDescription;
	}
	public void setAmieceDescription(String amieceDescription) {
		this.amieceDescription = amieceDescription;
	}
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankDescription() {
		return bankDescription;
	}
	public void setBankDescription(String bankDescription) {
		this.bankDescription = bankDescription;
	}
	public BigDecimal getServiceApplicabilityRuleId() {
		return serviceApplicabilityRuleId;
	}
	public void setServiceApplicabilityRuleId(BigDecimal serviceApplicabilityRuleId) {
		this.serviceApplicabilityRuleId = serviceApplicabilityRuleId;
	}
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	public BigDecimal getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(BigDecimal remittanceId) {
		this.remittanceId = remittanceId;
	}
	public BigDecimal getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	

	public static class AdditionalBankDetailsViewDtoComparator implements Comparator<AdditionalBankDetailsViewDto> {

		@Override
		public int compare(AdditionalBankDetailsViewDto arg0, AdditionalBankDetailsViewDto arg1) {
			if(null != arg0.getAmieceDescription()) {
				return arg0.getAmieceDescription().compareTo(arg1.getAmieceDescription());
			}
			return 0;
		}
		
	}


	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	@Override
	public String getResourceLocalName() {
		return this.localName;
	}
	
	
}
