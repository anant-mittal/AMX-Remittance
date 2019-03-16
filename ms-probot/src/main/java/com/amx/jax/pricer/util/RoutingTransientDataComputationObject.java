package com.amx.jax.pricer.util;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;

public class RoutingTransientDataComputationObject {

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

}
