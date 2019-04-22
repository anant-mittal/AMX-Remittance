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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.UserFinancialYear;

@Entity
@Table(name ="EX_REMIT_ADDL_DATA")
public class RemittanceAdditionalInstructionData implements Serializable {

	/**
	 * 
	 */
	
	 
	private static final long serialVersionUID = 1L;
	private BigDecimal remittanceTrnxAddDataId; //REMITTANCE_TRANX_ADD_DATA_ID
	private Document exDocument;
	private CompanyMaster fsCompanyMaster;
	private UserFinancialYear exUserFinancialYear;
	private RemittanceTransaction exRemittanceTransaction;
	private CountryMaster fsCountryMaster;
	private AdditionalBankRuleMap additionalBankFieldsId;
	private BigDecimal documentFinanceYear;
	private BigDecimal documentNo;
	private String flexField;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isactive;
	private String flexFieldValue;
	private String amiecCode;
	private BigDecimal companyCode;
	


	public RemittanceAdditionalInstructionData() {
	}

	public RemittanceAdditionalInstructionData(
			BigDecimal remittanceTrnxAddDataId) {
	
		this.remittanceTrnxAddDataId = remittanceTrnxAddDataId;
	}

	
	@Id
	@GeneratedValue(generator="ex_remit_trnx_add_data_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_remit_trnx_add_data_seq",sequenceName="EX_REMIT_TRANX_ADD_DATA_SEQ",allocationSize=1)
	@Column(name = "REMITTANCE_TRANX_ADD_DATA_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRemittanceTrnxAddDataId() {
		return this.remittanceTrnxAddDataId;
	}

	public void setRemittanceTrnxAddDataId(
			BigDecimal remittanceTrnxAddDataId) {
		this.remittanceTrnxAddDataId = remittanceTrnxAddDataId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_ID")
	public Document getExDocument() {
		return this.exDocument;
	}

	public void setExDocument(Document exDocument) {
		this.exDocument = exDocument;
	}

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	public CompanyMaster getFsCompanyMaster() {
		return this.fsCompanyMaster;
	}

	public void setFsCompanyMaster(CompanyMaster fsCompanyMaster) {
		this.fsCompanyMaster = fsCompanyMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_FINANCE_YEAR_ID")
	public UserFinancialYear getExUserFinancialYear() {
		return this.exUserFinancialYear;
	}

	public void setExUserFinancialYear(UserFinancialYear exUserFinancialYear) {
		this.exUserFinancialYear = exUserFinancialYear;
	}

	

	public void setExRemittanceTransaction(RemittanceTransaction exRemittanceTransaction) {
		this.exRemittanceTransaction = exRemittanceTransaction;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_COUNTRY_ID")
	public CountryMaster getFsCountryMaster() {
		return this.fsCountryMaster;
	}

	public void setFsCountryMaster(CountryMaster fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDITIONAL_BANK_RULE_ID")
	public AdditionalBankRuleMap getAdditionalBankFieldsId() {
		return this.additionalBankFieldsId;
	}

	public void setAdditionalBankFieldsId(AdditionalBankRuleMap additionalBankFieldsId) {
		this.additionalBankFieldsId = additionalBankFieldsId;
	}

	@Column(name = "DOCUMENT_FINANCE_YEAR", length = 4)
	public BigDecimal getDocumentFinanceYear() {
		return this.documentFinanceYear;
	}

	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	@Column(name = "DOCUMENT_NO", precision = 22, scale = 0)
	public BigDecimal getDocumentNo() {
		return this.documentNo;
	}

	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	@Column(name = "FLEX_FIELD", length = 40)
	public String getFlexField() {
		return this.flexField;
	}

	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}

	@Column(name = "CREATED_BY", length = 40)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE", length = 7)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "MODIFIED_BY", length = 40)
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getIsactive() {
		return this.isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	@Column(name = "FLEX_FIELD_VALUE", length = 40)
	public String getFlexFieldValue() {
		return this.flexFieldValue;
	}

	public void setFlexFieldValue(String flexFieldValue) {
		this.flexFieldValue = flexFieldValue;
	}

	
	@Column(name="AMIEC_CODE")
	public String getAmiecCode() {
		return amiecCode;
	}

	public void setAmiecCode(String amiecCode) {
		this.amiecCode = amiecCode;
	}

	@Column(name="COMPANY_CODE")
	public BigDecimal getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REMITTANCE_TRANSACTION_ID")
	public RemittanceTransaction getExRemittanceTransaction() {
		return exRemittanceTransaction;
	}

	
	
	
}
			