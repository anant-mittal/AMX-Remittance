package com.amx.amxlib.model.response;
/**
 * Author :Rabil
 */

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.meta.model.ShoppingCartDetailsDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.model.AbstractModel;


public class FcSaleOrderApplicationResponseModel extends AbstractModel {
	
	private static final long serialVersionUID = 9184782859804476157L;
	private BigDecimal txnFee;
	private ExchangeRateBreakup exRateBreakup;
	private String denominationType;
	private BigDecimal purposeOfTrnxId;
	private BigDecimal sourceOffundId;
	
	
	private BigDecimal remittanceAppId;
	private String documentIdForPayment;
	private BigDecimal documentFinancialYear;
	private BigDecimal netPayableAmount;
	private BigDecimal merchantTrackId;
	private CivilIdOtpModel civilIdOtpModel;

	private List<ShoppingCartDetailsDto> cartDetails;
	private PayGServiceCode pgCode = PayGServiceCode.DEFAULT;
	
	@Override
	public String getModelType() {
		return "fcsale_application";
	}
	
	public BigDecimal getTxnFee() {
		return txnFee;
	}
	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
	}
	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}
	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}
	public String getDenominationType() {
		return denominationType;
	}
	public void setDenominationType(String denominationType) {
		this.denominationType = denominationType;
	}
	public BigDecimal getPurposeOfTrnxId() {
		return purposeOfTrnxId;
	}
	public void setPurposeOfTrnxId(BigDecimal purposeOfTrnxId) {
		this.purposeOfTrnxId = purposeOfTrnxId;
	}
	public BigDecimal getSourceOffundId() {
		return sourceOffundId;
	}
	public void setSourceOffundId(BigDecimal sourceOffundId) {
		this.sourceOffundId = sourceOffundId;
	}

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

	public CivilIdOtpModel getCivilIdOtpModel() {
		return civilIdOtpModel;
	}

	public void setCivilIdOtpModel(CivilIdOtpModel civilIdOtpModel) {
		this.civilIdOtpModel = civilIdOtpModel;
	}

	public PayGServiceCode getPgCode() {
		return pgCode;
	}

	public void setPgCode(PayGServiceCode pgCode) {
		this.pgCode = pgCode;
	}

	public List<ShoppingCartDetailsDto> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<ShoppingCartDetailsDto> cartDetails) {
		this.cartDetails = cartDetails;
	}
	

}
