package com.amx.jax.partner.dto;

public class RemitTrnxSPDTO {
	
	private String actionInd;
	private String responseDescription;
	private String transactionId;
	
	public String getActionInd() {
		return actionInd;
	}
	public void setActionInd(String actionInd) {
		this.actionInd = actionInd;
	}
	
	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
		
}
