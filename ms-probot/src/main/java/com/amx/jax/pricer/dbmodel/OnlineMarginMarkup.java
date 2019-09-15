package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
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
@Table(name = "EX_ONLINE_MARGIN_MARKUP")
public class OnlineMarginMarkup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8010884338988580708L;

	@Id
	@GeneratedValue(generator="EX_ONLINE_MARGIN_MARKUP_SEQ",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="EX_ONLINE_MARGIN_MARKUP_SEQ" ,sequenceName="EX_ONLINE_MARGIN_MARKUP_SEQ",allocationSize=1)		
	@Column(name = "ONLINE_MARGIN_MARKUP_ID", unique = true, nullable = false)
	private BigDecimal onlineMarginMarkupId;

	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;

	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "BANK_ID")
	private BigDecimal bankId;

	@Column(name = "EMOS_COUNTRY")
	private String emosCountry;

	@Column(name = "EMOS_CURCOD")
	private String emosCurcod;

	@Column(name = "MARGIN_MARKUP")
	private BigDecimal marginMarkup;

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

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "REMARKS")
	private String remarks;

	public BigDecimal getOnlineMarginMarkupId() {
		return onlineMarginMarkupId;
	}

	public void setOnlineMarginMarkupId(BigDecimal onlineMarginMarkupId) {
		this.onlineMarginMarkupId = onlineMarginMarkupId;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getEmosCountry() {
		return emosCountry;
	}

	public void setEmosCountry(String emosCountry) {
		this.emosCountry = emosCountry;
	}

	public String getEmosCurcod() {
		return emosCurcod;
	}

	public void setEmosCurcod(String emosCurcod) {
		this.emosCurcod = emosCurcod;
	}

	public BigDecimal getMarginMarkup() {
		return marginMarkup;
	}

	public void setMarginMarkup(BigDecimal marginMarkup) {
		this.marginMarkup = marginMarkup;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "OnlineMarginMarkup [onlineMarginMarkupId=" + onlineMarginMarkupId + ", applicationCountryId="
				+ applicationCountryId + ", countryId=" + countryId + ", currencyId=" + currencyId + ", emosCountry="
				+ emosCountry + ", emosCurcod=" + emosCurcod + ", marginMarkup=" + marginMarkup + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", approvedBy=" + approvedBy + ", approvedDate=" + approvedDate + ", isActive="
				+ isActive + ", remarks=" + remarks + "]";
	}

}
