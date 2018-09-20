package com.amx.jax.dbmodel.promotion;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embeddable;


/**
 * @author Prashant
 *
 */
@Embeddable
public class PromotionHeaderPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected BigDecimal docNo;
	protected BigDecimal docFinYear;

	public PromotionHeaderPK(BigDecimal docNo, BigDecimal docFinYear) {
		super();
		this.docNo = docNo;
		this.docFinYear = docFinYear;
	}

	public PromotionHeaderPK() {
		super();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docFinYear == null) ? 0 : docFinYear.hashCode());
		result = prime * result + ((docNo == null) ? 0 : docNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PromotionHeaderPK other = (PromotionHeaderPK) obj;
		if (docFinYear == null) {
			if (other.docFinYear != null)
				return false;
		} else if (!docFinYear.equals(other.docFinYear))
			return false;
		if (docNo == null) {
			if (other.docNo != null)
				return false;
		} else if (!docNo.equals(other.docNo))
			return false;
		return true;
	}

}
