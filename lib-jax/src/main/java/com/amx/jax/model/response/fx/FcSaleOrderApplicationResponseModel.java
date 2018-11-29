package com.amx.jax.model.response.fx;
/**
 * Author :Rabil
 */

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.AbstractModel;


public class FcSaleOrderApplicationResponseModel extends AbstractModel {
	
	private static final long serialVersionUID = 9184782859804476157L;
	private BigDecimal txnFee;
	private FxExchangeRateBreakup exRateBreakup;
	private String denominationType;
	private BigDecimal purposeOfTrnxId;
	private BigDecimal sourceOffundId;
	private BigDecimal remittanceAppId;
	private String documentIdForPayment;
	private BigDecimal documentFinancialYear;
	private BigDecimal netPayableAmount;
	private BigDecimal merchantTrackId;
	private BigDecimal deliveryCharges;
	private List<TimeSlotDto> timeSlot;
	private List<ShoppingCartDetailsDto> cartDetails;
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
	public FxExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}
	public void setExRateBreakup(FxExchangeRateBreakup exRateBreakup) {
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
	public List<ShoppingCartDetailsDto> getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(List<ShoppingCartDetailsDto> cartDetails) {
		this.cartDetails = cartDetails;
	}

	/*public List<String> getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(List<String> timeSlot) {
		this.timeSlot = timeSlot;
	}*/

	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public List<TimeSlotDto> getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(List<TimeSlotDto> timeSlot) {
		this.timeSlot = timeSlot;
	}
	

}
