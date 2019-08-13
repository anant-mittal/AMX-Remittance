package com.amx.jax.dbmodel;

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
@Table(name="EX_TPC_UPLOAD")
public class FileUploadTempModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2347199070381763989L;
	
	@Column(name="ACCOUNT_NO")
	private String accountNo;
	@Column(name="APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	@Column(name="COMM_SHARE_LOCAL")
	private BigDecimal companyShareLocal;
	@Column(name="COMPANY_CODE")
	private BigDecimal companyCode;
	@Column(name="COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;
	@Column(name="DIRECTION")
	private String direction;
	@Column(name="DOCUMENT_CODE")
	private String documentCode;
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	@Column(name="DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	@Column(name="DOCUMENT_NO")
	private BigDecimal documentNo;
	@Column(name="EXCHANGE_GAIN_LOCAL")
	private BigDecimal exchangeGainLocal;
	@Column(name="EXCLUDE_REASON")
	private String excludeReason;
	@Column(name="LOCAL_CURRCODE")
	private String localCurrencyCode;
	@Column(name="LOCAL_YEAR")
	private BigDecimal localYear;
	@Column(name="LOCATION_NAME")
	private String locationName;
	@Column(name="MTCN_NO")
	private String mtcnNo;
	@Column(name="REPORTING_DATE")
	private BigDecimal reportingDate;
	@Column(name="REPORTING_MONTH")
	private BigDecimal reportingMonth;
	@Column(name="REPORTING_YEAR")
	private BigDecimal reportingYear;
	@Column(name="SEND_PAY_INDIC")
	private String sendPayIndicator;
	@Column(name="TPC_CODE")
	private String tpcCode;
	@Column(name="UPLOAD_DATE")
	private Date uploadDate;
	@Id
	@GeneratedValue(generator = "ex_tpc_upload_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_tpc_upload_seq", sequenceName = "EX_TPC_UPLOAD_SEQ", allocationSize = 1)
	@Column(name = "UPLOAD_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal uploadId;
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public BigDecimal getCompanyShareLocal() {
		return companyShareLocal;
	}
	public void setCompanyShareLocal(BigDecimal companyShareLocal) {
		this.companyShareLocal = companyShareLocal;
	}
	public BigDecimal getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}
	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}
	public BigDecimal getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}
	public BigDecimal getExchangeGainLocal() {
		return exchangeGainLocal;
	}
	public void setExchangeGainLocal(BigDecimal exchangeGainLocal) {
		this.exchangeGainLocal = exchangeGainLocal;
	}
	public String getExcludeReason() {
		return excludeReason;
	}
	public void setExcludeReason(String excludeReason) {
		this.excludeReason = excludeReason;
	}
	public String getLocalCurrencyCode() {
		return localCurrencyCode;
	}
	public void setLocalCurrencyCode(String localCurrencyCode) {
		this.localCurrencyCode = localCurrencyCode;
	}
	public BigDecimal getLocalYear() {
		return localYear;
	}
	public void setLocalYear(BigDecimal localYear) {
		this.localYear = localYear;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getMtcnNo() {
		return mtcnNo;
	}
	public void setMtcnNo(String mtcnNo) {
		this.mtcnNo = mtcnNo;
	}
	public BigDecimal getReportingDate() {
		return reportingDate;
	}
	public void setReportingDate(BigDecimal reportingDate) {
		this.reportingDate = reportingDate;
	}
	public BigDecimal getReportingMonth() {
		return reportingMonth;
	}
	public void setReportingMonth(BigDecimal reportingMonth) {
		this.reportingMonth = reportingMonth;
	}
	public BigDecimal getReportingYear() {
		return reportingYear;
	}
	public void setReportingYear(BigDecimal reportingYear) {
		this.reportingYear = reportingYear;
	}
	public String getSendPayIndicator() {
		return sendPayIndicator;
	}
	public void setSendPayIndicator(String sendPayIndicator) {
		this.sendPayIndicator = sendPayIndicator;
	}
	public String getTpcCode() {
		return tpcCode;
	}
	public void setTpcCode(String tpcCode) {
		this.tpcCode = tpcCode;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	public BigDecimal getUploadId() {
		return uploadId;
	}
	public void setUploadId(BigDecimal uploadId) {
		this.uploadId = uploadId;
	}
	
}
