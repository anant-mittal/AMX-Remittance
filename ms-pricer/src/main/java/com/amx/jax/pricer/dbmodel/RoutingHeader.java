package com.amx.jax.pricer.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_ROUTING_HEADER")
public class RoutingHeader implements java.io.Serializable {

	private static final long serialVersionUID = -59155724624336535L;

	private BigDecimal routingHeaderId;
	private BigDecimal applicationCountryId;
	private BigDecimal countryId;
	private BigDecimal routingCountryId;
	private BigDecimal routingBankId;
	private BigDecimal currenyId;
	private BigDecimal serviceMasterId;
	private BigDecimal remittanceModeId;
	private BigDecimal deliveryModeId;
	private String branchApplicability;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private String isActive;
	//private String activateImps;

	public RoutingHeader() {
		super();
	}

	@Id
	@GeneratedValue(generator = "ex_routing_header_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_routing_header_seq", sequenceName = "EX_ROUTING_HEADER_SEQ", allocationSize = 1)
	@Column(name = "ROUTING_HEADER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRoutingHeaderId() {
		return routingHeaderId;
	}

	public void setRoutingHeaderId(BigDecimal routingHeaderId) {
		this.routingHeaderId = routingHeaderId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountry) {
		this.applicationCountryId = applicationCountry;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal exCountryId) {
		this.countryId = exCountryId;
	}

	@Column(name = "ROUTING_COUNTRY_ID")
	public BigDecimal getRoutingCountryId() {
		return routingCountryId;
	}

	public void setRoutingCountryId(BigDecimal exRoutingCountryId) {
		this.routingCountryId = exRoutingCountryId;
	}

	@Column(name = "ROUTING_BANK_ID")
	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BigDecimal exRoutingBankId) {
		this.routingBankId = exRoutingBankId;
	}

	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrenyId() {
		return currenyId;
	}

	public void setCurrenyId(BigDecimal exCurrenyId) {
		this.currenyId = exCurrenyId;
	}

	@Column(name = "SERVICE_MASTER_ID")
	public BigDecimal getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(BigDecimal exServiceId) {
		this.serviceMasterId = exServiceId;
	}

	@Column(name = "DELIVERY_MODE_ID")
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal exDeliveryModeId) {
		this.deliveryModeId = exDeliveryModeId;
	}

	@Column(name = "BRANCH_APPLICABILITY")
	public String getBranchApplicability() {
		return branchApplicability;
	}

	public void setBranchApplicability(String branchApplicability) {
		this.branchApplicability = branchApplicability;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "REMITTANCE_MODE_ID")
	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}

	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	/*@Column(name = "ACTIVATE_IMPS")
	public String getActivateImps() {
		return activateImps;
	}

	public void setActivateImps(String activateImps) {
		this.activateImps = activateImps;
	}*/

}
