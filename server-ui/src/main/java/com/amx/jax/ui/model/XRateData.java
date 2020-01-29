package com.amx.jax.ui.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;

/**
 * The Class XRateData.
 */
public class XRateData {

	/** The for cur. */
	CurrencyMasterDTO forCur = null;

	/** The for X rate. */
	BigDecimal forXRate = null;

	/** The for amount. */
	BigDecimal forAmount = null;

	/** The dom cur. */
	CurrencyMasterDTO domCur = null;

	/** The dom X rate. */
	BigDecimal domXRate = null;

	/** The dom amount. */
	BigDecimal domAmount = null;

	/** The bene banks. */
	List<BankMasterDTO> beneBanks = null;

	/**
	 * Gets the bene banks.
	 *
	 * @return the bene banks
	 */
	public List<BankMasterDTO> getBeneBanks() {
		return beneBanks;
	}

	/**
	 * Sets the bene banks.
	 *
	 * @param set
	 *            the new bene banks
	 */
	public void setBeneBanks(List<BankMasterDTO> set) {
		this.beneBanks = set;
	}

	/**
	 * Gets the for cur.
	 *
	 * @return the for cur
	 */
	public CurrencyMasterDTO getForCur() {
		return forCur;
	}

	/**
	 * Sets the for cur.
	 *
	 * @param forCurcy
	 *            the new for cur
	 */
	public void setForCur(CurrencyMasterDTO forCurcy) {
		this.forCur = forCurcy;
	}

	/**
	 * Gets the for X rate.
	 *
	 * @return the for X rate
	 */
	public BigDecimal getForXRate() {
		return forXRate;
	}

	/**
	 * Sets the for X rate.
	 *
	 * @param forXRate
	 *            the new for X rate
	 */
	public void setForXRate(BigDecimal forXRate) {
		this.forXRate = forXRate;
	}

	/**
	 * Gets the for amount.
	 *
	 * @return the for amount
	 */
	public BigDecimal getForAmount() {
		return forAmount;
	}

	/**
	 * Sets the for amount.
	 *
	 * @param forAmount
	 *            the new for amount
	 */
	public void setForAmount(BigDecimal forAmount) {
		this.forAmount = forAmount;
	}

	/**
	 * Gets the dom cur.
	 *
	 * @return the dom cur
	 */
	public CurrencyMasterDTO getDomCur() {
		return domCur;
	}

	/**
	 * Sets the dom cur.
	 *
	 * @param domCur
	 *            the new dom cur
	 */
	public void setDomCur(CurrencyMasterDTO domCur) {
		this.domCur = domCur;
	}

	/**
	 * Gets the dom X rate.
	 *
	 * @return the dom X rate
	 */
	public BigDecimal getDomXRate() {
		return domXRate;
	}

	/**
	 * Sets the dom X rate.
	 *
	 * @param domXRate
	 *            the new dom X rate
	 */
	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}

	/**
	 * Gets the dom amount.
	 *
	 * @return the dom amount
	 */
	public BigDecimal getDomAmount() {
		return domAmount;
	}

	/**
	 * Sets the dom amount.
	 *
	 * @param domAmount
	 *            the new dom amount
	 */
	public void setDomAmount(BigDecimal domAmount) {
		this.domAmount = domAmount;
	}

}
