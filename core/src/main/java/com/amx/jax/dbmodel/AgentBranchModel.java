package com.amx.jax.dbmodel;

import java.math.BigDecimal;

public class AgentBranchModel {
	

	public AgentBranchModel(BigDecimal applicationCountryId,
							BigDecimal routingCountryId,
							BigDecimal serviceGroupId,
							BigDecimal routingBankId,
							BigDecimal currencyId,
							BigDecimal agentBankId,
							BigDecimal bankBranchId,
							BigDecimal routingBranchId,
		                    String branchFullName) {
		
		this.applicationCountryId = applicationCountryId;
		this.routingCountryId =routingCountryId;
		this.serviceGroupId = serviceGroupId;
		this.routingBankId=routingBankId;
		this.currencyId= currencyId;
		this.agentBankId=agentBankId;
		this.bankBranchId = bankBranchId;
		this.routingBranchId=routingBranchId;
		this.branchFullName=branchFullName;
	}
	
	private BigDecimal applicationCountryId;
	private BigDecimal routingCountryId;
	private BigDecimal serviceGroupId;
	private BigDecimal routingBankId;
	private BigDecimal currencyId;
	private BigDecimal agentBankId;
	private BigDecimal bankBranchId;
	private BigDecimal routingBranchId;
	private String branchFullName;
	
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
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	public BigDecimal getRoutingBranchId() {
		return routingBranchId;
	}
	public void setRoutingBranchId(BigDecimal routingBranchId) {
		this.routingBranchId = routingBranchId;
	}
	public String getBranchFullName() {
		return branchFullName;
	}
	public void setBranchFullName(String branchFullName) {
		this.branchFullName = branchFullName;
	}

}
