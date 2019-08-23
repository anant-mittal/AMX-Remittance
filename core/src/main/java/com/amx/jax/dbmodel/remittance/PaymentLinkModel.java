package com.amx.jax.dbmodel.remittance;

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
@Table(name = "JAX_PAYMENT_LINK")
public class PaymentLinkModel implements Serializable {
	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal linkId;
	private BigDecimal applicationCountryId;
	private BigDecimal customerId;
	private BigDecimal paymentAmount;
	private String verificationCode;
	private String applicationIds;
	private String isActive;
	private Date linkDate;
	private Date paymentDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	
	private String paymentId;
	private String resultCode;
	private String pgReferenceId;
	private String pgTransactionId;
	private String pgAuthCode;
	private String pgErrorText;
	private String pgReceiptDate;
	private String errorMessage;
	private String errorCategory;

	@Id
	@GeneratedValue(generator = "ex_payment_link_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_payment_link_seq", sequenceName = "EX_PAYMENT_LINK_SEQ", allocationSize = 1)
	@Column(name = "ID")
	public BigDecimal getLinkId() {
		return linkId;
	}

	public void setLinkId(BigDecimal linkId) {
		this.linkId = linkId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "PAYMENT_AMOUNT")
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	@Column(name = "VERIFICATION_CODE")
	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Column(name = "APPLICATION_IDS")
	public String getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(String applicationIds) {
		this.applicationIds = applicationIds;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "LINK_DATE")
	public Date getLinkDate() {
		return linkDate;
	}

	public void setLinkDate(Date linkDate) {
		this.linkDate = linkDate;
	}

	@Column(name = "PAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "PAYMENT_ID")
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	@Column(name = "RESULT_CODE")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Column(name = "PG_REFERENCE_ID")
	public String getPgReferenceId() {
		return pgReferenceId;
	}

	public void setPgReferenceId(String pgReferenceId) {
		this.pgReferenceId = pgReferenceId;
	}

	@Column(name = "PG_TRANSACTION_ID")
	public String getPgTransactionId() {
		return pgTransactionId;
	}

	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	@Column(name = "PG_AUTH_CODE")
	public String getPgAuthCode() {
		return pgAuthCode;
	}

	public void setPgAuthCode(String pgAuthCode) {
		this.pgAuthCode = pgAuthCode;
	}

	@Column(name = "PG_ERROR_TEXT")
	public String getPgErrorText() {
		return pgErrorText;
	}

	public void setPgErrorText(String pgErrorText) {
		this.pgErrorText = pgErrorText;
	}

	
	@Column(name = "ERROR_MESSAGE")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name = "ERROR_CATEGORY")
	public String getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}

	@Column(name = "PG_RCEIPT_DATE")
	public String getPgReceiptDate() {
		return pgReceiptDate;
	}

	public void setPgReceiptDate(String pgReceiptDate) {
		this.pgReceiptDate = pgReceiptDate;
	}

}
