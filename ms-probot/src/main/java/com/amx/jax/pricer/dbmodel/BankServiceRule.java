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
@Table(name="EX_BANK_SERVICE_RULE")
public class BankServiceRule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal bankServiceRuleId;
	private BigDecimal countryId;	
	private BigDecimal currencyId;
	private BigDecimal bankId;
	private String fullname;
	private BigDecimal deliveryDays;
	private String comissionAccNo;
	private String chargeAccNo;
	private String costDebitAccNo;
	private String costCreditAccNo;
	private String manualFeedBack;
	private String pinNo;
	private String pinPad;
	private String pinSpecialChar;
	private String remitSwift;
	private String isActive;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private String bankCode;
	private String testKeyInFile;
	private String bankFilePrefix;
	private Date approvedDate;
	private String approvedBy;
	private BigDecimal applicationCountryId;
	private BigDecimal remittanceModeId;
	private BigDecimal deliveryModeId;

	public BankServiceRule() {
		super();
	}

	@Id
	@GeneratedValue(generator="ex_bank_service_rule_id_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_bank_service_rule_id_seq",sequenceName="EX_BANK_SERVICE_RULE_ID_SEQ",allocationSize=1)
	@Column(name="BANK_SERVICE_RULE_ID", unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getBankServiceRuleId() {
		return bankServiceRuleId;
	}
	public void setBankServiceRuleId(BigDecimal bankServiceRuleId) {
		this.bankServiceRuleId = bankServiceRuleId;
	}

	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name="CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name="BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	@Column(name="FULL_NAME")
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Column(name="DELIVERY_DAYS")
	public BigDecimal getDeliveryDays() {
		return deliveryDays;
	}
	public void setDeliveryDays(BigDecimal deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	@Column(name="COMMISSION_ACC_NO")
	public String getComissionAccNo() {
		return comissionAccNo;
	}
	public void setComissionAccNo(String comissionAccNo) {
		this.comissionAccNo = comissionAccNo;
	}

	@Column(name="CHARGE_ACC_NO")
	public String getChargeAccNo() {
		return chargeAccNo;
	}
	public void setChargeAccNo(String chargeAccNo) {
		this.chargeAccNo = chargeAccNo;
	}

	@Column(name="COST_DB_ACC_NO")
	public String getCostDebitAccNo() {
		return costDebitAccNo;
	}
	public void setCostDebitAccNo(String costDebitAccNo) {
		this.costDebitAccNo = costDebitAccNo;
	}

	@Column(name="COST_CR_ACC_NO")
	public String getCostCreditAccNo() {
		return costCreditAccNo;
	}
	public void setCostCreditAccNo(String costCreditAccNo) {
		this.costCreditAccNo = costCreditAccNo;
	}

	@Column(name="MANUAL_FEEDBACK")
	public String getManualFeedBack() {
		return manualFeedBack;
	}
	public void setManualFeedBack(String manualFeedBack) {
		this.manualFeedBack = manualFeedBack;
	}

	@Column(name="PIN_NO")
	public String getPinNo() {
		return pinNo;
	}
	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}

	@Column(name="PIN_PAD")
	public String getPinPad() {
		return pinPad;
	}
	public void setPinPad(String pinPad) {
		this.pinPad = pinPad;
	}

	@Column(name="PIN_SPECIAL_CHAR")
	public String getPinSpecialChar() {
		return pinSpecialChar;
	}
	public void setPinSpecialChar(String pinSpecialChar) {
		this.pinSpecialChar = pinSpecialChar;
	}

	@Column(name="REMIT_SWIFT")
	public String getRemitSwift() {
		return remitSwift;
	}
	public void setRemitSwift(String remitSwift) {
		this.remitSwift = remitSwift;
	}

	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name="TEST_KEY_IN_FILE")
	public String getTestKeyInFile() {
		return testKeyInFile;
	}
	public void setTestKeyInFile(String testKeyInFile) {
		this.testKeyInFile = testKeyInFile;
	}

	@Column(name="BANK_FILE_PREFIX")
	public String getBankFilePrefix() {
		return bankFilePrefix;
	}
	public void setBankFilePrefix(String bankFilePrefix) {
		this.bankFilePrefix = bankFilePrefix;
	}

	@Column(name="APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name="APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name="REMITTANCE_MODE_ID")
	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}
	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	@Column(name="DELIVERY_MODE_ID")
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

}
