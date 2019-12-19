package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

public class RoutingStatusUpdateRequestDto implements Serializable {

	private static final long serialVersionUID = 950721144203488104L;

	@NotNull(message = "Country Id Can not be Null or Empty")
	private BigDecimal countryId;

	@NotNull(message = "Currency Id Can not be Null or Empty")
	private BigDecimal currencyId;

	@NotNull(message = "Bank Id Can not be Null or Empty")
	private BigDecimal bankId;

	@NotNull(message = "Service Mode Id Can not be Null or Empty")
	private BigDecimal serviceModeId;

	@NotNull(message = "Remit Mode Id Can not be Null or Empty")
	private BigDecimal remitModeId;

	private BigDecimal deliveryModeId;

	@NotNull(message = "Status to be updated Can not be Null or Empty")
	private ROUTING_STATUS updated;

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public BigDecimal getServiceModeId() {
		return serviceModeId;
	}

	public void setServiceModeId(BigDecimal serviceModeId) {
		this.serviceModeId = serviceModeId;
	}

	public BigDecimal getRemitModeId() {
		return remitModeId;
	}

	public void setRemitModeId(BigDecimal remitModeId) {
		this.remitModeId = remitModeId;
	}

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public ROUTING_STATUS getUpdated() {
		return updated;
	}

	public void setUpdated(ROUTING_STATUS updated) {
		this.updated = updated;
	}

}
