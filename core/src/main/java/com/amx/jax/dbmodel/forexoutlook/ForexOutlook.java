package com.amx.jax.dbmodel.forexoutlook;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "EX_FOREX_OUTLOOK")
public class ForexOutlook implements IResourceEntity  {


	private BigDecimal appCountryId;
	private BigDecimal pairId;
	private BigDecimal langId;
	private String outlookDesc;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	
	
	@Id
	@Column(name = "PAIR_ID")
	public BigDecimal getPairId() {
		return pairId;
	}

	public void setPairId(BigDecimal pairId) {
		this.pairId = pairId;
	}


	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(BigDecimal appCountryId) {
		this.appCountryId = appCountryId;
	}

	
	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLangId() {
		return langId;
	}

	public void setLangId(BigDecimal langId) {
		this.langId = langId;
	}

	@Column(name = "OUTLOOK_DESCRIPTION")
	public String getOutlookDesc() {
		return outlookDesc;
	}

	public void setOutlookDesc(String outlookDesc) {
		this.outlookDesc = outlookDesc;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	@Override
	public String toString() {
		return "ForexOutlook [appCountryId=" + appCountryId + ", pairId=" + pairId + ", langId=" + langId
				+ ", outlookDesc=" + outlookDesc + ", isActive=" + isActive + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ "]";
	}

	@Override
	public BigDecimal resourceId() {
		// TODO Auto-generated method stub
		return this.pairId;
	}

	@Override
	public String resourceName() {
		// TODO Auto-generated method stub
		return this.outlookDesc;
	}

	@Override
	public String resourceCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
