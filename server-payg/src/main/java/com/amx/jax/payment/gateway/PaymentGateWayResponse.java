package com.amx.jax.payment.gateway;

import com.amx.jax.payg.PayGModel;

public class PaymentGateWayResponse extends PayGModel {

	private static final long serialVersionUID = 3977135851433313693L;

	public static enum PayGStatus {
		INIT, CAPTURED, CANCELLED, ERROR, NOT_CAPTURED
	}
	
	public static enum CallbackScheme {
		REDIRECT, RENDER
	}

	String responseCode = null;
	String result = null;
	String tranxId = null;
	String auth = null;
	String ref = null;
	String postDate = null;
	CallbackScheme scheme;

	PayGStatus status = PayGStatus.INIT;

	public PayGStatus getPayGStatus() {
		return status;
	}

	public void setPayGStatus(PayGStatus payGStatus) {
		this.status = payGStatus;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public CallbackScheme getScheme() {
		return scheme;
	}

	public void setScheme(CallbackScheme scheme) {
		this.scheme = scheme;
	}

}
