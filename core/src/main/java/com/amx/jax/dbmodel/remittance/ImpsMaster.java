package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
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

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.CountryMaster;

@Entity
@Table(name = "EX_IMPS_MASTER")
public class ImpsMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal impsId;
	private CountryMaster applicationCountryId;
	private CountryMaster fsCountryMaster;
	private BankMasterMdlv1 routingBankId;
	private BankMasterMdlv1 beneBankId;
	private String createdBy;
	private String approvedBy;
	private Date createdDate;
	private Date approvedDate;
	private String isActive;

	@Id
	@GeneratedValue(generator = "ex_imps_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_imps_seq", sequenceName = "EX_IMPS_SEQ", allocationSize = 1)
	@Column(name = "IMPS_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getImpsId() {
		return impsId;
	}

	public void setImpsId(BigDecimal impsId) {
		this.impsId = impsId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROUTING_BANK_ID")
	public BankMasterMdlv1 getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BankMasterMdlv1 routingBankId) {
		this.routingBankId = routingBankId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BENEFICARY_BANK_ID")
	public BankMasterMdlv1 getBeneBankId() {
		return beneBankId;
	}

	public void setBeneBankId(BankMasterMdlv1 beneBankId) {
		this.beneBankId = beneBankId;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/*
	 * public String getRemarks() { return remarks; }
	 * 
	 * 
	 * public void setRemarks(String remarks) { this.remarks = remarks; }
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_COUNTRY_ID")
	public CountryMaster getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(CountryMaster applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public CountryMaster getFsCountryMaster() {
		return this.fsCountryMaster;
	}

	public void setFsCountryMaster(CountryMaster fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}

}
