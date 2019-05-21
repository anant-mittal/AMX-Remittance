
package com.amx.jax.pricer.dbmodel;

//default package
//Generated Jul 11, 2014 10:20:31 AM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



/**
* @author :Rabil
*/

@Entity
@Table(name = "EX_CURRENCY_MASTER")
public class CurrencyMasterModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3842382778044486312L;
	private BigDecimal currencyId;
	//private CountryMaster fsCountryMaster;
	private String currencyCode;
	private String currencyName;
	private String quoteName;
	//private Byte currencyDesc;
	private String decimalName;
	private String fimsCurrencyCode;
	private String isactive;
	private String arabicCurrencyName;
	private String arabicDecimalName;
	private String arabicQuoteName;
	private String swiftCurrency;
	private String isoCurrencyCode;
	private BigDecimal decinalNumber;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private String remarks;
	private BigDecimal countryId;
	private BigDecimal fundMinRate;
	private BigDecimal fundMaxRate;

	
	public CurrencyMasterModel() {
	}

	public CurrencyMasterModel(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	
	@Id
	@GeneratedValue(generator = "ex_currency_master_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_currency_master_seq", sequenceName = "EX_CURRENCY_MASTER_SEQ", allocationSize = 1)
	@Column(name = "CURRENCY_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public CountryMaster getFsCountryMaster() {
		return this.fsCountryMaster;
	}

	public void setFsCountryMaster(CountryMaster fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}*/

	@Column(name = "CURRENCY_CODE", length = 3, unique = true)
	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Column(name = "CURRENCY_NAME", length = 60)
	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Column(name = "QUOTE_NAME", length = 5)
	public String getQuoteName() {
		return this.quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	/*@Column(name = "CURRENCY_DESC", precision = 2, scale = 0)
	public Byte getCurrencyDesc() {
		return this.currencyDesc;
	}

	public void setCurrencyDesc(Byte currencyDesc) {
		this.currencyDesc = currencyDesc;
	}*/

	@Column(name = "DECIMAL_NAME", length = 10)
	public String getDecimalName() {
		return this.decimalName;
	}

	public void setDecimalName(String decimalName) {
		this.decimalName = decimalName;
	}

	@Column(name = "FIMS_CURRENCY_CODE", length = 3)
	public String getFimsCurrencyCode() {
		return this.fimsCurrencyCode;
	}

	public void setFimsCurrencyCode(String fimsCurrencyCode) {
		this.fimsCurrencyCode = fimsCurrencyCode;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getIsactive() {
		return this.isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	@Column(name = "ARABIC_CURRENCY_NAME", length = 120)
	public String getArabicCurrencyName() {
		return this.arabicCurrencyName;
	}

	public void setArabicCurrencyName(String arabicCurrencyName) {
		this.arabicCurrencyName = arabicCurrencyName;
	}

	@Column(name = "ARABIC_DECIMAL_NAME", length = 20)
	public String getArabicDecimalName() {
		return this.arabicDecimalName;
	}

	public void setArabicDecimalName(String arabicDecimalName) {
		this.arabicDecimalName = arabicDecimalName;
	}

	@Column(name = "ARABIC_QUOTE_NAME", length = 100)
	public String getArabicQuoteName() {
		return this.arabicQuoteName;
	}

	public void setArabicQuoteName(String arabicQuoteName) {
		this.arabicQuoteName = arabicQuoteName;
	}

	@Column(name = "SWIFT_CURRENCY", length = 3)
	public String getSwiftCurrency() {
		return this.swiftCurrency;
	}

	public void setSwiftCurrency(String swiftCurrency) {
		this.swiftCurrency = swiftCurrency;
	}

	@Column(name = "ISO_CURRENCY_CODE", length = 6)
	public String getIsoCurrencyCode() {
		return this.isoCurrencyCode;
	}

	public void setIsoCurrencyCode(String isoCurrencyCode) {
		this.isoCurrencyCode = isoCurrencyCode;
	}

	@Column(name = "DECIMAL_NUMBER", length = 1)
	public BigDecimal getDecinalNumber() {
		return decinalNumber;
	}

	public void setDecinalNumber(BigDecimal decinalNumber) {
		this.decinalNumber = decinalNumber;
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

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name="Fund_Min_Rate")
	public BigDecimal getFundMinRate() {
		return fundMinRate;
	}

	public void setFundMinRate(BigDecimal fundMinRate) {
		this.fundMinRate = fundMinRate;
	}

	@Column(name="Fund_Max_Rate")
	public BigDecimal getFundMaxRate() {
		return fundMaxRate;
	}

	public void setFundMaxRate(BigDecimal fundMaxRate) {
		this.fundMaxRate = fundMaxRate;
	}
	
}
