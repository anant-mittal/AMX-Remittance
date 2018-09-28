package com.amx.jax.dbmodel.promotion;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Prashant
 *
 */
@Entity
@Table(name = "PROMOTION_HD")
public class PromotionHeader {

	@EmbeddedId
	PromotionHeaderPK promotionHeaderPK;

	@Column(name = "FUDESC")
	String descrition;

	@Column(name = "FRMDAT")
	@Temporal(TemporalType.DATE)
	Date fromDate;

	@Column(name = "TODAT")
	@Temporal(TemporalType.DATE)
	Date toDate;

	@Column(name = "LOCCOD")
	BigDecimal locationCode;

	@Column(name = "RECSTS")
	String recSts;

	public PromotionHeaderPK getPromotionHeaderPK() {
		return promotionHeaderPK;
	}

	public void setPromotionHeaderPK(PromotionHeaderPK promotionHeaderPK) {
		this.promotionHeaderPK = promotionHeaderPK;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(BigDecimal locationCode) {
		this.locationCode = locationCode;
	}

	public String getDescrition() {
		return descrition;
	}

	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}

	public String getRecSts() {
		return recSts;
	}

	public void setRecSts(String recSts) {
		this.recSts = recSts;
	}
}
