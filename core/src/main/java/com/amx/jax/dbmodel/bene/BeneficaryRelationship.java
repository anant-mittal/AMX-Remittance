package com.amx.jax.dbmodel.bene;

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

import com.amx.jax.dbmodel.Customer;

@Entity
@Table(name="EX_BENEFICARY_RELATIONSHIP")
public class BeneficaryRelationship implements Serializable {
	
	private static final long serialVersionUID = 2315791709068216697L;


	private BigDecimal beneficaryRelationshipId;
	private BigDecimal applicationCountry;
	private Customer customerId;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private BigDecimal beneficaryMasterId;
	private BigDecimal beneficaryAccountId;
	private BigDecimal mapSequenceId;
	private String remarks ;
	
	
	@Id
	@GeneratedValue(generator="ex_beneficary_relationship_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_beneficary_relationship_seq",sequenceName="EX_BENEFICARY_RELATIONSHIP_SEQ",allocationSize=1)
	@Column(name="BENEFICARY_RELATIONSHIP_SEQ_ID",unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getBeneficaryRelationshipId() {
		return beneficaryRelationshipId;
	}
	
	public void setBeneficaryRelationshipId(BigDecimal beneficaryRelationshipId) {
		this.beneficaryRelationshipId = beneficaryRelationshipId;
	}

	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountry() {
		return applicationCountry;
	}
	
	public void setApplicationCountry(BigDecimal applicationCountry) {
		this.applicationCountry = applicationCountry;
	}
	
	
	@Column(name="CUSTOMER_ID")
	public Customer getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="BENEFICARY_MASTER_SEQ_ID")
	public BigDecimal getBeneficaryMasterId() {
		return beneficaryMasterId;
	}
	
	public void setBeneficaryMasterId(BigDecimal beneficaryMaster) {
		this.beneficaryMasterId = beneficaryMaster;
	}
	
	
	
	/**
	 * @return the isActive
	 */
	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the createdBy
	 */
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDate
	 */
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the modifiedBy
	 */
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the modifiedDate
	 */
	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	
	
	
	
	@Column(name="MAP_BENE_SEQ")
	public BigDecimal getMapSequenceId() {
		return mapSequenceId;
	}

	public void setMapSequenceId(BigDecimal mapSequenceId) {
		this.mapSequenceId = mapSequenceId;
	}
	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="BENEFICARY_ACCOUNT_SEQ_ID")
	public BigDecimal getBeneficaryAccountId() {
		return beneficaryAccountId;
	}

	public void setBeneficaryAccountId(BigDecimal beneficaryAccountId) {
		this.beneficaryAccountId = beneficaryAccountId;
	}
	 
}
