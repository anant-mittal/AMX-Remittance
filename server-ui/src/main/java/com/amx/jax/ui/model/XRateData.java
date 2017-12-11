package com.amx.jax.ui.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.meta.model.BankMasterDTO;

public class XRateData {

	BigDecimal forCur = null;
	String forCurDesc = null;
	BigDecimal forXRate = null;
	BigDecimal forAmount = null;

	BigDecimal domCur = null;
	String domCurDesc = null;
	BigDecimal domXRate = null;
	BigDecimal domAmount = null;

	List<BankMasterDTO> beneBanks = null;

	public List<BankMasterDTO> getBeneBanks() {
		return beneBanks;
	}

	public void setBeneBanks(List<BankMasterDTO> beneBanks) {
		this.beneBanks = beneBanks;
	}

	public BigDecimal getForCur() {
		return forCur;
	}

	public void setForCur(BigDecimal forCur) {
		this.forCur = forCur;
	}

	public String getForCurDesc() {
		return forCurDesc;
	}

	public void setForCurDesc(String forCurDesc) {
		this.forCurDesc = forCurDesc;
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

	public BigDecimal getDomCur() {
		return domCur;
	}

	public void setDomCur(BigDecimal domCur) {
		this.domCur = domCur;
	}

	public String getDomCurDesc() {
		return domCurDesc;
	}

	public void setDomCurDesc(String domCurDesc) {
		this.domCurDesc = domCurDesc;
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
