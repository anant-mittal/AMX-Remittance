package com.amx.jax.model.response;

import java.math.BigDecimal;

import com.amx.jax.AbstractModel;
import com.amx.jax.dict.PayGServiceCode;

public class FcSaleApplPaymentReponseModel extends AbstractModel {
	private static final long serialVersionUID = 1L;
	private BigDecimal remittanceAppId;
	private String documentIdForPayment;
	private BigDecimal documentFinancialYear;
	private BigDecimal netPayableAmount;
	private BigDecimal merchantTrackId;
	private PayGServiceCode pgCode = PayGServiceCode.DEFAULT;
	public BigDecimal getRemittanceAppId() {
		return remittanceAppId;
	}
	public void setRemittanceAppId(BigDecimal remittanceAppId) {
		this.remittanceAppId = remittanceAppId;
	}
	public String getDocumentIdForPayment() {
		return documentIdForPayment;
	}
	public void setDocumentIdForPayment(String documentIdForPayment) {
		this.documentIdForPayment = documentIdForPayment;
	}
	public BigDecimal getDocumentFinancialYear() {
		return documentFinancialYear;
	}
	public void setDocumentFinancialYear(BigDecimal documentFinancialYear) {
		this.documentFinancialYear = documentFinancialYear;
	}
	public BigDecimal getNetPayableAmount() {
		return netPayableAmount;
	}
	public void setNetPayableAmount(BigDecimal netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}
	public BigDecimal getMerchantTrackId() {
		return merchantTrackId;
	}
	public void setMerchantTrackId(BigDecimal merchantTrackId) {
		this.merchantTrackId = merchantTrackId;
	}
	public PayGServiceCode getPgCode() {
		return pgCode;
	}
	public void setPgCode(PayGServiceCode pgCode) {
		this.pgCode = pgCode;
	}

}


