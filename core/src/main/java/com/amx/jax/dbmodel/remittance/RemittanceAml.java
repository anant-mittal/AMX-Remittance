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

import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;

@Entity
@Table(name = "EX_REMIT_AML")
public class RemittanceAml implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal remitAmlId;
	private CompanyMaster fsCompanyMaster;
	private RemittanceTransaction exRemittancefromAml;
	private CountryMaster fsCountryMaster;
	private String blackListReason;
	private String blackListClear;
	private String blackListRemarks;
	private Date blackListDate;
	private String blackListUser;
	private String blackClearedUser;
	private String customerSignature;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private String isactive;
	
	private String authType;
	private String authorizedBy;
	
	
	

	public RemittanceAml() {
	}

	public RemittanceAml(BigDecimal remitAmlId) {
		this.remitAmlId = remitAmlId;
	}


	public RemittanceAml(BigDecimal remitApplAmlId, CompanyMaster fsCompanyMaster, RemittanceApplication exRemittanceAppfromAml, CountryMaster fsCountryMaster, String blackListReason, String blackListClear, String blackListRemarks, Date blackListDate, String blackListUser, String blackClearedUser,
			String customerSignature, Date createdDate, String createdBy, Date modifiedDate, String modifiedBy, String isactive, String authType, String authorizedBy) {
		super();
		this.remitAmlId = remitAmlId;
		this.fsCompanyMaster = fsCompanyMaster;
		this.exRemittancefromAml = exRemittancefromAml;
		this.fsCountryMaster = fsCountryMaster;
		this.blackListReason = blackListReason;
		this.blackListClear = blackListClear;
		this.blackListRemarks = blackListRemarks;
		this.blackListDate = blackListDate;
		this.blackListUser = blackListUser;
		this.blackClearedUser = blackClearedUser;
		this.customerSignature = customerSignature;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.isactive = isactive;
		this.authType = authType;
		this.authorizedBy = authorizedBy;
	}

	@Id
	@GeneratedValue(generator="ex_remit_trnx_aml_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_remit_trnx_aml_seq",sequenceName="EX_REMIT_TRANX_AML_SEQ",allocationSize=1)
	@Column(name = "REMIT_TRANX_AML_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRemitAmlId() {
		return this.remitAmlId;
	}

	public void setRemitAmlId(BigDecimal remitAmlId) {
		this.remitAmlId = remitAmlId;
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
	@JoinColumn(name = "REMITTANCE_TRANSACTION_ID")
	public RemittanceTransaction getExRemittancefromAml() {
		return exRemittancefromAml;
	}

	public void setExRemittancefromAml(RemittanceTransaction exRemittancefromAml) {
		this.exRemittancefromAml = exRemittancefromAml;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_COUNTRY_ID")
	public CountryMaster getFsCountryMaster() {
		return this.fsCountryMaster;
	}

	public void setFsCountryMaster(CountryMaster fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}

	@Column(name = "BLACK_LIST_REASON", length = 100)
	public String getBlackListReason() {
		return this.blackListReason;
	}

	public void setBlackListReason(String blackListReason) {
		this.blackListReason = blackListReason;
	}

	@Column(name = "BLACK_LIST_CLEAR", length = 1)
	public String getBlackListClear() {
		return this.blackListClear;
	}

	public void setBlackListClear(String blackListClear) {
		this.blackListClear = blackListClear;
	}

	@Column(name = "BLACK_LIST_REMARKS", length = 60)
	public String getBlackListRemarks() {
		return this.blackListRemarks;
	}

	public void setBlackListRemarks(String blackListRemarks) {
		this.blackListRemarks = blackListRemarks;
	}

	
	@Column(name = "BLACK_LIST_DATE", length = 7)
	public Date getBlackListDate() {
		return this.blackListDate;
	}

	public void setBlackListDate(Date blackListDate) {
		this.blackListDate = blackListDate;
	}

	@Column(name = "BLACK_LIST_USER", length = 60)
	public String getBlackListUser() {
		return this.blackListUser;
	}

	public void setBlackListUser(String blackListUser) {
		this.blackListUser = blackListUser;
	}

	@Column(name = "BLACK_CLEARED_USER", length = 60)
	public String getBlackClearedUser() {
		return this.blackClearedUser;
	}

	public void setBlackClearedUser(String blackClearedUser) {
		this.blackClearedUser = blackClearedUser;
	}

	@Column(name = "CUSTOMER_SIGNATURE", length = 4000)
	public String getCustomerSignature() {
		return this.customerSignature;
	}

	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}

	
	@Column(name = "CREATED_DATE", length = 7)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CREATED_BY", length = 50)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	
	@Column(name = "MODIFIED_DATE", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "MODIFIED_BY", length = 50)
	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getIsactive() {
		return this.isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	@Column(name = "AUTH_TYP")
	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	
	@Column(name = "AUTHORIZED_BY")
	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
