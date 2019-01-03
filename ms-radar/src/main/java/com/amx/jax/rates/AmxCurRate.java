package com.amx.jax.rates;

import java.math.BigDecimal;

import com.amx.jax.radar.AESDocument;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

//@Document(indexName = "polls", type = "vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AmxCurRate extends AESDocument {

	public AmxCurRate() {
		super();
	}

	public AmxCurRate(RType rType) {
		this();
		this.rType = rType;
	}

	public AmxCurRate(RType rType, BigDecimal rRate) {
		this();
		this.rType = rType;
		this.rRate = rRate;
	}

	public AmxCurRate(RSource rSrc, RCur rDomCur, RCur rForCur) {
		this();
		this.rSrc = rSrc;
		this.rDomCur = rDomCur;
		this.rForCur = rForCur;
	}

	private RSource rSrc;
	private RCur rForCur;
	private RCur rDomCur;
	private RType rType;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
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

	public AmxCurRate clone() {
		AmxCurRate rate = new AmxCurRate();
		rate.setrSrc(rSrc);
		rate.setrDomCur(rDomCur);
		rate.setrForCur(rForCur);
		rate.setTimestamp(this.getTimestamp());
		return rate;
	}

	public AmxCurRate clone(RType rType) {
		AmxCurRate rate = this.clone();
		rate.setrType(rType);
		return rate;
	}

	public AmxCurRate clone(RType rType, BigDecimal rRate) {
		AmxCurRate rate = this.clone();
		rate.setrType(rType);
		rate.setrRate(rRate);
		return rate;
	}

}
