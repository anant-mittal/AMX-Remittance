package com.amx.jax.dbmodel.remittance;

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

@Entity
@Table(name="LTY_CLAIM_REQUEST")
public class LoyaltyClaimRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 588780456533180093L;

	@Id
	@GeneratedValue(generator = "claim_request_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "claim_request_seq", sequenceName = "CLAIM_REQUEST_SEQ", allocationSize = 1)
	@Column(name = "REQID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal reqId;
	
	@Column(name = "CLAIMDATE")
	private Date claimDate;
	
	@Column(name = "ECM_CODE")
	private BigDecimal ecmCode;
	
	
	@Column(name = "CLAIMPOINTS")
	private BigDecimal claimPoints;
	

	@Column(name = "USERID")
	private String userId;
	
	@Column(name = "CALIMEDFLAG")
	private String claimFlag;
	
	@Column(name = "LOCCOD")
	private BigDecimal locCode;
	
	@Column(name = "DOCFYR")
	private BigDecimal docfyr;
	
	@Column(name = "VOUCHERNO")
	private BigDecimal voucherNo;
	
	@Column(name = "EC_DOCDAT")
	private Date ecDocDate;
	
	@Column(name = "EC_LOCCOD")
	private BigDecimal ecLocCode;
	
	@Column(name = "EC_VALUE")
	private BigDecimal ecValue;

	public BigDecimal getReqId() {
		return reqId;
	}

	public void setReqId(BigDecimal reqId) {
		this.reqId = reqId;
	}

	public Date getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate;
	}

	public BigDecimal getEcmCode() {
		return ecmCode;
	}

	public void setEcmCode(BigDecimal ecmCode) {
		this.ecmCode = ecmCode;
	}

	public BigDecimal getClaimPoints() {
		return claimPoints;
	}

	public void setClaimPoints(BigDecimal claimPoints) {
		this.claimPoints = claimPoints;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getClaimFlag() {
		return claimFlag;
	}

	public void setClaimFlag(String claimFlag) {
		this.claimFlag = claimFlag;
	}

	public BigDecimal getLocCode() {
		return locCode;
	}

	public void setLocCode(BigDecimal locCode) {
		this.locCode = locCode;
	}

	public BigDecimal getDocfyr() {
		return docfyr;
	}

	public void setDocfyr(BigDecimal docfyr) {
		this.docfyr = docfyr;
	}

	public BigDecimal getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(BigDecimal voucherNo) {
		this.voucherNo = voucherNo;
	}

	public Date getEcDocDate() {
		return ecDocDate;
	}

	public void setEcDocDate(Date ecDocDate) {
		this.ecDocDate = ecDocDate;
	}

	public BigDecimal getEcLocCode() {
		return ecLocCode;
	}

	public void setEcLocCode(BigDecimal ecLocCode) {
		this.ecLocCode = ecLocCode;
	}

	public BigDecimal getEcValue() {
		return ecValue;
	}

	public void setEcValue(BigDecimal ecValue) {
		this.ecValue = ecValue;
	}
	
}



