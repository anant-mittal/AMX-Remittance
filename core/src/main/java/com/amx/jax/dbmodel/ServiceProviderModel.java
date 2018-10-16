package com.amx.jax.dbmodel;

import java.math.BigDecimal;

public class ServiceProviderModel {
	

	public ServiceProviderModel(BigDecimal routingBankId, 
			                    String routingBankName, 
			                    String routingBankCode, 
			                    BigDecimal applicationCountryId, 
			                    BigDecimal routingCountryId,
			                    BigDecimal serviceGroupId) {
		
		this.applicationCountryId = applicationCountryId;
		this.routingBankId=routingBankId;
		this.routingBankName= routingBankName;
		this.routingBankCode = routingBankCode;
		this.routingCountryId =routingCountryId;
		this.serviceGroupId = serviceGroupId;
	}
	
	private BigDecimal applicationCountryId;
	private BigDecimal routingCountryId;
	private BigDecimal serviceGroupId;
	private BigDecimal routingBankId;
	private String routingBankCode;	
	private String routingBankName;
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}
	public void setRoutingCountryId(BigDecimal routingCountryId) {
		this.routingCountryId = routingCountryId;
	}
	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}
	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
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
	public String getRoutingBankName() {
		return routingBankName;
	}
	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}	


}
