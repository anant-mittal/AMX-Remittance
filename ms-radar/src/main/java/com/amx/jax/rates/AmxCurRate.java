package com.amx.jax.rates;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

	public AmxCurRate() {
		this.timestamp = new Date(System.currentTimeMillis());
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@JsonProperty(value = "@timestamp")
	private Date timestamp;

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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
