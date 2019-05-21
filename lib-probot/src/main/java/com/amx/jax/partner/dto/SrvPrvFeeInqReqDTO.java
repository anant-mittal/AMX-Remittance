package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class SrvPrvFeeInqReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;
	
	@NotNull(message="CustomerId can not be Null or Empty")
	private BigDecimal customerId;
	
	@NotNull(message="BeneficiaryRelationShipId can not be Null or Empty")
	private BigDecimal beneficiaryRelationShipId;
	
	@NotNull(message = "Application Country Id can not be Null or Empty")
	private BigDecimal applicationCountryId;
	
	@NotNull(message = "Destination Country Id can not be Null or Empty")
	private BigDecimal destinationCountryId;
	
	@NotNull(message = "Rountry Bank Id can not be Null or Empty")
	private BigDecimal routingBankId;
	
	@NotNull(message = "Local Currency Id can not be Null or Empty")
	private BigDecimal localCurrencyId;

	@NotNull(message = "Foreign Currency Id can not be Null or Empty")
	private BigDecimal foreignCurrencyId;
	
	@NotNull(message="Selected Currency Id can not be Null or Empty")
	private BigDecimal selectedCurrency;
	
	@NotNull(message="Remittance Id can not be Null or Empty")
	private BigDecimal remittanceId;
	
	@NotNull(message="Delivery Id can not be Null or Empty")
	private BigDecimal deliveryId;
	
	@NotNull(message="amount can not be Null or Empty")
	private BigDecimal amount;
	
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	public BigDecimal getBeneficiaryRelationShipId() {
		return beneficiaryRelationShipId;
	}
	public void setBeneficiaryRelationShipId(BigDecimal beneficiaryRelationShipId) {
		this.beneficiaryRelationShipId = beneficiaryRelationShipId;
	}
	
	public BigDecimal getSelectedCurrency() {
		return selectedCurrency;
	}
	public void setSelectedCurrency(BigDecimal selectedCurrency) {
		this.selectedCurrency = selectedCurrency;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getLocalCurrencyId() {
		return localCurrencyId;
	}
	public void setLocalCurrencyId(BigDecimal localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}
	
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
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
	
	public BigDecimal getDestinationCountryId() {
		return destinationCountryId;
	}
	public void setDestinationCountryId(BigDecimal destinationCountryId) {
		this.destinationCountryId = destinationCountryId;
	}
	
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
			
}
