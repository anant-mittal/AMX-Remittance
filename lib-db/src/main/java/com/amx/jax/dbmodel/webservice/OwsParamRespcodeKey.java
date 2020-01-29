package com.amx.jax.dbmodel.webservice;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OwsParamRespcodeKey implements Serializable 
{

	private static final long serialVersionUID = 5503765008780990656L;

	@Column(name = "BNKCOD")
	String bankCode;
	
	@Column(name = "RSP_CODE")
	String responseCode;
	
	@Column(name = "WS_CALL_TYPE")
	String wsCallType;
	
		
	public OwsParamRespcodeKey()
	{
		super();
	}

	public OwsParamRespcodeKey(String bankCode, String responseCode, String wsCallType)
	{
		super();
		this.bankCode = bankCode;
		this.responseCode = responseCode;
		this.wsCallType = wsCallType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getWsCallType() {
		return wsCallType;
	}

	public void setWsCallType(String wsCallType) {
		this.wsCallType = wsCallType;
	}
}
