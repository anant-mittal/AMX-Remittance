package com.amx.jax.dbmodel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.AmxSharedConfig.CommunicationPrefs;

@Entity
@Table(name = "JAX_COMMUNICATION_PREFERENCES")
public class CommunicationPrefsModel implements CommunicationPrefs {

	@Id
	@Column(name = "COMMUNICATION_EVENT")
	String event;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "PUSH_NOTIFICATION")
	String pushPrefs;

	@Column(name = "WHATSAPP")
	String waPrefs;

	@Column(name = "EMAIL")
	String emailPrefs;

	@Column(name = "SMS")
	String smsPrefs;

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
	public String getPushPrefs() {
		return pushPrefs;
	}

	public void setPushPrefs(String pushPrefs) {
		this.pushPrefs = pushPrefs;
	}

	@Override
	public String getWaPrefs() {
		return waPrefs;
	}

	public void setWaPrefs(String waPrefs) {
		this.waPrefs = waPrefs;
	}

	@Override
	public String getEmailPrefs() {
		return emailPrefs;
	}

	public void setEmailPrefs(String emailPrefs) {
		this.emailPrefs = emailPrefs;
	}

	@Override
	public String getSmsPrefs() {
		return smsPrefs;
	}

	public void setSmsPrefs(String smsPrefs) {
		this.smsPrefs = smsPrefs;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
