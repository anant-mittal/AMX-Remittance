package com.amx.jax.model.request.remittance;

import java.util.List;

public class CustomerBankRequest {
	
	List<CustomerBankDto> customerBankDetails;

	public List<CustomerBankDto> getCustomerBankDetails() {
		return customerBankDetails;
	}

	public void setCustomerBankDetails(List<CustomerBankDto> customerBankDetails) {
		this.customerBankDetails = customerBankDetails;
	}
	
	/*private BigDecimal bankId;
	private String bankCode;
	private String collectionMode;
	private String debitCardName;
	private BigDecimal relationsId;
	
	
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
	
	public String getCollectionMode() {
		return collectionMode;
	}
	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}
	
	public String getDebitCardName() {
		return debitCardName;
	}
	public void setDebitCardName(String debitCardName) {
		this.debitCardName = debitCardName;
	}
	public BigDecimal getRelationsId() {
		return relationsId;
	}
	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
	}*/
	
}
