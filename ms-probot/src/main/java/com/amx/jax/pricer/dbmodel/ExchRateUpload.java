package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.pricer.var.PricerServiceConstants.IS_ACTIVE;
import com.amx.jax.pricer.var.PricerServiceConstants.RATE_UPLOAD_STATUS;

@Entity
@Table(name = "JAX_PR_EXCH_RATE_UPLOAD")
public class ExchRateUpload implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9130922039626397029L;

	@Id
	@GeneratedValue(generator = "exch_rate_upload_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "exch_rate_upload_seq", sequenceName = "JAX_PR_EXCH_RATE_UPLOAD_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "RULE_ID")
	private String ruleId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;

	@Column(name = "COR_BANK_ID")
	private BigDecimal corBankId;

	@Column(name = "SERVICE_ID")
	private BigDecimal serviceId;

	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;

	@Column(name = "SELL_RATE")
	private BigDecimal sellRate;

	@Column(name = "BUY_RATE")
	private BigDecimal buyRate;

	@Column(name = "IS_ACTIVE")
	@Enumerated(value = EnumType.STRING)
	private IS_ACTIVE isActive;

	@Column(name = "STATUS")
	@Enumerated(value = EnumType.STRING)
	private RATE_UPLOAD_STATUS status;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "APPROVED_BY")
	private String approvedBy;

	@Column(name = "APPROVED_DATE")
	private Date approvedDate;

	@Column(name = "COMMENT")
	private String comment;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCorBankId() {
		return corBankId;
	}

	public void setCorBankId(BigDecimal corBankId) {
		this.corBankId = corBankId;
	}

	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public BigDecimal getSellRate() {
		return sellRate;
	}

	public void setSellRate(BigDecimal sellRate) {
		this.sellRate = sellRate;
	}

	public BigDecimal getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(BigDecimal buyRate) {
		this.buyRate = buyRate;
	}

	public IS_ACTIVE getIsActive() {
		return isActive;
	}

	public void setIsActive(IS_ACTIVE isActive) {
		this.isActive = isActive;
	}

	public RATE_UPLOAD_STATUS getStatus() {
		return status;
	}

	public void setStatus(RATE_UPLOAD_STATUS status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public ExchRateUpload clone() {
		try {
			return (ExchRateUpload) super.clone();
		} catch (CloneNotSupportedException e) {

			ExchRateUpload newRate = new ExchRateUpload();

			newRate.id = this.id;
			newRate.ruleId = this.ruleId;
			newRate.currencyId = this.currencyId;
			newRate.countryId = this.countryId;
			newRate.corBankId = this.corBankId;
			newRate.serviceId = this.serviceId;
			newRate.countryBranchId = this.countryBranchId;
			newRate.sellRate = this.sellRate;
			newRate.buyRate = this.buyRate;
			newRate.isActive = this.isActive;
			newRate.status = this.status;
			newRate.createdBy = this.createdBy;
			newRate.createdDate = this.createdDate;
			newRate.modifiedBy = this.modifiedBy;
			newRate.modifiedDate = this.modifiedDate;
			newRate.approvedBy = this.approvedBy;
			newRate.approvedDate = this.approvedDate;

			return newRate;
		}
	}

	/*
	 * public List<ExchRateUpload> multiCloneForField(Field field, List<Object>
	 * fieldValues) throws NoSuchFieldException, SecurityException {
	 * 
	 * List<ExchRateUpload> multiCloned = new ArrayList<ExchRateUpload>();
	 * 
	 * if (field == null || fieldValues == null || fieldValues.isEmpty()) {
	 * multiCloned.add(this.clone()); return multiCloned; } else if
	 * (field.equals(this.getClass().getDeclaredField("corBankId"))) {
	 * 
	 * }
	 * 
	 * }
	 */

}
