package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dbmodel.LanguageType;

@Entity
@Table(name = "EX_DOCUMENT")
public class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal documentID;
	private BigDecimal documentCode;
	private String documentDesc;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private String remarks;
	private String isActive;

	private LanguageType fsLanguageType;

	private Set<RemittanceApplication> exRemittanceApplication = new HashSet<RemittanceApplication>(0);
	private Set<RemittanceAppBenificiary> exRemittanceAppBenificiary = new HashSet<RemittanceAppBenificiary>(0);
	private Set<AdditionalInstructionData> additionalInstData = new HashSet<AdditionalInstructionData>(0);

	public Document() {
	}

	@Id
	@GeneratedValue(generator = "ex_document_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_document_seq", sequenceName = "EX_DOCUMENT_SEQ", allocationSize = 1)
	@Column(name = "DOCUMENT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getDocumentID() {
		return documentID;
	}

	public void setDocumentID(BigDecimal documentID) {
		this.documentID = documentID;
	}

	@Column(name = "DOCUMENT_CODE")
	public BigDecimal getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}

	@Column(name = "DOCUMENT_DESC")
	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
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

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy =
	 * "customerSpeacialDealReqDocument") public List<CustomerSpecialDealRequest>
	 * getCustomerSpeacialDealReqDocument() { return
	 * customerSpeacialDealReqDocument; }
	 * 
	 * public void setCustomerSpeacialDealReqDocument(
	 * List<CustomerSpecialDealRequest> customerSpeacialDealReqDocument) {
	 * this.customerSpeacialDealReqDocument = customerSpeacialDealReqDocument; }
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID")
	public LanguageType getFsLanguageType() {
		return fsLanguageType;
	}

	public void setFsLanguageType(LanguageType fsLanguageType) {
		this.fsLanguageType = fsLanguageType;
	}

	/*
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocument") public
	 * Set<TreasuryDealHeader> getExDealHeader() { return exDealHeader; }
	 * 
	 * public void setExDealHeader(Set<TreasuryDealHeader> exDealHeader) {
	 * this.exDealHeader = exDealHeader; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "treasuryDealDocument") public
	 * Set<TreasuryDealDetail> getExDealDetail() { return exDealDetail; }
	 * 
	 * public void setExDealDetail(Set<TreasuryDealDetail> exDealDetail) {
	 * this.exDealDetail = exDealDetail; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "treasurydocDocument") public
	 * Set<TreasuryStandardInstruction> getExTreasuryStandardIns() { return
	 * exTreasuryStandardIns; }
	 * 
	 * public void setExTreasuryStandardIns( Set<TreasuryStandardInstruction>
	 * exTreasuryStandardIns) { this.exTreasuryStandardIns = exTreasuryStandardIns;
	 * }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "doucDocumentId") public
	 * Set<DayBookHeader> getExDayBook() { return exDayBook; }
	 * 
	 * public void setExDayBook(Set<DayBookHeader> exDayBook) { this.exDayBook =
	 * exDayBook; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "dayBookDocumentId") public
	 * Set<DayBookDetails> getDayBookDetailsList() { return dayBookDetailsList; }
	 * 
	 * public void setDayBookDetailsList(Set<DayBookDetails> dayBookDetailsList) {
	 * this.dayBookDetailsList = dayBookDetailsList; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "specialRequestDocumentCode")
	 * public List<TreasuryDealDetail> getExTreasuryDealDetails() { return
	 * exTreasuryDealDetails; }
	 * 
	 * public void setExTreasuryDealDetails( List<TreasuryDealDetail>
	 * exTreasuryDealDetails) { this.exTreasuryDealDetails = exTreasuryDealDetails;
	 * }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "fsDocument") public
	 * List<SpecialRateRequest> getSpecialRateRequest() { return specialRateRequest;
	 * }
	 * 
	 * public void setSpecialRateRequest(List<SpecialRateRequest>
	 * specialRateRequest) { this.specialRateRequest = specialRateRequest; }
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocument") public
	 * Set<Remittance> getExRemittance() { return exRemittance; }
	 * 
	 * public void setExRemittance(Set<Remittance> exRemittance) { this.exRemittance
	 * = exRemittance; }
	 */

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocument")
	public Set<RemittanceApplication> getExRemittanceApplication() {
		return exRemittanceApplication;
	}

	public void setExRemittanceApplication(Set<RemittanceApplication> exRemittanceApplication) {
		this.exRemittanceApplication = exRemittanceApplication;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocument")
	public Set<RemittanceAppBenificiary> getExRemittanceAppBenificiary() {
		return exRemittanceAppBenificiary;
	}

	public void setExRemittanceAppBenificiary(Set<RemittanceAppBenificiary> exRemittanceAppBenificiary) {
		this.exRemittanceAppBenificiary = exRemittanceAppBenificiary;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exDocument")
	public Set<AdditionalInstructionData> getAdditionalInstData() {
		return additionalInstData;
	}

	public void setAdditionalInstData(Set<AdditionalInstructionData> additionalInstData) {
		this.additionalInstData = additionalInstData;
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

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}