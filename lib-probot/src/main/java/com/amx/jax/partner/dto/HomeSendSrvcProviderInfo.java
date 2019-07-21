package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import com.amx.jax.pricer.dto.ExchangeRateDetails;

public class HomeSendSrvcProviderInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Calendar offerExpirationDate;

	private String partnerTransactionReference;
	private String outGoingTransactionReference;
	private String settlementCurrency;
	private String destinationCurrency;
	private String actionInd;
	private String responseCode;
	private String responseDescription;
	private String technicalDetails;
	private String requestXML;
	private String responseXML;

	private boolean isBeneficiaryDeduct;

	private BigDecimal creditedAmountInDestinationCurrency;
	private BigDecimal initialAmountInSettlementCurrency;
	private BigDecimal fixChargedAmountInSettlementCurrency;
	private BigDecimal variableChargedAmountInSettlementCurrency;
	private BigDecimal totalChargedAmountInSettlementCurrency;
	private BigDecimal wholeSaleFxRate;
	private BigDecimal transactionMargin;
	
	private ExchangeRateDetails exchangeDiscountsData;
	
	
	public Calendar getOfferExpirationDate() {
		return offerExpirationDate;
	}
	public void setOfferExpirationDate(Calendar offerExpirationDate) {
		this.offerExpirationDate = offerExpirationDate;
	}
	
	public String getPartnerTransactionReference() {
		return partnerTransactionReference;
	}
	public void setPartnerTransactionReference(String partnerTransactionReference) {
		this.partnerTransactionReference = partnerTransactionReference;
	}
	
	public String getOutGoingTransactionReference() {
		return outGoingTransactionReference;
	}
	public void setOutGoingTransactionReference(String outGoingTransactionReference) {
		this.outGoingTransactionReference = outGoingTransactionReference;
	}
	
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}
	
	public String getDestinationCurrency() {
		return destinationCurrency;
	}
	public void setDestinationCurrency(String destinationCurrency) {
		this.destinationCurrency = destinationCurrency;
	}
	
	public String getActionInd() {
		return actionInd;
	}
	public void setActionInd(String actionInd) {
		this.actionInd = actionInd;
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	
	public String getTechnicalDetails() {
		return technicalDetails;
	}
	public void setTechnicalDetails(String technicalDetails) {
		this.technicalDetails = technicalDetails;
	}
	
	public String getRequestXML() {
		return requestXML;
	}
	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}
	
	public String getResponseXML() {
		return responseXML;
	}
	public void setResponseXML(String responseXML) {
		this.responseXML = responseXML;
	}
	
	public boolean isBeneficiaryDeduct() {
		return isBeneficiaryDeduct;
	}
	public void setBeneficiaryDeduct(boolean isBeneficiaryDeduct) {
		this.isBeneficiaryDeduct = isBeneficiaryDeduct;
	}
	
	public BigDecimal getCreditedAmountInDestinationCurrency() {
		return creditedAmountInDestinationCurrency;
	}
	public void setCreditedAmountInDestinationCurrency(BigDecimal creditedAmountInDestinationCurrency) {
		this.creditedAmountInDestinationCurrency = creditedAmountInDestinationCurrency;
	}
	
	public BigDecimal getInitialAmountInSettlementCurrency() {
		return initialAmountInSettlementCurrency;
	}
	public void setInitialAmountInSettlementCurrency(BigDecimal initialAmountInSettlementCurrency) {
		this.initialAmountInSettlementCurrency = initialAmountInSettlementCurrency;
	}
	
	public BigDecimal getFixChargedAmountInSettlementCurrency() {
		return fixChargedAmountInSettlementCurrency;
	}
	public void setFixChargedAmountInSettlementCurrency(BigDecimal fixChargedAmountInSettlementCurrency) {
		this.fixChargedAmountInSettlementCurrency = fixChargedAmountInSettlementCurrency;
	}
	
	public BigDecimal getVariableChargedAmountInSettlementCurrency() {
		return variableChargedAmountInSettlementCurrency;
	}
	public void setVariableChargedAmountInSettlementCurrency(BigDecimal variableChargedAmountInSettlementCurrency) {
		this.variableChargedAmountInSettlementCurrency = variableChargedAmountInSettlementCurrency;
	}
	
	public BigDecimal getTotalChargedAmountInSettlementCurrency() {
		return totalChargedAmountInSettlementCurrency;
	}
	public void setTotalChargedAmountInSettlementCurrency(BigDecimal totalChargedAmountInSettlementCurrency) {
		this.totalChargedAmountInSettlementCurrency = totalChargedAmountInSettlementCurrency;
	}
	
	public BigDecimal getWholeSaleFxRate() {
		return wholeSaleFxRate;
	}
	public void setWholeSaleFxRate(BigDecimal wholeSaleFxRate) {
		this.wholeSaleFxRate = wholeSaleFxRate;
	}
	
	public BigDecimal getTransactionMargin() {
		return transactionMargin;
	}
	public void setTransactionMargin(BigDecimal transactionMargin) {
		this.transactionMargin = transactionMargin;
	}
	
	public ExchangeRateDetails getExchangeDiscountsData() {
		return exchangeDiscountsData;
	}
	public void setExchangeDiscountsData(ExchangeRateDetails exchangeDiscountsData) {
		this.exchangeDiscountsData = exchangeDiscountsData;
	}
}
