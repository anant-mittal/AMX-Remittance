package com.amx.jax.rates;

import java.math.BigDecimal;

import com.amx.jax.rates.AmxConstants.RCur;
import com.amx.jax.rates.AmxConstants.RSource;
import com.amx.jax.rates.AmxConstants.RType;
import com.fasterxml.jackson.annotation.JsonInclude;

//@Document(indexName = "polls", type = "vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AmxCurRate {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private RSource rSrc;
	private RCur rForCur;
	private RCur rDomCur;
	private RType rType;
	private BigDecimal rRate;

	public RSource getrSrc() {
		return rSrc;
	}

	public void setrSrc(RSource rSrc) {
		this.rSrc = rSrc;
	}

	public RCur getrForCur() {
		return rForCur;
	}

	public void setrForCur(RCur rForCur) {
		this.rForCur = rForCur;
	}

	public RCur getrDomCur() {
		return rDomCur;
	}

	public void setrDomCur(RCur rDomCur) {
		this.rDomCur = rDomCur;
	}

	public RType getrType() {
		return rType;
	}

	public void setrType(RType rType) {
		this.rType = rType;
	}

	public BigDecimal getrRate() {
		return rRate;
	}

	public void setrRate(BigDecimal rRate) {
		this.rRate = rRate;
	}

}
