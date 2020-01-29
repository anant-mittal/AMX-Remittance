package com.amx.jax.model.request.partner;

import java.math.BigDecimal;

public class RemittanceTransactionPartnerDTO {
	
	private BigDecimal routingBankId;
	private String routingBankCode;
	private String routingNumber_Indic2;
	private String bsbNumber_Indic3;
	private String remittanceMode;
	private String deliveryMode;
	private BigDecimal destinationAmount;
	private String furtherInstruction;
	private String outGoingTransactionReference;
	private String partnerTransactionReference;
	private String purposeOfTransaction_Indic1;
	private String requestSequenceId;
	private BigDecimal settlementAmount;
	private String sourceOfFundDesc;
	private String trnxCollectionType;
	
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	
	public String getRoutingBankCode() {
		return routingBankCode;
	}
	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}
	
	public String getRoutingNumber_Indic2() {
		return routingNumber_Indic2;
	}
	public void setRoutingNumber_Indic2(String routingNumber_Indic2) {
		this.routingNumber_Indic2 = routingNumber_Indic2;
	}
	
	public String getBsbNumber_Indic3() {
		return bsbNumber_Indic3;
	}
	public void setBsbNumber_Indic3(String bsbNumber_Indic3) {
		this.bsbNumber_Indic3 = bsbNumber_Indic3;
	}
	
	public String getRemittanceMode() {
		return remittanceMode;
	}
	public void setRemittanceMode(String remittanceMode) {
		this.remittanceMode = remittanceMode;
	}
	
	public String getDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
	
	public BigDecimal getDestinationAmount() {
		return destinationAmount;
	}
	public void setDestinationAmount(BigDecimal destinationAmount) {
		this.destinationAmount = destinationAmount;
	}
	
	public String getFurtherInstruction() {
		return furtherInstruction;
	}
	public void setFurtherInstruction(String furtherInstruction) {
		this.furtherInstruction = furtherInstruction;
	}
	
	public String getOutGoingTransactionReference() {
		return outGoingTransactionReference;
	}
	public void setOutGoingTransactionReference(String outGoingTransactionReference) {
		this.outGoingTransactionReference = outGoingTransactionReference;
	}
	
	public String getPartnerTransactionReference() {
		return partnerTransactionReference;
	}
	public void setPartnerTransactionReference(String partnerTransactionReference) {
		this.partnerTransactionReference = partnerTransactionReference;
	}
	
	public String getRequestSequenceId() {
		return requestSequenceId;
	}
	public void setRequestSequenceId(String requestSequenceId) {
		this.requestSequenceId = requestSequenceId;
	}
	
	public BigDecimal getSettlementAmount() {
		return settlementAmount;
	}
	public void setSettlementAmount(BigDecimal settlementAmount) {
		this.settlementAmount = settlementAmount;
	}
	
	public String getSourceOfFundDesc() {
		return sourceOfFundDesc;
	}
	public void setSourceOfFundDesc(String sourceOfFundDesc) {
		this.sourceOfFundDesc = sourceOfFundDesc;
	}
	
	public String getTrnxCollectionType() {
		return trnxCollectionType;
	}
	public void setTrnxCollectionType(String trnxCollectionType) {
		this.trnxCollectionType = trnxCollectionType;
	}
	
	public String getPurposeOfTransaction_Indic1() {
		return purposeOfTransaction_Indic1;
	}
	public void setPurposeOfTransaction_Indic1(String purposeOfTransaction_Indic1) {
		this.purposeOfTransaction_Indic1 = purposeOfTransaction_Indic1;
	}

}
