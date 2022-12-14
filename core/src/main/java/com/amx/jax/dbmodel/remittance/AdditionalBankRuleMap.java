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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_ADDITIONAL_BANK_FIELDS")
public class AdditionalBankRuleMap implements Serializable {


	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal additionalBankRuleId;
	private String fieldName;
	private String flexField;
	private BigDecimal orderNo;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	private BigDecimal countryId;
	private Date approvedDate;
	private String approvedBy;
	
	private String remarks;
	

	private Set<AdditionalBankRuleAmiec> additionalBankRule = new HashSet<AdditionalBankRuleAmiec>(0);
	private Set<AdditionalBankRuleAddData> additionalBankRuleData = new HashSet<AdditionalBankRuleAddData>(0);
	private Set<AdditionalInstructionData> additionalInstData = new HashSet<AdditionalInstructionData>(0);
	
	
	public AdditionalBankRuleMap() {
	}

   
	public AdditionalBankRuleMap(BigDecimal additionalBankRuleId) {
		this.additionalBankRuleId = additionalBankRuleId;
	}

	

	

	@Id
	@GeneratedValue(generator = "ex_additional_bank_rule_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_additional_bank_rule_seq", sequenceName = "EX_ADDITIONAL_BANK_RULE_SEQ", allocationSize = 1)
	@Column(name = "ADDITIONAL_BANK_RULE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getAdditionalBankRuleId() {
		return additionalBankRuleId;
	}

	public void setAdditionalBankRuleId(BigDecimal additionalBankRuleId) {
		this.additionalBankRuleId = additionalBankRuleId;
	}

	

	@Column(name = "FIELD_NAME")
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "FLEX_FIELD")
	public String getFlexField() {
		return flexField;
	}

	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}

	@Column(name = "ORDER_NO")
	public BigDecimal getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(BigDecimal orderNo) {
		this.orderNo = orderNo;
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

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "additionalBankFieldId")
	public Set<AdditionalBankRuleAmiec> getAdditionalBankRule() {
		return additionalBankRule;
	}

	public void setAdditionalBankRule(
			Set<AdditionalBankRuleAmiec> additionalBankRule) {
		this.additionalBankRule = additionalBankRule;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "additionalBankFieldId")
	public Set<AdditionalBankRuleAddData> getAdditionalBankRuleData() {
		return additionalBankRuleData;
	}

	public void setAdditionalBankRuleData(
			Set<AdditionalBankRuleAddData> additionalBankRuleData) {
		this.additionalBankRuleData = additionalBankRuleData;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "additionalBankFieldsId")
	public Set<AdditionalInstructionData> getAdditionalInstData() {
		return additionalInstData;
	}

	public void setAdditionalInstData(
			Set<AdditionalInstructionData> additionalInstData) {
		this.additionalInstData = additionalInstData;
	}


	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}


	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}


	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}


	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}


	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
}
