package com.amx.jax.dbmodel.promotion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "EX_DAILY_PROMOTIONS_DTLS")
public class DailyPromotion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PROMO_CODE")
	private String promoCode;

	@Column(name = "REMITTANCE_TRANSACTION_ID")
	private BigDecimal remitTrnxId;

	@Column(name = "COMCOD")
	private BigDecimal comCode;

	@Column(name = "DOCCOD")
	private BigDecimal docCode;

	@Column(name = "DOCFYR")
	private BigDecimal docFyr;

	@Column(name = "DOCNO")
	private BigDecimal docNo;

	@Column(name = "UTILIZED")
	private String utilize;

	@Column(name = "UTILIZED_DATE")
	private Date utilizeDate;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public BigDecimal getRemitTrnxId() {
		return remitTrnxId;
	}

	public void setRemitTrnxId(BigDecimal remitTrnxId) {
		this.remitTrnxId = remitTrnxId;
	}

	public BigDecimal getComCode() {
		return comCode;
	}

	public void setComCode(BigDecimal comCode) {
		this.comCode = comCode;
	}

	public BigDecimal getDocCode() {
		return docCode;
	}

	public void setDocCode(BigDecimal docCode) {
		this.docCode = docCode;
	}

	public BigDecimal getDocFyr() {
		return docFyr;
	}

	public void setDocFyr(BigDecimal docFyr) {
		this.docFyr = docFyr;
	}

	public BigDecimal getDocNo() {
		return docNo;
	}

	public void setDocNo(BigDecimal docNo) {
		this.docNo = docNo;
	}

	public String getUtilize() {
		return utilize;
	}

	public void setUtilize(String utilize) {
		this.utilize = utilize;
	}

	public Date getUtilizeDate() {
		return utilizeDate;
	}

	public void setUtilizeDate(Date utilizeDate) {
		this.utilizeDate = utilizeDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
