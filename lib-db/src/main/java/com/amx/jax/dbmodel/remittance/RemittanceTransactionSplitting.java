package com.amx.jax.dbmodel.remittance;

/**
 * @author rabil
 */
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
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;


@Entity
@Table(name = "EX_REMIT_SPLIT")
public class RemittanceTransactionSplitting implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal remitTrnxSplitId;
	
	@Column(name = "REMITTANCE_TRANSACTION_ID")
	private BigDecimal remittanceTransactionId;
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	private CountryMaster applicationCountryId;
	
	@Column(name = "COMPANY_ID")
	private CompanyMaster companyId;
	
	@Column(name = "DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	
	@Column(name = "DOCUMENT_ID")
	private BigDecimal documentId;
	
	@Column(name = "DOCUMENT_NO")
	private BigDecimal documentNo;
	
	
	@Column(name = "DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name = "ACCOUNT_MMYYYY")
	private Date accountMmyyyy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "ISACTIVE")
	private String isactive;
	
	@Column(name = "SEND_DATE")
	private Date sendDate;
	
	@Column(name = "PAID_DATE")
	private Date paidDate;
	
	
	@Column(name = "RETURN_DATE")
	private Date returnDate;
	
	@Column(name = "SPLIT_DOCUMENT_NO") 
	private String splitDocumentNo;
	

	@Column(name = "FOREIGN_CURRENCY_ID")
	private BigDecimal foreignCurrencyId;
	@Column(name = "FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTranxAmount;
	@Column(name = "LOCAL_TRANX_CURRENCY_ID")
	private BigDecimal localTranxCurrencyId;
	@Column(name = "LOCAL_TRANX_AMOUNT")
	private BigDecimal localTranxAmount;
	
	

	@Id
	@GeneratedValue(generator="ex_remit_split_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_remit_split_seq",sequenceName="EX_REMIT_SPLIT_SEQ",allocationSize=1)
	@Column(name = "REMITTANCE_SPLIT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRemitTrnxSplitId() {
		return remitTrnxSplitId;
	}
	public void setRemitTrnxSplitId(BigDecimal remitTrnxSplitId) {
		this.remitTrnxSplitId = remitTrnxSplitId;
	}
	
	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}
	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}
	public CountryMaster getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(CountryMaster applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public CompanyMaster getCompanyId() {
		return companyId;
	}
	public void setCompanyId(CompanyMaster companyId) {
		this.companyId = companyId;
	}
	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}
	public BigDecimal getDocumentId() {
		return documentId;
	}
	public void setDocumentId(BigDecimal documentId) {
		this.documentId = documentId;
	}
	public BigDecimal getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public Date getAccountMmyyyy() {
		return accountMmyyyy;
	}
	public void setAccountMmyyyy(Date accountMmyyyy) {
		this.accountMmyyyy = accountMmyyyy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Date getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public String getSplitDocumentNo() {
		return splitDocumentNo;
	}
	public void setSplitDocumentNo(String splitDocumentNo) {
		this.splitDocumentNo = splitDocumentNo;
	}
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
	public BigDecimal getForeignTranxAmount() {
		return foreignTranxAmount;
	}
	public void setForeignTranxAmount(BigDecimal foreignTranxAmount) {
		this.foreignTranxAmount = foreignTranxAmount;
	}
	public BigDecimal getLocalTranxCurrencyId() {
		return localTranxCurrencyId;
	}
	public void setLocalTranxCurrencyId(BigDecimal localTranxCurrencyId) {
		this.localTranxCurrencyId = localTranxCurrencyId;
	}
	public BigDecimal getLocalTranxAmount() {
		return localTranxAmount;
	}
	public void setLocalTranxAmount(BigDecimal localTranxAmount) {
		this.localTranxAmount = localTranxAmount;
	}
	
}




