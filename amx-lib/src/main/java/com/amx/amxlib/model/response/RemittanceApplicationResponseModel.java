package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

public class RemittanceApplicationResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal remittanceAppId;
	private String documentIdForPayment;
	private BigDecimal netPayableAmount;
	private BigDecimal merchantTrackId;

	@Override
	public String getModelType() {
		return "remittance-application";
	}

	public BigDecimal getRemittanceAppId() {
		return remittanceAppId;
	}

	public void setRemittanceAppId(BigDecimal remittanceAppId) {
		this.remittanceAppId = remittanceAppId;
	}

	public BigDecimal getNetPayableAmount() {
		return netPayableAmount;
	}

	public void setNetPayableAmount(BigDecimal netPayableAmount) {
		this.netPayableAmount = netPayableAmount;
	}

	public String getPaymentId() {
		return documentIdForPayment;
	}

	public void setPaymentId(String paymentId) {
		this.documentIdForPayment = paymentId;
	}

	public String getDocumentIdForPayment() {
		return documentIdForPayment;
	}

	public void setDocumentIdForPayment(String documentIdForPayment) {
		this.documentIdForPayment = documentIdForPayment;
	}

	public BigDecimal getMerchantTrackId() {
		return merchantTrackId;
	}

	public void setMerchantTrackId(BigDecimal merchantTrackId) {
		this.merchantTrackId = merchantTrackId;
	}

}
