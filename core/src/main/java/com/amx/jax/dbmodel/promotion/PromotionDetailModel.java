package com.amx.jax.dbmodel.promotion;

import java.math.BigDecimal;

public class PromotionDetailModel {

	BigDecimal docNo;
	BigDecimal docFinYear;
	String prize;
	BigDecimal trnRef;

	public BigDecimal getDocNo() {
		return docNo;
	}

	public void setDocNo(BigDecimal docNo) {
		this.docNo = docNo;
	}

	public BigDecimal getDocFinYear() {
		return docFinYear;
	}

	public void setDocFinYear(BigDecimal docFinYear) {
		this.docFinYear = docFinYear;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public BigDecimal getTrnRef() {
		return trnRef;
	}

	public void setTrnRef(BigDecimal trnRef) {
		this.trnRef = trnRef;
	}
}
