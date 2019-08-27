package com.amx.jax.client.serviceprovider;

import java.math.BigDecimal;

public class RoutingBankMasterDTO {
	
	private BigDecimal applicationCountryId;
	
	private BigDecimal routingCountryId;
	
	private BigDecimal serviceGroupId;
	
	private BigDecimal serviceBankId;
	
	private String serviceBankCode;	
	
	private String serviceBankName;
	
	private BigDecimal agentBankId;
	
	private String agentBankName;
	
	private String agentBankCode;
	
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
	public BigDecimal getServiceBankId() {
		return serviceBankId;
	}
	public void setServiceBankId(BigDecimal serviceBankId) {
		this.serviceBankId = serviceBankId;
	}
	public String getServiceBankCode() {
		return serviceBankCode;
	}
	public void setServiceBankCode(String serviceBankCode) {
		this.serviceBankCode = serviceBankCode;
	}
	public String getServiceBankName() {
		return serviceBankName;
	}
	public void setServiceBankName(String serviceBankName) {
		this.serviceBankName = serviceBankName;
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
