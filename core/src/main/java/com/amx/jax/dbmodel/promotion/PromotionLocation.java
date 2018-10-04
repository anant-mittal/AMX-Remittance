package com.amx.jax.dbmodel.promotion;

import java.math.BigDecimal;

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
	
	@Column(name = "DOCNO")
	BigDecimal documentNo;
	
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

	public BigDecimal getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

}
