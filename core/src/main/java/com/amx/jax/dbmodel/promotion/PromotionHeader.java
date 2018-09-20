package com.amx.jax.dbmodel.promotion;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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
	BigDecimal descrition;

	@Column(name = "FRMDAT")
	Date fromDate;

	@Column(name = "TODAT")
	Date toDate;

	@Column(name = "LOCCOD")
	BigDecimal locationCode;

	public PromotionHeaderPK getPromotionHeaderPK() {
		return promotionHeaderPK;
	}

	public void setPromotionHeaderPK(PromotionHeaderPK promotionHeaderPK) {
		this.promotionHeaderPK = promotionHeaderPK;
	}

	public BigDecimal getDescrition() {
		return descrition;
	}

	public void setDescrition(BigDecimal descrition) {
		this.descrition = descrition;
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
}
