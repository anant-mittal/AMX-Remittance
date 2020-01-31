package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class ViewExGLCBalProvisional.
 */
@Entity
@Table(name = "VW_EX_GLCBAL_PROV")
public class ViewExGLCBalProvisional implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9011910401678486001L;

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

	/** The currency id. */
	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

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
	 * @param currencyId the new currency id
	 */
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
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

}
