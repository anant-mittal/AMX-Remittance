package com.amx.jax.partner.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;

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
	
	@NotNull(message = "Local Currency Id can not be Null or Empty")
	private BigDecimal localCurrencyId;

	@NotNull(message = "Foreign Currency Id can not be Null or Empty")
	private BigDecimal foreignCurrencyId;
	
	@NotNull(message="Selected Currency Id can not be Null or Empty")
	private BigDecimal selectedCurrency;
	
	@NotNull(message="amount can not be Null or Empty")
	private BigDecimal amount;
	
	private CUSTOMER_CATEGORY customerCategory;
	
	private Channel channel;
	
	private List<RoutingBankDetails> routingBankDetails;
	
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
	
	public BigDecimal getDestinationCountryId() {
		return destinationCountryId;
	}
	public void setDestinationCountryId(BigDecimal destinationCountryId) {
		this.destinationCountryId = destinationCountryId;
	}
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	public List<RoutingBankDetails> getRoutingBankDetails() {
		return routingBankDetails;
	}
	
	public void setRoutingBankDetails(List<RoutingBankDetails> routingBankDetails) {
		this.routingBankDetails = routingBankDetails;
	}
	
	public CUSTOMER_CATEGORY getCustomerCategory() {
		return customerCategory;
	}
	
	public void setCustomerCategory(CUSTOMER_CATEGORY customerCategory) {
		this.customerCategory = customerCategory;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	
	
				
}
