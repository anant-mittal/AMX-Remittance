package com.amx.jax.dbmodel.remittance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="EX_REMITTANCE_MODE", uniqueConstraints = @UniqueConstraint(columnNames = {"REMITTANCE_CODE" }))
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RemittanceModeMaster {
	
	private  BigDecimal remittanceModeId;
	private String remittance;
/*	private String remittanceDesEnglish;
	private String remittanceDesArabic;*/
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	private String approvedBy;
	private Date approvedDate;
	private String remarks;
	
	
	public RemittanceModeMaster(BigDecimal remittanceModeId) {
		super();
		this.remittanceModeId = remittanceModeId;
	}
	public RemittanceModeMaster() {
		super();
	}
	
	
	@Id
	@GeneratedValue(generator="ex_remittance_mode_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_remittance_mode_seq",sequenceName="EX_REMITTANCE_MODE_SEQ",allocationSize=1)
	@Column(name ="REMITTANCE_MODE_ID" , unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}
	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	
	@Column(name="REMITTANCE_CODE")
	public String getRemittance() {
		return remittance;
	}
	public void setRemittance(String remittance) {
		this.remittance = remittance;
	}
	
	/*@Column(name="REMITTANCE_DESC_ENGLISH")
	public String getRemittanceDesEnglish() {
		return remittanceDesEnglish;
	}
	public void setRemittanceDesEnglish(String remittanceDesEnglish) {
		this.remittanceDesEnglish = remittanceDesEnglish;
	}
	
	@Column(name="REMITTANCE_DESC_ARABIC")
	public String getRemittanceDesArabic() {
		return remittanceDesArabic;
	}
	public void setRemittanceDesArabic(String remittanceDesArabic) {
		this.remittanceDesArabic = remittanceDesArabic;
	}*/
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	@Column(name= "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column(name= "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "remittanceModeId")
	public List<ServiceApplicabilityRule> getServiceApplicabilityRule() {
		return serviceApplicabilityRule;
	}
	public void setServiceApplicabilityRule(
			List<ServiceApplicabilityRule> serviceApplicabilityRule) {
		this.serviceApplicabilityRule = serviceApplicabilityRule;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "remittanceModeId")
	public List<BankServiceRule> getBankServiceRule() {
		return bankServiceRule;
	}
	public void setBankServiceRule(List<BankServiceRule> bankServiceRule) {
		this.bankServiceRule = bankServiceRule;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exRemittanceMode")
	public Set<Remittance> getExRemittance() {
		return exRemittance;
	}
	public void setExRemittance(Set<Remittance> exRemittance) {
		this.exRemittance = exRemittance;
	}
	*/
	
	@Column(name= "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	@Column(name= "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}
	
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	@Column(name= "REMARKS")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
	
}