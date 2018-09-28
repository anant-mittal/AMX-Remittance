package com.amx.jax.dbmodel.routing;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_ROUTING_HEADER")
public class RoutingHeader implements java.io.Serializable {

	private static final long serialVersionUID = -59155724624336535L;

	private BigDecimal routingHeaderId;
	private BigDecimal exApplicationCountry;
	private BigDecimal exCountryId;
	private BigDecimal exRoutingCountryId;
	private BigDecimal exRoutingBankId;
	private BigDecimal exCurrenyId;
	private BigDecimal exServiceId;
	private BigDecimal exDeliveryModeId;
	private String branchApplicability;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;

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
	public BigDecimal getExApplicationCountry() {
		return exApplicationCountry;
	}

	public void setExApplicationCountry(BigDecimal exApplicationCountry) {
		this.exApplicationCountry = exApplicationCountry;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getExCountryId() {
		return exCountryId;
	}

	public void setExCountryId(BigDecimal exCountryId) {
		this.exCountryId = exCountryId;
	}

	@Column(name = "ROUTING_COUNTRY_ID")
	public BigDecimal getExRoutingCountryId() {
		return exRoutingCountryId;
	}

	public void setExRoutingCountryId(BigDecimal exRoutingCountryId) {
		this.exRoutingCountryId = exRoutingCountryId;
	}

	@Column(name = "ROUTING_BANK_ID")
	public BigDecimal getExRoutingBankId() {
		return exRoutingBankId;
	}

	public void setExRoutingBankId(BigDecimal exRoutingBankId) {
		this.exRoutingBankId = exRoutingBankId;
	}

	@Column(name = "CURRENCY_ID")
	public BigDecimal getExCurrenyId() {
		return exCurrenyId;
	}

	public void setExCurrenyId(BigDecimal exCurrenyId) {
		this.exCurrenyId = exCurrenyId;
	}

	@Column(name = "SERVICE_MASTER_ID")
	public BigDecimal getExServiceId() {
		return exServiceId;
	}

	public void setExServiceId(BigDecimal exServiceId) {
		this.exServiceId = exServiceId;
	}

	@Column(name = "DELIVERY_MODE_ID")
	public BigDecimal getExDeliveryModeId() {
		return exDeliveryModeId;
	}

	public void setExDeliveryModeId(BigDecimal exDeliveryModeId) {
		this.exDeliveryModeId = exDeliveryModeId;
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

}
