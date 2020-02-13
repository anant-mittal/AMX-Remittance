package com.amx.jax.probot.notifications;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

public class RoutingStatusChangeNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7461261291201238988L;

	private BigDecimal countryId;

	private String countryName;

	private BigDecimal currencyId;

	private String currencyName;

	private BigDecimal bankId;

	private String bankCode;

	private String bankName;

	private BigDecimal serviceId;

	private String service;

	private BigDecimal remitModeId;

	private String productName;

	private String productShortName;

	private ROUTING_STATUS routing;

	private ROUTING_STATUS productStatus;

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public BigDecimal getRemitModeId() {
		return remitModeId;
	}

	public void setRemitModeId(BigDecimal remitModeId) {
		this.remitModeId = remitModeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public ROUTING_STATUS getRouting() {
		return routing;
	}

	public void setRouting(ROUTING_STATUS routing) {
		this.routing = routing;
	}

	public ROUTING_STATUS getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ROUTING_STATUS productStatus) {
		this.productStatus = productStatus;
	}

}
