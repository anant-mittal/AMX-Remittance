package com.amx.jax.dbmodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;

@Entity
@Table(name = "JAX_COMMUNICATION_PREFERENCES")
public class CommunicationPrefsModel implements CommunicationPrefs {

	@Id
	@Column(name = "COMMUNICATION_EVENT")
	@Enumerated(value = EnumType.STRING)
	CommunicationEvents event;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "PUSH_NOTIFICATION")
	Long pushPrefs;

	@Column(name = "WHATSAPP")
	Long waPrefs;

	@Column(name = "EMAIL")
	Long emailPrefs;

	@Column(name = "SMS")
	Long smsPrefs;

	@Column(name = "ISACTIVE")
	String active;

	public CommunicationPrefsModel() {
		super();
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Long getPushPrefs() {
		return pushPrefs;
	}

	public void setPushPrefs(Long pushPrefs) {
		this.pushPrefs = pushPrefs;
	}

	@Override
	public Long getWaPrefs() {
		return waPrefs;
	}

	public void setWaPrefs(Long waPrefs) {
		this.waPrefs = waPrefs;
	}

	@Override
	public Long getEmailPrefs() {
		return emailPrefs;
	}

	public void setEmailPrefs(Long emailPrefs) {
		this.emailPrefs = emailPrefs;
	}

	@Override
	public Long getSmsPrefs() {
		return smsPrefs;
	}

	public void setSmsPrefs(Long smsPrefs) {
		this.smsPrefs = smsPrefs;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public CommunicationEvents getEvent() {
		return event;
	}

	public void setEvent(CommunicationEvents event) {
		this.event = event;
	}

}
