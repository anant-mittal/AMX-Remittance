/**
 * 
 */
package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.Map;

import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;

/**
 * @author Prashant
 *
 */
public class RemittanceTransactionResponsetModel extends AbstractModel {

	private static final long serialVersionUID = -6674547178341594857L;
	private BigDecimal txnFee;
	private BigDecimal totalLoyalityPoints;
	private BigDecimal maxLoyalityPointsAvailableForTxn;
	private ExchangeRateBreakup exRateBreakup;
	private Boolean canRedeemLoyalityPoints;
	private LoyalityPointState loyalityPointState;
	private BigDecimal loyalityAmountAvailableForTxn;
	private BigDecimal discountOnComission;
	private Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails;
	private Boolean discountAvailed;
	private Boolean costRateLimitReached;
	private String vatType;
	private BigDecimal vatPercentage;
	private BigDecimal vatAmount;
	/** For Better rate **/ 
	private boolean isBetterRateAvailable = false;
	private BigDecimal betterRateAmountSlab;
	private BigDecimal diffInBetterRateFcAmount;
	private String discountOnComissionFlag;
	/** added by Rabil**/
	private BigDecimal rackExchangeRate; /**Worst rate amount all the branches **/
	private BigDecimal youSavedAmount;
	private String customerChoice;
	private BigDecimal youSavedAmountInFC;
	private BigDecimal placeOrderId;
	private BigDecimal costExchangeRate;
	private BigDecimal corporateMasterId;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.amxlib.model.AbstractModel#getModelType()
	 */
	@Override
	public String getModelType() {
		return "remittance_transaction";
	}

	public BigDecimal getTxnFee() {
		return txnFee;
	}

	public void setTxnFee(BigDecimal txnFee) {
		this.txnFee = txnFee;
	}

	public BigDecimal getTotalLoyalityPoints() {
		return totalLoyalityPoints;
	}

	public void setTotalLoyalityPoints(BigDecimal totalLoyalityPoints) {
		this.totalLoyalityPoints = totalLoyalityPoints;
	}

	public BigDecimal getMaxLoyalityPointsAvailableForTxn() {
		return maxLoyalityPointsAvailableForTxn;
	}

	public void setMaxLoyalityPointsAvailableForTxn(BigDecimal maxLoyalityPointsAvailableForTxn) {
		this.maxLoyalityPointsAvailableForTxn = maxLoyalityPointsAvailableForTxn;
	}

	public ExchangeRateBreakup getExRateBreakup() {
		return exRateBreakup;
	}

	public void setExRateBreakup(ExchangeRateBreakup exRateBreakup) {
		this.exRateBreakup = exRateBreakup;
	}

	public Boolean getCanRedeemLoyalityPoints() {
		return canRedeemLoyalityPoints;
	}

	public void setCanRedeemLoyalityPoints(Boolean canRedeemLoyalityPoints) {
		this.canRedeemLoyalityPoints = canRedeemLoyalityPoints;
	}

	public LoyalityPointState getLoyalityPointState() {
		return loyalityPointState;
	}

	public void setLoyalityPointState(LoyalityPointState loyalityPointState) {
		this.loyalityPointState = loyalityPointState;
	}

	public BigDecimal getLoyalityAmountAvailableForTxn() {
		return loyalityAmountAvailableForTxn;
	}

	public void setLoyalityAmountAvailableForTxn(BigDecimal loyalityAmountAvailableForTxn) {
		this.loyalityAmountAvailableForTxn = loyalityAmountAvailableForTxn;
	}

	public BigDecimal getDiscountOnComission() {
		return discountOnComission;
	}

	public void setDiscountOnComission(BigDecimal discountOnComission) {
		this.discountOnComission = discountOnComission;
	}

	public Map<DISCOUNT_TYPE, ExchangeDiscountInfo> getCustomerDiscountDetails() {
		return customerDiscountDetails;
	}

	public void setCustomerDiscountDetails(Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails) {
		this.customerDiscountDetails = customerDiscountDetails;
	}

	public Boolean getDiscountAvailed() {
		return discountAvailed;
	}

	public void setDiscountAvailed(Boolean discountAvailed) {
		this.discountAvailed = discountAvailed;
	}

	public Boolean getCostRateLimitReached() {
		return costRateLimitReached;
	}

	public void setCostRateLimitReached(Boolean costRateLimitReached) {
		this.costRateLimitReached = costRateLimitReached;
	}

	public String getVatType() {
		return vatType;
	}

	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public BigDecimal getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	public boolean isBetterRateAvailable() {
		return isBetterRateAvailable;
	}

	public void setBetterRateAvailable(boolean isBetterRateAvailable) {
		this.isBetterRateAvailable = isBetterRateAvailable;
	}

	public BigDecimal getBetterRateAmountSlab() {
		return betterRateAmountSlab;
	}

	public void setBetterRateAmountSlab(BigDecimal betterRateAmountSlab) {
		this.betterRateAmountSlab = betterRateAmountSlab;
	}

	public BigDecimal getDiffInBetterRateFcAmount() {
		return diffInBetterRateFcAmount;
	}

	public void setDiffInBetterRateFcAmount(BigDecimal diffInBetterRateFcAmount) {
		this.diffInBetterRateFcAmount = diffInBetterRateFcAmount;
	}
	
	public String getDiscountOnComissionFlag() {
		return discountOnComissionFlag;
	}

	public void setDiscountOnComissionFlag(String discountOnComissionFlag) {
		this.discountOnComissionFlag = discountOnComissionFlag;
	}

	public BigDecimal getRackExchangeRate() {
		return rackExchangeRate;
	}

	public void setRackExchangeRate(BigDecimal rackExchangeRate) {
		this.rackExchangeRate = rackExchangeRate;
	}

	public BigDecimal getYouSavedAmount() {
		return youSavedAmount;
	}

	public void setYouSavedAmount(BigDecimal youSavedAmount) {
		this.youSavedAmount = youSavedAmount;
	}

	public String getCustomerChoice() {
		return customerChoice;
	}

	public void setCustomerChoice(String customerChoice) {
		this.customerChoice = customerChoice;
	}

	public BigDecimal getYouSavedAmountInFC() {
		return youSavedAmountInFC;
	}

	public void setYouSavedAmountInFC(BigDecimal youSavedAmountInFC) {
		this.youSavedAmountInFC = youSavedAmountInFC;
	}

	public BigDecimal getPlaceOrderId() {
		return placeOrderId;
	}

	public void setPlaceOrderId(BigDecimal placeOrderId) {
		this.placeOrderId = placeOrderId;
	}

	public BigDecimal getCostExchangeRate() {
		return costExchangeRate;
	}

	public void setCostExchangeRate(BigDecimal costExchangeRate) {
		this.costExchangeRate = costExchangeRate;
	}

	public BigDecimal getCorporateMasterId() {
		return corporateMasterId;
	}

	public void setCorporateMasterId(BigDecimal corporateMasterId) {
		this.corporateMasterId = corporateMasterId;
	}

}
