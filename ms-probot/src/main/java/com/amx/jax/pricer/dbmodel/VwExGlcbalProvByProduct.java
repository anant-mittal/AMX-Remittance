package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class VwExGlcbalProvByProduct.
 */
@Entity
@Table(name = "VW_EX_GLCBAL_PROV_PRODUCT_WISE")
public class VwExGlcbalProvByProduct implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5345451304798179857L;

	/** The row id. */
	@Id
	@Column(name = "ROW_ID")
	private BigDecimal rowId;

	/** The bank id. */
	@Column(name = "BANK_ID")
	private BigDecimal bankId;

	/** The bank code. */
	@Column(name = "BANK_CODE")
	private String bankCode;

	/** The bank full name. */
	@Column(name = "BANK_FULL_NAME")
	private String bankFullName;

	/** The currency code. */
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;

	/** The currency name. */
	@Column(name = "CURRENCY_NAME")
	private String currencyName;

	/** The slno. */
	@Column(name = "SLNO")
	private String SLNO;

	/** The rate fc cur bal. */
	@Column(name = "RATE_FCURBAL")
	private BigDecimal rateFcCurBal;

	/** The rate cur bal. */
	@Column(name = "RATE_CURBAL")
	private BigDecimal rateCurBal;

	/** The remittance mode id. */
	@Column(name = "REMITTANCE_MODE_ID")
	private BigDecimal remittanceModeId;

	/** The remittance code. */
	@Column(name = "REMITTANCE_CODE")
	private String remittanceCode;

	/** The remittance description. */
	@Column(name = "REMITTANCE_DESCRIPTION")
	private String remittanceDescription;

	/** The delivery mode id. */
	@Column(name = "DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;

	/** The delivery code. */
	@Column(name = "DELIVERY_CODE")
	private String deliveryCode;

	/** The delivery description. */
	@Column(name = "DELIVERY_DESCRIPTION")
	private String deliveryDescription;

	/**
	 * Gets the row id.
	 *
	 * @return the row id
	 */
	public BigDecimal getRowId() {
		return rowId;
	}

	/**
	 * Sets the row id.
	 *
	 * @param rowId
	 *            the new row id
	 */
	public void setRowId(BigDecimal rowId) {
		this.rowId = rowId;
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
	 * Gets the bank full name.
	 *
	 * @return the bank full name
	 */
	public String getBankFullName() {
		return bankFullName;
	}

	/**
	 * Sets the bank full name.
	 *
	 * @param bankFullName
	 *            the new bank full name
	 */
	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	/**
	 * Gets the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Sets the currency code.
	 *
	 * @param currencyCode
	 *            the new currency code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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
	 * Gets the slno.
	 *
	 * @return the slno
	 */
	public String getSLNO() {
		return SLNO;
	}

	/**
	 * Sets the slno.
	 *
	 * @param sLNO
	 *            the new slno
	 */
	public void setSLNO(String sLNO) {
		SLNO = sLNO;
	}

	/**
	 * Gets the rate fc cur bal.
	 *
	 * @return the rate fc cur bal
	 */
	public BigDecimal getRateFcCurBal() {
		return rateFcCurBal;
	}

	/**
	 * Sets the rate fc cur bal.
	 *
	 * @param rateFcCurBal
	 *            the new rate fc cur bal
	 */
	public void setRateFcCurBal(BigDecimal rateFcCurBal) {
		this.rateFcCurBal = rateFcCurBal;
	}

	/**
	 * Gets the rate cur bal.
	 *
	 * @return the rate cur bal
	 */
	public BigDecimal getRateCurBal() {
		return rateCurBal;
	}

	/**
	 * Sets the rate cur bal.
	 *
	 * @param rateCurBal
	 *            the new rate cur bal
	 */
	public void setRateCurBal(BigDecimal rateCurBal) {
		this.rateCurBal = rateCurBal;
	}

	/**
	 * Gets the remittance mode id.
	 *
	 * @return the remittance mode id
	 */
	public BigDecimal getRemittanceModeId() {
		return remittanceModeId;
	}

	/**
	 * Sets the remittance mode id.
	 *
	 * @param remittanceModeId
	 *            the new remittance mode id
	 */
	public void setRemittanceModeId(BigDecimal remittanceModeId) {
		this.remittanceModeId = remittanceModeId;
	}

	/**
	 * Gets the remittance code.
	 *
	 * @return the remittance code
	 */
	public String getRemittanceCode() {
		return remittanceCode;
	}

	/**
	 * Sets the remittance code.
	 *
	 * @param remittanceCode
	 *            the new remittance code
	 */
	public void setRemittanceCode(String remittanceCode) {
		this.remittanceCode = remittanceCode;
	}

	/**
	 * Gets the remittance description.
	 *
	 * @return the remittance description
	 */
	public String getRemittanceDescription() {
		return remittanceDescription;
	}

	/**
	 * Sets the remittance description.
	 *
	 * @param remittanceDescription
	 *            the new remittance description
	 */
	public void setRemittanceDescription(String remittanceDescription) {
		this.remittanceDescription = remittanceDescription;
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
	 * @param deliveryModeId
	 *            the new delivery mode id
	 */
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	/**
	 * Gets the delivery code.
	 *
	 * @return the delivery code
	 */
	public String getDeliveryCode() {
		return deliveryCode;
	}

	/**
	 * Sets the delivery code.
	 *
	 * @param deliveryCode
	 *            the new delivery code
	 */
	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	/**
	 * Gets the delivery description.
	 *
	 * @return the delivery description
	 */
	public String getDeliveryDescription() {
		return deliveryDescription;
	}

	/**
	 * Sets the delivery description.
	 *
	 * @param deliveryDescription
	 *            the new delivery description
	 */
	public void setDeliveryDescription(String deliveryDescription) {
		this.deliveryDescription = deliveryDescription;
	}

}
