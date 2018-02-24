package com.amx.jax.ui.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;

public class XRateData {

	CurrencyMasterDTO forCur = null;
	BigDecimal forXRate = null;
	BigDecimal forAmount = null;

	CurrencyMasterDTO domCur = null;
	BigDecimal domXRate = null;
	BigDecimal domAmount = null;

	List<BankMasterDTO> beneBanks = null;

	public List<BankMasterDTO> getBeneBanks() {
		return beneBanks;
	}

	public void setBeneBanks(List<BankMasterDTO> set) {
		this.beneBanks = set;
	}

	public CurrencyMasterDTO getForCur() {
		return forCur;
	}

	public void setForCur(CurrencyMasterDTO forCurcy) {
		this.forCur = forCurcy;
	}

	public BigDecimal getForXRate() {
		return forXRate;
	}

	public void setForXRate(BigDecimal forXRate) {
		this.forXRate = forXRate;
	}

	public BigDecimal getForAmount() {
		return forAmount;
	}

	public void setForAmount(BigDecimal forAmount) {
		this.forAmount = forAmount;
	}

	public CurrencyMasterDTO getDomCur() {
		return domCur;
	}

	public void setDomCur(CurrencyMasterDTO domCur) {
		this.domCur = domCur;
	}

	public BigDecimal getDomXRate() {
		return domXRate;
	}

	public void setDomXRate(BigDecimal domXRate) {
		this.domXRate = domXRate;
	}

	public BigDecimal getDomAmount() {
		return domAmount;
	}

	public void setDomAmount(BigDecimal domAmount) {
		this.domAmount = domAmount;
	}

}
