package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.customer.CivilIdOtpModel;

public class RemittanceApplicationResponseModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal remittanceAppId;
	private String documentIdForPayment;
	private BigDecimal documentFinancialYear;
	private BigDecimal netPayableAmount;
	private BigDecimal merchantTrackId;
	private CivilIdOtpModel civilIdOtpModel;

	private PayGServiceCode pgCode = PayGServiceCode.DEFAULT;

	public PayGServiceCode getPgCode() {
		return pgCode;
	}

	public void setPgCode(PayGServiceCode pgCode) {
		this.pgCode = pgCode;
	}

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

	public BigDecimal getDocumentFinancialYear() {
		return documentFinancialYear;
	}

	public void setDocumentFinancialYear(BigDecimal documentFinancialYear) {
		this.documentFinancialYear = documentFinancialYear;
	}

	public CivilIdOtpModel getCivilIdOtpModel() {
		return civilIdOtpModel;
	}

	public void setCivilIdOtpModel(CivilIdOtpModel civilIdOtpModel) {
		this.civilIdOtpModel = civilIdOtpModel;
	}

	public String toString() {
		String str = null;
		if (civilIdOtpModel != null)
			str = "RemittanceApplicationResponseModel [remittanceAppId=" + remittanceAppId + ", documentIdForPayment="
					+ documentIdForPayment + ", documentFinancialYear=" + documentFinancialYear + ", netPayableAmount="
					+ netPayableAmount + ", merchantTrackId=" + merchantTrackId + ", pgCode=" + pgCode
					+ ", civilIdOtpModel=" + civilIdOtpModel.toString() + "]";
		else {
			str = "RemittanceApplicationResponseModel [remittanceAppId=" + remittanceAppId + ", documentIdForPayment="
					+ documentIdForPayment + ", documentFinancialYear=" + documentFinancialYear + ", netPayableAmount="
					+ netPayableAmount + ", merchantTrackId=" + merchantTrackId + ", pgCode=" + pgCode
					+ ", civilIdOtpModel=null" + "]";
		}

		return str;
	}

}
