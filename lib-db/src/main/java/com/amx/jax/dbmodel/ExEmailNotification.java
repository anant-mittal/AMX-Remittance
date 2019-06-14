package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "EX_EMAIL_NOTIFICATION")
public class ExEmailNotification implements Serializable {
	
	  private static final long serialVersionUID = 1L;
	
	  private BigDecimal exEmailNotificationId;
	  private BigDecimal applicationCountryId;
	  private BigDecimal companyId;
	  private String emailId;
	  private String sendInd;
	  private String description;
	  private String createdBy;
	  private Date createdDate;
	  private String isActive;
	
	  

		public ExEmailNotification() {
	      super();
		}

		
		
		public ExEmailNotification( BigDecimal exEmailNotificationId,
				BigDecimal applicationCountryId, 
			    BigDecimal companyId, String emailId,
			    String sendInd, String description, String createdBy,
				Date createdDate, String isActive) {
			super();
			this.exEmailNotificationId = exEmailNotificationId;
			this.applicationCountryId = applicationCountryId;
			this.companyId = companyId;
			this.emailId = emailId;
			this.sendInd = sendInd;
			this.description = description;
			this.createdBy = createdBy;
			this.createdDate = createdDate;
			this.createdDate = createdDate;
			this.isActive = isActive;
		}

	  
	@Id
	@Column(name = "EX_EMAIL_NOTIFICATION_ID")
	public BigDecimal getExEmailNotificationId() {
		return exEmailNotificationId;
	}
	public void setExEmailNotificationId(BigDecimal exEmailNotificationId) {
		this.exEmailNotificationId = exEmailNotificationId;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name = "COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	
	@Column(name = "EMAIL_ID")
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	@Column(name = "SEND_IND")
	public String getSendInd() {
		return sendInd;
	}
	public void setSendInd(String sendInd) {
		this.sendInd = sendInd;
	}
	
	@Column(name = "DESCRIPTON")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	 	  
}
