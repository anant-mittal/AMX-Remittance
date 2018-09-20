package com.amx.jax.dbmodel.promotion;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author Prashant
 *
 */
@Embeddable
public class PromotionLocationPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="DOCFYR")
	protected BigDecimal docFinYear;
	@Column(name="LOCCOD")
	protected BigDecimal locCode;
	
	public PromotionLocationPK(BigDecimal docFinYear, BigDecimal locCode) {
		super();
		this.docFinYear = docFinYear;
		this.locCode = locCode;
	}

	public PromotionLocationPK() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
}
