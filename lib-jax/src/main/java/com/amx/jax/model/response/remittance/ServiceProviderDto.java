package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.Calendar;

public class ServiceProviderDto {

	private BigDecimal amgSessionId;
	private String partnerSessionId;
	private String partnerReferenceNo;
	private BigDecimal intialAmountInSettlCurr;
	private BigDecimal fixedCommInSettlCurr;
	private BigDecimal variableCommInSettlCurr;
	private String settlementCurrency;
	private BigDecimal transactionMargin;
	private Calendar offerExpirationDate;
	private Calendar offerStartingDate;
	
	public BigDecimal getAmgSessionId() {
		return amgSessionId;
	}
	public void setAmgSessionId(BigDecimal amgSessionId) {
		this.amgSessionId = amgSessionId;
	}
	
	public String getPartnerSessionId() {
		return partnerSessionId;
	}
	public void setPartnerSessionId(String partnerSessionId) {
		this.partnerSessionId = partnerSessionId;
	}
	
	public String getPartnerReferenceNo() {
		return partnerReferenceNo;
	}
	public void setPartnerReferenceNo(String partnerReferenceNo) {
		this.partnerReferenceNo = partnerReferenceNo;
	}
	
	public BigDecimal getIntialAmountInSettlCurr() {
		return intialAmountInSettlCurr;
	}
	public void setIntialAmountInSettlCurr(BigDecimal intialAmountInSettlCurr) {
		this.intialAmountInSettlCurr = intialAmountInSettlCurr;
	}
	
	public BigDecimal getFixedCommInSettlCurr() {
		return fixedCommInSettlCurr;
	}
	public void setFixedCommInSettlCurr(BigDecimal fixedCommInSettlCurr) {
		this.fixedCommInSettlCurr = fixedCommInSettlCurr;
	}
	
	public BigDecimal getVariableCommInSettlCurr() {
		return variableCommInSettlCurr;
	}
	public void setVariableCommInSettlCurr(BigDecimal variableCommInSettlCurr) {
		this.variableCommInSettlCurr = variableCommInSettlCurr;
	}
	
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}
	
	public BigDecimal getTransactionMargin() {
		return transactionMargin;
	}
	public void setTransactionMargin(BigDecimal transactionMargin) {
		this.transactionMargin = transactionMargin;
	}
	
	public Calendar getOfferExpirationDate() {
		return offerExpirationDate;
	}
	public void setOfferExpirationDate(Calendar offerExpirationDate) {
		this.offerExpirationDate = offerExpirationDate;
	}
	
	public Calendar getOfferStartingDate() {
		return offerStartingDate;
	}
	public void setOfferStartingDate(Calendar offerStartingDate) {
		this.offerStartingDate = offerStartingDate;
	}
	
	/*Map<String, ServiceProviderDetailsDto> mapSrvProvDetails = new HashMap<>();

	public Map<String, ServiceProviderDetailsDto> getMapSrvProvDetails() {
		return mapSrvProvDetails;
	}
	public void setMapSrvProvDetails(Map<String, ServiceProviderDetailsDto> mapSrvProvDetails) {
		this.mapSrvProvDetails = mapSrvProvDetails;
	}*/

}
