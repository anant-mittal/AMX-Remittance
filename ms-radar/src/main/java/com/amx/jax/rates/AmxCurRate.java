package com.amx.jax.rates;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.radar.AESDocument;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

//@Document(indexName = "polls", type = "vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AmxCurRate {

	public AmxCurRate() {
		this.timestamp = new Date(System.currentTimeMillis());
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@JsonProperty(value = "timestamp")
	protected Date timestamp;

	public AmxCurRate(RateType rType) {
		this();
		this.rType = rType;
	}

	public AmxCurRate(RateType rType, BigDecimal rRate) {
		this();
		this.rType = rType;
		this.rRate = rRate;
	}

	public AmxCurRate(RateSource rSrc, Currency rDomCur, Currency rForCur) {
		this();
		this.rSrc = rSrc;
		this.rDomCur = rDomCur;
		this.rForCur = rForCur;
	}

	private RateSource rSrc;
	private Currency rForCur;
	private Currency rDomCur;
	private RateType rType;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private BigDecimal rRate;

	public RateSource getrSrc() {
		return rSrc;
	}

	public void setrSrc(RateSource rSrc) {
		this.rSrc = rSrc;
	}

	public Currency getrForCur() {
		return rForCur;
	}

	public void setrForCur(Currency rForCur) {
		this.rForCur = rForCur;
	}

	public Currency getrDomCur() {
		return rDomCur;
	}

	public void setrDomCur(Currency rDomCur) {
		this.rDomCur = rDomCur;
	}

	public RateType getrType() {
		return rType;
	}

	public void setrType(RateType rType) {
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
		return rate;
	}

	public AmxCurRate clone(RateType rType) {
		AmxCurRate rate = this.clone();
		rate.setrType(rType);
		return rate;
	}

	public AmxCurRate clone(RateType rType, BigDecimal rRate) {
		AmxCurRate rate = this.clone();
		rate.setrType(rType);
		rate.setrRate(rRate);
		return rate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
