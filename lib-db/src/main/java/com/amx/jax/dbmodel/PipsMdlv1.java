package com.amx.jax.dbmodel;

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

@Entity
@Table(name = "EX_PIPS_MASTER")
public class PipsMdlv1 implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal pipsMasterId;
	private CountryBranchMdlv1 countryBranch;
	private CountryMaster countryMaster;
	private BankMasterMdlv1 bankMaster;
	private CurrencyMasterMdlv1 currencyMaster;
	/* private ServiceIndicator serviceIndicator; */
	private BigDecimal pipsNo;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private BigDecimal fromAmount;
	private BigDecimal toAmount;
	private String approvedBy;
	private Date approvedDate;
	private BigDecimal derivedSellRate;

	//private String pipsTypeCode;

	public PipsMdlv1() {
	}

	public PipsMdlv1(BigDecimal pipsMasterId) {
		this.pipsMasterId = pipsMasterId;
	}

	@Id
	@GeneratedValue(generator = "ex_pips_master_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_pips_master_seq", sequenceName = "EX_PIPS_MASTER_SEQ", allocationSize = 1)
	@Column(name = "PIPS_MASTER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getPipsMasterId() {
		return pipsMasterId;
	}

	public void setPipsMasterId(BigDecimal pipsMasterId) {
		this.pipsMasterId = pipsMasterId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_BRANCH_ID")
	public CountryBranchMdlv1 getCountryBranch() {
		return countryBranch;
	}

	public void setCountryBranch(CountryBranchMdlv1 countryBranch) {
		this.countryBranch = countryBranch;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public CountryMaster getCountryMaster() {
		return countryMaster;
	}

	public void setCountryMaster(CountryMaster countryMaster) {
		this.countryMaster = countryMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_ID")
	public BankMasterMdlv1 getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMasterMdlv1 bankMaster) {
		this.bankMaster = bankMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENCY_ID")
	public CurrencyMasterMdlv1 getCurrencyMaster() {
		return currencyMaster;
	}

	public void setCurrencyMaster(CurrencyMasterMdlv1 currencyMaster) {
		this.currencyMaster = currencyMaster;
	}

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "SERVICE_INDICATOR_ID") public ServiceIndicator
	 * getServiceIndicator() { return serviceIndicator; } public void
	 * setServiceIndicator(ServiceIndicator serviceIndicator) {
	 * this.serviceIndicator = serviceIndicator; }
	 */
	@Column(name = "PIPS_NO")
	public BigDecimal getPipsNo() {
		return pipsNo;
	}

	public void setPipsNo(BigDecimal pipsNo) {
		this.pipsNo = pipsNo;
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

	@Column(name = "FROM_AMOUNT")
	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	@Column(name = "TO_AMOUNT")
	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
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

	@Column(name = "DERIVED_SELL_RATE")
	public BigDecimal getDerivedSellRate() {
		return derivedSellRate;
	}

	public void setDerivedSellRate(BigDecimal derivedSellRate) {
		this.derivedSellRate = derivedSellRate;
	}

}
