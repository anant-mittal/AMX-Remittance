package com.amx.jax.rates;

import com.fasterxml.jackson.annotation.JsonInclude;

//@Document(indexName = "polls", type = "vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CurRate {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String rSrc;
	private String rForCur;
	private String rDomCur;
	private String rType;
	private String rRate;

	public String getrSrc() {
		return rSrc;
	}

	public void setrSrc(String rSrc) {
		this.rSrc = rSrc;
	}

	public String getrForCur() {
		return rForCur;
	}

	public void setrForCur(String rForCur) {
		this.rForCur = rForCur;
	}

	public String getrDomCur() {
		return rDomCur;
	}

	public void setrDomCur(String rDomCur) {
		this.rDomCur = rDomCur;
	}

	public String getrType() {
		return rType;
	}

	public void setrType(String rType) {
		this.rType = rType;
	}

	public String getrRate() {
		return rRate;
	}

	public void setrRate(String rRate) {
		this.rRate = rRate;
	}

}
