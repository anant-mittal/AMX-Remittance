package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_EX_ROUTING_AGENTS")
public class RoutingBankMasterView {
	
	private String srno;
	private BigDecimal applicationCountryId;
	private BigDecimal countryId;
	private String countryCode;
	private String countryName;	
	private BigDecimal routingCountryId;
	private String routingCountryCode;	
	private String routingCountryName;	
	private BigDecimal routingBankId;
	private String routingBankCode;	
	private String routingBankName;	
	private BigDecimal currencyId;
	private String currencyCode;	
	private String currencyName;	
	private String serviceCode;
	private String serviceDescription;
	private String serviceGroupCode;	
	private BigDecimal serviceGroupId;
	private BigDecimal agentBankId;
	private String agentBankCode;
	private String agentBankName;
	private String routingDetailBranchAppl;
	private BigDecimal bankBranchId;
	private String languageInd;
	
	@Id
	@Column(name ="SRNO")
	public String getSrno() {
		return srno;
	}
	public void setSrno(String srno) {
		this.srno = srno;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID ")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name = "COUNTRY_CODE")
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	@Column(name = "COUNTRY_NAME")
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	@Column(name = "ROUTING_COUNTRY_ID")
	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}
	public void setRoutingCountryId(BigDecimal routingCountryId) {
		this.routingCountryId = routingCountryId;
	}
	
	@Column(name = "ROUTING_COUNTRY_CODE")
	public String getRoutingCountryCode() {
		return routingCountryCode;
	}
	public void setRoutingCountryCode(String routingCountryCode) {
		this.routingCountryCode = routingCountryCode;
	}
	
	@Column(name = "ROUTING_COUNTRY_NAME")
	public String getRoutingCountryName() {
		return routingCountryName;
	}
	public void setRoutingCountryName(String routingCountryName) {
		this.routingCountryName = routingCountryName;
	}
	
	@Column(name = "ROUTING_BANK_ID")
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	
	@Column(name = "ROUTING_BANK_CODE")
	public String getRoutingBankCode() {
		return routingBankCode;
	}
	public void setRoutingBankCode(String routingBankCode) {
		this.routingBankCode = routingBankCode;
	}
	
	@Column(name = "ROUTING_BANK_NAME")
	public String getRoutingBankName() {
		return routingBankName;
	}
	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}
	
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name = "CURRENCY_CODE")
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Column(name = "CURRENCY_NAME")
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	@Column(name = "SERVICE_CODE")
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	@Column(name = "SERVICE_DESCRIPTION")
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
	@Column(name = "SERVICE_GROUP_CODE")
	public String getServiceGroupCode() {
		return serviceGroupCode;
	}
	public void setServiceGroupCode(String serviceGroupCode) {
		this.serviceGroupCode = serviceGroupCode;
	}
	
	@Column(name = "SERVICE_GROUP_ID")
	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}
	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	@Column(name = "AGENT_BANK_ID")
	public BigDecimal getAgentBankId() {
		return agentBankId;
	}
	public void setAgentBankId(BigDecimal agentBankId) {
		this.agentBankId = agentBankId;
	}
	
	@Column(name = "AGENT_BANK_CODE")
	public String getAgentBankCode() {
		return agentBankCode;
	}
	public void setAgentBankCode(String agentBankCode) {
		this.agentBankCode = agentBankCode;
	}
	
	@Column(name = "AGENT_BANK_NAME")
	public String getAgentBankName() {
		return agentBankName;
	}
	public void setAgentBankName(String agentBankName) {
		this.agentBankName = agentBankName;
	}
	
	@Column(name = "ROUTING_DTL_BRANCH_APPL")
	public String getRoutingDetailBranchAppl() {
		return routingDetailBranchAppl;
	}
	public void setRoutingDetailBranchAppl(String routingDetailBranchAppl) {
		this.routingDetailBranchAppl = routingDetailBranchAppl;
	}
	
	@Column(name = "BANK_BRANCH_ID")
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	
	@Column(name = "LANGUAGE_IND")
	public String getLanguageInd() {
		return languageInd;
	}
	public void setLanguageInd(String languageInd) {
		this.languageInd = languageInd;
	}

}
