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
@Table(name = "PROMOTION_LOCATIONS")
public class PromotionLocation {

	@EmbeddedId
	PromotionLocationPK promotionLocationPK;

	@Column(name = "COMCOD")
	BigDecimal comCode;
	
	@Column(name = "LOCCOD")
	BigDecimal locCode;

	public PromotionLocationPK getPromotionLocationPK() {
		return promotionLocationPK;
	}

	public void setPromotionLocationPK(PromotionLocationPK promotionLocationPK) {
		this.promotionLocationPK = promotionLocationPK;
	}

	public BigDecimal getComCode() {
		return comCode;
	}

	public void setComCode(BigDecimal comCode) {
		this.comCode = comCode;
	}

	public BigDecimal getLocCode() {
		return locCode;
	}

	public void setLocCode(BigDecimal locCode) {
		this.locCode = locCode;
	}

}
