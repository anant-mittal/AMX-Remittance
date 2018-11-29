package com.amx.jax.payg;

import com.amx.jax.dict.PayGServiceCode;
import com.amx.utils.ArgUtil;

public class Payment {
	String docNo;
	String docFinYear;
	String netPayableAmount;
	String merchantTrackId;
	Object product;
	PayGServiceCode pgCode;

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getDocFinYear() {
		return docFinYear;
	}

	public void setDocFinYear(Object docFinYear) {
		this.docFinYear = ArgUtil.parseAsString(docFinYear);
	}

	public String getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(Object netPayableAmount) {
		this.netPayableAmount = ArgUtil.parseAsString(netPayableAmount);
	}

	public String getMerchantTrackId() {
		return merchantTrackId;
	}

	public void setMerchantTrackId(Object merchantTrackId) {
		this.merchantTrackId = ArgUtil.parseAsString(merchantTrackId);
	}

	public PayGServiceCode getPgCode() {
		return pgCode;
	}

	public void setPgCode(PayGServiceCode payGServiceCode) {
		this.pgCode = payGServiceCode;
	}

	public Object getProduct() {
		return product;
	}

	public void setProduct(Object product) {
		this.product = product;
	}
}
