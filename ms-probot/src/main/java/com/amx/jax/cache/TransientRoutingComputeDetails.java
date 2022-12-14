package com.amx.jax.cache;

import java.io.Serializable;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.dto.ExchangeRateDetails;

public class TransientRoutingComputeDetails implements Serializable, Comparable<TransientRoutingComputeDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7075576617980715134L;

	private ViewExRoutingMatrix viewExRoutingMatrix;

	private EstimatedDeliveryDetails routingBankDeliveryDetails;

	private EstimatedDeliveryDetails processingDeliveryDetails;

	private EstimatedDeliveryDetails beneBankDeliveryDetails;

	private EstimatedDeliveryDetails finalDeliveryDetails;

	private ExchangeRateDetails exchangeRateDetails;

	public ViewExRoutingMatrix getViewExRoutingMatrix() {
		return viewExRoutingMatrix;
	}

	public void setViewExRoutingMatrix(ViewExRoutingMatrix viewExRoutingMatrix) {
		this.viewExRoutingMatrix = viewExRoutingMatrix;
	}

	public EstimatedDeliveryDetails getRoutingBankDeliveryDetails() {
		return routingBankDeliveryDetails;
	}

	public void setRoutingBankDeliveryDetails(EstimatedDeliveryDetails routingBankDeliveryDetails) {
		this.routingBankDeliveryDetails = routingBankDeliveryDetails;
	}

	public EstimatedDeliveryDetails getProcessingDeliveryDetails() {
		return processingDeliveryDetails;
	}

	public void setProcessingDeliveryDetails(EstimatedDeliveryDetails processingDeliveryDetails) {
		this.processingDeliveryDetails = processingDeliveryDetails;
	}

	public EstimatedDeliveryDetails getBeneBankDeliveryDetails() {
		return beneBankDeliveryDetails;
	}

	public void setBeneBankDeliveryDetails(EstimatedDeliveryDetails beneBankDeliveryDetails) {
		this.beneBankDeliveryDetails = beneBankDeliveryDetails;
	}

	public EstimatedDeliveryDetails getFinalDeliveryDetails() {
		return finalDeliveryDetails;
	}

	public void setFinalDeliveryDetails(EstimatedDeliveryDetails finalDeliveryDetails) {
		this.finalDeliveryDetails = finalDeliveryDetails;
	}

	public ExchangeRateDetails getExchangeRateDetails() {
		return exchangeRateDetails;
	}

	public void setExchangeRateDetails(ExchangeRateDetails exchangeRateDetails) {
		this.exchangeRateDetails = exchangeRateDetails;
	}

	@Override
	public int compareTo(TransientRoutingComputeDetails that) {
		int rateComparator = this.compareRateTo(that);
		if (rateComparator == 0) {
			rateComparator = this.compareDeliveryTimeTo(that);
		}

		return rateComparator;
	}

	public int compareRateTo(TransientRoutingComputeDetails that) {
		if (this.exchangeRateDetails == null)
			if (that == null || that.exchangeRateDetails == null)
				return 0;
			else
				return -1;
		else if (that.exchangeRateDetails == null)
			return 1;
		else
			return this.exchangeRateDetails.compareTo(that.exchangeRateDetails);

	}

	public int compareDeliveryTimeTo(TransientRoutingComputeDetails that) {
		if (this.finalDeliveryDetails == null)
			if (that == null || that.finalDeliveryDetails == null)
				return 0; // equal
			else
				return 1; // null is More than any other value
		else if (that.finalDeliveryDetails == null)
			return -1; // all other strings are before null
		else
			return this.finalDeliveryDetails.compareTo(that.finalDeliveryDetails);
	}

}
