package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dict.ContactType;
import com.amx.jax.logger.AuditActor.ActorType;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.AmxDBConstants.Status;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

@Entity
@Table(name = "EX_CONTACT_VERIFICATION")
public class CustomerContactVerification implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final int EXPIRY_DAY = 1;
	public static final int EXPIRY_DAY_WHATS_APP = 30;

	public CustomerContactVerification() {
	}

	public CustomerContactVerification(BigDecimal id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CustomerContactVerification [id=" + id + ", appCountryId=" + appCountryId + ", contactType="
				+ contactType + ", contactValue=" + contactValue + ", verificationCode=" + verificationCode
				+ ", customerId=" + customerId + ", isActive=" + isActive + ", createdDate=" + createdDate
				+ ", sendDate=" + sendDate
				+ ", verifiedDate=" + verifiedDate + "]";
	}

	private BigDecimal id;

	@Id
	@GeneratedValue(generator = "ex_contact_verification_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_contact_verification_seq", sequenceName = "EX_CONTACT_VERIFICATION_SEQ",
			allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	BigDecimal appCountryId;

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(BigDecimal appCountryId) {
		this.appCountryId = appCountryId;
	}

	ContactType contactType;

	@Column(name = "CONTACT_TYPE", length = 20)
	@Enumerated(value = EnumType.STRING)
	public ContactType getContactType() {
		return this.contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	String contactValue;

	@Column(name = "CONTACT_VALUE", length = 50)
	public String getContactValue() {
		return this.contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}

	@Column(name = "VERIFICATION_CODE", length = 20)
	String verificationCode;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	BigDecimal customerId;

	@Column(name = "CUSTOMER_ID", precision = 6, scale = 0)
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	Status isActive;

	@Column(name = "ISACTIVE", length = 1)
	@Enumerated(value = EnumType.STRING)
	public Status getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Status isActive) {
		this.isActive = isActive;
	}

	Date createdDate;

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	BigDecimal createdById;

	@Column(name = "CREATED_BY_ID")
	public BigDecimal getCreatedById() {
		return this.createdById;
	}

	public void setCreatedById(BigDecimal createdById) {
		this.createdById = createdById;
	}

	ActorType createdByType;

	@Column(name = "CREATED_BY_TYPE", length = 1)
	@Enumerated(value = EnumType.STRING)
	public ActorType getCreatedByType() {
		return createdByType;
	}

	public void setCreatedByType(ActorType createdByType) {
		this.createdByType = createdByType;
	}

	Date sendDate;

	@Column(name = "SEND_DATE")
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

//	BigDecimal sendById;
//
//	@Column(name = "SEND_BY_ID")
//	public BigDecimal getSendById() {
//		return this.sendById;
//	}
//
//	public void setSendById(BigDecimal sendById) {
//		this.sendById = sendById;
//	}
//
//	ActorType sendByType;
//
//	@Column(name = "Send_BY_TYPE", length = 1)
//	@Enumerated(value = EnumType.STRING)
//	public ActorType getSendByType() {
//		return sendByType;
//	}
//
//	public void setSendByType(ActorType sendByType) {
//		this.sendByType = sendByType;
//	}

	Date verifiedDate;

	@Column(name = "VERIFIED_DATE")
	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public boolean hasValidStatus() {
		return AmxDBConstants.Status.Y.equals(this.getIsActive());
	}

	public boolean hasExpired() {
		long intrval = Constants.TimeInterval.DAY;
		if (ContactType.WHATSAPP.equals(contactType)) {
			intrval = intrval * EXPIRY_DAY_WHATS_APP;
		}

		if (ArgUtil.isEmpty(this.getSendDate())) {
			return TimeUtils.isExpired(this.getCreatedDate(), intrval);
		}
		return TimeUtils.isExpired(this.getSendDate(), intrval);
	}

	public boolean hasVerified() {
		return AmxDBConstants.Status.N.equals(this.getIsActive());
	}

}
