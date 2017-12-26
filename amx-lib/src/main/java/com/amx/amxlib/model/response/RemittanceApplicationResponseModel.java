package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

public class RemittanceApplicationResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal remittanceAppId;
	private String paymentId;
	private BigDecimal netPayableAmount;

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
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

}
