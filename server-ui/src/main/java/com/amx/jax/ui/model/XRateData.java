package com.amx.jax.ui.model;

import java.math.BigDecimal;

public class XRateData {

	String forCur = null;
	String forCurDesc = null;
	BigDecimal forXRate = null;
	BigDecimal forAmount = null;

	String domCur = null;
	String domCurDesc = null;
	BigDecimal domXRate = null;
	BigDecimal domAmount = null;

	public String getForCur() {
		return forCur;
	}

	public void setForCur(String forCur) {
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

	public String getDomCur() {
		return domCur;
	}

	public void setDomCur(String domCur) {
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
