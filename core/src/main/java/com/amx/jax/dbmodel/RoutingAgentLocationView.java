package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_EX_ROUTING_AGENT_LOCATIONS")
public class RoutingAgentLocationView {
	
	private String srno;
	private BigDecimal applicationCountryId;
	private BigDecimal countryId;
	private BigDecimal routingCountryId;
	private BigDecimal currencyId;
	private BigDecimal routingBankId;
	private BigDecimal routingBranchId;
	private BigDecimal serviceGroupId;
	private BigDecimal agentBankId;
	private BigDecimal bankBranchId;
	private BigDecimal branchCode;
	private String branchFullName;
	private String swiftBic;	
	private String address1;	
	private String address2;
	private String ifsc;	
	private String stateName;
	private String districtName;
	
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
	
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name = "ROUTING_BANK_ID")
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}
	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}
	
	@Column(name = "ROUTING_BRANCH_ID")
	public BigDecimal getRoutingBranchId() {
		return routingBranchId;
	}
	public void setRoutingBranchId(BigDecimal routingBranchId) {
		this.routingBranchId = routingBranchId;
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
	
	@Column(name = "BANK_BRANCH_ID")
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	
	@Column(name = "BRANCH_CODE")
	public BigDecimal getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(BigDecimal branchCode) {
		this.branchCode = branchCode;
	}
	
	@Column(name = "BRANCH_FULL_NAME")
	public String getBranchFullName() {
		return branchFullName;
	}
	public void setBranchFullName(String branchFullName) {
		this.branchFullName = branchFullName;
	}
	
	@Column(name = "SWIFT_BIC")
	public String getSwiftBic() {
		return swiftBic;
	}
	public void setSwiftBic(String swiftBic) {
		this.swiftBic = swiftBic;
	}
	
	@Column(name = "ADDRESS1")
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	@Column(name = "ADDRESS2")
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	@Column(name = "IFSC")
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	
	@Column(name = "STATE_NAME")
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@Column(name = "DISTRICT_NAME")
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	


}
