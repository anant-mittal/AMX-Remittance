package com.amx.jax.cache;

import java.io.Serializable;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;

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

	@Override
	public int compareTo(TransientRoutingComputeDetails that) {

		if (this.finalDeliveryDetails == null)
			if (that == null || that.finalDeliveryDetails == null)
				return 0; // equal
			else
				return -1; // null is before other strings
		else if (that.finalDeliveryDetails == null)
			return 1; // all other strings are after null
		else
			return this.finalDeliveryDetails.compareTo(that.finalDeliveryDetails);

	}

}
