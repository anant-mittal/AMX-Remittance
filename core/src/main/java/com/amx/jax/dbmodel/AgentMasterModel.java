package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class AgentMasterModel {
	

	public AgentMasterModel(BigDecimal applicationCountryId,
							BigDecimal routingCountryId,
							BigDecimal serviceGroupId,
							BigDecimal routingBankId,
							BigDecimal currencyId,
							BigDecimal agentBankId,
		                    String agentBankName, 
		                    String agentBankCode) {
		
		this.applicationCountryId = applicationCountryId;
		this.routingCountryId =routingCountryId;
		this.serviceGroupId = serviceGroupId;
		this.routingBankId=routingBankId;
		this.currencyId= currencyId;
		this.agentBankId=agentBankId;
		this.agentBankName = agentBankName;
		this.agentBankCode=agentBankCode;
	}
	
	private BigDecimal applicationCountryId;
	private BigDecimal routingCountryId;
	private BigDecimal serviceGroupId;
	private BigDecimal routingBankId;
	private BigDecimal currencyId;
	private BigDecimal agentBankId;
	private String agentBankName;
	private String agentBankCode;
	
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
	public String getAgentBankName() {
		return agentBankName;
	}
	public void setAgentBankName(String agentBankName) {
		this.agentBankName = agentBankName;
	}
	public String getAgentBankCode() {
		return agentBankCode;
	}
	public void setAgentBankCode(String agentBankCode) {
		this.agentBankCode = agentBankCode;
	}	

}
