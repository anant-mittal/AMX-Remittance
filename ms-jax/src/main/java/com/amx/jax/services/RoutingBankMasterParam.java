package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.dbmodel.AgentMasterModel;

public class RoutingBankMasterParam {
	
	
	
	public RoutingBankMasterParam(BigDecimal applicationCountryId, BigDecimal routingCountryId,BigDecimal serviceGroupId){
		this.applicationCountryId = applicationCountryId;
		this.routingCountryId = routingCountryId;
		this.serviceGroupId = serviceGroupId;
	}
	
	public RoutingBankMasterParam(BigDecimal applicationCountryId, BigDecimal routingCountryId,BigDecimal serviceGroupId, BigDecimal routingBankId, BigDecimal currencyId ){
		this.applicationCountryId = applicationCountryId;
		this.routingCountryId = routingCountryId;
		this.serviceGroupId = serviceGroupId;
		this.routingBankId = routingBankId;
		this.currencyId = currencyId;
	}
	
	public RoutingBankMasterParam(BigDecimal applicationCountryId, BigDecimal routingCountryId,BigDecimal serviceGroupId, BigDecimal routingBankId, BigDecimal currencyId, BigDecimal agentBankId ){
		this.applicationCountryId = applicationCountryId;
		this.routingCountryId = routingCountryId;
		this.serviceGroupId = serviceGroupId;
		this.routingBankId = routingBankId;
		this.currencyId = currencyId;
		this.agentBankId = agentBankId;
	}
	
	private BigDecimal applicationCountryId;
	private BigDecimal routingCountryId;
	private BigDecimal serviceGroupId;
	private BigDecimal routingBankId;
	private String routingBankCode;	
	private String routingBankName;
	private BigDecimal currencyId;
	private BigDecimal agentBankId;
	
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
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getAgentBankId() {
		return agentBankId;
	}

	public void setAgentBankId(BigDecimal agentBankId) {
		this.agentBankId = agentBankId;
	}	

}
