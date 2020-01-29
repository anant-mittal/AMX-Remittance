package com.amx.jax.dbmodel.auth;

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

import com.amx.jax.util.AmxDBConstants;

@Entity
@Table(name = "JAX_AUTH_BLOCKED_IP")
public class BlockedIPAdress {

	@Id
	@GeneratedValue(generator = "JAX_BLOCKED_IP_SEQ", strategy = GenerationType.SEQUENCE)
	@Column(name = "BLOCKED_IP_ID")
	@SequenceGenerator(sequenceName = "JAX_BLOCKED_IP_SEQ", name = "JAX_BLOCKED_IP_SEQ", allocationSize = 1)
	BigDecimal blockedIpId;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "UPDATED_DATE")
	Date updatedData;

	@Column(name = "IP_ADDRESS")
	String ipAddress;

	@Column(name = "BLOCK_REASON_CODE")
	@Enumerated(EnumType.STRING)
	IPBlockedReasoncode reasonCode;

	@Column(name = "ISACTIVE")
	String isActive;
	
	

	public BlockedIPAdress() {
		super();
	}

	public BlockedIPAdress(String ipAddress, IPBlockedReasoncode reasonCode) {
		super();
		this.ipAddress = ipAddress;
		this.reasonCode = reasonCode;
		this.createdDate = new Date();
		this.isActive = AmxDBConstants.Yes;
	}

	public BigDecimal getBlockedIpId() {
		return blockedIpId;
	}

	public void setBlockedIpId(BigDecimal blockedIpId) {
		this.blockedIpId = blockedIpId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedData() {
		return updatedData;
	}

	public void setUpdatedData(Date updatedData) {
		this.updatedData = updatedData;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public IPBlockedReasoncode getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(IPBlockedReasoncode reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
