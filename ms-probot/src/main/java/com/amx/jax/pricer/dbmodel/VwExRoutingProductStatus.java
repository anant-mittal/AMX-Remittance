package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.pricer.var.PricerServiceConstants.ROUTING_STATUS;

/**
 * The Class VwExRoutingProductStatus.
 */
@Entity
@Table(name = "VW_EX_ROUTING_PRODUCT_STATUS")
public class VwExRoutingProductStatus implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3330856131308727527L;

	/** The uuid. */
	@Id
	@Column(name = "UUID")
	private String uuid;

	/** The country id. */
	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;

	/** The country name. */
	@Column(name = "COUNTRY_NAME")
	private String countryName;

	/** The currency id. */
	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	/** The currency name. */
	@Column(name = "CURRENCY_NAME")
	private String currencyName;

	/** The bank id. */
	@Column(name = "BANK_ID")
	private BigDecimal bankId;

	/** The bank code. */
	@Column(name = "BANK_CODE")
	private String bankCode;

	/** The bank name. */
	@Column(name = "BANK_NAME")
	private String bankName;

	/** The service id. */
	@Column(name = "SERVICE_MASTER_ID")
	private BigDecimal serviceId;

	/** The service. */
	@Column(name = "SERVICE")
	private String service;

	/** The routing. */
	@Column(name = "ROUTING")
	@Enumerated(value = EnumType.STRING)
	private ROUTING_STATUS routing;

	/** The remit mode id. */
	@Column(name = "REMITTANCE_MODE_ID")
	private BigDecimal remitModeId;

	/** The product name. */
	@Column(name = "PRODUCT_NAME")
	private String productName;

	/** The product status. */
	@Column(name = "PRODUCT_STATUS")
	@Enumerated(value = EnumType.STRING)
	private ROUTING_STATUS productStatus;

	/** The fc glbal. */
	@Column(name = "FC_GLCBAL")
	private BigDecimal fcGlbal;

	/** The product desc. */
	@Column(name = "PRODUCT_LONG_DESC")
	private String productDesc;

	/** The final status. */
	@Column(name = "FINAL_STATUS")
	private String finalStatus;

	/** The product short name. */
	@Column(name = "PRODUCT_SHORT_NAME")
	private String productShortName;

	/** The delivery mode id. */
	@Column(name = "DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;

	/** The destination country id. */
	@Column(name = "DESTINATION_COUNTRY_ID")
	private BigDecimal destinationCountryId;

	/** The destination country. */
	@Column(name = "DESTINATION_COUNTRY")
	private String destinationCountry;

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid
	 *            the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the country id.
	 *
	 * @return the country id
	 */
	public BigDecimal getCountryId() {
		return countryId;
	}

	/**
	 * Sets the country id.
	 *
	 * @param countryId
	 *            the new country id
	 */
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	/**
	 * Gets the country name.
	 *
	 * @return the country name
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Sets the country name.
	 *
	 * @param countryName
	 *            the new country name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * Gets the currency id.
	 *
	 * @return the currency id
	 */
	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	/**
	 * Sets the currency id.
	 *
	 * @param currencyId
	 *            the new currency id
	 */
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * Gets the currency name.
	 *
	 * @return the currency name
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * Sets the currency name.
	 *
	 * @param currencyName
	 *            the new currency name
	 */
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * Gets the bank id.
	 *
	 * @return the bank id
	 */
	public BigDecimal getBankId() {
		return bankId;
	}

	/**
	 * Sets the bank id.
	 *
	 * @param bankId
	 *            the new bank id
	 */
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	/**
	 * Gets the bank code.
	 *
	 * @return the bank code
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * Sets the bank code.
	 *
	 * @param bankCode
	 *            the new bank code
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * Gets the bank name.
	 *
	 * @return the bank name
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * Sets the bank name.
	 *
	 * @param bankName
	 *            the new bank name
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public BigDecimal getServiceId() {
		return serviceId;
	}

	/**
	 * Sets the service id.
	 *
	 * @param serviceId
	 *            the new service id
	 */
	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * Sets the service.
	 *
	 * @param service
	 *            the new service
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * Gets the routing.
	 *
	 * @return the routing
	 */
	public ROUTING_STATUS getRouting() {
		return routing;
	}

	/**
	 * Sets the routing.
	 *
	 * @param routing
	 *            the new routing
	 */
	public void setRouting(ROUTING_STATUS routing) {
		this.routing = routing;
	}

	/**
	 * Gets the remit mode id.
	 *
	 * @return the remit mode id
	 */
	public BigDecimal getRemitModeId() {
		return remitModeId;
	}

	/**
	 * Sets the remit mode id.
	 *
	 * @param remitModeId
	 *            the new remit mode id
	 */
	public void setRemitModeId(BigDecimal remitModeId) {
		this.remitModeId = remitModeId;
	}

	/**
	 * Gets the product name.
	 *
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * Sets the product name.
	 *
	 * @param productName
	 *            the new product name
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * Gets the product status.
	 *
	 * @return the product status
	 */
	public ROUTING_STATUS getProductStatus() {
		return productStatus;
	}

	/**
	 * Sets the product status.
	 *
	 * @param productStatus
	 *            the new product status
	 */
	public void setProductStatus(ROUTING_STATUS productStatus) {
		this.productStatus = productStatus;
	}

	/**
	 * Gets the fc glbal.
	 *
	 * @return the fc glbal
	 */
	public BigDecimal getFcGlbal() {
		return fcGlbal;
	}

	/**
	 * Sets the fc glbal.
	 *
	 * @param fcGlbal
	 *            the new fc glbal
	 */
	public void setFcGlbal(BigDecimal fcGlbal) {
		this.fcGlbal = fcGlbal;
	}

	/**
	 * Gets the product desc.
	 *
	 * @return the product desc
	 */
	public String getProductDesc() {
		return productDesc;
	}

	/**
	 * Sets the product desc.
	 *
	 * @param productDesc
	 *            the new product desc
	 */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	/**
	 * Gets the final status.
	 *
	 * @return the final status
	 */
	public String getFinalStatus() {
		return finalStatus;
	}

	/**
	 * Sets the final status.
	 *
	 * @param finalStatus
	 *            the new final status
	 */
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	/**
	 * Gets the product short name.
	 *
	 * @return the product short name
	 */
	public String getProductShortName() {
		return productShortName;
	}

	/**
	 * Sets the product short name.
	 *
	 * @param productShortName
	 *            the new product short name
	 */
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	/**
	 * Gets the delivery mode id.
	 *
	 * @return the delivery mode id
	 */
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	/**
	 * Sets the delivery mode id.
	 *
	 * @param deliveryModeId the new delivery mode id
	 */
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	/**
	 * Gets the destination country id.
	 *
	 * @return the destination country id
	 */
	public BigDecimal getDestinationCountryId() {
		return destinationCountryId;
	}

	/**
	 * Sets the destination country id.
	 *
	 * @param destinationCountryId the new destination country id
	 */
	public void setDestinationCountryId(BigDecimal destinationCountryId) {
		this.destinationCountryId = destinationCountryId;
	}

	/**
	 * Gets the destination country.
	 *
	 * @return the destination country
	 */
	public String getDestinationCountry() {
		return destinationCountry;
	}

	/**
	 * Sets the destination country.
	 *
	 * @param destinationCountry the new destination country
	 */
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

}
