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

	public BigDecimal getDocFinYear() {
		return docFinYear;
	}

	public void setDocFinYear(BigDecimal docFinYear) {
		this.docFinYear = docFinYear;
	}

	public BigDecimal getLocCode() {
		return locCode;
	}

	public void setLocCode(BigDecimal locCode) {
		this.locCode = locCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docFinYear == null) ? 0 : docFinYear.hashCode());
		result = prime * result + ((locCode == null) ? 0 : locCode.hashCode());
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
		PromotionLocationPK other = (PromotionLocationPK) obj;
		if (docFinYear == null) {
			if (other.docFinYear != null)
				return false;
		} else if (!docFinYear.equals(other.docFinYear))
			return false;
		if (locCode == null) {
			if (other.locCode != null)
				return false;
		} else if (!locCode.equals(other.locCode))
			return false;
		return true;
	}
	
	

	
}
